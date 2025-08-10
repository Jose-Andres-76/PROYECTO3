package com.project.demo.rest.gemini;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class GeminiConsoleTest {

    @Test
    void testGetApiKey_returnsConfiguredValue() {
        GeminiConsole geminiConsole = new GeminiConsole();
        ReflectionTestUtils.setField(geminiConsole, "apiKey", "TEST_API_KEY_123");

        assertThat(geminiConsole.getApiKey())
                .isEqualTo("TEST_API_KEY_123");
    }
}
