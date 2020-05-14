package com.app.trackingelement.ui.myprofile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.app.trackingelement.Model.ModelProfile;
import com.app.trackingelement.R;
import com.app.trackingelement.SharedPref.SharedPref;
import com.app.trackingelement.ui.EditProfile;
import com.app.trackingelement.ui.Login;

import java.util.Locale;

public class myprofile extends Fragment {



    CardView CrdEdit,CrdLogout;
    ImageView imageView;
    Spinner spnrLanguage;
    boolean firstRun=true;
    TextView txtUserName,txtUserEmail,txtUserCountry;
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_myprofile, container, false);
        init(root);
        SetVals();
        return root;
    }

    private void init(View root)
    {





        imageView=root.findViewById(R.id.imageView);
        txtUserName=root.findViewById(R.id.txtUserName);
        txtUserEmail=root.findViewById(R.id.txtUserEmail);
        txtUserCountry=root.findViewById(R.id.txtUserCountry);




        CrdEdit=root.findViewById(R.id.CrdEdit);
        CrdLogout=root.findViewById(R.id.CrdLogout);


        CrdLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().finish();
                startActivity(new Intent(getActivity(), Login.class));

            }
        });

        CrdEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new EditProfile()).commit();
                }
            }
        });




        //SPINNER (DROP DOWN LIST)
        spnrLanguage=root.findViewById(R.id.spnrLanguage);
        final String getlang = SharedPref.getlang(getActivity());

        if(getlang.equals("en"))
            spnrLanguage.setSelection(0);
        else
            spnrLanguage.setSelection(1);




        spnrLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                if(spnrLanguage.getSelectedItemPosition()==0)
                {
                    changeLocale("en");
                }
                else
                {
                    changeLocale("de");
                }


                //REFRESH ALL ACTVITIES

                if(!firstRun)
                {
                    getActivity().finish();
                    startActivity(getActivity().getIntent());

                }
                else
                    firstRun=false;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void SetVals()
    {


        ModelProfile userProfile = SharedPref.getUserProfile(getActivity());
        txtUserCountry.setText(userProfile.getUserCountry());
        txtUserEmail.setText(userProfile.getUserEmail());
        txtUserName.setText(userProfile.getUserName());





        LoadImage(userProfile.getImage());
    }
    public void changeLocale(String lang)
    {

        SharedPref.saveLang(getActivity(),lang);
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
    private void LoadImage(final String image)
    {

        final ProgressDialog pb=new ProgressDialog(getActivity());
        pb.setMessage("Loading Image..");
        pb.setCancelable(false);
        pb.show();
        new Thread()
        {
            @Override
            public void run()
            {


                final Bitmap bitmapFromBase64 = getBitmapFromBase64(image);
               if(getActivity()!=null)
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap( bitmapFromBase64);
                        pb.dismiss();
                    }
                });


            }
        }.start();



    }

    public Bitmap getBitmapFromBase64(String base64String)
    {
        try {

            return BitmapFactory.decodeByteArray(Base64.decode(base64String, Base64.DEFAULT), 0, Base64.decode(base64String, Base64.DEFAULT).length);
        }

        catch (Exception e)
        {
            e.printStackTrace();
            return   BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.userpic);
        }

    }
}
