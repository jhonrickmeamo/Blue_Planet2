package com.example.orderdeliverytrackingsystem;

public class employeeID {
    String FirstName, email, password;


    public employeeID() {

    }
    public employeeID(String firstName, String email, String password) {
        this.FirstName = firstName;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }
}
