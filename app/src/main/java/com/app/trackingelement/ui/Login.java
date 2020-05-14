package com.app.trackingelement.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.trackingelement.MainActivity;
import com.app.trackingelement.Model.ModelProfile;
import com.app.trackingelement.R;
import com.app.trackingelement.SharedPref.SharedPref;

public class Login extends AppCompatActivity
{




    AutoCompleteTextView editTextUserName,editTextUserPassword;
    Button btn_sign_in;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        init();
    }

    private void init()
    {

        editTextUserName=findViewById(R.id.edt_user_name);
        editTextUserPassword=findViewById(R.id.edt_password);
        btn_sign_in=findViewById(R.id.btn_sign_in);


        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(dataValidated())
                {

                    Authenticate();

                }

            }
        });

    }

    private boolean dataValidated()
    {

        for(AutoCompleteTextView editText:new AutoCompleteTextView[]{editTextUserName,editTextUserPassword})
        {
            if(TextUtils.isEmpty(editText.getText().toString()))
            {
                editText.setError(getString(R.string.fieldRequired));
                editText.requestFocus();
                return false;
            }

        }

        return true;
    }

    private void Authenticate()
    {


        ModelProfile userProfile = SharedPref.getUserProfile(this);


        if(editTextUserName.getText().toString().trim().equalsIgnoreCase(userProfile.getUserEmail().trim()))
        {

            if(editTextUserPassword.getText().toString().equals(userProfile.getUserPassword()))
            {

                finish();
                startActivity(new Intent(Login.this, MainActivity.class));

                return;
            }

        }

        Toast.makeText(Login.this,"UserName Or Password Is Incorrect",Toast.LENGTH_SHORT).show();
    }


}
