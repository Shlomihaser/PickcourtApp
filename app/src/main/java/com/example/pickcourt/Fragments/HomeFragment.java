package com.example.pickcourt.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import com.example.pickcourt.DataBaseManager;
import com.example.pickcourt.Interfaces.OnCourtClickListener;
import com.example.pickcourt.Interfaces.OpenDrawerCallback;
import com.example.pickcourt.Models.Court;
import com.example.pickcourt.R;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment implements OnCourtClickListener {

    private FrameLayout home_FRAME_courts;
    private CourtsFragment courtsFragment;
    private MaterialButton home_DATE_BTN;
    private Spinner home_SPINNER;
    private EditText home_SEARCH_BAR;
    private AppCompatImageButton toggle_sidebar_button;
    private OpenDrawerCallback openDrawerCallback;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_home, container, false);
        findViews(v);
        initSpinner();
        initViews();
        DataBaseManager.getInstance().setCourtsFilterCallback(this::updateFilteredCourts);
        return v;
    }



    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireActivity(),
                R.array.sports_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        home_SPINNER.setAdapter(adapter);
        int defaultPosition = adapter.getPosition("Tennis");
        home_SPINNER.setSelection(defaultPosition);

        home_SPINNER.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSport = parent.getItemAtPosition(position).toString();
                DataBaseManager.getInstance().fetchCourts(selectedSport);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void findViews(View v) {
        home_SPINNER = v.findViewById(R.id.home_SPINNER);
        home_SEARCH_BAR = v.findViewById(R.id.home_SEARCH_BAR);
        home_FRAME_courts = v.findViewById(R.id.home_FRAME_courts);
        toggle_sidebar_button = v.findViewById(R.id.toggle_sidebar_button);
    }


    private void initViews() {
        courtsFragment = new CourtsFragment();
        getChildFragmentManager().beginTransaction().add(R.id.home_FRAME_courts, courtsFragment).commit();
        setupSearchbar();

        toggle_sidebar_button.setOnClickListener( (v) -> openDrawerCallback.openDrawer());
    }

    private void setupSearchbar() {
        home_SEARCH_BAR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                String selectedSport = home_SPINNER.getSelectedItem().toString();
                DataBaseManager.getInstance().searchCourts(selectedSport,query);
            }
        });
    }
    private void updateFilteredCourts(ArrayList<Court> courts) {
        if(courts == null) return;
        courtsFragment.updateCourts(courts);
    }



    public void setOpenDrawerCallback(OpenDrawerCallback openDrawerCallback) {
        this.openDrawerCallback = openDrawerCallback;
    }

    @Override
    public void onCourtClick(Court court) {
        CourtInfoFragment courtInfoFragment = new CourtInfoFragment();
        Bundle args = new Bundle();
        args.putString("courtName", court.getName());
        args.putString("sportType",home_SPINNER.getSelectedItem().toString());
        args.putString("favorite",String.valueOf(court.isFavorite()));
        courtInfoFragment.setArguments(args);

        getParentFragmentManager().beginTransaction().replace(R.id.dash_frame, courtInfoFragment)
                        .addToBackStack(null)
                                .commit();
    }

}