package com.example.pickcourt.Fragments;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;


import com.example.pickcourt.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;



public class SubCourtInfoFragment extends Fragment implements OnMapReadyCallback {

    private ArrayList<String> availableHours;
    private String courtName;
    private TextView court_LBL_about;
    private TextView court_LBL_opening_hours;
    private TextView court_LBL_offers;
    private TextView court_LBL_location_text;
    private GoogleMap mMap;
    private LatLng courtLocation;
    private AppCompatButton book_button;
    private String courtImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_sub_info, container, false);
        findViews(view);
        getCourtInfo();
        initViews();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_google);
        if (mapFragment != null) mapFragment.getMapAsync(this);
        return view;
    }

    private void findViews(View view) {
        book_button = view.findViewById(R.id.book_button);
        court_LBL_about = view.findViewById(R.id.court_LBL_about);
        court_LBL_opening_hours = view.findViewById(R.id.court_LBL_opening_hours);
        court_LBL_offers = view.findViewById(R.id.court_LBL_offers);
        court_LBL_location_text = view.findViewById(R.id.court_LBL_location_text);
    }

    private void initViews() {
        book_button.setOnClickListener((v) -> {
            Bundle args = new Bundle();
            args.putString("courtName",getCourtName());
            args.putStringArrayList("availableHours",getAvailableHours());
            args.putString("courtImage",getCourtImage());
            BookingFragment bookingFragment = new BookingFragment();
            bookingFragment.setArguments(args);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.sub_FRAME_info, bookingFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    public String getCourtName() {
        return courtName;
    }
    public String getCourtImage() {
        return courtImage;
    }
    public ArrayList<String> getAvailableHours() {
        return availableHours;
    }

    public void setAvailableHours(ArrayList<String> availableHours) {
        this.availableHours = availableHours;
    }
    public void setCourtImage(String courtImage) {
        this.courtImage = courtImage;
    }
    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);

//        LatLng courtLocation = new LatLng(32.7767, -96.797); // Example coordinates
//        mMap.addMarker(new MarkerOptions().position(courtLocation).title("Court Location"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(courtLocation, 15));
        // TODO: FIX THE MAP
        if (courtLocation != null) {
            mMap.addMarker(new MarkerOptions().position(courtLocation).title("Court Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(courtLocation, 4));
        }
    }



    private void getCourtInfo() {
        Bundle args = getArguments();
        if (args == null) return;
        ArrayList<String> coordinates = args.getStringArrayList("coordinates");
        double latitude = Double.parseDouble(coordinates.get(0));
        double longitude = Double.parseDouble(coordinates.get(1));
        courtLocation = new LatLng(latitude, longitude);
        String courtName = args.getString("courtName");
        setCourtName(courtName);
        ArrayList<String> availableDays = args.getStringArrayList("days");
        ArrayList<String> availableHours = args.getStringArrayList("hours");
        setAvailableHours(availableHours);
        ArrayList<String> amenities = args.getStringArrayList("amenities");

        String location = args.getString("location");
        String imgUrl = args.getString("courtImage");
        setCourtImage(imgUrl);

        court_LBL_about.setText(args.getString("about"));
        SpannableStringBuilder hoursBuilder = new SpannableStringBuilder();
        if (!availableHours.isEmpty()) {
            String firstHour = availableHours.get(0);
            String lastHour = availableHours.get(availableHours.size() - 1);
            for (String day : availableDays) {
                String hoursText = String.format("%-15s %s - %s\n",day + ":", firstHour, lastHour);
                hoursBuilder.append(hoursText);
            }
        }
        court_LBL_opening_hours.setText(hoursBuilder);

        SpannableStringBuilder offersBuilder = new SpannableStringBuilder();
        for (String amenity : amenities) {
            SpannableString spannableString = new SpannableString(amenity + "\n\n");
            spannableString.setSpan(new BulletSpan(10), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            offersBuilder.append(spannableString);
        }
        court_LBL_offers.setText(offersBuilder);

        court_LBL_location_text.setText(location); // Location Setting
    }



}
