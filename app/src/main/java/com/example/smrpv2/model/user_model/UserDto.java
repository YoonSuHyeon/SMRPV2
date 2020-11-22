package com.example.smrpv2.model.user_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDto {




    @SerializedName("userId")//사용자 아이디
    String userId;
    @SerializedName("email")//사용자 이메일
    String email;
    @SerializedName("name")//사용자 이름
    String name;
    @SerializedName("gender")//사용자 성별
    String gender;
    @SerializedName("birth")//사용자 생일
    String birth;
    @SerializedName("createdAt")//계정 생성 날짜
    String createdAt;

    /*@SerializedName("regMedicineList")
    <L>
    @SerializedName("medicineAlarms")
    String email;*/






    public String getUserId(){
        return userId;
    }
    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getBirth() {
        return birth;
    }

    public String getCreatedAt() {
        return createdAt;
    }

   /* public UserDto getInstance(){
        return userDto;
    }*/
}
