package com.picpaysimplificado.service;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.dtos.userDTO;
import com.picpaysimplificado.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.math.BigDecimal;

@Service
public class UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    @Autowired
    private UserRepository repository;

    public void validateTransaction(User sender, BigDecimal amout) throws Exception {

        LOGGER.log(Level.INFO, "Validacao de user");
        if (sender.getType() != UserType.COMOM) {
            LOGGER.log(Level.INFO, "User MERCHANT");
            throw new Exception("Merchant não pode enviar dinheiro");
        }
        LOGGER.log(Level.INFO, "Validacao de saldo");
        if (sender.getBalance().compareTo(amout) < 0) {
            throw new Exception("Saldo insuficiente");
        }
    }

    public User findUserById(Long id) throws Exception {
        LOGGER.log(Level.INFO, "Buscar por Id");
        return this.repository.findUserById(id).orElseThrow(() -> new Exception("Usuário não encontrado"));
    }

    public User findUserByDocument(String document) throws Exception {
        LOGGER.log(Level.INFO, "Buscar por document");
        //TODO - Validar se o document é um CPF ou CNPJ
        return this.repository.findUserByDocument(document).orElseThrow(() -> new Exception("Usuário não encontrado"));
    }

    public User createUser(userDTO data) {
        LOGGER.log(Level.INFO, "Criando user");
        User newUser = new User(data);
        this.saveUser(newUser);
        return newUser;
    }

    public List<User> getAllUsers() {
        LOGGER.log(Level.INFO, "Buscando todos os users");
        return this.repository.findAll();
    }
    public void saveUser(User user) {
        LOGGER.log(Level.INFO, "Salvando na base");
        this.repository.save(user);
        LOGGER.log(Level.INFO, user.toString());
    }
}
