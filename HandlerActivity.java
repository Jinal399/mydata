package sample.getdatafromserver.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import sample.getdatafromserver.R;
import sample.getdatafromserver.adapter.ConAdapter;
import sample.getdatafromserver.model.ContactsItem;
import sample.getdatafromserver.model.Phone;

public class HandlerActivity extends AppCompatActivity {
    ArrayList<ContactsItem> cList=new ArrayList<>();
    Handler handler=new Handler();
    RecyclerView reView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        reView= (RecyclerView) findViewById(R.id.reView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    doWebcall();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }).start();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        reView.setLayoutManager(linearLayoutManager);

    }

    private void doWebcall() throws IOException {
        HttpClient httpclient=new DefaultHttpClient();
        HttpResponse httpresponse=httpclient.execute(new HttpGet("http://api.androidhive.info/contacts/"));
        StatusLine status=httpresponse.getStatusLine();
        if(status.getStatusCode()== HttpStatus.SC_OK){
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            httpresponse.getEntity().writeTo(out);
            String responseString=out.toString();
            parseJson(responseString);
            final int length=cList.size();
           // Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    reView.setHasFixedSize(true);

                    ConAdapter ca=new ConAdapter(cList,getApplicationContext());
                    reView.setAdapter(ca);
                    /*for(int clength=0;clength<length;clength++){
                        ContactsItem contact=cList.get(clength);
                        //Toast.makeText(getApplicationContext(),contact.getName(),Toast.LENGTH_LONG).show();
                    }*/

                }
            });
            out.close();
        }
    }

    private void parseJson(String responseString) {

        try {


            JSONObject jsonObject=new JSONObject(responseString);
            JSONArray jsonArray=jsonObject.getJSONArray("contacts");
            int conLength=jsonArray.length();
            for (int con=0;con<conLength;con++){
                JSONObject obj=jsonArray.getJSONObject(con);
                ContactsItem contact=new ContactsItem();
                contact.setId(obj.getString("id"));
                contact.setName(obj.getString("name"));
                contact.setEmail(obj.getString("email"));
                contact.setAddress(obj.getString("address"));
                contact.setGender(obj.getString("gender"));
                JSONObject object=obj.getJSONObject("phone");
                Phone phone=new Phone();
                phone.setHome(object.getString("home"));
                phone.setMobile(object.getString("mobile"));
                phone.setOffice(object.getString("office"));
                cList.add(contact);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
