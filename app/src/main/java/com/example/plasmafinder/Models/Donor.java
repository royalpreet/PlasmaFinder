package com.example.plasmafinder.Models;

import java.util.Date;

public class Donor {
    private String name_of_donor;
    private String age;
    private String blood_group;
    private String phone;
    private String email;
    private String description;
    private Date timestamp;
    private String donor_id;
    private String user_id;

    public Donor(String name_of_donor, String age, String blood_group, String phone, String email, String description, Date timestamp, String donor_id, String user_id) {
        this.name_of_donor = name_of_donor;
        this.age = age;
        this.blood_group = blood_group;
        this.phone = phone;
        this.email = email;
        this.description = description;
        this.timestamp = timestamp;
        this.donor_id = donor_id;
        this.user_id = user_id;
    }

    public Donor() {
    }

    public String getName_of_donor() {
        return name_of_donor;
    }

    public void setName_of_donor(String name_of_donor) {
        this.name_of_donor = name_of_donor;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getDonor_id() {
        return donor_id;
    }

    public void setDonor_id(String donor_id) {
        this.donor_id = donor_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}

