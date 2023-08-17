package com.picpaysimplificado.service;


import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.notificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Service
public class NotificationService {
    private static final Logger LOGGER = Logger.getLogger(NotificationService.class.getName());
    @Autowired
    private RestTemplate restTemplate;

    public void sendNotification(User user, String message) throws Exception {
        LOGGER.info("Enviando notificacao");
        String email = user.getEmail();
        notificationDTO notificationRequest = new notificationDTO(email, message);

     /*   String url = "http://o4d9z.mocklab.io/notify";
        ResponseEntity<String> notificationResponse = restTemplate.postForEntity(url, notificationRequest, String.class);
        LOGGER.info("Verifica Status do servico de notificacao");
        if (!(notificationResponse.getStatusCode() == HttpStatus.OK)) {
            LOGGER.info("Servico esta fora do ar");
            throw new Exception("Servico esta fora do ar");
        }*/

        System.out.println("Enviando notificacao para " + email + " com a mensagem: " + message);
    }

}
