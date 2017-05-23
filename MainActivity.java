package sample.sqliteexample;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import sample.sqliteexample.database.Tbl_Student;
import sample.sqliteexample.model.StudentModel;

public class MainActivity extends AppCompatActivity {
    public SQLiteDatabase database;
        public ArrayList<StudentModel> studentList;
    Tbl_Student student1;
    StudentModel s1=new StudentModel();
    Button btnselect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnselect= (Button) findViewById(R.id.btnselect);
        student1=new Tbl_Student(this);
        studentList=insertstudent();

        student1.Insert(studentList);
        Toast.makeText(getApplicationContext(),s1.getName(),Toast.LENGTH_SHORT).show();
        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<StudentModel> stuselect=student1.SelectAll();
                Toast.makeText(getApplicationContext(),stuselect.get(0).getName(),Toast.LENGTH_SHORT).show();

            }

        });




    }

    private ArrayList<StudentModel> insertstudent() {
        ArrayList<StudentModel> mlist=new ArrayList<>();
        StudentModel student=new StudentModel("jinal","sajhgda","sadg","a","5255555555");

        mlist.add(student);
        return mlist;

    }

}
