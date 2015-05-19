package net.rimoto.core;

import net.rimoto.core.models.AccessToken;
import net.rimoto.core.models.Policy;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.GET;

public class API {
    public interface RimotoAPI {
        @GET("/subscriber/me/ovpn") void getOvpn(Callback<Response> cb);
        @GET("/subscriber/me/ovpn") Response getOvpn();

        @GET("/subscriber/me/policy") void getPolicies(Callback<Policy[]> cb);
        @GET("/subscriber/me/policy") Policy[] getPolicies();
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
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(RimotoCore.getApiEndpoint())
                    .setRequestInterceptor(sRequestInterceptor)
                    .build();

            sInstance = restAdapter.create(RimotoAPI.class);
        }
        return sInstance;
    }
}
