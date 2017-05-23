package sample.sqliteexample;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;

import sample.sqliteexample.database.DbHelper;

/**
 * Created by Dell on 08-04-2017.
 */

public class SqliteApplication extends Application {

    public SQLiteDatabase sqLiteDatabase;

    public DbHelper mDbHelper;
    private SqliteApplication mAppilcation;


    Context mContext;
    public SqliteApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        mAppilcation=this;
        mDbHelper=new DbHelper(mContext);
        try {
            mDbHelper.createDataBase();
            sqLiteDatabase=    mDbHelper.openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
