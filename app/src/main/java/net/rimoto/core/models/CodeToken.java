package net.rimoto.core.models;

public class CodeToken implements TokenInterface {
    private String token;
    private String type;

    public CodeToken(String code) {
        this.token  = code;
        this.type   = "code";
    }

    @Override
    public String toString() {
        return token;
    }

    @Override
    public String getToken() {
        return null;
    }

    @Override
    public String getType() {
        return type;
    }
}