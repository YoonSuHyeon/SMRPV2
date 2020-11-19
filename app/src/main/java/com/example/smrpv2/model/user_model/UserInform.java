package com.example.smrpv2.model.user_model;

public class UserInform {


    //static UserInform userInform;


    static String userId,email,name,gender, birth,createdAt;


    public UserInform(String userId, String email,String name,String gender, String birth,String createdAt) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.birth = birth;
        this.createdAt = createdAt;

    }
    static public String getUserId() {
        return userId;
    }

    static public String getEmail() {
        return email;
    }

    static public String getName() {
        return name;
    }

    static public String getGender() {
        return gender;
    }

    static public String getBirth() {
        return birth;
    }

    static public String getCreatedAt() {
        return createdAt;
    }

   /* public UserInform getInstance(){
        return userInform;
    }*/

}
