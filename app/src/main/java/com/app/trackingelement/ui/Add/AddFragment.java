package com.app.trackingelement.ui.Add;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.app.trackingelement.Database.Database;
import com.app.trackingelement.Model.ModelElement;
import com.app.trackingelement.R;

public class AddFragment extends Fragment implements View.OnClickListener {



    EditText editTextTitle,editTextValue,editTextCSV;
    Spinner spnrUnit;
    Button btnAdd;


    boolean isEdit=false;
    String editId=null;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View root = inflater.inflate(R.layout.fragment_add, container, false);

        init(root);



        if(getArguments()!=null)
        {
            assignEditValues();
        }

        return root;
    }

    private void assignEditValues()
    {

        try
        {


            isEdit=true;
            editId=getArguments().getString("ID");
            editTextTitle.setText(getArguments().getString("Title"));
            editTextValue.setText(getArguments().getString("Value"));
            editTextCSV.setText(getArguments().getString("CSV"));
            String unit = getArguments().getString("Unit");
            int Index=getIndexOfUnit(unit);
            spnrUnit.setSelection(Index);




        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }


    }

    private int getIndexOfUnit(String unit)
    {

        String[] stringArray = getActivity().getResources().getStringArray(R.array.SpnRUnitValues);
        for(int i=0;i<stringArray.length;i++)
        {
            if(stringArray[i].equalsIgnoreCase(unit))
                return i;
        }

        return  0;
    }

    private void init(View root)
    {

        isEdit=false;

        // EDITTEXT
        editTextTitle=root.findViewById(R.id.edittextTitle);
        editTextValue=root.findViewById(R.id.edittextValue);
        editTextCSV=root.findViewById(R.id.edittextCSV);


        // BUTTON
        btnAdd=root.findViewById(R.id.btnAdd);



        //SPINNER (DROP DOWN LIST)
        spnrUnit=root.findViewById(R.id.SpnRUnitValues);



        //ONCLICKLISTENER FOR ADD BUTTON
        btnAdd.setOnClickListener(this);








    }

    @Override
    public void onClick(View v)
    {


        if(v==btnAdd)
        {
            if(isDataValid())
            {
                addDataInDB();
            }
        }


    }

    private void addDataInDB()
    {


        //OBJECTS
        ModelElement obj=new ModelElement();
        Database DbObj=new Database(getActivity());


        obj.setTitle(editTextTitle.getText().toString());
        obj.setCSV(editTextCSV.getText().toString());
        obj.setValue(editTextValue.getText().toString());
        obj.setUnit(spnrUnit.getSelectedItem().toString());


        boolean Result=false;
        String SuccessMessage=null;
        String ErrorMessage=null;
        if(isEdit)
        {
            Result= DbObj.editTrackingElement(obj,editId);
            SuccessMessage=getString(R.string.successeditelement);
            ErrorMessage=getString(R.string.failededit);

        }
        else
        {
            Result= DbObj.addTrackingElement(obj);
            SuccessMessage=getActivity().getString(R.string.succesSaved);
            ErrorMessage=getActivity().getString(R.string.failedtoSave);

        }

        if(Result)
        {
            Toast.makeText(getActivity(),SuccessMessage,Toast.LENGTH_SHORT).show();



            StringBuilder builder=new StringBuilder();
            builder.append(getString(R.string.title)+obj.getTitle());
            builder.append(getString(R.string.value)+obj.getValue());
            builder.append(getString(R.string.unit)+obj.getUnit());
            builder.append(getString(R.string.changevalue)+obj.getCSV());
            ShowNotification(getActivity(),builder.toString());
        }
        else
        {
            Toast.makeText(getActivity(),ErrorMessage,Toast.LENGTH_SHORT).show();
        }


    }


    public void ShowNotification(Context context,String Data) {

        try{


            Intent intent = getActivity().getIntent();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            int notificationId = 1;
            String channelId = "001122";
            String channelName = "Notification";
            Bitmap largeicon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.bigText("New Element Added Successfully \n"+Data);
            bigText.setBigContentTitle("Tracking Manager");
            bigText.setSummaryText("Tracking Manager");
            int importance = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                importance = NotificationManager.IMPORTANCE_HIGH;
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, importance);
                notificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Tracking Manager")
                    .setLargeIcon(largeicon)
                    .setStyle(bigText)
                    .setContentText("New Element Added Successfully \n"+Data);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(intent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            mBuilder.setContentIntent(resultPendingIntent);
            notificationManager.notify(notificationId, mBuilder.build());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean isDataValid() //Check If Any TextFiled Is Empty
    {

        for(EditText editText:new EditText[]{editTextTitle,editTextValue,editTextCSV})
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
}
