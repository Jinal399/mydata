package sample.getdatafromserver.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import sample.getdatafromserver.model.ContactResponse;

/**
 * Created by Dell on 04-06-2017.
 */

public interface APIService {

    @GET("contacts/")
    Call<ContactResponse> getJson();
}
