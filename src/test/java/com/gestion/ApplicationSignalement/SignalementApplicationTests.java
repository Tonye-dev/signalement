package com.gestion.ApplicationSignalement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import com.gestion.ApplicationSignalement.config.TestDatabaseConfig;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestDatabaseConfig.class)  // <-- force Spring à utiliser H2
class SignalementApplicationTests {

    @Autowired
    Environment env;

    @Test
    void contextLoads() {
        System.out.println("Datasource utilisée : " + env.getProperty("spring.datasource.url"));
    }
}