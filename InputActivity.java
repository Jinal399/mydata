package sample.sqlitewithoutassest;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import sample.sqlitewithoutassest.databinding.ActivityMainBinding;

public class InputActivity extends AppCompatActivity implements View.OnClickListener {

ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(InputActivity.this,R.layout.activity_main);
        mBinding.btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==mBinding.btnSubmit){
            model m1=new model(mBinding.edtName.getText().toString().trim(),mBinding.edtAge.getText().toString().trim());
            DatabaseHandler db=new DatabaseHandler(this);
            db.addContact(m1);
            mBinding.edtName.setText("");
            mBinding.edtAge.setText("");
            Intent i=new Intent(InputActivity.this,DisplayActivity.class);
            startActivity(i);
        }

    }
}
