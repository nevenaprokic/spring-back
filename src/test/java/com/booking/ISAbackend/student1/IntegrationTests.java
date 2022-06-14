package com.booking.ISAbackend.student1;
import static org.springframework.integration.util.MessagingAnnotationUtils.hasValue;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.booking.ISAbackend.config.WebConfig;
import com.booking.ISAbackend.config.WebSecurityConfig;
import com.booking.ISAbackend.controller.ClientController;
import com.booking.ISAbackend.controller.CottageController;
import com.booking.ISAbackend.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class IntegrationTests {

    private static final String URL_PREFIX = "/cottage";

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    public void cottageInfoTest() throws Exception {
            mockMvc.perform(get(URL_PREFIX + "/get-info?idCottage=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value("Vikendica Raj"))
                .andExpect(jsonPath("$.price").value(30.00))
                .andExpect(jsonPath("$.rulesOfConduct").value("Nije dozvoljeno bilo kakvo unistavanje imovine."))
                .andExpect(jsonPath("$.city").value("Novi Sad"))
                .andExpect(jsonPath("$.street").value("Omladinska 19"))
                .andExpect(jsonPath("$.numberOfPerson").value(2))
                .andExpect(jsonPath("$.roomNumber").value(2))
                .andExpect(jsonPath("$.bedNumber").value(2))
                .andExpect(jsonPath("$.state").value("Srbija"))
                .andReturn();
    }

    @Test
    public void getCottagesTest() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/get-all")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.[*].id").value(hasItem(1)))
                .andExpect(jsonPath("$.[*].price").value(hasItem(30.00)))
                .andExpect(jsonPath("$.[*].rulesOfConduct").value(hasItem("Nije dozvoljeno bilo kakvo unistavanje imovine.")))
                .andExpect(jsonPath("$.[*].city").value(hasItem("Kopaonik")))
                .andExpect(jsonPath("$.[*].name").value(hasItem("Vikendica Raj")))
                .andExpect(jsonPath("$.[*].name").value(hasItem("Brvnara")));

    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    @Transactional
    public void makeComplaintTest() throws Exception {

        HashMap<String, String> map = new HashMap<>();
        map.put("reservationId", "1");
        map.put("comment", "Super je bilo!");
        map.put("email", "markoooperic123+pera@gmail.com");
        String data = TestUtil.json(map);

        mockMvc.perform(put("/client/make-complaint").contentType(contentType).content(data)).andExpect(status().isCreated());
    }
}
