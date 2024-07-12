package com.greatkapital.messageservice.controller;

import com.greatkapital.messageservice.model.MessageRequestPOJO;
import com.greatkapital.messageservice.service.EncryptMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/api")
public class EncryptMessageController {
    @Autowired
    private final EncryptMessageService encryptMessageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptMessageController.class);

    public EncryptMessageController(EncryptMessageService encryptMessageService) {
        this.encryptMessageService = encryptMessageService;
    }

    /**
     * This POST API encrypts the input message and adds it into the database.
     *
     * @param messageRequestPOJO MessageRequestPOJO object containing the input message.
     * @return Response Entity object containing the response body.
     */
    @PostMapping(path = "/message")
    public ResponseEntity<Object> encryptAndAddMessage(@RequestBody MessageRequestPOJO messageRequestPOJO) {
        LOGGER.info("IN EncryptMessageController.encryptAndAddMessage with MessageRequestPOJO: {}", messageRequestPOJO);
        ResponseEntity<Object> response;
        Map<String, String> responseMap = encryptMessageService.encryptAndAddMessage(messageRequestPOJO);
        response = ResponseEntity.ok(responseMap);
        LOGGER.info("OUT EncryptMessageController.encryptAndAddMessage");
        return response;
    }
}
