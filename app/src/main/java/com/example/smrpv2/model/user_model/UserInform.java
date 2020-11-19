package com.example.smrpv2.model.user_model;

public class UserInform {
    
    static String userId,email,name,gender, birth,createdAt;
    
    public UserInform(String userId, String email,String name,String gender, String birth,String createdAt) {
        this.userId = userId; //사용자 ID
        this.email = email; //사용자 Email
        this.name = name; //사용자 이름
        this.gender = gender; //사용자 성별
        this.birth = birth; //사용자 생년월일
        this.createdAt = createdAt; //계정 생성연월일

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
    
}
