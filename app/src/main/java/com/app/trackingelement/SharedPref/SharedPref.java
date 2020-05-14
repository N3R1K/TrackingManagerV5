package com.app.trackingelement.SharedPref;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.app.trackingelement.Model.ModelElement;
import com.app.trackingelement.Model.ModelProfile;
import com.app.trackingelement.R;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;

import static android.content.Context.MODE_PRIVATE;

public class SharedPref
{





    public static boolean SaveUserProfile(Context ac, ModelProfile userRecord)
    {
        try
        {

            SharedPreferences sharedPreferences = ac.getSharedPreferences("userRecord", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(userRecord);
            editor.putString("userRecord", json);
            editor.apply();
            return true;
        }
        catch (Exception e)
         {
             e.printStackTrace();
             return false;

         }

    }

    public static ModelProfile getUserProfile(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userRecord",  MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("userRecord", null);
        Type type = new TypeToken<ModelProfile>() {}.getType();
        ModelProfile userRecord = gson.fromJson(json, type);
        if (userRecord == null)
        {
            userRecord=getDefaultRecord(context);
        }
        return userRecord;
    }

    private static ModelProfile getDefaultRecord(Context context)
    {

            ModelProfile userRecord=new ModelProfile();

            userRecord.setUserCountry("USA");
            userRecord.setUserEmail("admin@gmail.com");
            userRecord.setUserPassword("admin");
            userRecord.setUserName("Admin");
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.userpic);
            userRecord.setImage(convertImageToBase64(bitmap));
            return userRecord;
    }
        public static String convertImageToBase64(Bitmap image)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap bitmap = image;
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return  imageString;
        }

    public static  String getlang(Context context)
    {
        SharedPreferences mPrefs = context.getSharedPreferences("Lang",MODE_PRIVATE);
        String lang = mPrefs.getString("Lang", "en");
        return lang;
    }

    public  static  void saveLang(Context context,String Lang)
    {

        SharedPreferences mPrefs = context.getSharedPreferences("Lang",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString("Lang", Lang);
        prefsEditor.apply();
    }

}
