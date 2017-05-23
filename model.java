package sample.sqlitewithoutassest;

/**
 * Created by Dell on 22-05-2017.
 */

public class model {
    int id;
    String strname;
    String strAge;
    public model(){

    }

    public model(String strname, String strAge) {
        this.strname = strname;
        this.strAge = strAge;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrname() {
        return strname;
    }

    public void setStrname(String strname) {
        this.strname = strname;
    }

    public String getStrAge() {
        return strAge;
    }

    public void setStrAge(String strAge) {
        this.strAge = strAge;
    }
}
