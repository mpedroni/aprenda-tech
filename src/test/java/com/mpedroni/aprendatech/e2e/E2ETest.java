package com.mpedroni.aprendatech.e2e;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles("test-e2e")
@Testcontainers
@AutoConfigureMockMvc
class E2ETest {
    @Autowired
    public MockMvc mockMvc;

    @BeforeEach
    public void setupRestAssuredMockMvc() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }
}
