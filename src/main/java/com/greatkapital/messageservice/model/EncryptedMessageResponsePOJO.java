package com.greatkapital.messageservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncryptedMessageResponsePOJO implements Serializable {
    private String messageId;
    private String encryptedMessage;
}
