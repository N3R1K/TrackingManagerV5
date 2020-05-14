package com.app.trackingelement.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.trackingelement.Model.ModelElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Database extends SQLiteOpenHelper {
    private static final String DBNAME="TrackingElement.db";
    private static final int DBVERSION=1;
    private SQLiteDatabase db;
    public Database (Context context)
    {
        super(context,DBNAME,null,DBVERSION);
    }


    /*
     private  String ID;
    private  String Unit;
    private  String Title;
    private  String Value;
    private  String CSV;
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("Create table  if not exists 'Element' ( ID INTEGER PRIMARY KEY   AUTOINCREMENT,Unit VARCHAR(20),Title TEXT,Value TEXT,CSV TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF  EXISTS 'Element'");
        onCreate(db);
    }

    public   ArrayList<ModelElement> getAllElements()
    {
        ArrayList<ModelElement> list=new ArrayList<>();
        try
        {
            db = this.getReadableDatabase();
            String[] Columns = { "ID","Unit","Title","Value","CSV"};

            try (Cursor cursor = db.query("Element", Columns, null,null,null, null, null))
            {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
                {
                    ModelElement obj=new ModelElement();
                    obj.setID(String.valueOf(cursor.getInt(cursor.getColumnIndex("ID"))));
                    obj.setValue(cursor.getString(cursor.getColumnIndex("Value")));
                    obj.setUnit(cursor.getString(cursor.getColumnIndex("Unit")));
                    obj.setCSV(cursor.getString(cursor.getColumnIndex("CSV")));
                    obj.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                    list.add(obj);
                }
            }
            return list;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return list;
        }

    }
    public  boolean addTrackingElement(ModelElement toAdd)
    {
        db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            //String[] Columns = { "ID","Unit","Title","Value","CSV"};
            cv.put("Unit", toAdd.getUnit());
            cv.put("Title", toAdd.getTitle());
            cv.put("Value", toAdd.getValue());
            cv.put("CSV", toAdd.getCSV());
            long element = db.insert("Element", null, cv);


            return element > 0;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }


    public  boolean editTrackingElement(ModelElement toAdd,String ID)
    {
        db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            //String[] Columns = { "ID","Unit","Title","Value","CSV"};
            cv.put("Unit", toAdd.getUnit());
            cv.put("Title", toAdd.getTitle());
            cv.put("Value", toAdd.getValue());
            cv.put("CSV", toAdd.getCSV());
            long element = db.update("Element", cv, "ID=?", new String[]{ID});
            return element > 0;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }




    public  boolean deleteTrackingElement(String ID){
        try {
            db = this.getWritableDatabase();
            String Database=null;
            Database="Element";
            int x = 0;

                x = db.delete(Database, "ID=\"" + ID + "\"", null);

                return x>0;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}

