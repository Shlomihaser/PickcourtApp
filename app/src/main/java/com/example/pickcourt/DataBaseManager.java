package com.example.pickcourt;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pickcourt.Activities.BaseActivity;
import com.example.pickcourt.Interfaces.CourtsFetchListener;
import com.example.pickcourt.Interfaces.CourtsFilterCallback;
import com.example.pickcourt.Interfaces.FavoriteCourtsFetchCallback;
import com.example.pickcourt.Interfaces.PaymentsFetchListener;
import com.example.pickcourt.Interfaces.ReservationsFetchCallback;
import com.example.pickcourt.Interfaces.UserExistCallback;
import com.example.pickcourt.Interfaces.isFavoriteCallback;
import com.example.pickcourt.Models.Court;
import com.example.pickcourt.Models.Payment;
import com.example.pickcourt.Models.Reservation;
import com.example.pickcourt.Utilities.SignalManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DataBaseManager extends BaseActivity {
    private static volatile DataBaseManager instance = null;
    private PaymentsFetchListener paymentCallback;
    private CourtsFetchListener courtsCallback;
    private UserExistCallback userExistCallback;
    private CourtsFilterCallback courtsFilterCallback;
    private isFavoriteCallback isFavoriteCallback;
    private FavoriteCourtsFetchCallback favoriteCourtsFetchCallback;
    private ReservationsFetchCallback reservationsFetchCallback;
    public DataBaseManager(){}

    public static DataBaseManager init (Context context){
        if(instance == null)
        {
            synchronized (DataBaseManager.class){
                if(instance == null) instance = new DataBaseManager();
            }
        }
        return getInstance();
    }

    public static DataBaseManager getInstance(){
        return instance;
    }

    public void fetchCreditCards() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid()).child("payments");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Payment> paymentArrayList = new ArrayList<>();
                for (DataSnapshot paymentSnapshot : snapshot.getChildren()) {
                    Payment payment = paymentSnapshot.getValue(Payment.class);
                    if (payment != null) paymentArrayList.add(payment);
                }
                paymentCallback.onSuccess(paymentArrayList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void addCreditCard(Payment payment) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        DatabaseReference userPaymentsRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(currentUser.getUid())
                .child("payments");

        userPaymentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isFirstCard = !dataSnapshot.exists();
                payment.setDefaultCard(isFirstCard);
                userPaymentsRef.push().setValue(payment);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void setNewDefaultCard(DatabaseReference userRef) {
        userRef.limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot paymentSnapshot : snapshot.getChildren()) {
                    paymentSnapshot.getRef().child("defaultCard").setValue(true);
                    return;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                SignalManager.getInstance().toast("Failed to set new default card");
            }
        });
    }

    public void deleteCreditCard(Payment payment) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                .child(currentUser.getUid())
                .child("payments");

        userRef.orderByChild("cardNumber").equalTo(payment.getCardNumber())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot paymentSnapshot : snapshot.getChildren()) {
                            paymentSnapshot.getRef().removeValue()
                                    .addOnSuccessListener(aVoid -> {
                                        SignalManager.getInstance().toast("Payment deleted successfully");
                                        if (payment.getDefaultCard()) {
                                            setNewDefaultCard(userRef);
                                        }
                                    })
                                    .addOnFailureListener(e -> SignalManager.getInstance().toast("Failed to delete payment: " + e.getMessage()));
                            return;
                        }
                        SignalManager.getInstance().toast("Payment not found");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        SignalManager.getInstance().toast("Database error: " + error.getMessage());
                    }
                });
    }




    public void setDefaultCard(Payment newDefaultCard) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                .child(currentUser.getUid())
                .child("payments");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot paymentSnapshot : snapshot.getChildren()) {
                    String cardNumber = paymentSnapshot.child("cardNumber").getValue(String.class);
                    if (cardNumber != null) {
                        if (cardNumber.equals(newDefaultCard.getCardNumber())) paymentSnapshot.getRef().child("defaultCard").setValue(true);
                         else paymentSnapshot.getRef().child("defaultCard").setValue(false);

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }


    public void checkIfUserExists(String uid) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userExistCallback.onResult(dataSnapshot.exists());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void createNewUser(String userId) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("payments", new ArrayList<>());
        newUser.put("reservations", new ArrayList<>());
        newUser.put("favorites", new ArrayList<>());

        usersRef.child(userId).setValue(newUser)
                .addOnSuccessListener(aVoid -> SignalManager.getInstance().toast("New user created successfully!"))
                .addOnFailureListener(e -> SignalManager.getInstance().toast("Failed to create new user: " + e.getMessage()));
    }

    public void setCourtsCallback(CourtsFetchListener courtsCallback) {
        this.courtsCallback = courtsCallback;
    }
    public void fetchCourts(String courtType) {
        DatabaseReference courtsRef = FirebaseDatabase.getInstance().getReference("courts").child(courtType);

        courtsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Court> courts = new ArrayList<>();
                for (DataSnapshot courtSnapshot : dataSnapshot.getChildren()) {
                    Court court = courtSnapshot.getValue(Court.class);
                    if (court != null) {
                        court.setSportType(courtType);
                        courts.add(court);
                    }
                }
                if (courtsCallback != null) courtsCallback.onCourtsFetchSuccess(courts);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void setUserExistCallback(UserExistCallback userExistCallback) {
        this.userExistCallback = userExistCallback;
    }

    public void setPaymentCallback(PaymentsFetchListener paymentCallback) {
        this.paymentCallback = paymentCallback;
    }

    public DataBaseManager setIsFavoriteCallback(com.example.pickcourt.Interfaces.isFavoriteCallback isFavoriteCallback) {
        this.isFavoriteCallback = isFavoriteCallback;
        return this;
    }

    public DataBaseManager setCourtsFilterCallback(CourtsFilterCallback courtsFilterCallback) {
        this.courtsFilterCallback = courtsFilterCallback;
        return this;
    }

    public DataBaseManager setFavoriteCourtsFetchCallback(FavoriteCourtsFetchCallback favoriteCourtsFetchCallback) {
        this.favoriteCourtsFetchCallback = favoriteCourtsFetchCallback;
        return this;
    }

    public void setReservationsFetchCallback(ReservationsFetchCallback reservationsFetchCallback) {
        this.reservationsFetchCallback = reservationsFetchCallback;
    }

    public void searchCourts(String sportType, String query) {
        DatabaseReference courtsRef = FirebaseDatabase.getInstance().getReference("courts").child(sportType);

        courtsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Court> filteredCourts = new ArrayList<>();
                for (DataSnapshot courtSnapshot : dataSnapshot.getChildren()) {
                    Court court = courtSnapshot.getValue(Court.class);
                    if (court != null && (court.getName().toLowerCase().contains(query.toLowerCase())))
                        filteredCourts.add(court);
                }
                courtsFilterCallback.onCourtsFiltered(filteredCourts);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void checkFavoriteStatus(String courtName) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;

        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("users").child(user.getUid()).child("favorites");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isFavorite = false;
                if (snapshot.exists()) {
                    for (DataSnapshot favoriteSnapshot : snapshot.getChildren()) {
                        if (courtName.equals(favoriteSnapshot.getValue(String.class))) {
                            isFavorite = true;
                            break;
                        }
                    }
                }
                isFavoriteCallback.onFavoriteStatus(courtName, isFavorite);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DataBaseManager", "Error checking favorite status", error.toException());
            }
        });
    }

    public void toggleFavorite(String courtName, boolean addToFavorites) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;

        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("users").child(user.getUid());

        userRef.child("favorites").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                ArrayList<String> favorites = new ArrayList<>();

                if (snapshot.exists())
                    for (DataSnapshot favoriteSnapshot : snapshot.getChildren())
                        favorites.add(favoriteSnapshot.getValue(String.class));

                boolean changed = false;
                if (addToFavorites && !favorites.contains(courtName)) {
                    favorites.add(courtName);
                    changed = true;
                } else if (!addToFavorites && favorites.contains(courtName)) {
                    favorites.remove(courtName);
                    changed = true;
                }
                if (changed) userRef.child("favorites").setValue(favorites);

            }
        });
    }

    public void getFavoriteCourts() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("users").child(user.getUid()).child("favorites");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> favoriteCourtNames = new ArrayList<>();
                for (DataSnapshot favoriteSnapshot : snapshot.getChildren()) {
                    String courtName = favoriteSnapshot.getValue(String.class);
                    if (courtName != null) favoriteCourtNames.add(courtName);

                }
                fetchCourtDetails(favoriteCourtNames);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void fetchCourtDetails(ArrayList<String> courtNames) {
        ArrayList<Court> favoriteCourts = new ArrayList<>();
        DatabaseReference courtsRef = FirebaseDatabase.getInstance().getReference("courts");

        courtsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (String courtName : courtNames) {
                    boolean found = false;
                    for (DataSnapshot sportTypeSnapshot : snapshot.getChildren()) {
                        String sportType = sportTypeSnapshot.getKey();
                        for (DataSnapshot courtSnapshot : sportTypeSnapshot.getChildren()) {
                            Court court = courtSnapshot.getValue(Court.class);
                            if (court != null && court.getName().equals(courtName)) {
                                court.setFavorite(true);
                                court.setSportType(sportType);
                                favoriteCourts.add(court);
                                found = true;
                                break;
                            }
                        }
                        if (found) break;
                    }
                }
                favoriteCourtsFetchCallback.onFavoriteCourtsFetch(favoriteCourts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error, maybe call the callback with an empty list
                favoriteCourtsFetchCallback.onFavoriteCourtsFetch(new ArrayList<>());
            }
        });
    }

    public void cancelReservation(String reservationId) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        DatabaseReference userReservationsRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(currentUser.getUid())
                .child("reservations");

        userReservationsRef.child(reservationId).removeValue()
                .addOnSuccessListener(aVoid -> SignalManager.getInstance().toast("Reservation canceled successfully"))
                .addOnFailureListener(e -> SignalManager.getInstance().toast("Failed to cancel reservation: " + e.getMessage()));
    }

    public void addReservation(Reservation reservation) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        DatabaseReference userReservationsRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(currentUser.getUid())
                .child("reservations");

        String reservationId = userReservationsRef.push().getKey(); // Generate a unique ID
        if (reservationId != null) {
            Map<String, Object> reservationData = new HashMap<>();
            reservationData.put("courtName", reservation.getCourtName());
            reservationData.put("hour", reservation.getHour());
            reservationData.put("date", reservation.getDate());
            reservationData.put("numOfPlayers", reservation.getNumOfPlayers());
            reservationData.put("courtImageUrl",reservation.getCourtImageUrl());

            userReservationsRef.child(reservationId).setValue(reservationData)
                    .addOnSuccessListener(aVoid -> SignalManager.getInstance().toast("Reservation added successfully"))
                    .addOnFailureListener(e -> SignalManager.getInstance().toast("Failed to add reservation: " + e.getMessage()));
        }
    }

    public void fetchReservations() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        DatabaseReference userReservationsRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(currentUser.getUid())
                .child("reservations");

        userReservationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Reservation> reservationList = new ArrayList<>();
                for (DataSnapshot reservationSnapshot : snapshot.getChildren()) {
                    Reservation reservation = reservationSnapshot.getValue(Reservation.class);
                    if (reservation != null) {
                        reservation.setReservationId(reservationSnapshot.getKey()); // Assign the key as the ID
                        reservationList.add(reservation);
                    }
                }
                reservationsFetchCallback.onFetch(reservationList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                SignalManager.getInstance().toast("Failed to fetch reservations: " + error.getMessage());
            }
        });
    }

}
