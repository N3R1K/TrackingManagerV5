package com.app.trackingelement.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.trackingelement.Model.ModelProfile;
import com.app.trackingelement.R;
import com.app.trackingelement.SharedPref.SharedPref;
import com.app.trackingelement.ui.myprofile.myprofile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class EditProfile extends Fragment
{

    ImageView imageView,imgSelect;
    CardView CrdSave;


    EditText editTextUserName,editTextUserEmail,editTextPass;
    Spinner SpnrCountry;

    String pathToFile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       View root= inflater.inflate(R.layout.fragment_edit_profile, container, false);
       init(root);
       SetVals();
        return  root;
    }

    private void init(View root)
    {

        imageView=root.findViewById(R.id.imageView);

        imgSelect=root.findViewById(R.id.imgSelect);

        CrdSave=root.findViewById(R.id.CrdSave);

        editTextUserName=root.findViewById(R.id.editTextUserName);
        editTextPass=root.findViewById(R.id.edittextPass);
        editTextUserEmail=root.findViewById(R.id.editTextUserEmail);

        SpnrCountry=root.findViewById(R.id.spnrCountry);



        CrdSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveData();
            }
        });



        imgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(checkPermission())
                {
                    ShowDialogCaptRSelect();
                }


            }
        });


    }
    public  void ShowDialogCaptRSelect()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        View child = getLayoutInflater().inflate(R.layout.dialog_custom_2, null);
        final Button btn_capture=child.findViewById(R.id.btn_capture);
        final Button btn_Gallery=child.findViewById(R.id.btn_gallery);

        builder1.setView(child);
        builder1.setCancelable(true);
        final AlertDialog dialog = builder1.create();
        dialog.show();


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();

                if(v==btn_capture)
                {
                    dispatchPictureTakerAction();
                }
                if(v==btn_Gallery)
                {
                    Filechooser();
                }

            }
        };

        btn_capture.setOnClickListener(onClickListener);
        btn_Gallery.setOnClickListener(onClickListener);


    }

    private void Filechooser()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }
    private void dispatchPictureTakerAction() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createPhotoFile();
            } catch (IOException ex) {
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.app.trackingelement.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
            }
            else
            {
                startActivityForResult(takePictureIntent, 2);
            }

        }

    }

    private File createPhotoFile() throws  IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        pathToFile = image.getAbsolutePath();
        return image;
    }

    public  boolean checkPermission()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
            {
                requestPermissions(new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                return false;
            }



        }
        return true;
    }

    
    
    public  String convertImageToBase64(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = image;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return  imageString;
    }
    
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK)
        {

            Bitmap photo=(Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
        if (resultCode == RESULT_OK && requestCode==1 && data!=null &&  data.getData() !=null)
        {
            Uri imguri = data.getData();
            imageView.setImageURI(imguri);
        } else if (resultCode == RESULT_OK && requestCode==1 && pathToFile !=null)
        {
            Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
            imageView.setImageBitmap(bitmap);
        }

    }
    private void SaveData()
    {

            if(dataIsValidated())
            {

                ModelProfile obj=new ModelProfile();
                Drawable drawable = imageView.getDrawable();

                Bitmap bitmapFromDrawable = getBitmapFromDrawable(drawable);

                String s = convertImageToBase64(bitmapFromDrawable);

                imageView.setImageBitmap(bitmapFromDrawable);
               // bitmapFromDrawable.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                obj.setImage(s);

                obj.setUserName(editTextUserName.getText().toString());
                obj.setUserPassword(editTextPass.getText().toString());
                obj.setUserEmail(editTextUserEmail.getText().toString());
                obj.setUserCountry(SpnrCountry.getSelectedItem().toString());

                boolean Result = SharedPref.SaveUserProfile(getActivity(), obj);

                if(Result)
                {
                    Toast.makeText(getActivity(),getString(R.string.successeditelement),Toast.LENGTH_LONG).show();

                   if (getFragmentManager() != null) {
                        getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new myprofile()).commit();
                    }


                }
                else
                {
                    Toast.makeText(getActivity(),getString(R.string.failededit),Toast.LENGTH_LONG).show();
                }


            }

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

    public static Bitmap getBitmapFromDrawable (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private boolean dataIsValidated()
    {


        for(EditText editText:new EditText[]{editTextUserName,editTextUserEmail,editTextPass})
        {

            if(TextUtils.isEmpty(editText.getText()))
            {

                editText.setError(getString(R.string.fieldRequired));
                editText.requestFocus();
                return false;

            }


        }
        return true;

    }

    private void SetVals()
    {


        ModelProfile userProfile = SharedPref.getUserProfile(getActivity());




        SpnrCountry.setSelection(getIndexOfCountry(userProfile.getUserCountry()));


        editTextUserEmail.setText(userProfile.getUserEmail());

        editTextPass.setText(userProfile.getUserPassword());
        editTextUserName.setText(userProfile.getUserName());


        LoadImage(userProfile.getImage());
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
    private int getIndexOfCountry(String TargetCountry)
    {

        String[] stringArray = getActivity().getResources().getStringArray(R.array.countries_array);
        for(int i=0;i<stringArray.length;i++)
        {
            if(stringArray[i].equalsIgnoreCase(TargetCountry))
                return i;
        }

        return  0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    ShowDialogCaptRSelect();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), R.string.permissionDenied, Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
