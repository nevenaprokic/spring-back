package com.booking.ISAbackend.student2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class IntegrationTests {
    private static final String URL_PREFIX_COTTAGE = "/cottage";
    private static final String URL_PREFIX_SHIP = "/ship";
    private static final String URL_PREFIX_RESERVATION = "/reservation";

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
    @WithMockUser(authorities = {"COTTAGE_OWNER"})
    @Transactional
    public void getCottagesTest() throws Exception {
        mockMvc.perform(get(URL_PREFIX_COTTAGE + "/get-cottages-by-owner-email?email=markoooperic123+mile@gmail.com")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.[*].id").value(hasItem(1)))
                .andExpect(jsonPath("$.[*].price").value(hasItem(150.00)))
                .andExpect(jsonPath("$.[*].price").value(hasItem(120.00)))
                .andExpect(jsonPath("$.[*].cancellationConditions").value(hasItem("Dozvoljeno je otkazivanje 3 dana pre.")))
                .andExpect(jsonPath("$.[*].city").value(hasItem("Kopaonik")))
                .andExpect(jsonPath("$.[*].name").value(hasItem("Sumska vila")))
                .andExpect(jsonPath("$.[*].name").value(hasItem("Holiday Home Floris")));

    }

    @Test
    @WithMockUser(authorities = {"COTTAGE_OWNER"})
    @Transactional
    public void deleteCottagesTest() throws Exception {
        mockMvc.perform(delete(URL_PREFIX_COTTAGE + "/delete?cottageId=4")).andExpect(status().isOk());

    }

    @Test
    @WithMockUser(authorities = {"COTTAGE_OWNER"})
    @Transactional
    public void getPastReservationTest() throws Exception {
        mockMvc.perform(get(URL_PREFIX_RESERVATION + "/get-all-by-cottage-owner?ownerId=markoooperic123+mile@gmail.com&role=COTTAGE_OWNER")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(24)))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.[*].startDate").value(hasItem("01/01/2022")))
                .andExpect(jsonPath("$.[*].clinetId").value(hasItem(1)))
                .andExpect(jsonPath("$.[*].offerId").value(hasItem(1)))
                .andExpect(jsonPath("$.[*].numberOfPerson").value(hasItem(2)));

    }

    @Test
    @WithMockUser(authorities = {"SHIP_OWNER"})
    @Transactional
    public void getShipsTest() throws Exception {
        mockMvc.perform(get(URL_PREFIX_SHIP + "/get-all-by-owner?email=markoooperic123+ksenija@gmail.com")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.[*].id").value(hasItem(5)))
                .andExpect(jsonPath("$.[*].price").value(hasItem(50.00)))
                .andExpect(jsonPath("$.[*].price").value(hasItem(120.00)))
                .andExpect(jsonPath("$.[*].rulesOfConduct").value(hasItem("Nije dozvoljeno bilo kakvo unistavanje imovine.")))
                .andExpect(jsonPath("$.[*].city").value(hasItem("Novi Sad")))
                .andExpect(jsonPath("$.[*].name").value(hasItem("Bela ladja")))
                .andExpect(jsonPath("$.[*].name").value(hasItem("Sidro")));

    }
}
