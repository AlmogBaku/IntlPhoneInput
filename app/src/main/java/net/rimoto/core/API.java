package net.rimoto.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.rimoto.core.models.AccessToken;
import net.rimoto.core.models.Policy;

import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;

public class API {
    public interface RimotoAPI {
        @GET("/subscriber/me/ovpn") void getOvpn(Callback<Response> cb);
        @GET("/subscriber/me/ovpn") Response getOvpn();

        @GET("/subscriber/me/policy") void getPolicies(Callback<List<Policy>> cb);
        @GET("/subscriber/me/policy") List<Policy> getPolicies();
    }

    private static RequestInterceptor sRequestInterceptor = request -> {
        AccessToken accessToken = Session.getCurrentAccessToken();
        if(accessToken != null) {
            request.addQueryParam("access_token", accessToken.getToken());
        }
    };
    private static RimotoAPI sInstance;

    public static RimotoAPI getInstance() {
        if(sInstance == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(RimotoCore.getApiEndpoint())
                    .setRequestInterceptor(sRequestInterceptor)
                    .setConverter(new GsonConverter(gson))
                    .build();

            sInstance = restAdapter.create(RimotoAPI.class);
        }
        return sInstance;
    }
}
