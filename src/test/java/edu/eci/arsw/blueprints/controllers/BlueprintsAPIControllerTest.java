package edu.eci.arsw.blueprints.controllers;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashSet;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlueprintsAPIController.class)
class BlueprintsAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlueprintsServices services;

    @Test
    void getAllBlueprintsShouldReturnOk() throws Exception {
        given(services.getAllBlueprints()).willReturn(new HashSet<>());

        mockMvc.perform(get("/api/v1/blueprints"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Blueprints retrieved successfully"));
    }

    @Test
    void getBlueprintByAuthorAndNameShouldReturnNotFoundWhenMissing() throws Exception {
        given(services.getBlueprint("john", "house")).willThrow(new BlueprintNotFoundException("Not found"));

        mockMvc.perform(get("/api/v1/blueprints/john/house"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    void createBlueprintShouldReturnCreated() throws Exception {
        String json = "{\"author\":\"john\",\"name\":\"kitchen\",\"points\":[]}";

        mockMvc.perform(post("/api/v1/blueprints")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(201));
    }
}
