package com.bsep.domain;

public class AdminTokenState {

    private String accessToken;
    private long expiresIn;

    public AdminTokenState() {
        super();
        // TODO Auto-generated constructor stub
    }

    public AdminTokenState(String accessToken, long expiresIn) {
        super();
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }


}
