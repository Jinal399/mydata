package sample.mylocation.helper;

/**
 * Created by Dell on 29-03-2017.
 */

public interface Constants {
    // All Static variables
    // Database Version
    int DATABASE_VERSION = 1;

    // Database Name
     String DATABASE_NAME = "khushiDatabase";

    // Contacts table name
     String TABLE_CONTACTS = "myTravel";

    // myTravel Table Columns names

     String KEY_ID="userId";
     String KEY_LATITUDE="latitide";
     String KEY_LONGITUDE="longitude";
     String KEY_DATE="date";
     String KEY_TIME="time";
     String KET_DISTANCE="distance";


    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
     long UPDATE_INTERVAL_IN_MILLISECONDS = 30000;

    /**
     * If accuracy is lesser than 100m , discard it
     */
     int ACCURACY_THRESHOLD = 100;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
     long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;


    /**
     * Broadcast Receiver Action to update location
     */
     String LOACTION_ACTION = "sample.mylocation.showmedistance.LOCATION_ACTION";

    /**
     * Message key for data with in the broadcast
     */
     String LOCATION_MESSAGE = "sample.myLocation.showmedistance.LOCATION_DATA";


     int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 123;

}