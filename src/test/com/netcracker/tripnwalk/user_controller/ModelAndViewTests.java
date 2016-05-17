package com.netcracker.tripnwalk.user_controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.netcracker.tripnwalk.Application;
import com.netcracker.tripnwalk.config.WebAppContext;
import com.netcracker.tripnwalk.controller.SessionBean;
import com.netcracker.tripnwalk.entry.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, WebAppContext.class})
@WebAppConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
public class ModelAndViewTests {
    @Autowired
    private WebApplicationContext webApp;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApp).build();
    }

    @Test
    public void testIndex() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void testRegister() throws Exception {
        this.mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    @DatabaseSetup("resources/tripWalkData.xml")
    @DatabaseTearDown("resources/tearDown.xml")
    public void testMainPageForSessionUser() throws Exception {
        Long id = 1L;
        User user = new User("Ivan", "Ivanov", "12.12.2012", "vano", "qwerty", "ivan@gmail.com");
        user.setId(id);
        SessionBean sessionBean = new SessionBean();
        sessionBean.setSessionId(id);

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute("scopedTarget.sessionBean", sessionBean);

        this.mockMvc.perform(get("/").session(mockSession))
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute("scopedTarget.sessionBean", hasProperty("sessionId", is(id))))
                .andExpect(model().attribute("isMy", true))
                .andExpect(model().attribute("user", user))
                .andExpect(view().name("main-page"));
    }

    @Test
    @DatabaseSetup("resources/tripWalkData.xml")
    @DatabaseTearDown("resources/tearDown.xml")
    public void testMainPageForGuestUser() throws Exception {
        Long id = 1L;
        User user = new User("Дмитрий", "Ничик", "19.12.1990", "user", "123", "1@gmail.com");
        user.setId(2L);
        SessionBean sessionBean = new SessionBean();
        sessionBean.setSessionId(id);

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute("scopedTarget.sessionBean", sessionBean);

        this.mockMvc.perform(get("/" + 2L).session(mockSession))
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute("scopedTarget.sessionBean", hasProperty("sessionId", is(id))))
                .andExpect(model().attribute("isMy", false))
                .andExpect(model().attribute("user", user))
                .andExpect(view().name("main-page"));
    }

}
