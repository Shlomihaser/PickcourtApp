package com.example.pickcourt.Interfaces;

import com.example.pickcourt.Models.Reservation;

import java.util.ArrayList;

public interface ReservationsFetchCallback {
    void onFetch(ArrayList<Reservation> reservationList);
}
