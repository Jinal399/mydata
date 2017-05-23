
package co.example.dell.callcontrol;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
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
import java.util.ArrayList;

/**
 * Created by Dell on 11-01-2017.
 */

public class LoginActivity extends BaseActivity implements Constants{
    private Button btnLogin;
    Context con=this;
    private TextInputLayout tilEmail;
    private EditText edtemail;
    private String email;
    ArrayList<String> a=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        prepareViews();
        setListeners();
        initComponents();
    }

    // Making HTTP Request

    public  void initComponents(){

    }

    public void prepareViews() {

        tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        edtemail=(EditText) findViewById(R.id.edtemail);

    }

    public void setListeners() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isValidate()) {

                    return;
                }

                email=edtemail.getText().toString().trim();
                startAsync();

               /* new Thread(new Runnable() {
                    @Override
                    public void run() {

                        doWebCall();

                    }

                }).start();*/




            }
        });



    }


    public boolean isValidate() {

        boolean isValid = true;

        if (TextUtils.isEmpty(tilEmail.getEditText().getText().toString().trim())) {

            isValid = false;
            tilEmail.setError(tilEmail.getHint() + " " + getString(R.string.error_empty));
        } else {

            if (!Patterns.EMAIL_ADDRESS.matcher(tilEmail.getEditText().getText().toString().trim()).matches()) {

                isValid = false;
                tilEmail.setError(getString(R.string.error_invalid) + " " + tilEmail.getHint());
            }
        }

        return isValid;
    }


    public void startAsync() {

        new DownloadImageTask().execute(DOMAIN_NAME);
    }

     class DownloadImageTask extends AsyncTask<String, Void, String> {

         String responseString=null;
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
        /** The system calls this to perform work in a worker thread and
         * delivers it the parameters given to AsyncTask.execute() */
        protected String doInBackground(String... urls) {
            HttpClient hc = new DefaultHttpClient();
            String message;

            HttpPost httpPost = new HttpPost(urls[0]);
            JSONObject object = new JSONObject();
            try {
//            {"method":"login_request_otp","params":{"email":"kpchetnani29@gmail.com"}}
                //{"method":"login_verify_otp","params":{"email":"kpchetnani29@gmail.com","code":5220}}

                object.put("method", "login_request_otp");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("email",email);
                //jsonObject.put("code",321);
                object.put("params", jsonObject);


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

                    // showMessage("OTP successful",Toast.LENGTH_SHORT);
                     email=edtemail.getText().toString().trim();
                     Toast.makeText(getApplicationContext(),"Otp Send on your email",Toast.LENGTH_SHORT).show();
                     Intent loginIntent = new Intent(LoginActivity.this, OtpActivityVolly.class);
                     loginIntent.putExtra("key",email);
                     startActivity(loginIntent);


                 }else{

                     showMessage(response.getString("message"),Toast.LENGTH_SHORT);
                 }
             } catch (JSONException e) {
                 e.printStackTrace();
             }

         }

    }
}
