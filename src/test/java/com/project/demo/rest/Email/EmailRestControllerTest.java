package com.project.demo.rest.Email;

import com.project.demo.logic.entity.auth.JwtAuthenticationFilter;
import com.project.demo.logic.entity.email.EmailModel;
import com.project.demo.services.email.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmailRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EmailRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testSendEmail_success() throws Exception {
        doNothing().when(emailService).sendSimpleEmail(any(EmailModel.class));

        String json = """
            {
                "to": "test@example.com",
                "subject": "Test Subject",
                "body": "Test Body"
            }
        """;

        mockMvc.perform(post("/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Email sent successfully to test@example.com"));
    }
}
