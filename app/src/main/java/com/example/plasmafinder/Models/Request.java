package com.example.plasmafinder.Models;

import java.util.Date;

public class Request {
    private String name_of_patient;
    private String age;
    private String blood_group;
    private String phone;
    private String email;
    private String description;
    private Date timestamp;
    private String request_id;
    private String user_id;

    public Request(String name_of_patient, String age, String blood_group, String phone, String email, String description, Date timestamp, String request_id, String user_id) {
        this.name_of_patient = name_of_patient;
        this.age = age;
        this.blood_group = blood_group;
        this.phone = phone;
        this.email = email;
        this.description = description;
        this.timestamp = timestamp;
        this.request_id = request_id;
        this.user_id = user_id;
    }

    public Request() {
    }

    public String getName_of_patient() {
        return name_of_patient;
    }

    public void setName_of_patient(String name_of_patient) {
        this.name_of_patient = name_of_patient;
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

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
