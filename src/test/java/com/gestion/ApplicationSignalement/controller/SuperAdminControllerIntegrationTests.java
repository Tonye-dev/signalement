package com.gestion.ApplicationSignalement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestion.ApplicationSignalement.entity.Administrateur;
import com.gestion.ApplicationSignalement.entity.Mairie;
import com.gestion.ApplicationSignalement.entity.TypeProbleme;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SuperAdminControllerIntegrationTests extends TestDataSetup {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAjouterMairie() throws Exception {
        Mairie mairie = new Mairie();
        mairie.setNom("Nouvelle Mairie");
        mairie.setAdresse("Centre");

        mockMvc.perform(post("/api/superadmin/mairies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mairie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Nouvelle Mairie"));
    }

    @Test
    void testAjouterAdministrateur() throws Exception {
        Administrateur admin = new Administrateur();
        admin.setNom("AdminTest");
        admin.setPrenom("Test");
        admin.setEmail("admintest@test.com");
        admin.setMotdepasse("1234");
       

        mockMvc.perform(post("/api/superadmin/mairies/" + mairieTest.getId() + "/administrateurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(admin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admintest@test.com"));
    }

    @Test
    void testCreerTypeProbleme() throws Exception {
        TypeProbleme typeProbleme = new TypeProbleme();
        typeProbleme.setNomType("Voirie");
        typeProbleme.setDescriptionType("Problèmes de voirie");

        mockMvc.perform(post("/api/superadmin/typeproblemes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(typeProbleme)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomType").value("Voirie"));
    }
}