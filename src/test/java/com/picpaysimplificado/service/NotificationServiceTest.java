package com.picpaysimplificado.service;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private RestTemplate mockRestTemplate;

    @InjectMocks
    private NotificationService notificationServiceUnderTest;

    @Test
    void testSendNotification() throws Exception {
        // Setup
        final User user = new User();
        user.setId(0L);
        user.setType(UserType.COMOM);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email");

        // Run the test
        notificationServiceUnderTest.sendNotification(user, "message");

        // Verify the results
    }

    @Test
    void testSendNotification_ThrowsException() {
        // Setup
        final User user = new User();
        user.setId(0L);
        user.setType(UserType.COMOM);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email");

        // Run the test
        assertThatThrownBy(() -> notificationServiceUnderTest.sendNotification(user, "message"))
                .isInstanceOf(Exception.class);
    }
}
