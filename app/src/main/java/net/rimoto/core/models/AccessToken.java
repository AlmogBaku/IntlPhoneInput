package net.rimoto.core.models;

import java.util.Date;

public class AccessToken implements TokenInterface {
    private final int SAFE_TIME_SPACE = 10; //in minutes
    private String token;
    private long expiresIn;
    private Date expiresAt;
    private String type;

    public AccessToken(String access_token, long expires_in, String type) {
        this.token = access_token;
        this.expiresIn = expires_in;
        this.type = type;

        long expires = (System.currentTimeMillis() + (this.expiresIn * 1000));
        long safe_time_space = (SAFE_TIME_SPACE *60*1000);
        this.expiresAt = new Date(expires-safe_time_space);
    }
    public AccessToken(String access_token, String expires_in, String type) {
        this(access_token, Long.valueOf(expires_in), type);
    }

    public boolean isValid() {
        return token != null && expiresAt != null && new Date().before(expiresAt);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return token;
    }
}