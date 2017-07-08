package sample.mylocation;

import android.*;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sample.mylocation.activity.DisplayLocationActivity;
import sample.mylocation.helper.Constants;
import sample.mylocation.model.MyTravelDatabaseHandler;
import sample.mylocation.model.MyTravelModel;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,Constants {

    private GoogleMap mMap;
    Context context;
    MyTravelModel myTravelModel=new MyTravelModel();
    Intent intent;


    /**
     * Broadcast Receiver listening to Location updates and updating UI in activity
     */
    private MapsActivity.LocationReceiver locationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationReceiver = new LocationReceiver();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MapsActivity.this)
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
                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        ) {
                    // All Permissions Granted
                    startLocationService();
                } else {
                    // Permission Denied
                    Toast.makeText(MapsActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
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
        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_COARSE_LOCATION))
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

                ArrayList<LatLng> points;
                PolylineOptions lineOptions = null;
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();


                LatLng myOldLocation;
                LatLng myNewLocation = new LatLng(myTravelModel.getStrLatitude(), myTravelModel.getStrlongitude());
                mMap.addMarker(new MarkerOptions().position(myNewLocation));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myNewLocation));

                LatLngBounds.Builder builder=new LatLngBounds.Builder();
                builder.include(myNewLocation).build();

                final LatLngBounds bounds=builder.build();

                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,30));
                    }
                });


                insertData();

                myOldLocation=new LatLng(myNewLocation.latitude,myNewLocation.longitude);
                myNewLocation=new LatLng(myTravelModel.getStrLatitude(), myTravelModel.getStrlongitude());
                if(myNewLocation!=null && myOldLocation!=null){
                    lineOptions.add(myOldLocation).add(myNewLocation);
                    lineOptions.width(10);
                    lineOptions.color(Color.RED);
                    if(lineOptions != null) {
                        mMap.addPolyline(lineOptions);
                    }


                }

               // Toast.makeText(getApplicationContext(),"Total:"+myTravelModel.getStrDistance()+"km",Toast.LENGTH_SHORT).show();
               /* points.add(myLocation);
                int pSize=points.size();
                if(pSize>=2){
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(Color.RED);

                    if(lineOptions != null) {
                        mMap.addPolyline(lineOptions);
                    }

                }*/



//                PolylineOptions line=new PolylineOptions().add(myLocation).width(10).color(Color.RED);
//
//                mMap.addPolyline(line);



               // mBinding.txtDisplayLocation.setText(sbLocationData);

                /*StringBuilder sbLocationData = new StringBuilder();
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
                        .append(" meters");*/



            }

        }
    }
    public void insertData(){
        Log.d("Insert: ", "Inserting ..");

        MyTravelDatabaseHandler myTravelDatabaseHandler =new MyTravelDatabaseHandler(this);

        myTravelDatabaseHandler.addLocationDetails(new MyTravelModel(myTravelModel.getStrLatitude(),myTravelModel.getStrlongitude(),
                myTravelModel.getStrDate(),myTravelModel.getStrTime(),myTravelModel.getStrDistance()));
        Toast.makeText(getApplicationContext(),"Record inserted" + myTravelModel.getStrlongitude() + ":::::" +myTravelModel.getStrLatitude(),Toast.LENGTH_SHORT).show();


    }





    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //locationReceiver.onReceive(this,intent);

        // Add a marker in Sydney and move the camera

    }
}
