package com.mpedroni.aprendatech;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

public class RestAssuredSetup implements BeforeAllCallback {
    @Override
    public void beforeAll(ExtensionContext context) {
        var appContext = SpringExtension.getApplicationContext(context);
        var mockMvc = appContext.getBean(MockMvc.class);

        RestAssuredMockMvc.mockMvc(mockMvc);
    }
}
