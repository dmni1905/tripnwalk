package com.netcracker.tripnwalk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
public class AuthController {
    private String client_id = "5368462";
    private String redirect_uri = "http://localhost:9095/";
    private String display = "popup";
    private String response_type = "token";
    private String access_token;
    private URL url;
    private HttpURLConnection connection;
    private BufferedReader reader;
    private String line;
    public String result = "";

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public String auth() throws IOException {
        String reqUrl = "http://oauth.vk.com/authorize?" +
                "client_id=" + client_id +
                "&redirect_uri=" + redirect_uri +
                "&display=" + display +
                "&response_type=" + response_type;

        try {
            url = new URL(reqUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}