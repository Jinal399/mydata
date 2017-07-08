package sample.mylocation.service;

import android.Manifest;
import android.app.Service;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.Date;

import sample.mylocation.R;
import sample.mylocation.helper.Constants;
import sample.mylocation.model.MyTravelModel;

/**
 * Created by Dell on 27-03-2017.
 */

public class LocationService extends Service implements Constants, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = LocationService.class.getSimpleName();

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;


    private String mLatitudeLabel;
    private String mLongitudeLabel;
    private String mLastUpdateTimeLabel;
    private String mDistance;


    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;
    protected String mDate;


    private Location oldLocation;

    private Location newLocation;


    /**
     * Total distance covered
     */
    private float distance;

    @Override
    public void onCreate() {
        super.onCreate();
        oldLocation = new Location("Point A");
        newLocation = new Location("Point B");

        mLatitudeLabel = getString(R.string.latitude_label);
        mLongitudeLabel = getString(R.string.longitude_label);
        mLastUpdateTimeLabel = getString(R.string.last_update_time_label);
        mDistance = getString(R.string.distance);
        mDate = "Date";

        mLastUpdateTime = "";

        Log.d(TAG, "onCreate Distance: " + distance);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        buildGoogleApiClient();

        mGoogleApiClient.connect();

        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }

        return START_STICKY;

    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {

        try {

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

        } catch (SecurityException ex) {


        }
    }


    /**
     * Updates the latitude, the longitude, and the last location time in the UI.
     */
    private void updateUI() {

        if (null != mCurrentLocation) {


            MyTravelModel myTravelModel = new MyTravelModel();
            myTravelModel.setStrLatitude(mCurrentLocation.getLatitude());
            myTravelModel.setStrlongitude(mCurrentLocation.getLongitude());
            myTravelModel.setStrDate(mDate);
            myTravelModel.setStrTime(mLastUpdateTime);
            myTravelModel.setStrDistance(getUpdatedDistance());
            Gson gson = new Gson();
            String jsonInString = gson.toJson(myTravelModel);
            sendLocationBroadcast(jsonInString.toString());


        } else {

            Toast.makeText(this, R.string.unable_to_find_location, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Send broadcast using LocalBroadcastManager to update UI in activity
     *
     * @param sbLocationData
     */
    private void sendLocationBroadcast(String sbLocationData) {

        Intent locationIntent = new Intent();
        locationIntent.setAction(LOACTION_ACTION);
        locationIntent.putExtra(LOCATION_MESSAGE, sbLocationData);

        LocalBroadcastManager.getInstance(this).sendBroadcast(locationIntent);

    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    @Override
    public void onDestroy() {

        //appPreferences.putFloat(PREF_DISTANCE, distance);

        stopLocationUpdates();

        mGoogleApiClient.disconnect();

        Log.d(TAG, "onDestroy Distance " + distance);


        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Connected to GoogleApiClient");


        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            mDate=DateFormat.getDateInstance().format(new Date());
            updateUI();
        }

        startLocationUpdates();

    }


    @Override
    public void onConnectionSuspended(int i) {

        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        mDate=DateFormat.getDateInstance().format(new Date());
        updateUI();

    }
    private float getUpdatedDistance() {

        /**
         * There is 68% chance that user is with in 100m from this location.
         * So neglect location updates with poor accuracy
         */


        if (mCurrentLocation.getAccuracy() > ACCURACY_THRESHOLD) {

            return distance;
        }


        if (oldLocation.getLatitude() == 0 && oldLocation.getLongitude() == 0) {

            oldLocation.setLatitude(mCurrentLocation.getLatitude());
            oldLocation.setLongitude(mCurrentLocation.getLongitude());

            newLocation.setLatitude(mCurrentLocation.getLatitude());
            newLocation.setLongitude(mCurrentLocation.getLongitude());

            return distance;
        } else {

            oldLocation.setLatitude(newLocation.getLatitude());
            oldLocation.setLongitude(newLocation.getLongitude());

            newLocation.setLatitude(mCurrentLocation.getLatitude());
            newLocation.setLongitude(mCurrentLocation.getLongitude());

        }


        /**
         * Calculate distance between last two geo locations
         */
        distance += newLocation.distanceTo(oldLocation);

        return distance;
    }
}
