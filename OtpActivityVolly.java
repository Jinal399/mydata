package co.example.dell.callcontrol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by Dell on 05-02-2017.
 */


public class OtpActivityVolly extends BaseActivity {
    private Button btnverify;
    public static SharedPreferences sharedprefrence;
    Context con = this;
    TextInputLayout tilotp;
    Handler handler = new Handler();
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


    public void prepareViews() {

        tilotp = (TextInputLayout) findViewById(R.id.tilotp);
        btnverify = (Button) findViewById(R.id.btnverify);
        etotp = (EditText) findViewById(R.id.etotp);

    }

    public void setListeners() {

        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

                if (!isValidate()) {

                    return;
                }
                tilotp.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        if (s.length() > 0 && s.length() < 4) {
                            tilotp.setError(getString(R.string.error_invalid) + " " + tilotp.getHint());

                        } else {
                            tilotp.setError(null);
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                Bundle b = getIntent().getExtras();
                if (b != null) {
                    email = b.getString("key");

                }
                otp = etotp.getText().toString().trim();

                setupAndWebCallByVolley();
            }
        });



    }
    public void showMessage (String message,int option){


        Toast.makeText(this, message, option).show();
    }


    @Override
    public void initComponents () {

    }



    public boolean isValidate() {

        boolean isValid = true;

        if (TextUtils.isEmpty(tilotp.getEditText().getText().toString().trim())) {

            isValid = false;
            tilotp.setError(tilotp.getHint() + " " + getString(R.string.error_empty));
        } else {


            if (!Patterns.PHONE.matcher(tilotp.getEditText().getText().toString().trim()).matches()) {

                isValid = false;
                tilotp.setError(getString(R.string.error_invalid) + " " + tilotp.getHint());
            }
        }

        return isValid;


    }

    public void setupAndWebCallByVolley() {



       JSONObject jsonParams= getJsonParams();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.DOMAIN_NAME, jsonParams, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        showMessage(response.toString(),Toast.LENGTH_SHORT);
                        startRegistrationActivity();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showMessage(error.getMessage().toString(),Toast.LENGTH_SHORT);

                    }
                });
// Add the request to the RequestQueue.
        queue.add(jsObjRequest);

    }

    private void startRegistrationActivity() {
        Intent registrationIntent =new Intent(this,RegistrationActivity.class);
        startActivity(registrationIntent);
    }


    public JSONObject getJsonParams() {
        JSONObject rootJson = new JSONObject();
        try {
//            {"method":"login_request_otp","params":{"email":"kpchetnani29@gmail.com"}}
            //{"method":"login_verify_otp","params":{"email":"kpchetnani29@gmail.com","code":5220}}

            rootJson.put("method", "login_verify_otp");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("code", otp);
            rootJson.put("params", jsonObject);

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return rootJson;
    }
}
