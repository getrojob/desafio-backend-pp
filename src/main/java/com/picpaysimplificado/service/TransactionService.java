package com.picpaysimplificado.service;

import com.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class TransactionService {

    private static final Logger LOGGER = Logger.getLogger(TransactionService.class.getName());

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;
    final String apiUrl = "https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6";

    public Transaction createTransaction(TransactionDTO transaction) throws Exception {

        LOGGER.log(Level.INFO, "Localizando o user");
        User sender = this.userService.findUserById(transaction.senderId());
        LOGGER.log(Level.INFO, "Achei o Sender", sender.getFirstName() + " " + sender.getLastName());

        LOGGER.log(Level.INFO, "buscando o receiver");
        User recipient = this.userService.findUserById(transaction.recipientId());
        LOGGER.log(Level.INFO, "Achei o receiver", recipient.getFirstName() + " " + recipient.getLastName());

        LOGGER.log(Level.INFO, "Validando a transacao");
        userService.validateTransaction(sender, transaction.value());

        boolean isAutorized = this.authorizeTransaction(sender, transaction.value());
        LOGGER.log(Level.INFO, "Verificando a autorizacao da transacao");
        if (!isAutorized) {
            LOGGER.log(Level.INFO, "Transacao nao autorizada");
            throw new Exception("Transacao nao autorizada");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(recipient);
        newTransaction.setTimestamp(LocalDateTime.now());

        operationsFinance(transaction, sender, recipient);

        LOGGER.log(Level.INFO, "Salvando a transacao");
        this.transactionRepository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(recipient);

        sendNotification(sender, recipient);

        return newTransaction;
    }

    private void operationsFinance(TransactionDTO transaction, User sender, User recipient) {
        LOGGER.log(Level.INFO, "Subtraindo o valor");
        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        LOGGER.log(Level.INFO, "Adicionando o valor");
        recipient.setBalance(recipient.getBalance().add(transaction.value()));
    }

    private void sendNotification(User sender, User recipient) throws Exception {
        LOGGER.log(Level.INFO, "Enviando notificacao de realizacao de transacao");
        this.notificationService.sendNotification(sender, "Transacao realizada com sucesso");
        LOGGER.log(Level.INFO, "Enviando notificacao de transacao recebida");
        this.notificationService.sendNotification(recipient, "Transacao recebida com sucesso");
    }

    public boolean authorizeTransaction(User sender, BigDecimal amount) {
        ResponseEntity<Map> autorizationResponse = restTemplate.getForEntity(apiUrl, Map.class, sender.getId(), amount);

        if (autorizationResponse.getStatusCode() == HttpStatus.OK) {
            String message = (String) autorizationResponse.getBody().get("message");
            return "Autorizado".equalsIgnoreCase(message);
        } else return false;
    }

}
