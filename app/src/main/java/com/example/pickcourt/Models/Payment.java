package com.example.pickcourt.Models;

public class Payment {
    private String id;
    private String cardNumber;
    private String expirationDate;
    private String cvv;
    private boolean defaultCard;

    public Payment(){}

    public Payment(String id, String cardNumber, String expirationDate, String cvv,boolean defaultCard) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.defaultCard = defaultCard;
    }

    public boolean getDefaultCard() {
        return defaultCard;
    }
    public void setDefaultCard(boolean defaultCard) {
        this.defaultCard = defaultCard;
    }

    public String getId() {
        return id;
    }

    public Payment setId(String id) {
        this.id = id;
        return this;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Payment setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        return this;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public Payment setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public String getCvv() {
        return cvv;
    }

    public Payment setCvv(String cvv) {
        this.cvv = cvv;
        return this;
    }
}
