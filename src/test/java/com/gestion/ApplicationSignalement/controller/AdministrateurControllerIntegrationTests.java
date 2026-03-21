package com.gestion.ApplicationSignalement.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdministrateurControllerIntegrationTests extends TestDataSetup {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testVoirProblemesAssignes() throws Exception {
        mockMvc.perform(get("/api/administrateurs/" + adminTest.getId() + "/problemes"))
                .andExpect(status().isOk());
    }

    @Test
    void testFiltrerProblemes() throws Exception {
        mockMvc.perform(get("/api/administrateurs/" + adminTest.getId() + "/problemes?statut=SIGNALE"))
                .andExpect(status().isOk());
    }

    @Test
    void testVoirRapports() throws Exception {
        mockMvc.perform(get("/api/administrateurs/" + adminTest.getId() + "/rapports"))
                .andExpect(status().isOk());
    }
}