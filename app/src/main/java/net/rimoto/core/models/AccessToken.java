package net.rimoto.core.models;

import java.util.Date;

public class AccessToken implements TokenInterface {
    private final int SAFE_TIME_SPACE = 10; //in minutes
    private String token;
    private long expiresIn;
    private Date expiresAt;
    private String type;

    /**
     * Access Token
     * @param access_token String token
     * @param expires_in long minutes
     * @param type String token_type
     */
    public AccessToken(String access_token, long expires_in, String type) {
        this.token = access_token;
        this.expiresIn = expires_in;
        this.type = type;

        long expires = (System.currentTimeMillis() + (this.expiresIn * 1000));
        long safe_time_space = (SAFE_TIME_SPACE *60*1000);
        this.expiresAt = new Date(expires-safe_time_space);
    }

    /**
     * Access Token
     * @param access_token Stringtoken
     * @param expires_in long minutes
     * @param type String token_type
     */
    public AccessToken(String access_token, String expires_in, String type) {
        this(access_token, Long.valueOf(expires_in), type);
    }

    /**
     * Is token valid
     * @return boolean
     */
    public boolean isValid() {
        return token != null && expiresAt != null && new Date().before(expiresAt);
    }

    /**
     * Get token
     * @return String
     */
    @Override
    public String getToken() {
        return token;
    }

    /**
     * Set token
     * @param token String
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Expires in
     * @return long minutes
     */
    public long getExpiresIn() {
        return expiresIn;
    }

    /**
     * @param expiresIn long minutes
     */
    @SuppressWarnings("unused")
    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    /**
     * Get expire at
     * @return Date of expiration
     */
    public Date getExpiresAt() {
        return expiresAt;
    }

    /**
     * Set expiration
     * @param expiresAt Date of expiration
     */
    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    /**
     * Get token type
     * @return String
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Set token type
     * @param type String
     */
    @SuppressWarnings("unused")
    public void setType(String type) {
        this.type = type;
    }

    /**
     * To string
     * @return String token
     */
    @Override
    public String toString() {
        return token;
    }
}