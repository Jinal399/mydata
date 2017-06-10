package sample.getdatafromserver.model;


import sample.getdatafromserver.interfaces.APIService;

/**
 * Created by Dell on 13-04-2017.
 */

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "http://api.androidhive.info/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
