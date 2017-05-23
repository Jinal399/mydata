package co.example.dell.callcontrol;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.DomainCombiner;

/**
 * Created by Dell on 11-01-2017.
 */

public class OtpActivity extends BaseActivity implements Constants{

    private Button btnverify;
      public static SharedPreferences sharedprefrence;
    Context con=this;
    TextInputLayout tilotp;
    Handler handler=new Handler();
    public static String email;
    String otp;
    EditText etotp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_activity);

        prepareViews();
        setListeners();
        initComponents();

    }


    public void prepareViews(){

        tilotp= (TextInputLayout) findViewById(R.id.tilotp);
        btnverify= (Button) findViewById(R.id.btnverify);
        etotp=(EditText) findViewById(R.id.etotp);

    }

    public void setListeners() {

        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(!isValidate()){

                    return;
                }
                tilotp.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        if(s.length()>0&&s.length()<4){
                            tilotp.setError(getString(R.string.error_invalid)+" "+tilotp.getHint());

                        }
                        else {
                            tilotp.setError(null);
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                Bundle b = getIntent().getExtras();
                if(b!=null)
                {
                     email = b.getString("key");

                }
                otp=etotp.getText().toString().trim();
                startAsync();

            }
        });



    }


    public boolean isValidate() {

        boolean isValid=true;

        if(TextUtils.isEmpty(tilotp.getEditText().getText().toString().trim())){

            isValid=false;
            tilotp.setError(tilotp.getHint()+" "+getString(R.string.error_empty));
        }
        else{



            if(!Patterns.PHONE.matcher(tilotp.getEditText().getText().toString().trim()).matches()){

                isValid=false;
                tilotp.setError(getString(R.string.error_invalid)+" "+tilotp.getHint());
            }
        }

        return isValid;



    }
    public void startAsync() {
            new DownloadImageTask().execute(DOMAIN_NAME);
       // new DownloadImageTask().execute(DOMAIN_NAME);
    }

    class DownloadImageTask extends AsyncTask<String, Void, String> {

        String responseString=null;
        /** The system calls this to perform work in a worker thread and
         * delivers it the parameters given to AsyncTask.execute() */
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(con);
            pd.setTitle("Loading");
            pd.setMessage("Please Wait");
            pd.show();
        }

        protected String doInBackground(String... urls) {
            HttpClient hc = new DefaultHttpClient();
            String message;

            HttpPost httpPost = new HttpPost(urls[0]);
            JSONObject object = new JSONObject();
            try {
//            {"method":"login_request_otp","params":{"email":"kpchetnani29@gmail.com"}}
                //{"method":"login_verify_otp","params":{"email":"kpchetnani29@gmail.com","code":5220}}

                object.put("method", "login_verify_otp");
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("email",email);
                jsonObject.put("code",otp);
                object.put("params",jsonObject);

            } catch (Exception ex) {

                ex.printStackTrace();
            }

            try {
                message = object.toString();
                httpPost.setEntity(new StringEntity(message, "UTF8"));
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse resp = null;
                try {
                    resp = hc.execute(httpPost);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    resp.getEntity().writeTo(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                responseString = out.toString();


                Log.d("result", responseString);

            } catch (Exception e) {

            }

            return responseString;

        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        protected void onPostExecute(String responseString) {
            pd.dismiss();
            JSONObject response= null;
            try {
                response = new JSONObject(responseString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if(response.getInt("error")==0){

                    showMessage("OTP successful", Toast.LENGTH_SHORT);
                    sharedprefrence=getSharedPreferences(SplashActivity.EMAIL,Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedprefrence.edit();
                    editor.putString(SplashActivity.EMAIL,email);
                    editor.putBoolean("UserVerified",true);
                    editor.commit();
                    Intent otpIntent=new Intent(OtpActivity.this,RegistrationActivity.class);
                    startActivity(otpIntent);


                }else{

                    showMessage(response.getString("message"),Toast.LENGTH_SHORT);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void showMessage(String message,int option){


        Toast.makeText(this,message,option).show();
    }

    @Override
    public void initComponents() {

    }


}
