package sample.mylocation.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import sample.mylocation.helper.Constants;

/**
 * Created by Dell on 29-03-2017.
 */

public class MyTravelDatabaseHandler extends SQLiteOpenHelper implements Constants{


    public MyTravelDatabaseHandler(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT," + KEY_DATE + "TEXT," +KEY_TIME + "TEXT," + KET_DISTANCE +"TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void addLocationDetails(MyTravelModel myTravelModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE, myTravelModel.getStrLatitude());
        values.put(KEY_LONGITUDE,myTravelModel.getStrlongitude());
        values.put(KEY_DATE,myTravelModel.getStrDate());
        values.put(KEY_TIME,myTravelModel.getStrTime());
        values.put(KET_DISTANCE,myTravelModel.getStrDistance());

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }


}
