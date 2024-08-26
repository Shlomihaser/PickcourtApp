package com.example.pickcourt.Interfaces;

import com.example.pickcourt.Models.Court;

import java.util.ArrayList;

public interface CourtsFilterCallback {
    void onCourtsFiltered(ArrayList<Court> filteredCourts);
}
