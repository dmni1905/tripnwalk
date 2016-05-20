package com.netcracker.tripnwalk.user_controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.netcracker.tripnwalk.Application;
import com.netcracker.tripnwalk.component.ReadUsersFromResponse;
import com.netcracker.tripnwalk.config.WebAppContext;
import com.netcracker.tripnwalk.controller.SessionBean;
import com.netcracker.tripnwalk.entry.User;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, WebAppContext.class})
@WebAppConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("resources/tripWalkData.xml")
@DatabaseTearDown("resources/tearDown.xml")
public class FindUserInDBTests {
    @Autowired
    private WebApplicationContext webApp;

    private MockMvc mockMvc;
    private User user1;
    private User user2;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApp).build();
        user1 = new User("Ivan", "Ivanov", "12.12.2012", "vano", "qwerty", "ivan@gmail.com");
        user2 = new User("Дмитрий", "Ничик", "19.12.1990", "user", "123", "1@gmail.com");
    }

    @Test
    public void testFindUserByName() throws Exception {
        Long id = 1L;
        user2.setId(2L);
        SessionBean sessionBean = new SessionBean();
        sessionBean.setSessionId(id);

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute("scopedTarget.sessionBean", sessionBean);

        ResultActions resultActions = this.mockMvc.perform(get("/find-user").
                session(mockSession)
                .param("name", "Дмитрий")
                .param("surname", ""))
                .andDo(print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute("scopedTarget.sessionBean", hasProperty("sessionId", is(id))));

        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        response.setCharacterEncoding("utf-8");
        ReadUsersFromResponse responseUser = new ReadUsersFromResponse(response.getContentAsString());

        JSONObject jsonObject = responseUser.getSoloUserFromList();

        assertEquals(user2.getName(), jsonObject.get("name"));
        assertEquals(user2.getSurname(), jsonObject.get("surname"));
        assertEquals(user2.getBirthDate(), jsonObject.get("birthDate"));
        assertEquals(user2.getLogin(), jsonObject.get("login"));
        assertEquals(user2.getEmail(), jsonObject.get("email"));
    }


    @Test
    public void testFindUserByFullName() throws Exception {
        Long id = 2L;
        user1.setId(1L);
        SessionBean sessionBean = new SessionBean();
        sessionBean.setSessionId(id);

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute("scopedTarget.sessionBean", sessionBean);

        ResultActions resultActions = this.mockMvc.perform(get("/find-user").
                session(mockSession)
                .param("name", "Ivan")
                .param("surname", "Ivanov"))
                .andDo(print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute("scopedTarget.sessionBean", hasProperty("sessionId", is(id))));

        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        response.setCharacterEncoding("utf-8");
        ReadUsersFromResponse responseUser = new ReadUsersFromResponse(response.getContentAsString());

        JSONObject jsonObject = responseUser.getSoloUserFromList();

        assertEquals(user1.getName(), jsonObject.get("name"));
        assertEquals(user1.getSurname(), jsonObject.get("surname"));
        assertEquals(user1.getBirthDate(), jsonObject.get("birthDate"));
        assertEquals(user1.getLogin(), jsonObject.get("login"));
        assertEquals(user1.getEmail(), jsonObject.get("email"));
    }

    @Test
    public void testFindAllUser() throws Exception {
        Long id = 5L;
        SessionBean sessionBean = new SessionBean();
        sessionBean.setSessionId(id);
        List<User> user = new ArrayList<>();
        user.add(user1);
        user.add(user2);

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute("scopedTarget.sessionBean", sessionBean);

        ResultActions resultActions = this.mockMvc.perform(get("/find-user").
                session(mockSession)
                .param("name", "")
                .param("surname", ""));
        resultActions
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute("scopedTarget.sessionBean", hasProperty("sessionId", is(id))));

        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        response.setCharacterEncoding("utf-8");
        ReadUsersFromResponse responseUser = new ReadUsersFromResponse(response.getContentAsString());
        List<JSONObject> jsonObjectList = responseUser.getListUser();

        assertEquals(jsonObjectList.size(), 2);

        for (int i = 0; i < jsonObjectList.size(); i++) {
            int j = 0;
            if(jsonObjectList.get(i).get("id").toString().equals("1")){
                j = 0;
            } else if(jsonObjectList.get(i).get("id").toString().equals("2")){
                j = 1;
            }
            assertEquals(user.get(j).getName(), jsonObjectList.get(i).get("name"));
            assertEquals(user.get(j).getSurname(), jsonObjectList.get(i).get("surname"));
            assertEquals(user.get(j).getLogin(), jsonObjectList.get(i).get("login"));
            assertEquals(user.get(j).getEmail(), jsonObjectList.get(i).get("email"));
        }
    }

}
