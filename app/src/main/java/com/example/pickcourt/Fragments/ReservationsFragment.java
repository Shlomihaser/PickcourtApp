package com.example.pickcourt.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickcourt.Adapters.CourtAdapter;
import com.example.pickcourt.Adapters.ReservationAdapter;
import com.example.pickcourt.DataBaseManager;
import com.example.pickcourt.Interfaces.OpenDrawerCallback;
import com.example.pickcourt.R;

public class ReservationsFragment extends Fragment {

    private AppCompatImageView toggle_sidebar_button_reservations;
    private RecyclerView reservations_recycler_view;
    private OpenDrawerCallback openDrawerCallback;
    private ReservationAdapter reservationAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_reservations, container, false);
        findViews(v);
        initViews();
        return v;
    }



    private void findViews(View v) {
        toggle_sidebar_button_reservations = v.findViewById(R.id.toggle_sidebar_button_reservations);
        reservations_recycler_view = v.findViewById(R.id.reservations_recycler_view);
    }


    private void initViews() {
        toggle_sidebar_button_reservations.setOnClickListener(v -> openDrawerCallback.openDrawer());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        reservations_recycler_view.setLayoutManager(linearLayoutManager);

        DataBaseManager.getInstance().setReservationsFetchCallback((reservations) -> {
            reservationAdapter = new ReservationAdapter(reservations);
            reservations_recycler_view.setAdapter(reservationAdapter);
        });
        DataBaseManager.getInstance().fetchReservations();
    }

    public void setOpenDrawerCallback(OpenDrawerCallback openDrawerCallback) {
        this.openDrawerCallback = openDrawerCallback;
    }

}
