package com.app.trackingelement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;

import com.app.trackingelement.SharedPref.SharedPref;
import com.app.trackingelement.ui.Login;

import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        changeLocale(SharedPref.getlang(SplashScreen.this));

        new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish()
            {

                finish();
                startActivity(new Intent(SplashScreen.this, Login.class));
            }
        }.start();
    }
    public void changeLocale(String lang)
    {


        System.out.println("LANG :"+lang);
        SharedPref.saveLang(SplashScreen.this,lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            conf.setLocale(new Locale(lang.toLowerCase())); // API 17+ only.
        }
        else
            conf.locale = new Locale(lang.toLowerCase());
        res.updateConfiguration(conf, dm);
    }
}
