package com.netcracker.tripnwalk.component;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class ReadUsersFromResponse {
    private String response;

    public ReadUsersFromResponse(String response) {
        this.response = response;
    }

    public JSONObject getSoloUser() {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jsonParser.parse(response);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getSoloUserFromList() {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jsonParser.parse(response.substring(1, response.length() - 1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public List<JSONObject> getListUser(){
        String[] res = response.split("\\},\\{");
        res[0] = res[0].substring(1, res[0].length()) + "}";
        res[res.length - 1] = "{" + res[res.length - 1].substring(0, res[res.length - 1].length() - 1);
        for (int i = 1; i < res.length - 1; i++) {
            res[i] = "{" + res[i] + "}";
        }
        List<JSONObject> jsonArray = new ArrayList<>();
        for (int i = 0; i < res.length; i++) {
            JSONParser jsonParser = new JSONParser();
            try {
                jsonArray.add((JSONObject) jsonParser.parse(res[i]));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return jsonArray;
    }
}
