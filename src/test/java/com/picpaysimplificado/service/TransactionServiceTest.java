package com.picpaysimplificado.service;

import com.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private UserService mockUserService;
    @Mock
    private TransactionRepository mockTransactionRepository;
    @Mock
    private RestTemplate mockRestTemplate;
    @Mock
    private NotificationService mockNotificationService;

    @InjectMocks
    private TransactionService transactionServiceUnderTest;

    @Test
    void testCreateTransaction() throws Exception {
        // Setup
        final TransactionDTO transaction = new TransactionDTO(new BigDecimal("0.00"), 0L, 0L);
        final Transaction expectedResult = new Transaction();
        expectedResult.setAmount(new BigDecimal("0.00"));
        final User sender = new User();
        sender.setId(0L);
        sender.setFirstName("firstName");
        sender.setLastName("lastName");
        sender.setBalance(new BigDecimal("0.00"));
        expectedResult.setSender(sender);
        final User receiver = new User();
        receiver.setId(0L);
        receiver.setFirstName("firstName");
        receiver.setLastName("lastName");
        receiver.setBalance(new BigDecimal("0.00"));
        expectedResult.setReceiver(receiver);
        expectedResult.setTimestamp(LocalDateTime.of(2020, 1, 1, 0, 0, 0));

        // Configure UserService.findUserById(...).
        final User user = new User();
        user.setId(0L);
        user.setType(UserType.COMOM);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setBalance(new BigDecimal("0.00"));
        when(mockUserService.findUserById(0L)).thenReturn(user);

        // Configure RestTemplate.getForEntity(...).
        final ResponseEntity<Map> mapResponseEntity = new ResponseEntity<>(Map.ofEntries(), HttpStatusCode.valueOf(0));
        when(mockRestTemplate.getForEntity("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", Map.class,
                0L)).thenReturn(mapResponseEntity);

        // Run the test
        final Transaction result = transactionServiceUnderTest.createTransaction(transaction);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);

        // Confirm UserService.validateTransaction(...).
        final User sender1 = new User();
        sender1.setId(0L);
        sender1.setType(UserType.COMOM);
        sender1.setFirstName("firstName");
        sender1.setLastName("lastName");
        sender1.setBalance(new BigDecimal("0.00"));
        verify(mockUserService).validateTransaction(sender1, new BigDecimal("0.00"));

        // Confirm TransactionRepository.save(...).
        final Transaction entity = new Transaction();
        entity.setAmount(new BigDecimal("0.00"));
        final User sender2 = new User();
        sender2.setId(0L);
        sender2.setFirstName("firstName");
        sender2.setLastName("lastName");
        sender2.setBalance(new BigDecimal("0.00"));
        entity.setSender(sender2);
        final User receiver1 = new User();
        receiver1.setId(0L);
        receiver1.setFirstName("firstName");
        receiver1.setLastName("lastName");
        receiver1.setBalance(new BigDecimal("0.00"));
        entity.setReceiver(receiver1);
        entity.setTimestamp(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        verify(mockTransactionRepository).save(entity);

        // Confirm UserService.saveUser(...).
        final User user1 = new User();
        user1.setId(0L);
        user1.setType(UserType.COMOM);
        user1.setFirstName("firstName");
        user1.setLastName("lastName");
        user1.setBalance(new BigDecimal("0.00"));
        verify(mockUserService).saveUser(user1);

        // Confirm NotificationService.sendNotification(...).
        final User user2 = new User();
        user2.setId(0L);
        user2.setType(UserType.COMOM);
        user2.setFirstName("firstName");
        user2.setLastName("lastName");
        user2.setBalance(new BigDecimal("0.00"));
        verify(mockNotificationService).sendNotification(user2, "Transacao realizada com sucesso");
    }

    @Test
    void testCreateTransaction_UserServiceFindUserByIdThrowsException() throws Exception {
        // Setup
        final TransactionDTO transaction = new TransactionDTO(new BigDecimal("0.00"), 0L, 0L);
        when(mockUserService.findUserById(0L)).thenThrow(Exception.class);

        // Run the test
        assertThatThrownBy(() -> transactionServiceUnderTest.createTransaction(transaction))
                .isInstanceOf(Exception.class);
    }

    @Test
    void testCreateTransaction_UserServiceValidateTransactionThrowsException() throws Exception {
        // Setup
        final TransactionDTO transaction = new TransactionDTO(new BigDecimal("0.00"), 0L, 0L);

        // Configure UserService.findUserById(...).
        final User user = new User();
        user.setId(0L);
        user.setType(UserType.COMOM);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setBalance(new BigDecimal("0.00"));
        when(mockUserService.findUserById(0L)).thenReturn(user);

        // Configure UserService.validateTransaction(...).
        final User sender = new User();
        sender.setId(0L);
        sender.setType(UserType.COMOM);
        sender.setFirstName("firstName");
        sender.setLastName("lastName");
        sender.setBalance(new BigDecimal("0.00"));
        doThrow(Exception.class).when(mockUserService).validateTransaction(sender, new BigDecimal("0.00"));

        // Run the test
        assertThatThrownBy(() -> transactionServiceUnderTest.createTransaction(transaction))
                .isInstanceOf(Exception.class);
    }

    @Test
    void testCreateTransaction_RestTemplateThrowsRestClientException() throws Exception {
        // Setup
        final TransactionDTO transaction = new TransactionDTO(new BigDecimal("0.00"), 0L, 0L);

        // Configure UserService.findUserById(...).
        final User user = new User();
        user.setId(0L);
        user.setType(UserType.COMOM);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setBalance(new BigDecimal("0.00"));
        when(mockUserService.findUserById(0L)).thenReturn(user);

        when(mockRestTemplate.getForEntity("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", Map.class,
                0L)).thenThrow(RestClientException.class);

        // Run the test
        assertThatThrownBy(() -> transactionServiceUnderTest.createTransaction(transaction))
                .isInstanceOf(RestClientException.class);

        // Confirm UserService.validateTransaction(...).
        final User sender = new User();
        sender.setId(0L);
        sender.setType(UserType.COMOM);
        sender.setFirstName("firstName");
        sender.setLastName("lastName");
        sender.setBalance(new BigDecimal("0.00"));
        verify(mockUserService).validateTransaction(sender, new BigDecimal("0.00"));
    }

    @Test
    void testCreateTransaction_NotificationServiceThrowsException() throws Exception {
        // Setup
        final TransactionDTO transaction = new TransactionDTO(new BigDecimal("0.00"), 0L, 0L);

        // Configure UserService.findUserById(...).
        final User user = new User();
        user.setId(0L);
        user.setType(UserType.COMOM);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setBalance(new BigDecimal("0.00"));
        when(mockUserService.findUserById(0L)).thenReturn(user);

        // Configure RestTemplate.getForEntity(...).
        final ResponseEntity<Map> mapResponseEntity = new ResponseEntity<>(Map.ofEntries(), HttpStatusCode.valueOf(0));
        when(mockRestTemplate.getForEntity("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", Map.class,
                0L)).thenReturn(mapResponseEntity);

        // Configure NotificationService.sendNotification(...).
        final User user1 = new User();
        user1.setId(0L);
        user1.setType(UserType.COMOM);
        user1.setFirstName("firstName");
        user1.setLastName("lastName");
        user1.setBalance(new BigDecimal("0.00"));
        doThrow(Exception.class).when(mockNotificationService).sendNotification(user1,
                "Transacao realizada com sucesso");

        // Run the test
        assertThatThrownBy(() -> transactionServiceUnderTest.createTransaction(transaction))
                .isInstanceOf(Exception.class);

        // Confirm UserService.validateTransaction(...).
        final User sender = new User();
        sender.setId(0L);
        sender.setType(UserType.COMOM);
        sender.setFirstName("firstName");
        sender.setLastName("lastName");
        sender.setBalance(new BigDecimal("0.00"));
        verify(mockUserService).validateTransaction(sender, new BigDecimal("0.00"));

        // Confirm TransactionRepository.save(...).
        final Transaction entity = new Transaction();
        entity.setAmount(new BigDecimal("0.00"));
        final User sender1 = new User();
        sender1.setId(0L);
        sender1.setFirstName("firstName");
        sender1.setLastName("lastName");
        sender1.setBalance(new BigDecimal("0.00"));
        entity.setSender(sender1);
        final User receiver = new User();
        receiver.setId(0L);
        receiver.setFirstName("firstName");
        receiver.setLastName("lastName");
        receiver.setBalance(new BigDecimal("0.00"));
        entity.setReceiver(receiver);
        entity.setTimestamp(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        verify(mockTransactionRepository).save(entity);

        // Confirm UserService.saveUser(...).
        final User user2 = new User();
        user2.setId(0L);
        user2.setType(UserType.COMOM);
        user2.setFirstName("firstName");
        user2.setLastName("lastName");
        user2.setBalance(new BigDecimal("0.00"));
        verify(mockUserService).saveUser(user2);
    }

    @Test
    void testAuthorizeTransaction() {
        // Setup
        final User sender = new User();
        sender.setId(0L);
        sender.setType(UserType.COMOM);
        sender.setFirstName("firstName");
        sender.setLastName("lastName");
        sender.setBalance(new BigDecimal("0.00"));

        // Configure RestTemplate.getForEntity(...).
        final ResponseEntity<Map> mapResponseEntity = new ResponseEntity<>(Map.ofEntries(), HttpStatusCode.valueOf(0));
        when(mockRestTemplate.getForEntity("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", Map.class,
                0L)).thenReturn(mapResponseEntity);

        // Run the test
        final boolean result = transactionServiceUnderTest.authorizeTransaction(sender, new BigDecimal("0.00"));

        // Verify the results
        assertThat(result).isFalse();
    }

    @Test
    void testAuthorizeTransaction_RestTemplateThrowsRestClientException() {
        // Setup
        final User sender = new User();
        sender.setId(0L);
        sender.setType(UserType.COMOM);
        sender.setFirstName("firstName");
        sender.setLastName("lastName");
        sender.setBalance(new BigDecimal("0.00"));

        when(mockRestTemplate.getForEntity("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", Map.class,
                0L)).thenThrow(RestClientException.class);

        // Run the test
        assertThatThrownBy(
                () -> transactionServiceUnderTest.authorizeTransaction(sender, new BigDecimal("0.00")))
                .isInstanceOf(RestClientException.class);
    }
}
