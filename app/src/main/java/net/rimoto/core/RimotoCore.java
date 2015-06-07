package net.rimoto.core;

import android.content.Context;
import android.text.TextUtils;

/**
 *
 */
public class RimotoCore {
    private static String REDIRECT_URI          = "http://localhost";
    private static String RESPONSE_TYPE         = "code";
    private static final String API_ENDPOINT    = "http://core.rimoto.net/api";
    private static final String AUTH_ENDPOINT   = "/oauth/v2/auth";
    private static String OAUTH_SCOPE           = "";

    private static Context sApplicationContext;
    private static String sClientId;
    /**
     * Initialize
     * @param applicationContext Context
     * @param client Client_ID
     * @param redirect_uri Redirect_URI
     */
    public static void init(Context applicationContext, String client, String redirect_uri) {
        init(applicationContext, client);
        REDIRECT_URI = redirect_uri;
    }
    /**
     * Initialize
     * @param applicationContext Context
     * @param client Client_ID
     */
    public static void init(Context applicationContext, String client) {
        sApplicationContext = applicationContext;
        sClientId = client;
    }

    /**
     * Get application context
     * @return Context
     */
    public static Context getsApplicationContext() {
        return sApplicationContext;
    }

    /**
     * Set client_id
     * @param sClientId String
     */
    @SuppressWarnings("unused")
    public static void setsClientId(String sClientId) {
        RimotoCore.sClientId = sClientId;
    }
    /**
     * Get client_id
     * @return String
     */
    public String getClientId() {
        return sClientId;
    }

    /**
     * Set redirect_uri
     * @param redirectUri String
     */
    @SuppressWarnings("unused")
    public static void setRedirectUri(String redirectUri) {
        REDIRECT_URI = redirectUri;
    }

    /**
     * Get redirect_uri
     * @return String
     */
    public String getRedirectUri() {
        return REDIRECT_URI;
    }

    /**
     * Set authentication type(oauth's response_type)
     * @param authType token|code
     */
    public static void setAuthType(String authType) throws RimotoException {
        authType=authType.toLowerCase().trim();
        if(!authType.equals("token") && !authType.equals("code")) {
            throw new RimotoException("Invalid AuthType");
        }
        RESPONSE_TYPE = authType;
    }

    /**
     * Get authentication type(oauth's response_type)
     * @return string
     */
    public String getAuthType() {
        return RESPONSE_TYPE;
    }

    /**
     * Get API Endpoint
     * @return String
     */
    public static String getApiEndpoint() {
        return API_ENDPOINT;
    }

    /**
     * Get 3-Legs Authentication Endpoint
     * @return String
     */
    public String getAuthEndpoint() {
        return API_ENDPOINT+AUTH_ENDPOINT;
    }

    /**
     * Set scope
     * @param scope String
     */
    @SuppressWarnings("unused")
    public static void setScope(String scope) {
        OAUTH_SCOPE = scope;
    }
    /**
     * Set scope from array
     * @param scope String
     */
    @SuppressWarnings("unused")
    public static void setScope(String[] scope) {
        OAUTH_SCOPE = TextUtils.join(",", scope);
    }
    /**
     * Get scope
     * @return String
     */
    public String getScope() {
        return OAUTH_SCOPE;
    }

    private static RimotoCore sInstance = null;
    public static RimotoCore getInstance() throws RimotoException {
        if(sClientId ==null || sApplicationContext==null) {
            throw new RimotoException("You must Initialize the API");
        }

        if(sInstance == null)
        {
            sInstance = new RimotoCore();
        }
        return sInstance;
    }
}