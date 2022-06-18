package com.booking.ISAbackend.student3;

import com.booking.ISAbackend.model.ClientCategory;
import com.booking.ISAbackend.model.OwnerCategory;
import com.booking.ISAbackend.util.TestUtil;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class IntegrationTests {

    private static final String URL_PREFIX_MARK = "/mark";
    private static final String URL_PREFIX_ADMIN = "/admin";
    private static final String URL_PREFIX_LOYALTY = "/loyalty";

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
    @WithMockUser(authorities = {"ADMIN"})
    public void getNotReviewedMarsTest() throws Exception {
            mockMvc.perform(get(URL_PREFIX_MARK + "/all-unchecked"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.[*].id").value(hasItem(1)))
                    .andExpect(jsonPath("$.[*].approved").value(hasItem(false)))
                    .andExpect(jsonPath("$.[*].comment").value(hasItem("Odlican ambijent!")))
                    .andExpect(jsonPath("$.[*].mark").value(hasItem(5)))
                    .andExpect(jsonPath("$.[*].sendingTime").value(hasItem("09/05/2022")));

    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void getNotReviewedComplaintsTest() throws Exception {
        mockMvc.perform(get(URL_PREFIX_ADMIN + "/all-complaints"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.[*].id").value(hasItem(1)))
                .andExpect(jsonPath("$.[*].offerName").value(hasItem("Vikendica Raj")))
                .andExpect(jsonPath("$.[*].clientName").value(hasItem("Marko Slavic")))
                .andExpect(jsonPath("$.[*].clientCategory").value(hasItem("CASUAL_CLIENT")))
                .andExpect(jsonPath("$.[*].clientPenalty").value(hasItem(0)))
                .andExpect(jsonPath("$.[*].reservationStartDate").value(hasItem("29/03/2022")))
                .andExpect(jsonPath("$.[*].reservationEndDate").value(hasItem("01/04/2022")))
                .andExpect(jsonPath("$.[*].recivedTime").value(hasItem("01/06/2022")));

    }


    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void addLoyaltyCategoryTest() throws Exception {

        ClientCategory newCategory = new ClientCategory();
        newCategory.setId(4);
        newCategory.setName("PLATINUM");
        newCategory.setLowLimitPoints(101);
        newCategory.setHeighLimitPoints(150);
        newCategory.setDiscount(20.0);
        newCategory.setReservationPoints(6);
        newCategory.setCategoryColor("#050124");
        String data = TestUtil.json(newCategory);

        mockMvc.perform(post( URL_PREFIX_LOYALTY + "/add-client-category").contentType(contentType).content(data)).andExpect(status().isOk());

    }

}


