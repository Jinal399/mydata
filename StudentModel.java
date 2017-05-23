package sample.sqliteexample.model;

import java.io.Serializable;

/**
 * Created by Dell on 08-04-2017.
 */

public class StudentModel implements Serializable{
    public StudentModel(){

    }

    public int _Id;

    public String Name="Name";
    public String Address="Address";

    public String SchoolName="SchoolName";

    public String Division="Division";

    public String PhoneNo="PhoneNo";

    public StudentModel(String name, String address, String schoolName, String division, String phoneNo) {

        Name = name;
        Address = address;
        SchoolName = schoolName;
        Division = division;
        PhoneNo = phoneNo;
    }

    public int get_Id() {
        return _Id;
    }

    public String getName() {
        return Name;
    }

    public String getAddress() {
        return Address;
    }

    public String getSchoolName() {
        return SchoolName;
    }

    public String getDivision() {
        return Division;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }
}
