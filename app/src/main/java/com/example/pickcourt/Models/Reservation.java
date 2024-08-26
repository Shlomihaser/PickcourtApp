package com.example.pickcourt.Models;

import com.google.firebase.database.PropertyName;

public class Reservation {
    private String reservationId;
    private String courtImageUrl;
    private String courtName;
    private String hour;
    private String date;
    private int numOfPlayers;

    public Reservation(){}
    // Constructor, getters, and setters
    public Reservation(String courtName, String hour, String date, int numOfPlayers,String courtImageUrl) {
        this.courtName = courtName;
        this.hour = hour;
        this.date = date;
        this.numOfPlayers = numOfPlayers;
        this.courtImageUrl = courtImageUrl;
    }

    public String getCourtName() { return courtName; }
    public String getHour() { return hour; }
    public String getDate() { return date; }
    public int getNumOfPlayers() { return numOfPlayers; }
    public String getReservationId() {
        return reservationId;
    }
    public String getCourtImageUrl() {
        return courtImageUrl;
    }

    public void setCourtImageUrl(String courtImageUrl) {
        this.courtImageUrl = courtImageUrl;
    }
    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }
    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }
    public void seHour(String Hour) {
        this.hour = hour;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }
}