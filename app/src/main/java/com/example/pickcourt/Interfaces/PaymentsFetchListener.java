package com.example.pickcourt.Interfaces;

import com.example.pickcourt.Models.Payment;

import java.util.ArrayList;


public interface PaymentsFetchListener {
    void onSuccess(ArrayList<Payment> payments);
}
