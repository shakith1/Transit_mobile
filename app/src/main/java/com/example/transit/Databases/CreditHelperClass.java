package com.example.transit.Databases;

public class CreditHelperClass {

    String username;
    Float credit;

    public CreditHelperClass(){}

    public CreditHelperClass(String username, Float credit){
        this.username = username;
        this.credit = credit;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Float getCredit() {
        return credit;
    }

    public void setCredit(Float credit) {
        this.credit = credit;
    }
}
