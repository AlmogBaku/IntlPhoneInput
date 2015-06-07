package net.rimoto.core;

public class RimotoException extends Exception {
    public int statusCode;
    public String url;

    /**
     *
     * @param statusCode Status Code
     * @param detailMessage String
     * @param url String url
     */
    public RimotoException(int statusCode, String detailMessage, String url) {
        super(detailMessage);
        this.statusCode = statusCode;
        this.url = url;
    }

    /**
     *
     * @param detailMessage String
     */
    public RimotoException(String detailMessage) {
        super(detailMessage);
    }
}