package sample.mylocation.model;

/**
 * Created by Dell on 29-03-2017.
 */

public class MyTravelModel {

    private int strUserId;
    private double strLatitude;
    private double strlongitude;
    private String strDate;
    private String strTime;
    private float strDistance;
    public MyTravelModel(double strLatitude,double strlongitude){
        this.strLatitude = strLatitude;
        this.strlongitude = strlongitude;

    }

    public MyTravelModel(){

    }

    public MyTravelModel( double strLatitude, double strlongitude, String strDate, String strTime, float strDistance) {

        this.strLatitude = strLatitude;
        this.strlongitude = strlongitude;
        this.strDate = strDate;
        this.strTime = strTime;
        this.strDistance = strDistance;
    }

    public int getStrUserId() {
        return strUserId;
    }

    public void setStrUserId(int strUserId) {
        this.strUserId = strUserId;
    }

    public double getStrLatitude() {
        return strLatitude;
    }

    public void setStrLatitude(double strLatitude) {
        this.strLatitude = strLatitude;
    }

    public double getStrlongitude() {
        return strlongitude;
    }

    public void setStrlongitude(double strlongitude) {
        this.strlongitude = strlongitude;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getStrTime() {
        return strTime;
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
    }

    public float getStrDistance() {
        return strDistance;
    }

    public void setStrDistance(float strDistance) {
        this.strDistance = strDistance;
    }
}
