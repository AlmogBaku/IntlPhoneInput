package net.rimoto.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.OkHttpClient;

import net.rimoto.core.models.AccessToken;
import net.rimoto.core.models.Policy;
import net.rimoto.core.models.Subscriber;

import java.io.File;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

public class API {
    public interface RimotoAPI {
        @GET("/subscriber/me") void getMe(Callback<Subscriber> cb);
        @GET("/subscriber/me") Subscriber getMe();

        @GET("/subscriber/me/ovpn") void getOvpn(Callback<Response> cb);
        @GET("/subscriber/me/ovpn") Response getOvpn();

        @GET("/subscriber/me/policy") void getPolicies(Callback<List<Policy>> cb);
        @GET("/subscriber/me/policy") List<Policy> getPolicies();

        @FormUrlEncoded
        @POST("/subscriber/me/appPolicy")
        void addAppPolicy(@Field("home_operator") String home_operator, @Field("visited_operator") String visited_operator, Callback<Policy> cb);

        @FormUrlEncoded
        @POST("/subscriber/me/appPolicy")
        void addAppPolicy(@Field("home_operator") String home_operator, @Field("visited_operator") String visited_operator, @Field("plan") int plan, Callback<Policy> cb);

        @FormUrlEncoded
        @POST("/subscriber/me/log")
        void sendLogs(@Field("logs") String logs, @Field("device") String device, Callback<Boolean> cb);
    }

    private static RequestInterceptor sRequestInterceptor = request -> {
        AccessToken accessToken = Session.getCurrentAccessToken();
        if(accessToken != null) {
            request.addQueryParam("access_token", accessToken.getToken());
        }
    };
    private static RimotoAPI sInstance;
    private static OkHttpClient okHttpClient = new OkHttpClient();

    static {
        okHttpClient.setConnectionPool(ConnectionPool.getDefault());
        File httpCacheDirectory = new File(
                RimotoCore.getsApplicationContext().getCacheDir(),
                "httpCache");
        okHttpClient.setCache(new Cache(httpCacheDirectory, 10 * 1024 * 1024));
    }

    public static RimotoAPI getInstance() {
        if(sInstance == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(RimotoCore.getApiEndpoint())
                    .setRequestInterceptor(sRequestInterceptor)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setClient(new OkClient(okHttpClient))
                    .setConverter(new GsonConverter(gson))
                    .build();

            sInstance = restAdapter.create(RimotoAPI.class);
        }
        return sInstance;
    }
    public static void clearPool() {
        okHttpClient.getConnectionPool().evictAll();
    }
    public static String rimotoOperatorFormat(String networkOperator) {
        if(networkOperator==null || networkOperator.isEmpty()) return "N/A";

        int mcc = Integer.parseInt(networkOperator.substring(0, 3));
        int mnc = Integer.parseInt(networkOperator.substring(3));
        return String.format("%03d", mcc) + "/" + String.format("%03d", mnc);
    }
}
