package co.example.dell.callcontrol;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Dell on 13-01-2017.
 */
public abstract class BaseActivity extends AppCompatActivity{



    protected void showMessage(String message,int option){

        Toast.makeText(this,message,option).show();

    }

    public abstract void initComponents();
    public abstract void prepareViews();
    public abstract void setListeners();


}
