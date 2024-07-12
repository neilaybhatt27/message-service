package com.greatkapital.messageservice.service;

import com.greatkapital.messageservice.dao.EncryptMessageDAO;
import com.greatkapital.messageservice.model.EncryptedMessageResponsePOJO;
import com.greatkapital.messageservice.model.MessageRequestPOJO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class EncryptMessageService {
    @Autowired
    private final EncryptMessageDAO encryptMessageDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptMessageService.class);

    public EncryptMessageService(EncryptMessageDAO encryptMessageDAO) {
        this.encryptMessageDAO = encryptMessageDAO;
    }

    /**
     * This service method validates the message input and encrypts the message.
     *
     * @param messageRequestPOJO MessageRequestPOJO object containing the input message.
     * @return Map of String and String containing the status and encrypted message.
     */
    public Map<String, String> encryptAndAddMessage(MessageRequestPOJO messageRequestPOJO) {
        LOGGER.info("IN EncryptMessageService.encryptAndAddMessage with MessageRequestPOJO: {}", messageRequestPOJO);
        if(Objects.isNull(messageRequestPOJO.getMessage())) {
            LOGGER.error("Invalid input: message cannot be null");
            throw new IllegalArgumentException("Message cannot be null");
        }
        Map<String, String> responseMap;
        try {
            RestTemplate restTemplate = new RestTemplate();
            String encryptionServiceUrl = "http://localhost:8081/api/encrypt";
            ResponseEntity<Map> encryptionServiceResponse = restTemplate.postForEntity(encryptionServiceUrl, messageRequestPOJO, Map.class);
            messageRequestPOJO.setEncryptedMessage(Objects.requireNonNull(encryptionServiceResponse.getBody()).get("encryptedMessage").toString());
            encryptMessageDAO.addMessage(messageRequestPOJO);
            responseMap = new LinkedHashMap<>();
            responseMap.put("status", "success");
            responseMap.put("encryptedMessage", messageRequestPOJO.getEncryptedMessage());
        } catch (Exception e) {
            LOGGER.error("Some error occurred in the server! Error: {}, Error Stack Trace: {}", e.getMessage(), e.getStackTrace());
            throw new RuntimeException("Some error occurred in the server!");
        }
        LOGGER.info("OUT EncryptMessageService.encryptAndAddMessage successfully.");
        return responseMap;
    }

    /**
     * This service method validates the input message id and retrieves the encrypted message.
     *
     * @param messageId id of the message that is to be fetched.
     * @return EncryptedMessageResponsePOJO object containing the encrypted message and its id.
     */
    public EncryptedMessageResponsePOJO getEncryptedMessageById(long messageId) {
        LOGGER.info("IN EncryptMessageService.getEncryptedMessageById with messageId: {}", messageId);
        if(messageId <= 0) {
            LOGGER.error("Invalid input: message id cannot be 0 or negative");
            throw new IllegalArgumentException("Invalid input: message id cannot be 0 or negative");
        }
        EncryptedMessageResponsePOJO encryptedMessageResponsePOJO;
        try {
            if(!encryptMessageDAO.isMessageIdValid(messageId)) {
                LOGGER.error("Invalid input: message id does not exist");
                throw new IllegalArgumentException("Invalid input: message id does not exist");
            }
            encryptedMessageResponsePOJO = encryptMessageDAO.getEncryptedMessageById(messageId);
        } catch (Exception e) {
            LOGGER.error("Some error occurred in the server! Error: {}, Error Stack Trace: {}", e.getMessage(), e.getStackTrace());
            throw new RuntimeException("Some error occurred in the server!");
        }
        LOGGER.info("OUT EncryptMessageService.getEncryptedMessageById successfully.");
        return encryptedMessageResponsePOJO;
    }
}
