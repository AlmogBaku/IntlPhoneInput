package net.rimoto.wheninroam.utils.RimotoCore;

import java.util.Date;

public class AccessToken implements TokenInterface {
    public String access_token;
    public long expires_in;
    public Date expires_at;
    public String token_type;

    public AccessToken(String access_token, long expires_in, String token_type) {
        this.access_token=access_token;
        this.expires_in=expires_in;
        this.expires_at=new Date(System.currentTimeMillis()+(this.expires_in*1000));
        this.token_type=token_type;
    }
    public AccessToken(String access_token, String expires_in, String token_type) {
        this(access_token, Long.valueOf(expires_in), token_type);
    }

    @Override
    public String toString() {
        return access_token;
    }
}