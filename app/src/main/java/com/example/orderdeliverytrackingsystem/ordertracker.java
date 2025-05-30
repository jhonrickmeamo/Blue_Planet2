package com.example.orderdeliverytrackingsystem;

public class ordertracker {
    String firstname, lastname, number, address, barangay, city, street, status, docID;


    public ordertracker() {
    }

    public ordertracker(String firstname, String lastname, String number, String address, String barangay, String city, String street, String status) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.number = number;
        this.address = address;
        this.barangay = barangay;
        this.city = city;
        this.street = street;
        this.status = status;
        this.docID = docID;
    }

    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }
    public String getBarangay() {
        return barangay;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCity() {
        return city;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getStreet() {
        return street;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getDocID() {
        return docID;
    }
    public void setDocID(String docID) {
        this.docID = docID;
    }

}
