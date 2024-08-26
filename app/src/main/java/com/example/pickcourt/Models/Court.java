package com.example.pickcourt.Models;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import com.google.firebase.database.PropertyName;

public class Court {
    private String name;
    private String location;
    private boolean indoor;
    @PropertyName("price_per_hour")
    private double pricePerHour;
    @PropertyName("available_days")
    private ArrayList<String> availableDays;
    private boolean isFavorite;
    private String sportType;
    @PropertyName("available_hours")
    private ArrayList<String> availableHours;
    private String about;
    private ArrayList<String> amenities;
    private double rating;
    private ArrayList<String> images;
    private Coordinates coordinates;

    // Default constructor
    public Court() {}

    // Constructor with all fields
    public Court(String name, String location, boolean indoor, double pricePerHour,
                 ArrayList<String> availableDays, ArrayList<String> availableHours,
                 ArrayList<String> amenities, double rating, ArrayList<String> images,String about,Coordinates coordinates) {
        this.name = name;
        this.location = location;
        this.indoor = indoor;
        this.pricePerHour = pricePerHour;
        this.availableDays = availableDays;
        this.availableHours = availableHours;
        this.amenities =  amenities;
        this.rating = rating;
        this.images = images;
        this.about = about;
        this.coordinates = coordinates;
    }

    // Getters and Setters

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getAbout() {
        return about;
    }

    public Court setAbout(String about) {
        this.about = about;
        return this;
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public String getName() {
        return name;
    }

    public Court setName(String name) {
        this.name = name;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Court setLocation(String location) {
        this.location = location;
        return this;
    }

    public boolean isIndoor() {
        return indoor;
    }

    public Court setIndoor(boolean indoor) {
        this.indoor = indoor;
        return this;
    }
    @PropertyName("price_per_hour")
    public double getPricePerHour() {
        return pricePerHour;
    }
    @PropertyName("price_per_hour")
    public Court setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
        return this;
    }
    @PropertyName("available_days")
    public ArrayList<String> getAvailableDays() {
        return availableDays;
    }
    @PropertyName("available_days")
    public Court setAvailableDays(ArrayList<String> availableDays) {
        this.availableDays = availableDays;
        return this;
    }
    @PropertyName("available_hours")
    public ArrayList<String> getAvailableHours() {
        return availableHours;
    }
    @PropertyName("available_hours")
    public Court setAvailableHours(ArrayList<String> availableHours) {
        this.availableHours = availableHours;
        return this;
    }

    public ArrayList<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(ArrayList<String> amenities) {
        this.amenities = amenities;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @NonNull
    @Override
    public String toString() {
        return "Court{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", indoor=" + indoor +
                ", pricePerHour=" + pricePerHour +
                ", availableDays=" + availableDays +
                ", isFavorite=" + isFavorite +
                ", sportType='" + sportType + '\'' +
                ", availableHours=" + availableHours +
                ", about='" + about + '\'' +
                ", amenities=" + amenities +
                ", rating=" + rating +
                ", images=" + images +
                ", coordinates=" + coordinates +
                '}';
    }
}
