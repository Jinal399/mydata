package co.example.dell.json2;

/**
 * Created by Dell on 28-01-2017.
 */

public class JsonStudent {

    private String id;
    private String email;
    private String address;
    private String gender;
    private JsonPhoneNumber phoneNumber;

    public JsonPhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(JsonPhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }





    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

   static String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




}
