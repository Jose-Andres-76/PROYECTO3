package com.project.demo.rest.cloudinary;

import com.project.demo.logic.entity.auth.JwtService;
import com.project.demo.rest.Email.EmailRestController;
import com.project.demo.rest.cloudinary.CloudinaryRestController;
import com.project.demo.services.cloudinary.CloudinaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(CloudinaryRestController.class)
public class CloudinaryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CloudinaryService cloudinaryService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser
    public void testGetAll_ReturnsOkAndMessage() throws Exception {
        mockMvc.perform(get("/cloudinary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Im using Cloudinary"));
    }
}
