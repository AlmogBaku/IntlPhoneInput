package net.rimoto.android.utils;

import com.instabug.library.Instabug;

import net.rimoto.core.API;
import net.rimoto.core.models.Subscriber;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class InstabugRimoto {
    public static void attachUser() {
        API.getInstance().getMe(new Callback<Subscriber>() {
            @Override
            public void success(Subscriber subscriber, Response response) {
                Instabug.getInstance().setUserData(subscriber.toString());
            }
            @Override public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }
}
