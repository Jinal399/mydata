package sample.getdatafromserver.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sample.getdatafromserver.R;
import sample.getdatafromserver.adapter.ConAdapter;
import sample.getdatafromserver.model.ContactsItem;
import sample.getdatafromserver.model.Phone;


/**
 * Created by Dell on 04-06-2017.
 */

public class VollyActivity extends AppCompatActivity {

    com.android.volley.RequestQueue requestQueue;
    String url = "http://api.androidhive.info/contacts/";
    ArrayList<ContactsItem> conList;
    RecyclerView recyclerView;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        recyclerView= (RecyclerView) findViewById(R.id.reView);
        conList=new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {



                            JSONArray jsonArray=response.getJSONArray("contacts");
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
                                //Toast.makeText(getApplicationContext(),contact.getName(),Toast.LENGTH_LONG).show();
                                conList.add(contact);
                                /*int l=conList.size();
                                for (int i=0;i<l;i++){
                                    ContactsItem ci=conList.get(i);

                                }*/
                                recyclerView.setHasFixedSize(true);
                                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                ConAdapter ca=new ConAdapter(conList,getApplicationContext());
                                recyclerView.setAdapter(ca);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();

                    }
                });
        requestQueue.add(jsObjRequest);


    }

}
