package sample.mylocation.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sample.mylocation.R;
import sample.mylocation.databinding.ActivityDisplayLocationBinding;
import sample.mylocation.helper.Constants;
import sample.mylocation.model.MyTravelDatabaseHandler;
import sample.mylocation.model.MyTravelModel;



public class DisplayLocationActivity extends AppCompatActivity implements Constants {
    ActivityDisplayLocationBinding mBinding;
    Context context;
    MyTravelModel myTravelModel=new MyTravelModel();


    /**
     * Broadcast Receiver listening to Location updates and updating UI in activity
     */
   private LocationReceiver locationReceiver;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this, R.layout.activity_display_location);



        locationReceiver = new LocationReceiver();
    }



    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(DisplayLocationActivity.this)
                .setMessage(message)
                .setIcon(R.drawable.location)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }




    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                       ) {
                    // All Permissions Granted
                    startLocationService();
                } else {
                    // Permission Denied
                    Toast.makeText(DisplayLocationActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
        }
    }


    private void startLocationService() {

        Intent serviceIntent = new Intent(this, sample.mylocation.service.LocationService.class);
        startService(serviceIntent);

    }

    @Override
    protected void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(this).registerReceiver(locationReceiver, new IntentFilter(LOACTION_ACTION));
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("Location");


        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }
        startLocationService();
    }
    @Override
    protected void onStop() {
        super.onStop();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationReceiver);
    }

    private class LocationReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {

            /*Getting value from the service*/

            if (null != intent && intent.getAction().equals(LOACTION_ACTION)) {

                String locationData = intent.getStringExtra(LOCATION_MESSAGE);

                Gson gson=new Gson();
                 myTravelModel=gson.fromJson(locationData, MyTravelModel.class);

                StringBuilder sbLocationData = new StringBuilder();
                sbLocationData.append("Latitude::  ")
                                .append(myTravelModel.getStrLatitude())
                                .append("\n")
                                .append("Longitude::  ")
                                .append(myTravelModel.getStrlongitude())
                                 .append("\n")
                                .append("Date::  ")
                                .append(myTravelModel.getStrDate())
                                 .append("\n")
                                .append("Time::  ")
                                .append(myTravelModel.getStrTime())
                                 .append("\n")
                                .append("Distance::  ")
                                .append(myTravelModel.getStrDistance())
                                .append(" meters");



                mBinding.txtDisplayLocation.setText(sbLocationData);
                insertData();
            }

        }
    }
    public void insertData(){
        Log.d("Insert: ", "Inserting ..");

        MyTravelDatabaseHandler myTravelDatabaseHandler =new MyTravelDatabaseHandler(this);

        myTravelDatabaseHandler.addLocationDetails(new MyTravelModel(myTravelModel.getStrLatitude(),myTravelModel.getStrlongitude(),
                myTravelModel.getStrDate(),myTravelModel.getStrTime(),myTravelModel.getStrDistance()));
        Toast.makeText(getApplicationContext(),"Record inserted" + myTravelModel.getStrUserId() + ":::::" +myTravelModel.getStrLatitude(),Toast.LENGTH_SHORT).show();

    }



}
