package sample.kcs.com.mediamonitoring.interfaces;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import sample.kcs.com.mediamonitoring.model.LogoutResponse;
import sample.kcs.com.mediamonitoring.model.ModelLoginRequest;
import sample.kcs.com.mediamonitoring.model.ModelLoginResponse;
import sample.kcs.com.mediamonitoring.model.ModelLoginResult;
import sample.kcs.com.mediamonitoring.model.ModelMediaMonitoringResponse;
import sample.kcs.com.mediamonitoring.model.NotificationResponse;

/**
 * Created by Dell on 11-04-2017.
 */

public interface APIService {
    @FormUrlEncoded
    @POST("login/login?format=json")
    Call<ModelLoginResponse> createLogin(@Field("json") String modelLogin);

    @FormUrlEncoded
    @POST("mediamonitor/setMediaMonitorEntries?format=json")
    Call<ModelMediaMonitoringResponse> addMediaMonitoring(@Field("json") String  modelMediaMonitoringResponse);

    @POST("login/logout?format=json")
    Call<LogoutResponse> userLogout(@Query("member_id") String member_id,
                                    @Query("login_token") String login_token);

    @FormUrlEncoded
    @POST("notification/getNotificationEntries?format=json")
    Call<NotificationResponse> getNotification(@Field("json") String notificationResponse,@Query("member_id") String member_id,
                                          @Query("login_token") String login_token);

}
