package com.example.pickcourt.Fragments;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.text.style.TabStopSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pickcourt.DataBaseManager;
import com.example.pickcourt.Models.Court;
import com.example.pickcourt.R;
import com.example.pickcourt.Utilities.ImageLoader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.imageview.ShapeableImageView;
import androidx.appcompat.widget.AppCompatImageButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CourtInfoFragment extends Fragment  {


    private FrameLayout sub_FRAME_info;
    private SubCourtInfoFragment subCourtInfoFragment;
    private AppCompatImageButton back_button;
    private TextView court_LBL_name;
    private TextView court_LBL_indoorOutdoor;
    private TextView court_LBL_price;
    private ShapeableImageView court_IMG_photo;
    private AppCompatImageButton court_FAB_favorite;
    private TextView court_LBL_rating;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_court_info, container, false);
        findViews(view);
        initViews();
        getInfo();
        return view;
    }

    private void findViews(View view) {
        court_LBL_name = view.findViewById(R.id.court_LBL_name);
        court_LBL_indoorOutdoor = view.findViewById(R.id.court_LBL_indoorOutdoor);
        court_LBL_price = view.findViewById(R.id.court_LBL_price);
        court_IMG_photo = view.findViewById(R.id.court_IMG_photo);
        court_FAB_favorite = view.findViewById(R.id.court_FAB_favorite);
        back_button = view.findViewById(R.id.back_button);
        sub_FRAME_info = view.findViewById(R.id.sub_FRAME_info);
        court_LBL_rating = view.findViewById(R.id.court_LBL_rating);
    }

    private void initViews() {
        back_button.setOnClickListener(v -> navigateToHomeFragment());
    }


    private void getInfo() {
        Bundle args = getArguments();
        if (args == null) return;

        String courtName = args.getString("courtName");
        DataBaseManager.getInstance().searchCourts(args.getString("sportType"), courtName);

        DataBaseManager.getInstance().setCourtsFilterCallback(court -> {
            Court currentCourt = court.get(0);

            court_LBL_rating.setText("Rating: " + currentCourt.getRating());
            court_LBL_name.setText(currentCourt.getName());
            court_LBL_indoorOutdoor.setText(currentCourt.isIndoor() ? "Indoor" : "Outdoor");
            court_LBL_price.setText(String.format("%.2f₪", currentCourt.getPricePerHour()));
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && user.isAnonymous()) court_FAB_favorite.setVisibility(View.INVISIBLE);
            updateFavoriteIcon(court_FAB_favorite, Objects.equals(args.getString("favorite"), "true"));
            DataBaseManager.getInstance().checkFavoriteStatus(currentCourt.getName());

            court_FAB_favorite.setOnClickListener(v -> {
                currentCourt.setFavorite(!currentCourt.isFavorite());
                DataBaseManager.getInstance().toggleFavorite(currentCourt.getName(), currentCourt.isFavorite());
                updateFavoriteIcon(court_FAB_favorite, currentCourt.isFavorite());
            });
            if (!currentCourt.getImages().isEmpty())
                ImageLoader.getInstance().load(currentCourt.getImages().get(0), court_IMG_photo);

            subCourtInfoFragment = new SubCourtInfoFragment();
            Bundle args2 = new Bundle();
            args2.putString("about",currentCourt.getAbout());
            args2.putStringArrayList("days",currentCourt.getAvailableDays());
            args2.putStringArrayList("hours",currentCourt.getAvailableHours());
            ArrayList<String> coordinates = new ArrayList<>();
            coordinates.add(String.valueOf(currentCourt.getCoordinates().getLongitude()));
            coordinates.add(String.valueOf(currentCourt.getCoordinates().getLatitude()));
            args2.putStringArrayList("coordinates",coordinates);
            args2.putString("location",currentCourt.getLocation());
            args2.putStringArrayList("amenities",currentCourt.getAmenities());
            args2.putString("courtName",currentCourt.getName());
            args2.putString("courtImage",currentCourt.getImages().get(0));
            subCourtInfoFragment.setArguments(args2);
            getChildFragmentManager().beginTransaction().add(R.id.sub_FRAME_info, subCourtInfoFragment).commit();
        });
    }

    public void navigateToHomeFragment() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }


    private void updateFavoriteIcon(AppCompatImageButton fab, boolean isFavorite) {
        fab.setImageResource(isFavorite ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite);
    }

}