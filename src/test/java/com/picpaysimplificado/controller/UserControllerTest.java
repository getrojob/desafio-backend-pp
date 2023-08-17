package com.picpaysimplificado.controller;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.dtos.userDTO;
import com.picpaysimplificado.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService mockUserService;

    @Test
    void testCreateUser() throws Exception {
        // Setup
        // Configure UserService.createUser(...).
        final User user = new User();
        user.setId(0L);
        user.setType(UserType.COMOM);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setDocument("document");
        when(mockUserService.createUser(
                new userDTO("firstName", "lastName", "document", new BigDecimal("0.00"), "email", "password",
                        UserType.COMOM))).thenReturn(user);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/users")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetAllUsers() throws Exception {
        // Setup
        // Configure UserService.getAllUsers(...).
        final User user = new User();
        user.setId(0L);
        user.setType(UserType.COMOM);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setDocument("document");
        final List<User> users = List.of(user);
        when(mockUserService.getAllUsers()).thenReturn(users);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetAllUsers_UserServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockUserService.getAllUsers()).thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    void testGetUserByDocument() throws Exception {
        // Setup
        // Configure UserService.findUserByDocument(...).
        final User user = new User();
        user.setId(0L);
        user.setType(UserType.COMOM);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setDocument("document");
        when(mockUserService.findUserByDocument("doc")).thenReturn(user);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/users/{document}")
                        .param("doc", "doc")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetUserByDocument_UserServiceThrowsException() throws Exception {
        // Setup
        when(mockUserService.findUserByDocument("doc")).thenThrow(Exception.class);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/users/{document}")
                        .param("doc", "doc")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }
}
