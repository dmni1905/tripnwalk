package com.netcracker.tripnwalk.user_controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.*;

import com.netcracker.tripnwalk.Application;
import com.netcracker.tripnwalk.config.WebAppContext;
import com.netcracker.tripnwalk.entry.User;
import com.netcracker.tripnwalk.service.UserService;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, WebAppContext.class})
@WebAppConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("resources/tripWalkData.xml")
@DatabaseTearDown("resources/tearDown.xml")
public class UserPutTests {
    @Autowired
    private WebApplicationContext webApp;
    @Autowired
    UserService userService;

    private MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApp).build();
    }

    @Test
    public void testPutUser() throws Exception {
        Map<String, String> user = new HashMap<>();
        user.put("name", "newName");
        user.put("surname", "newSurname");
        user.put("birthDate", "18.01.1993");
        user.put("login", "login");
        user.put("password", "12345");
        user.put("email", "new@ya.ru");

        JSONObject obj = new JSONObject(user);
        ResultActions resultActions = this.mockMvc.perform(put("/").contentType(APPLICATION_JSON_UTF8).content(obj.toJSONString()))
                .andExpect(status().isOk());

        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        assertNotNull(contentAsString);
        assertTrue(NumberUtils.isNumber(contentAsString));

        User byId = userService.getById(Long.parseLong(contentAsString)).get();
        assertEquals(byId.getName(), user.get("name"));
        assertEquals(byId.getSurname(), user.get("surname"));
        assertEquals(byId.getLogin(), user.get("login"));
        assertEquals(byId.getEmail(), user.get("email"));
    }

    @Test
    public void testPutRepeatUser() throws Exception {
        Map<String, String> user = new HashMap<>();
        user.put("name", "Николай");
        user.put("surname", "Николаев");
        user.put("birthDate", "18.01.1993");
        user.put("login", "user");
        user.put("password", "12345");
        user.put("email", "test@ya.ru");

        JSONObject obj = new JSONObject(user);
        this.mockMvc.perform(put("/").contentType(APPLICATION_JSON_UTF8).content(obj.toJSONString()))
                .andExpect(status().isBadRequest());
    }
}
