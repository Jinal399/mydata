package sample.kcs.com.mediamonitoring.model;

import sample.kcs.com.mediamonitoring.interfaces.APIService;

/**
 * Created by Dell on 13-04-2017.
 */

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "http://180.211.99.238:8092/khushi_sta_qc/api/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
