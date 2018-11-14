package com.example.lara.sing;

/**
 * Created by Lara on 5/10/2018.
 */

public class User {

    public User(){}

    private String fullName;
    private String gender;
    private String dob;
    private String email;
    private String password;
    private String country;
    private String date;
    private String firstName;
    private String lastName;
    int userId,totalRec, totalClaps;

//    // Full Name, Gender , Age, Email , Pass, Country, TotalRecords, date
//    public User(String fullName,String gender,String age, String email,String password, String country,int totalRec, int totalClaps, String date)
//    {
//        this.fullName = fullName;
//        this.gender = gender;
//        this.age = age;
//        this.email = email;
//        this.password  =password;
//        this.country = country;
//        this.totalRec = totalRec;
//        this.totalClaps = totalClaps;
//        this.date = date;
//    }

//    public User(String email, String password){
//        this.email = email;
//        this.password = password;
//        fullName="";
//        gender="";
//        age="";
//        country="";
//        totalRec=-1;
//        date="";
//    }

//    public User(int userId,String fullName,String gender, String age, String email,String password, String country,int totalRec, int totalClaps, String date)
//    {   this.userId = userId;
//        this.fullName = fullName;
//        this.gender = gender;
//        this.age = age;
//        this.email = email;
//        this.password = password;
//        this.country = country;
//        this.totalRec = totalRec;
//        this.totalClaps = totalClaps;
//        this.date = date;
//
//    }


    public User(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;

    }

    public User(int userId, String firstName, String lastName){
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;

    }


    public User(String firstName, String lastName, String country, String dob, String gender){
        this.firstName= firstName;
        this.lastName = lastName;
        this.country = country;
        this.dob = dob;
        this.gender = gender;

    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
