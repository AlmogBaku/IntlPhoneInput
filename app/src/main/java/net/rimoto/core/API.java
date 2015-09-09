package net.rimoto.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.OkHttpClient;

import net.rimoto.core.models.AccessToken;
import net.rimoto.core.models.FAQ_Question;
import net.rimoto.core.models.Policy;
import net.rimoto.core.models.Subscriber;
import net.rimoto.core.utils.VpnUtils;
import net.rimoto.vpnlib.VpnFileLog;
import net.rimoto.vpnlib.VpnManager;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.blinkt.openvpn.core.VpnStatus;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

public class API {
    public interface RimotoAPI {
        @GET("/subscriber/me") void getMe(Callback<Subscriber> cb);
        @GET("/subscriber/me") Subscriber getMe();

        @GET("/subscriber/me/ovpn") void getOvpn(Callback<Response> cb);

        @GET("/subscriber/me/policy") void getPolicies(Callback<List<Policy>> cb);

        @FormUrlEncoded
        @POST("/subscriber/me/appPolicy")
        void addAppPolicy(@Field("home_operator") String home_operator, @Field("visited_operator") String visited_operator, Callback<Policy> cb);

        @FormUrlEncoded
        @POST("/subscriber/me/appPolicy")
        void addAppPolicy(@Field("home_operator") String home_operator, @Field("visited_operator") String visited_operator, @Field("plan") int plan, Callback<Policy> cb);

        @Multipart
        @POST("/subscriber/me/support")
        void sendSupportRequest(
                @Part("message") String message,
                @Nullable @Part("vpn_logs") TypedFile logs_file,
                @Nullable @Part("device") String device,
                @Nullable @Part("wifi") Boolean wifi,
                @Nullable @Part("home_operator") String home_operator,
                @Part("visited_operator") String visited_operator,
                @Part("rimoto_enabled") Boolean rimoto_enabled,
                @Part("vpn_connected") Boolean vpn_connected,
                @Part("vpn_state") String vpn_state,
                @Nullable @Part("extras") String extras,
                Callback<Boolean> cb
        );

        @GET("/faq") void getFAQ(Callback<List<FAQ_Question>> cb);
        @GET("/faq") List<FAQ_Question> getFAQ();
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
        okHttpClient.setConnectionPool(null);
        File httpCacheDirectory = new File(
                RimotoCore.getsApplicationContext().getCacheDir(),
                "httpCache");
        okHttpClient.setCache(new Cache(httpCacheDirectory, 10 * 1024 * 1024));
        System.setProperty("http.keepAlive", "false");
    }

    public static RimotoAPI getInstance() {
        if(sInstance == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(RimotoCore.getApiEndpoint())
                    .setRequestInterceptor(sRequestInterceptor)
                    .setLogLevel(RestAdapter.LogLevel.FULL) //TODO remove on prod
                    .setClient(new OkClient(okHttpClient))
                    .setConverter(new GsonConverter(gson))
                    .build();

            sInstance = restAdapter.create(RimotoAPI.class);
        }
        return sInstance;
    }
    public static void clearPool() {
        ConnectionPool pool = okHttpClient.getConnectionPool();
        if(pool!=null) {
            pool.evictAll();
        }
    }
    public static void clearCache() {
        try {
            okHttpClient.getCache().evictAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String rimotoOperatorFormat(String networkOperator) {
        if(networkOperator==null || networkOperator.isEmpty()) return "N/A";

        int mcc = Integer.parseInt(networkOperator.substring(0, 3));
        int mnc = Integer.parseInt(networkOperator.substring(3));
        return String.format("%03d", mcc) + "/" + String.format("%03d", mnc);
    }

    public static void sendSupportRequest(Context context, String message, Callback<Boolean> callback) {
        String device = String.format("SDK Level %s, %s %s (%s)", Build.VERSION.SDK_INT, Build.BRAND, Build.MODEL, Build.PRODUCT);

        TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String home_operator = API.rimotoOperatorFormat(tel.getSimOperator());
        String visited_operator = API.rimotoOperatorFormat(tel.getNetworkOperator());
        Boolean rimotoIsEnabled = !(VpnUtils.getCurrentProfileUUID(context) == null);


        API.getInstance().sendSupportRequest(
                message,
                new TypedFile("text/plain", VpnFileLog.logFile),
                device,
                (VpnManager.getCurrentNetworkInfo(context).getType() == ConnectivityManager.TYPE_WIFI),
                home_operator,
                visited_operator,
                rimotoIsEnabled,
                VpnManager.isConnected(),
                VpnStatus.getLaststate(),
                null,
                callback
        );
    }
}
