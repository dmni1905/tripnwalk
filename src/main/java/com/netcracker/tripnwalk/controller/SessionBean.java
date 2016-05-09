package com.netcracker.tripnwalk.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionBean {
    private Long sessionId;

    private String accessToken = "";
    private String expiresIn = "";

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String access_token) {
        this.accessToken = access_token;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expires_in) {
        this.expiresIn = expires_in;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }
}
