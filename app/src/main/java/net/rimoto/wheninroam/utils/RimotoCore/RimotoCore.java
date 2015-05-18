package net.rimoto.wheninroam.utils.RimotoCore;

import android.text.TextUtils;

/**
 *
 */
public class RimotoCore {
    private static String CLIENT_ID     = null;
    private static String REDIRECT_URI  = "http://localhost";
    private static String RESPONSE_TYPE = "code";
    private static String AUTH_ENDPOINT = "http://core.rimoto.net/api/oauth/v2/auth";
    private static String OAUTH_SCOPE   = "";

    /**
     * Initialize
     * @param client Client_ID
     * @param redirect_uri Redirect_URI
     */
    public static void init(String client, String redirect_uri) {
        CLIENT_ID = client;
        REDIRECT_URI = redirect_uri;
    }
    /**
     * Initialize
     * @param client Client_ID
     */
    public static void init(String client) {
        CLIENT_ID = client;
    }


    /**
     * Set client_id
     * @param clientId String
     */
    public static void setClientId(String clientId) {
        CLIENT_ID = clientId;
    }
    /**
     * Get client_id
     * @return String
     */
    public String getClientId() {
        return CLIENT_ID;
    }

    /**
     * Set redirect_uri
     * @param redirectUri String
     */
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
     * Get auth_endpoint
     * @return String
     */
    public String getAuthEndpoint() {
        return AUTH_ENDPOINT;
    }

    /**
     * Set scope
     * @param scope String
     */
    public static void setScope(String scope) {
        OAUTH_SCOPE = scope;
    }
    /**
     * Set scope from array
     * @param scope String
     */
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

    private static RimotoCore mInstance = null;
    public static RimotoCore getInstance() throws RimotoException {
        if(CLIENT_ID==null) {
            throw new RimotoException("You must Initialize the API");
        }

        if(mInstance == null)
        {
            mInstance = new RimotoCore();
        }
        return mInstance;
    }
}