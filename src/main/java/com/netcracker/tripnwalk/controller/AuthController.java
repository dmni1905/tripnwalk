package com.netcracker.tripnwalk.controller;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

class AuthController {
    private String client_id = "5368462";
    private String redirect_uri = "http://localhost:9095/";
    private String display = "popup";
    private String response_type = "token";
    private String access_token;

    AuthController() throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        String reqUrl = "http://oauth.vk.com/authorize?" +
                "client_id=" + client_id +
                "&redirect_uri=" + redirect_uri +
                "&display=" + display +
                "&response_type=" + response_type;
        HttpResponse response = null;

        try {
            Desktop.getDesktop().browse(new URL(reqUrl).toURI());
        } catch (URISyntaxException ex) {
            throw new IOException(ex);
        }
       // String HeaderLocation = response.getFirstHeader("location").getValue(); //а тут фейл, не может найти "location"
    }
}