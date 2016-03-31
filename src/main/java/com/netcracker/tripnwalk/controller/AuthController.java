package com.netcracker.tripnwalk.controller;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.JSONException;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

@RestController
class AuthController {
    private static final Logger logger = LogManager.getLogger(AuthController.class);

    //Для получения ID пользователя, e-mail, access_token
    private static final String CLIENT_ID = "5368462";
    private static final String REDIRECT_URL = "http://localhost:9095/";
    private static final String DISPLAY = "popup";
    private static final String SCOPE = "friends,email";
    private static final String RESPONSE_TYPE = "token";
    // Для запроса списка друзей пользователя
    private static final String VKUSER_ID = "9911063";
    private static final String OFFSET = "100";
    private static final String FIELDS = "first_name";
    private static final String NAME_CASE = "nom";
    private static final String VERSION = "5.50";

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public ResponseEntity<String> auth() throws IOException {
        StringBuilder result = new StringBuilder();
        String reqUrl = "http://oauth.vk.com/authorize?" +
                "client_id=" + CLIENT_ID +
                "&display=" + DISPLAY +
                "&redirect_uri=" + REDIRECT_URL +
                "&scope=" + SCOPE +
                "&response_type=" + RESPONSE_TYPE +
                "&v=" + VERSION + 5.50;
        HttpGet httpGet = new HttpGet(reqUrl);
        try (CloseableHttpClient httpclient = HttpClients.createDefault();
             CloseableHttpResponse response = httpclient.execute(httpGet);
             BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
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

    @RequestMapping(value = "/getfriends", method = RequestMethod.GET) //TODO уточнить переадресацию
        public void getfriends() throws IOException {
            String resultJSON = "";
            StringBuilder result = new StringBuilder();
            String reqUrl1 = "https://api.vk.com/method/friends.get?" +
                    "user_id=" + VKUSER_ID +
                    "&offset=" + OFFSET +
                    "&fields=" + FIELDS +
                    "&name_case=" + NAME_CASE +
                    "&v=" + VERSION + 5.50;
            HttpGet httpGet = new HttpGet(reqUrl1);


            try (CloseableHttpClient httpclient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpclient.execute(httpGet);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                String line = reader.readLine();

                while (line != null) {
                    result.append(line);
                    line = reader.readLine();

                }
            } catch (Exception e) {
                logger.error(e);
            }
            try {
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
                jsonObject = (JSONObject)jsonObject.get("response");
                JSONObject obj;
                JSONArray jsonArray=(JSONArray) jsonObject.get("items");
                for (int i = 0; i < jsonArray.size(); ++i) {
                    obj = (JSONObject) jsonArray.get(i);
                    System.out.println(obj.get("id"));
                }

            } catch (ParseException ex) {
                ex.printStackTrace();
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
    }
}