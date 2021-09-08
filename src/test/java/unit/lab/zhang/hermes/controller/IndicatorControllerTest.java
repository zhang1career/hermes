package unit.lab.zhang.hermes.controller;

import lab.zhang.hermes.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class IndicatorControllerTest {

    @Autowired
    ApplicationContext ctx;

    @Autowired
    private MockMvc mockMvc;

    @Before()
    public void setUp() {
    }

    @Test
    public void test_list() throws Exception {
        String url = "/indicators";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api" + url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
        ;
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println(content);
    }

    @Test
    public void test_item() throws Exception {
        String url = "/indicators/1";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api" + url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
        ;
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println(content);
    }
}
