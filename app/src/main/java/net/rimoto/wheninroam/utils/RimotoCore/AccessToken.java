package net.rimoto.wheninroam.utils.RimotoCore;

import java.util.Date;

public class AccessToken implements TokenInterface {
    private final int SECURE_TIME_SPACE = 10; //in minutes
    private String access_token;
    private long expires_in;
    private Date expires_at;
    private String token_type;

    public AccessToken(String access_token, long expires_in, String token_type) {
        this.access_token=access_token;
        this.expires_in=expires_in;
        this.setExpires_at(new Date(System.currentTimeMillis()+(this.expires_in*1000)));
        this.token_type=token_type;
    }
    public AccessToken(String access_token, String expires_in, String token_type) {
        this(access_token, Long.valueOf(expires_in), token_type);
    }

    public boolean isValid() {
        if(access_token==null) return false;
        if(expires_at==null) return false;
        return new Date().before(expires_at);
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public Date getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(Date expires_at) {
        int minusTime = SECURE_TIME_SPACE*60*1000;
        this.expires_at = new Date(expires_at.getTime()-(10*60*1000));
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    @Override
    public String toString() {
        return access_token;
    }
}