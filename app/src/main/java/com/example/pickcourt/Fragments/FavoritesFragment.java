package com.example.pickcourt.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickcourt.Adapters.CourtAdapter;
import com.example.pickcourt.DataBaseManager;
import com.example.pickcourt.Interfaces.OpenDrawerCallback;
import com.example.pickcourt.Models.Court;
import com.example.pickcourt.R;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {
    private RecyclerView favorites_recycler_view;
    private CourtAdapter courtAdapter;
    private AppCompatImageView toggle_sidebar_button;
    private OpenDrawerCallback openDrawerCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_favorites, container, false);
        findViews(v);
        initViews();
        DataBaseManager.getInstance().getFavoriteCourts();
        return v;
    }

    private void findViews(View v) {
        favorites_recycler_view = v.findViewById(R.id.favorites_recycler_view);
        toggle_sidebar_button = v.findViewById(R.id.toggle_sidebar_button);
    }

    public FavoritesFragment setOpenDrawerCallback(OpenDrawerCallback openDrawerCallback) {
        this.openDrawerCallback = openDrawerCallback;
        return this;
    }

    private void initViews(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        favorites_recycler_view.setLayoutManager(linearLayoutManager);

        toggle_sidebar_button.setOnClickListener(v -> openDrawerCallback.openDrawer());

        DataBaseManager.getInstance().setFavoriteCourtsFetchCallback((courts) -> {
            courtAdapter = new CourtAdapter(courts);
            courtAdapter.setCourtClickListener(this::onCourtClick);
            favorites_recycler_view.setAdapter(courtAdapter);
        });
    }


    public void onCourtClick(Court court) {
        CourtInfoFragment courtInfoFragment = new CourtInfoFragment();
        Bundle args = new Bundle();

        args.putString("courtName", court.getName());
        args.putString("favorite",String.valueOf(court.isFavorite()));
        args.putString("sportType",court.getSportType());
        courtInfoFragment.setArguments(args);

        getParentFragmentManager().beginTransaction().replace(R.id.dash_frame, courtInfoFragment)
                .addToBackStack(null)
                .commit();
    }


}
