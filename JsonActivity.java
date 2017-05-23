package co.example.dell.json2;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class JsonActivity extends AppCompatActivity {
    Handler handler=new Handler();
    final ArrayList<JsonStudent> arrayList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new Thread(new Runnable() {
            @Override
            public void run() {
                dowebcall();
            }
        }).start();


    }
    private void dowebcall() {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet("http://api.androidhive.info/contacts/"));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                final String responseString = out.toString();
                parseJson(responseString);
                final int arraylength=arrayList.size();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for(int cnt=0;cnt<arraylength;cnt++){

                            JsonStudent student=arrayList.get(cnt);
                            Log.d("json","json"+student.getName());


                            showMessage(student.getName(), Toast.LENGTH_SHORT);
                        }

                    }});
                out.close();
            } else {
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    private void parseJson(String responseString) {
//
        try {
            JSONObject jsonobject =new JSONObject(responseString);
            JSONArray jsonarray= jsonobject.getJSONArray("contacts");
            int length=jsonarray.length();

            for (int cnt = 0; cnt < length; cnt++){

                JSONObject obj = jsonarray.getJSONObject(cnt);
                JsonStudent student  = new JsonStudent();
                student.setId(obj.getString("id"));
                student.setName(obj.getString("name"));
                student.setEmail(obj.getString("email"));
                student.setAddress(obj.getString("address"));
                student.setGender(obj.getString("gender"));
                JSONObject object = obj.getJSONObject("phone");
                JsonPhoneNumber phone=new JsonPhoneNumber();

                phone.setHome(object.getString("home"));
                phone.setMobile(object.getString("mobile"));
                phone.setOffice(object.getString("office"));

                arrayList.add(student);

//
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void showMessage(String message,int option){
        Toast.makeText(this,message,option).show();
    }
}
