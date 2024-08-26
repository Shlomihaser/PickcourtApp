package com.example.pickcourt.Interfaces;

import com.example.pickcourt.Models.Court;

import java.util.ArrayList;

public interface CourtsFetchListener {
    void onCourtsFetchSuccess(ArrayList<Court> courts);
}
