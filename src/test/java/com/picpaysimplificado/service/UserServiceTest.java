package com.picpaysimplificado.service;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.dtos.userDTO;
import com.picpaysimplificado.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository mockRepository;

    @InjectMocks
    private UserService userServiceUnderTest;

    @Test
    void testValidateTransaction() throws Exception {
        // Setup
        final User sender = new User();
        sender.setId(0L);
        sender.setType(UserType.COMOM);
        sender.setFirstName("firstName");
        sender.setLastName("lastName");
        sender.setBalance(new BigDecimal("0.00"));

        // Run the test
        userServiceUnderTest.validateTransaction(sender, new BigDecimal("0.00"));

        // Verify the results
    }

    @Test
    void testValidateTransaction_ThrowsException() {
        // Setup
        final User sender = new User();
        sender.setId(0L);
        sender.setType(UserType.COMOM);
        sender.setFirstName("firstName");
        sender.setLastName("lastName");
        sender.setBalance(new BigDecimal("0.00"));

        // Run the test
        assertThatThrownBy(() -> userServiceUnderTest.validateTransaction(sender, new BigDecimal("0.00")))
                .isInstanceOf(Exception.class);
    }

    @Test
    void testFindUserById() throws Exception {
        // Setup
        final User expectedResult = new User();
        expectedResult.setId(0L);
        expectedResult.setType(UserType.COMOM);
        expectedResult.setFirstName("firstName");
        expectedResult.setLastName("lastName");
        expectedResult.setBalance(new BigDecimal("0.00"));

        // Configure UserRepository.findUserById(...).
        final User user1 = new User();
        user1.setId(0L);
        user1.setType(UserType.COMOM);
        user1.setFirstName("firstName");
        user1.setLastName("lastName");
        user1.setBalance(new BigDecimal("0.00"));
        final Optional<User> user = Optional.of(user1);
        when(mockRepository.findUserById(0L)).thenReturn(user);

        // Run the test
        final User result = userServiceUnderTest.findUserById(0L);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testFindUserById_UserRepositoryReturnsAbsent() {
        // Setup
        when(mockRepository.findUserById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> userServiceUnderTest.findUserById(0L)).isInstanceOf(Exception.class);
    }

    @Test
    void testFindUserByDocument() throws Exception {
        // Setup
        final User expectedResult = new User();
        expectedResult.setId(0L);
        expectedResult.setType(UserType.COMOM);
        expectedResult.setFirstName("firstName");
        expectedResult.setLastName("lastName");
        expectedResult.setBalance(new BigDecimal("0.00"));

        // Configure UserRepository.findUserByDocument(...).
        final User user1 = new User();
        user1.setId(0L);
        user1.setType(UserType.COMOM);
        user1.setFirstName("firstName");
        user1.setLastName("lastName");
        user1.setBalance(new BigDecimal("0.00"));
        final Optional<User> user = Optional.of(user1);
        when(mockRepository.findUserByDocument("document")).thenReturn(user);

        // Run the test
        final User result = userServiceUnderTest.findUserByDocument("document");

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testFindUserByDocument_UserRepositoryReturnsAbsent() {
        // Setup
        when(mockRepository.findUserByDocument("document")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> userServiceUnderTest.findUserByDocument("document")).isInstanceOf(Exception.class);
    }

    @Test
    void testCreateUser() {
        // Setup
        final userDTO data = new userDTO("firstName", "lastName", "document", new BigDecimal("0.00"), "email",
                "password", UserType.COMOM);
        final User expectedResult = new User();
        expectedResult.setId(0L);
        expectedResult.setType(UserType.COMOM);
        expectedResult.setFirstName("firstName");
        expectedResult.setLastName("lastName");
        expectedResult.setBalance(new BigDecimal("0.00"));

        // Run the test
        final User result = userServiceUnderTest.createUser(data);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);

        // Confirm UserRepository.save(...).
        final User entity = new User();
        entity.setId(0L);
        entity.setType(UserType.COMOM);
        entity.setFirstName("firstName");
        entity.setLastName("lastName");
        entity.setBalance(new BigDecimal("0.00"));
        verify(mockRepository).save(entity);
    }

    @Test
    void testGetAllUsers() {
        // Setup
        final User user = new User();
        user.setId(0L);
        user.setType(UserType.COMOM);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setBalance(new BigDecimal("0.00"));
        final List<User> expectedResult = List.of(user);

        // Configure UserRepository.findAll(...).
        final User user1 = new User();
        user1.setId(0L);
        user1.setType(UserType.COMOM);
        user1.setFirstName("firstName");
        user1.setLastName("lastName");
        user1.setBalance(new BigDecimal("0.00"));
        final List<User> users = List.of(user1);
        when(mockRepository.findAll()).thenReturn(users);

        // Run the test
        final List<User> result = userServiceUnderTest.getAllUsers();

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetAllUsers_UserRepositoryReturnsNoItems() {
        // Setup
        when(mockRepository.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        final List<User> result = userServiceUnderTest.getAllUsers();

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testSaveUser() {
        // Setup
        final User user = new User();
        user.setId(0L);
        user.setType(UserType.COMOM);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setBalance(new BigDecimal("0.00"));

        // Run the test
        userServiceUnderTest.saveUser(user);

        // Verify the results
        // Confirm UserRepository.save(...).
        final User entity = new User();
        entity.setId(0L);
        entity.setType(UserType.COMOM);
        entity.setFirstName("firstName");
        entity.setLastName("lastName");
        entity.setBalance(new BigDecimal("0.00"));
        verify(mockRepository).save(entity);
    }
}
