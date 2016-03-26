package com.netcracker.tripnwalk.controller;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Controller
public class AuthController {
    private static final Logger logger = LogManager.getLogger(AuthController.class);
    private static final String CLIENT_ID = "5368462";
    private static final String REDIRECT_URL = "http://localhost:9095/";
    private static final String DISPLAY = "popup";
    private static final String RESPONSE_TYPE = "token";

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public ResponseEntity<String> auth() throws IOException {
        StringBuilder result = new StringBuilder();
        String reqUrl = "http://oauth.vk.com/authorize?" +
                "client_id=" + CLIENT_ID +
                "&redirect_uri=" + REDIRECT_URL +
                "&display=" + DISPLAY +
                "&response_type=" + RESPONSE_TYPE;
        HttpGet httpGet = new HttpGet(reqUrl);


        try (CloseableHttpClient httpclient = HttpClients.createDefault();
             CloseableHttpResponse response = httpclient.execute(httpGet);
             BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
            ;
            String line = reader.readLine();

            while (line != null) {
                result.append(line);

                line = reader.readLine();
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }
}