package com.netcracker.tripnwalk.user_controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.netcracker.tripnwalk.Application;
import com.netcracker.tripnwalk.component.TestDataResource;
import com.netcracker.tripnwalk.config.WebAppContext;
import com.netcracker.tripnwalk.controller.SessionBean;
import com.netcracker.tripnwalk.service.UserService;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, WebAppContext.class})
@WebAppConfiguration
@DbUnitConfiguration(dataSetLoader = TestDataResource.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("resources/tripWalkData.xml")
@DatabaseTearDown("resources/tearDown.xml")
public class UserModifyTests {
    @Autowired
    private WebApplicationContext webApp;
    @Autowired
    UserService userService;

    private MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private MockMvc mockMvc;
    private Map<String, String> user;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApp).build();
        user = new HashMap<>();
        user.put("id", "1");
        user.put("name", "Ivan-newName");
        user.put("surname", "Ivanov");
        user.put("birthDate", "12.12.1998");
        user.put("password", "qwerty");
        user.put("login", "vano");
        user.put("email", "ivan@gmail.com");

    }

    @Test
    @ExpectedDatabase("resources/expectedTripData.xml")
    public void testModifyUser() throws Exception {
        Long id = 1L;
        SessionBean sessionBean = new SessionBean();
        sessionBean.setSessionId(id);

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute("scopedTarget.sessionBean", sessionBean);
        JSONObject obj = new JSONObject(user);
        this.mockMvc.perform(patch("/" + id).contentType(APPLICATION_JSON_UTF8).content(obj.toJSONString()).session(mockSession))
                .andExpect(status().isOk());
    }

    @Test
    public void testModifyUncorrectedUser() throws Exception {
        Long id = 1L;
        user.remove("email");
        JSONObject obj = new JSONObject(user);
        this.mockMvc.perform(patch("/" + id).contentType(APPLICATION_JSON_UTF8).content(obj.toJSONString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testModifyUnauthorizedUser() throws Exception {
        Long id = 1L;
        user.remove("id");
        JSONObject obj = new JSONObject(user);
        this.mockMvc.perform(patch("/" + id).contentType(APPLICATION_JSON_UTF8).content(obj.toJSONString()))
                .andExpect(status().isUnauthorized());
    }


}
