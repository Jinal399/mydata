package sample.kcs.com.mediamonitoring.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import sample.kcs.com.mediamonitoring.R;
import sample.kcs.com.mediamonitoring.databinding.ContentDashboardBinding;
import sample.kcs.com.mediamonitoring.databinding.DashactivityBinding;
import sample.kcs.com.mediamonitoring.databinding.NavHeaderDashboardBinding;
import sample.kcs.com.mediamonitoring.fragment.DashBoardFragment;
import sample.kcs.com.mediamonitoring.fragment.DsrFragment;
import sample.kcs.com.mediamonitoring.fragment.MediaMonitoringFragment;
import sample.kcs.com.mediamonitoring.fragment.MyTaskFragment;
import sample.kcs.com.mediamonitoring.fragment.MyTravelFragment;
import sample.kcs.com.mediamonitoring.fragment.NotificationFragment;
import sample.kcs.com.mediamonitoring.interfaces.APIService;
import sample.kcs.com.mediamonitoring.model.ApiUtils;
import sample.kcs.com.mediamonitoring.model.LogoutResponse;
import sample.kcs.com.mediamonitoring.model.MConstant;
import sample.kcs.com.mediamonitoring.model.ModelLoginRequest;
import sample.kcs.com.mediamonitoring.model.ModelLoginResponse;
import sample.kcs.com.mediamonitoring.model.NotificationRequest;
import sample.kcs.com.mediamonitoring.model.NotificationResponse;
import sample.kcs.com.mediamonitoring.model.RetrofitClient;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

DashactivityBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(NavigationActivity.this,R.layout.dashactivity);



        setSupportActionBar(mBinding.dashactivityInctop.toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mBinding.drawerLayout, mBinding.dashactivityInctop.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mBinding.drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mBinding.navView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.nav_dashboard);

    }

    @Override
    public void onBackPressed() {

        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment=null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_dashboard:
                fragment = new DashBoardFragment();
                break;
            case R.id.nav_mediamonitoring:
                fragment = new MediaMonitoringFragment();
                break;
            case R.id.nav_mydsr:
                fragment = new DsrFragment();
                break;
            case R.id.nav_mytravel:
                fragment = new MyTravelFragment();
                break;
            case R.id.nav_mytask:
                fragment = new MyTaskFragment();
                break;
            case R.id.nav_notification:
                fragment = new NotificationFragment();
                NotificationRequest notificationRequest=new NotificationRequest(1,"1");
                sendNetworkRequest(notificationRequest);
                break;
            case R.id.nav_logout:
                userLogout();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void userLogout() {
        SharedPreferences splogintoken=getSharedPreferences(LoginActivity.SPRELOGINTOKEN,Context.MODE_PRIVATE);
        String strlogintoken=splogintoken.getString(LoginActivity.KEYLOGINTOKEN,"");
        // Toast.makeText(getApplicationContext(),strlogintoken,Toast.LENGTH_LONG).show();
        SharedPreferences spmemberId=getSharedPreferences(LoginActivity.SPREMEMBERID,Context.MODE_PRIVATE);
        String strmemberid=spmemberId.getString(LoginActivity.KEYMEMBERID,"");

        APIService mApiService = ApiUtils.getAPIService();
        Call<LogoutResponse> call=mApiService.userLogout(strmemberid,strlogintoken);
        call.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                LogoutResponse resultModel = getResponse(response, LogoutResponse.class);
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),resultModel.getMessage(),Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"problem",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {

            }
        });
    }

    private void sendNetworkRequest(NotificationRequest notificationRequest) {
        SharedPreferences splogintoken=getSharedPreferences(LoginActivity.SPRELOGINTOKEN,Context.MODE_PRIVATE);
        String strlogintoken=splogintoken.getString(LoginActivity.KEYLOGINTOKEN,"");
        // Toast.makeText(getApplicationContext(),strlogintoken,Toast.LENGTH_LONG).show();
        SharedPreferences spmemberId=getSharedPreferences(LoginActivity.SPREMEMBERID,Context.MODE_PRIVATE);
        String strmemberid=spmemberId.getString(LoginActivity.KEYMEMBERID,"");


        APIService  mApiService = ApiUtils.getAPIService();


            String jsonData = new Gson().toJson(notificationRequest);
            Call<NotificationResponse> call= mApiService.getNotification(jsonData,strmemberid,strlogintoken);
            call.enqueue(new Callback<NotificationResponse>() {
                @Override
                public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                    NotificationResponse resultModel = getResponse(response, NotificationResponse.class);
                    if(response.isSuccessful()){
                        Toast.makeText(getApplication(),"Record Submitted"+resultModel.getResult().getNotificationEntries(),Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<NotificationResponse> call, Throwable t) {

                }
            });



        }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());

        return true;
    }
    public static <T> T getResponse(Response<T> tResponse, Class<T> tClass) {
        if (tResponse.code() > 199 && tResponse.code() < 300) {
            T t = tResponse.body();

            if (t == null) {
                t = new GsonBuilder().create().fromJson(createErrorMsgJson(), tClass);
            }
            return t;
        } else {
            Converter<ResponseBody, T> errorConverter =
                    RetrofitClient.getClient("http://180.211.99.238:8092/khushi_sta_qc/api/").responseBodyConverter(tClass, (java.lang.annotation.Annotation[]) new Annotation[0]);

            try {
                return errorConverter.convert(tResponse.errorBody());
            } catch (IOException e) {
                e.printStackTrace();
                return new GsonBuilder().create().fromJson(createErrorMsgJson(), tClass);
            }
        }

    }
    // dummy error message
    private static String createErrorMsgJson() {

        return "{\n" +
                "\"Status\": true,\n" +
                "\"StatusCode\": 0,\n" +
                "\"Message\": \"Due to network connection error we\\'re having trouble\"\n" +
                "}";
    }


}
