package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.Itineraries;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

@Controller
public class AuthController {
    private String client_id = "5368462";
    private String redirect_uri = "http://localhost:9095/";
    private String display = "popup";
    private String response_type = "token";
    private String access_token;

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public void auth() throws IOException {
//        HttpClient httpClient = new DefaultHttpClient();
        String reqUrl = "http://oauth.vk.com/authorize?" +
                "client_id=" + client_id +
                "&redirect_uri=" + redirect_uri +
                "&display=" + display +
                "&response_type=" + response_type;
//        HttpResponse response = null;

        try {
            Desktop.getDesktop().browse(new URL(reqUrl).toURI());
        } catch (URISyntaxException ex) {
            throw new IOException(ex);
        }
       // String HeaderLocation = response.getFirstHeader("location").getValue(); //а тут фейл, не может найти "location"
    }
}