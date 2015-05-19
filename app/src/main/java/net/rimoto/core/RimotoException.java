package net.rimoto.core;

public class RimotoException extends Exception {
    public int statusCode;
    public String url;
    public RimotoException(int statusCode, String detailMessage, String url) {
        super(detailMessage);
        this.statusCode = statusCode;
        this.url = url;
    }
    public RimotoException(String detailMessage) {
        super(detailMessage);
    }
}