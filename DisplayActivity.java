package sample.sqlitewithoutassest;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import sample.sqlitewithoutassest.databinding.ActivityDisplayBinding;

/**
 * Created by Dell on 22-05-2017.
 */

public class DisplayActivity extends AppCompatActivity {

    ActivityDisplayBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(DisplayActivity.this, R.layout.activity_display);
        DatabaseHandler db = new DatabaseHandler(this);
        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        ArrayList<model> contacts = db.getAllContacts();
        ArrayList<String> clist = new ArrayList<>();


        for (int i=0;i<contacts.size();i++) {
            model m1=contacts.get(i);
            clist.add(m1.getStrname());


        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, clist);
        mBinding.lvContactList.setAdapter(adapter);
    }
}
