package sample.getdatafromserver.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.getdatafromserver.R;
import sample.getdatafromserver.adapter.ConAdapter;
import sample.getdatafromserver.interfaces.APIService;
import sample.getdatafromserver.model.ApiUtils;
import sample.getdatafromserver.model.ContactResponse;
import sample.getdatafromserver.model.ContactsItem;

/**
 * Created by Dell on 04-06-2017.
 */

public class RetrfitActivity extends AppCompatActivity {
    APIService mApiServise;
    RecyclerView recyclerView;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        recyclerView= (RecyclerView) findViewById(R.id.reView);
        mApiServise= ApiUtils.getAPIService();
        Call<ContactResponse> call=mApiServise.getJson();
        call.enqueue(new Callback<ContactResponse>() {
            @Override
            public void onResponse(Call<ContactResponse> call, Response<ContactResponse> response) {
                ContactResponse ci=response.body();
                ArrayList<ContactsItem> s= (ArrayList<ContactsItem>) ci.getContacts();
               // ContactsItem ci=getResponse(response,ContactsItem.class);
                if(response.isSuccessful()){
                    /*for(int i=0;i<s.size();i++){
                        ContactsItem c=s.get(i);
                        Toast.makeText(getApplicationContext(),c.getName(),Toast.LENGTH_LONG).show();

                    }*/
                    recyclerView.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    ConAdapter ca=new ConAdapter(s,getApplicationContext());
                    recyclerView.setAdapter(ca);

                }

            }

            @Override
            public void onFailure(Call<ContactResponse> call, Throwable t) {

            }
        });

    }


}
