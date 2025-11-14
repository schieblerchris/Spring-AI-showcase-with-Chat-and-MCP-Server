package com.github.sc.apps.saisc.chatmodel;

import com.github.sc.apps.saisc.TestcontainersConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@Slf4j
class OpenAIAdapterTest {

    @Autowired
    private OpenAIAdapter openAIAdapter;

    @Test
    void getModels() {
        var models = openAIAdapter.getModels();
        log.info("models: {}", models);
        assertNotNull(models);
    }

}