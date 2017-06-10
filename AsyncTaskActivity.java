package sample.getdatafromserver.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sample.getdatafromserver.R;
import sample.getdatafromserver.model.ContactsItem;
import sample.getdatafromserver.model.HttpHandler;
import sample.getdatafromserver.model.Phone;

/**
 * Created by Dell on 01-06-2017.
 */

public class AsyncTaskActivity extends AppCompatActivity {
    ArrayList<ContactsItem> conList=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        new GetData().execute();
    }

    class GetData extends AsyncTask<Void,Void,Void>{



        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler hp=new HttpHandler();
            String jsonStr=hp.makeServiceCall("http://api.androidhive.info/contacts/");
            parseJson(jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            int conlength=conList.size();
            for(int i=0;i<conlength;i++){
                ContactsItem ci=conList.get(i);
                Toast.makeText(getApplicationContext(),ci.getName(),Toast.LENGTH_LONG).show();
            }
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
                conList.add(contact);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
