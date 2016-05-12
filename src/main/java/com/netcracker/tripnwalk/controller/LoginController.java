package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.User;
import com.netcracker.tripnwalk.repository.UserRepository;
import com.netcracker.tripnwalk.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
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
import java.util.*;

@RestController
class LoginController {
    private static final Logger logger = LogManager.getLogger(LoginController.class);

    // Для запроса списка друзей пользователя
    private static final String VKUSER_ID = "9911063";
    private static final String FIELDS = "bdate,photo_200_orig";
    private static final String NAME_CASE = "nom";
    private static final String VERSION = "5.50";

    @Inject
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private SessionBean sessionBean;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView registerPage() throws IOException {
        return new ModelAndView("register");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> getAuth(HttpServletRequest request, @RequestBody User user) {
        Optional<User> userBD = userService.getByLogin(user.getLogin());

        if (userBD.isPresent()) {
            User userFromBD = userBD.get();
            if (userFromBD.getPassword().equals(DigestUtils.md5Hex(user.getPassword()))) {
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

        if ((!isUserDeleted) && (!isUserExist)) {
            User user = userRepository.findByOauthID(userIDOauth);
            User userEmail = userRepository.findByEmail(email);

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONArray jsonArray = (JSONArray) jsonObject.get("response");
            jsonObject = (JSONObject) jsonArray.get(0);

            if (userEmail != null) {
                userEmail.setSourceId(userIDOauth);
                userEmail.setSourceType("VK");
                userRepository.save(userEmail);
                user = userEmail;
            } else if (user == null) {

                user = new User((String) jsonObject.get("first_name"), (String) jsonObject.get("last_name"), email,
                        userIDOauth, "VK");

                if (jsonObject.get("bdate") != null) {
                    user.setBirthDate((String) jsonObject.get("bdate"));
                }
                if (jsonObject.get("photo_200_orig") != "http://vk.com/images/camera_200.png") {
                    user.setImgSrc((String) jsonObject.get("photo_200_orig"));
                }

                //Add friends from VK
                user = addFriendFromVK(user);
            }

            sessionBean.setSessionId(user.getId());

            if (user.getBirthDate() != null) {
                resultJson.put("bdate", user.getBirthDate());
            }
            resultJson.put("email", user.getEmail());
            resultJson.put("last_name", user.getSurname());
            resultJson.put("first_name", user.getName());
            resultJson.put("session_id", sessionBean.getSessionId());
            result = resultJson.toString();

            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    private User addFriendFromVK(User user) {
        String reqUrl = "https://api.vk.com/method/friends.get?" +
                "user_id=" + VKUSER_ID +
                "&fields=" + FIELDS +
                "&name_case=" + NAME_CASE +
                "&v=" + VERSION;

        String result = sendHttpRequest(reqUrl).toString();

        //коллекция для получения ID друзей из ВК
        Set<String> userVkMap = new HashSet<>();
        //коллекция, где значение - ID VK, а ключ user
        Map<String, User> userMap = new HashMap<>();
        userRepository.findAll().forEach(u -> {
            if (Optional.ofNullable(u.getSourceType()).isPresent()) {
                if (u.getSourceType().equals("VK")) {
                    userMap.put(u.getSourceId(), u);
                }
            }
        });
        userMap.remove(user.getSourceId());

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            jsonObject = (JSONObject) jsonObject.get("response");
            JSONObject obj;
            JSONArray jsonArray = (JSONArray) jsonObject.get("items");

            for (Object aJsonArray : jsonArray) {
                obj = (JSONObject) aJsonArray;
                userVkMap.add(obj.get("id").toString());
                //System.out.println(obj.get("id")); TODO
            }
        } catch (ParseException | NullPointerException ex) {
            ex.printStackTrace();
        }

        userVkMap.retainAll(userMap.keySet());
        for (String userVK : userVkMap) {
            user.addFriend(userMap.get(userVK));
        };
        userRepository.save(user);
        return user;
    }


    private StringBuilder sendHttpRequest(String reqUrl) {
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