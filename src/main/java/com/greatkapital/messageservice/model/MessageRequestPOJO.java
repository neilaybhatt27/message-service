package com.greatkapital.messageservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequestPOJO {
    private String message;
    @JsonIgnore
    private String encryptedMessage;
}
