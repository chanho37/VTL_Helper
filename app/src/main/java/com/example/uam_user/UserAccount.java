package com.example.uam_user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class UserAccount  {

    private String idToken;
    private String emailId;
    private String password;

    public UserAccount(){
    }

    public String getIdToken(){
        return idToken;
    }

    public void setIdToken(String idToken){
        this.idToken = idToken;
    }


    public String getemailId(){
        return emailId;
    }

    public void setEmailId(String emailId){
        this.emailId = emailId;
    }


    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

}