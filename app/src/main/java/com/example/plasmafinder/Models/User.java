package com.example.plasmafinder.Models;

public class User {
    private String user_id;
    private String name;
    private String age;
    private String email;
    private String phone;

    public User(String user_id, String name, String age, String email, String phone) {
        this.user_id = user_id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }

    public User(){
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
