package com.app.trackingelement.Model;

import android.graphics.Bitmap;

public class ModelProfile
{

        String Image;
        String UserName;
        String UserEmail;
        String UserPassword;

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public String getUserCountry() {
        return UserCountry;
    }

    public void setUserCountry(String userCountry) {
        UserCountry = userCountry;
    }

    String UserCountry;

}
