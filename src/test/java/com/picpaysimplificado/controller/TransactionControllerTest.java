package com.picpaysimplificado.controller;

import com.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.service.TransactionService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService mockTransactionService;

    @Test
    void testCreateTransaction() throws Exception {
        // Setup
        // Configure TransactionService.createTransaction(...).
        final Transaction transaction = new Transaction();
        transaction.setId(0L);
        transaction.setAmount(new BigDecimal("0.00"));
        final User sender = new User();
        sender.setId(0L);
        sender.setType(UserType.COMOM);
        transaction.setSender(sender);
        when(mockTransactionService.createTransaction(new TransactionDTO(new BigDecimal("0.00"), 0L, 0L)))
                .thenReturn(transaction);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/transactions")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testCreateTransaction_TransactionServiceThrowsException() throws Exception {
        // Setup
        when(mockTransactionService.createTransaction(new TransactionDTO(new BigDecimal("0.00"), 0L, 0L)))
                .thenThrow(Exception.class);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/transactions")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }
}
