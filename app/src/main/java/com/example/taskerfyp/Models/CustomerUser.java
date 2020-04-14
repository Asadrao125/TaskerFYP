package com.example.taskerfyp.Models;

public class CustomerUser {

    private String customerUsername, customerPhonenumber , customerGender , customerEmail , id;

    public CustomerUser() {
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public String getCustomerPhonenumber() {
        return customerPhonenumber;
    }

    public void setCustomerPhonenumber(String customerPhonenumber) {
        this.customerPhonenumber = customerPhonenumber;
    }

    public String getCustomerGender() {
        return customerGender;
    }

    public void setCustomerGender(String customerGender) {
        this.customerGender = customerGender;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }


    public CustomerUser(String customerUsername, String customerPhonenumber, String customerGender, String customerEmail, String id) {
        this.customerUsername = customerUsername;
        this.customerPhonenumber = customerPhonenumber;
        this.customerGender = customerGender;
        this.customerEmail = customerEmail;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
