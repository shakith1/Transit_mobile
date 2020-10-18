package com.example.transit.Databases;

public class CardHelperClass {

    String name,no,date,cvc;

    public CardHelperClass(){}

    public CardHelperClass(String name, String no, String date, String cvc) {
        this.name = name;
        this.no = no;
        this.date = date;
        this.cvc = cvc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }
}
