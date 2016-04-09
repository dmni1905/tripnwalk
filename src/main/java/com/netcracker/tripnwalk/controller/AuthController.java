package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.User;
import com.netcracker.tripnwalk.repository.UserRepository;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;

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
    private static final String FIELDS = "bdate,photo_200_orig";
    private static final String NAME_CASE = "nom";
    private static final String VERSION = "5.50";

    @Inject
    UserRepository userRepository;

    @Autowired
    private SessionController sessionController;

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public ResponseEntity<String> auth() throws IOException {
        String reqUrl = "http://oauth.vk.com/authorize?" +
                "client_id=" + CLIENT_ID +
                "&display=" + DISPLAY +
                "&redirect_uri=" + REDIRECT_URL +
                "&scope=" + SCOPE +
                "&response_type=" + RESPONSE_TYPE +
                "&v=" + VERSION;

        String result = sendHttpRequest(reqUrl).toString();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/getuserinfo", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> getUserInfoOauth(@RequestBody JSONObject strJson) throws ParseException, java.text.ParseException {

        String access_token = (String) strJson.get("access_token");
        sessionController.setAccessToken(access_token);
        String expires_in = (String) strJson.get("expires_in");
        sessionController.setExpiresIn(expires_in);
        String userIDOauth = (String) strJson.get("user_id");
        String email = (String) strJson.get("email");

        String reqUrl = "https://api.vk.com/method/users.get?" +
                "user_ids=" + userIDOauth +
                "&fields=" + FIELDS +
                "&name_case=" + NAME_CASE +
                "&v=" + VERSION;

        String result = sendHttpRequest(reqUrl).toString();

        //Is user exist in VK
        boolean isUserDeleted = result.contains("DELETED");
        if (isUserDeleted) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            User user = userRepository.findByOauthID(userIDOauth);
            if (user != null) {
                JSONObject resultJson = new JSONObject();
                resultJson.put("first_name", user.getName());
                resultJson.put("last_name", user.getSurname());
                resultJson.put("bdate", user.getBirthDate());
                result = resultJson.toString();
            } else {
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
                JSONArray jsonArray = (JSONArray) jsonObject.get("response");
                jsonObject = (JSONObject) jsonArray.get(0);

                user.setName((String) jsonObject.get("first_name"));
                user.setSurname((String) jsonObject.get("last_name"));
                user.setEmail(email);

                SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy");
                java.util.Date date = sdf.parse((String) jsonObject.get("bdate"));
                java.sql.Date sqlDate = new Date(date.getTime());
                user.setBirthDate(sqlDate);

                userRepository.save(user);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/getfriends", method = RequestMethod.GET) //TODO уточнить переадресацию
    public void getfriends() throws IOException {
        String reqUrl = "https://api.vk.com/method/friends.get?" +
                "user_id=" + VKUSER_ID +
                "&offset=" + OFFSET +
                "&fields=" + FIELDS +
                "&name_case=" + NAME_CASE +
                "&v=" + VERSION + 5.50;

        StringBuilder result = sendHttpRequest(reqUrl);
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
            jsonObject = (JSONObject) jsonObject.get("response");
            JSONObject obj;
            JSONArray jsonArray = (JSONArray) jsonObject.get("items");
            for (Object aJsonArray : jsonArray) {
                obj = (JSONObject) aJsonArray;
                System.out.println(obj.get("id"));
            }

        } catch (ParseException | NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public StringBuilder sendHttpRequest(String reqUrl) {
        StringBuilder result = new StringBuilder();
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
        return result;
    }
}