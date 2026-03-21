package com.gestion.ApplicationSignalement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CitoyenControllerIntegrationTests extends TestDataSetup {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSignalerProblemeAvecPhotos() throws Exception {
        MockMultipartFile photo1 = new MockMultipartFile(
                "photos", "photo1.jpg", "image/jpeg", "fake-content".getBytes()
        );

        mockMvc.perform(multipart("/api/citoyens/" + citoyenTest.getId() + "/problemes")
                        .file(photo1)
                        .param("titre", "Panne")
                        .param("description", "Lampadaire cassé")
                        .param("localisation", "Centre-ville")
                        .param("typeProblemeId", typeProblemeTest.getId().toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titre").value("Panne"))
                .andExpect(jsonPath("$.statut").value("SIGNALE"));
    }

    @Test
    void testVoirProblemes() throws Exception {
        mockMvc.perform(get("/api/citoyens/" + citoyenTest.getId() + "/problemes"))
                .andExpect(status().isOk());
    }
}