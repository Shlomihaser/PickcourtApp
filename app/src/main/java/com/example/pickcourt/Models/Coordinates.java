package com.example.pickcourt.Models;

public class Coordinates {
    private double latitude;
    private double longitude;

    // Default constructor
    public Coordinates() {}

    // Constructor with parameters
    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public double getLatitude() {
        return latitude;
    }

    public Coordinates setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Coordinates setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}