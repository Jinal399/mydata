package sample.kcs.com.mediamonitoring.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import sample.kcs.com.mediamonitoring.R;
import sample.kcs.com.mediamonitoring.databinding.ActivityLoginBinding;
import sample.kcs.com.mediamonitoring.interfaces.APIService;
import sample.kcs.com.mediamonitoring.model.ApiUtils;
import sample.kcs.com.mediamonitoring.model.MConstant;
import sample.kcs.com.mediamonitoring.model.ModelLoginRequest;
import sample.kcs.com.mediamonitoring.model.ModelLoginResponse;
import sample.kcs.com.mediamonitoring.model.RetrofitClient;

/**
 * Created by Dell on 11-04-2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    ActivityLoginBinding mBindind;

    String deviceType;
    public static final String SPRELOGINTOKEN="spreflogintoken";
    public static final String SPREMEMBERID="sprememberid";
    public static final String KEYLOGINTOKEN="logintoken";
    public static final String KEYMEMBERID="memberid";





    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBindind= DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);
        mBindind.loginBtnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==mBindind.loginBtnLogin){
             deviceType="A";

            ModelLoginRequest login=new ModelLoginRequest(deviceType,
                    mBindind.loginEdtMno.getText().toString().trim(),
                    mBindind.loginEdtName.getText().toString().trim());

                    sendNetworkRequest(login);

        }

    }

    private void sendNetworkRequest(ModelLoginRequest login) {

        APIService  mApiService = ApiUtils.getAPIService();

        if (!TextUtils.isEmpty(mBindind.loginEdtMno.getText()) && !TextUtils.isEmpty(mBindind.loginEdtName.getText()) && !TextUtils.isEmpty(deviceType)) {
            String jsonData = new Gson().toJson(login);
            Call<ModelLoginResponse> call= mApiService.createLogin(jsonData);


                    call.enqueue(new Callback<ModelLoginResponse>() {
                        @Override
                        public void onResponse(Call<ModelLoginResponse> call, Response<ModelLoginResponse> response) {
                            ModelLoginResponse resultModel = getResponse(response, ModelLoginResponse.class);
                            if(response.isSuccessful()){

                                Toast.makeText(getApplication(),"Record Submitted"+ resultModel.getResult().getLoginToken(),Toast.LENGTH_LONG).show();
                                String otp=Integer.toString(resultModel.getResult().getOtp());
                                String strlogintoken=resultModel.getResult().getLoginToken();
                                String strmemberId=resultModel.getResult().getMemberId();

                                SharedPreferences sploginToken=getSharedPreferences(SPRELOGINTOKEN,Context.MODE_PRIVATE);
                                SharedPreferences.Editor edtLoginToken=sploginToken.edit();
                                edtLoginToken.putString(KEYLOGINTOKEN,strlogintoken);
                                edtLoginToken.commit();

                                SharedPreferences spMemberId=getSharedPreferences(SPREMEMBERID,Context.MODE_PRIVATE);
                                SharedPreferences.Editor edtMemberId=spMemberId.edit();
                                edtMemberId.putString(KEYMEMBERID,strmemberId);
                                edtMemberId.commit();


                                Intent otpIntent=new Intent(LoginActivity.this,OtpActivity.class);
                                otpIntent.putExtra(MConstant.OTP,otp);
                                startActivity(otpIntent);
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"problem ouccur",Toast.LENGTH_LONG).show();
                            }


                        }

                        @Override
                        public void onFailure(Call<ModelLoginResponse> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),"something wrong done",Toast.LENGTH_LONG).show();
                        }
                    });
        }
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
