package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.User;
import com.netcracker.tripnwalk.repository.UserRepository;
import com.netcracker.tripnwalk.service.UserService;
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
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

@RestController
class LoginController {
    private static final Logger logger = LogManager.getLogger(LoginController.class);

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
    //"https://api.vk.com/method/users.get?user_ids=9911063&fields=bdate,photo_200_orig&name_case=nom&v=5.50;
    @Inject
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    private SessionBean sessionBean;

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


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView registerPage() throws IOException {
        return new ModelAndView("register");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> getAuth(HttpServletRequest request, @RequestBody User user) {
        Optional<User> userBD = userService.getByLogin(user.getLogin());
        if (userBD.isPresent()) {
            User userFromBD = userBD.get();
            if (userFromBD.getPassword().equals(user.getPassword())) {
                sessionBean.setSessionId(userFromBD.getId());
                String session = sessionBean.getSessionId().toString();
                return new ResponseEntity<>(session, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity<String> logout() {
        Optional<Long> sessionId = Optional.ofNullable(sessionBean.getSessionId());
        if (sessionId.isPresent()) {
            sessionBean.setSessionId(null);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/session", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> getUserInfoOauth(HttpServletRequest request, @RequestBody JSONObject strJson) throws ParseException {

        String userIDOauth = (String) strJson.get("user_id");
        String email = (String) strJson.get("email");

        String reqUrl = "https://api.vk.com/method/users.get?" +
                "user_ids=" + userIDOauth +
                "&fields=" + FIELDS +
                "&name_case=" + NAME_CASE +
                "&v=" + VERSION;

        String result = sendHttpRequest(reqUrl).toString();

        //Is user exist in VK
        JSONObject resultJson = new JSONObject();
        boolean isUserDeleted = result.contains("DELETED");
        boolean isUserExist = result.contains("error");

        if ((isUserDeleted) || (isUserExist)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            User user = userRepository.findByOauthID(userIDOauth);
            User userEmail = userRepository.findByEmail(email);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONArray jsonArray = (JSONArray) jsonObject.get("response");
            jsonObject = (JSONObject) jsonArray.get(0);

            if (userEmail != null) {
                userEmail.setSourceType(userIDOauth);
                userEmail.setSourceId("VK");
                userRepository.save(userEmail);

                user = userEmail;
            } else if (user == null) {

                user = new User((String) jsonObject.get("first_name"), (String) jsonObject.get("last_name"), email,
                        userIDOauth, "VK");

                if (jsonObject.get("bdate") != null) {
                    user.setBirthDate((String) jsonObject.get("bdate"));
                }
                userRepository.save(user);
            }

            sessionBean.setSessionId(user.getId());

            if (user.getBirthDate() != null) {
                resultJson.put("bdate", user.getBirthDate().toString());
            }
            resultJson.put("email", user.getEmail());
            resultJson.put("last_name", user.getSurname());
            resultJson.put("first_name", user.getName());
            resultJson.put("session_id", sessionBean.getSessionId());
            result = resultJson.toString();
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