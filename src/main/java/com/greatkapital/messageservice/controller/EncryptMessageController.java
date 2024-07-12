package com.greatkapital.messageservice.controller;

import com.greatkapital.messageservice.model.EncryptedMessageResponsePOJO;
import com.greatkapital.messageservice.model.MessageRequestPOJO;
import com.greatkapital.messageservice.service.EncryptMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Encrypt the input message text and add it to the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Encryption and save successful",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = """
                            {
                                "status": "success",
                                "encryptedMessage": "5zb59KX/ZPp28KlYI5mA71Bh6D2ZQiKSh8WqtZBmMaNJZS5xsMxln74qaQlU0opd"
                            }
                            """)})}),
            @ApiResponse(responseCode = "422",
                    description = "Invalid input",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = """
                            {
                                "errorObject": "Message cannot be null"
                            }
                            """)})})
    })

    @PostMapping(path = "/message")
    public ResponseEntity<Map<String, String>> encryptAndAddMessage(@RequestBody MessageRequestPOJO messageRequestPOJO) {
        LOGGER.info("IN EncryptMessageController.encryptAndAddMessage with MessageRequestPOJO: {}", messageRequestPOJO);
        ResponseEntity<Map<String, String>> response;
        Map<String, String> responseMap = encryptMessageService.encryptAndAddMessage(messageRequestPOJO);
        response = ResponseEntity.ok(responseMap);
        LOGGER.info("OUT EncryptMessageController.encryptAndAddMessage");
        return response;
    }

    /**
     * This GET API fetches the encrypted message of the given message id.
     *
     * @param messageId id of the message that is to be fetched.
     * @return ResponseEntity object containing the response body.
     */

    @Operation(summary = "fetch the encrypted message by id from the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetch successful",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = """
                            {
                                "messageId": "1",
                                "encryptedMessage": "XSu2r4IUA54sdyDRcl3nAQ=="
                            }
                            """)})}),
            @ApiResponse(responseCode = "422",
                    description = "Invalid input",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = """
                            {
                                "errorObject": "Invalid input: wrong message id"
                            }
                            """)})})
    })

    @GetMapping(path = "/message/{messageId}")
    public ResponseEntity<EncryptedMessageResponsePOJO> getEncryptedMessageById(@PathVariable long messageId) {
        LOGGER.info("IN EncryptMessageController.getEncryptedMessageById with messageId: {}", messageId);
        ResponseEntity<EncryptedMessageResponsePOJO> response;
        EncryptedMessageResponsePOJO encryptedMessageResponsePOJO = encryptMessageService.getEncryptedMessageById(messageId);
        response = ResponseEntity.ok(encryptedMessageResponsePOJO);
        LOGGER.info("OUT EncryptMessageController.getEncryptedMessageById");
        return response;
    }
}
