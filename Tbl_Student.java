package sample.sqliteexample.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import sample.sqliteexample.SqliteApplication;
import sample.sqliteexample.model.StudentModel;

/**
 * Created by Dell on 08-04-2017.
 */

public class Tbl_Student {
    public static final String TableName = "Student";
    public static final String  _ID = "_Id";
    public static final String  NAME = "Name";
    public static final String  ADDRESS = "Address";
    public static final String  SCHOOLNAME = "SchoolName";
    public static final String  DIVISION = "Division";
    public static final String  PHONENO = "PhoneNo";
    public static String TAG= "Tbl_Student";
    SQLiteDatabase sqldb;

    public Tbl_Student(Context context) {

        sqldb = ((SqliteApplication) context.getApplicationContext()).sqLiteDatabase;
    }

    public ArrayList<StudentModel> SelectAll(){
        ArrayList<StudentModel> arrModelList=null;
        Cursor cursor =null;
        String Query="Select * from "+TableName;
        Log.i(TAG,Query);
        cursor = sqldb.rawQuery(Query, null);
        if(cursor !=null && cursor.moveToFirst()){
            arrModelList=new ArrayList<StudentModel>();
            do{
                StudentModel  model =GetModel(cursor);
                arrModelList.add(model);
            } while (cursor.moveToNext());
            cursor.close();
        }//end if(cursor!=null)
        return arrModelList;
    }
    public void Insert(ArrayList<StudentModel> arrListModel){
        sqldb.beginTransaction();
        for (StudentModel model : arrListModel)
        {
            ContentValues values=getContentValues(model);
            sqldb.insert(TableName, null, values);
        }
        sqldb.setTransactionSuccessful();
        sqldb.endTransaction();
    }//End insert method
    public StudentModel  GetModel(Cursor cursor)
    {
        StudentModel model = new StudentModel();
        model._Id = (cursor.getInt(cursor.getColumnIndex(_ID)));
        model.Name = (cursor.getString(cursor.getColumnIndex(NAME)));
        model.Address = (cursor.getString(cursor.getColumnIndex(ADDRESS)));
        model.SchoolName = (cursor.getString(cursor.getColumnIndex(SCHOOLNAME)));
        model.Division = (cursor.getString(cursor.getColumnIndex(DIVISION)));
        model.PhoneNo = (cursor.getString(cursor.getColumnIndex(PHONENO)));
        return model;
    }
    public ContentValues getContentValues(StudentModel model){
        ContentValues values = new ContentValues();
        values.put(_ID,model._Id);
        values.put(NAME,model.Name);
        values.put(ADDRESS,model.Address);
        values.put(SCHOOLNAME,model.SchoolName);
        values.put(DIVISION,model.Division);
        values.put(PHONENO,model.PhoneNo);
        return values;
    }
    public int deleteAll() {
        int row = sqldb.delete(TableName, null, null);
        return row;
    }
}
