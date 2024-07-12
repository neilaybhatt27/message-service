package com.greatkapital.messageservice.dao;

import com.greatkapital.messageservice.model.EncryptedMessageResponsePOJO;
import com.greatkapital.messageservice.model.MessageRequestPOJO;
import org.apache.logging.log4j.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Repository
public class EncryptMessageDAO {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptMessageDAO.class);

    private static final String PUBLIC_SCHEMA = "public";

    public EncryptMessageDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * This DAO method inserts the original message and inserted message into the database.
     *
     * @param messageRequestPOJO MessageRequestPOJO object containing the original message and encrypted message.
     * @throws SQLException If an error occurs during database operation.
     */
    public void addMessage(MessageRequestPOJO messageRequestPOJO) throws SQLException {
        LOGGER.info("IN EncryptMessageDAO.addMessage with MessageRequestPOJO: {}", messageRequestPOJO);
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ")
                .append(PUBLIC_SCHEMA).append(".message")
                .append(" (original_message, encrypted_message) VALUES (?, ?)");

        try {
            LOGGER.info("Executing SQL query: {}", sqlBuilder);
            jdbcTemplate.update(sqlBuilder.toString(), messageRequestPOJO.getMessage(), messageRequestPOJO.getEncryptedMessage());
        } catch (DataAccessException e) {
            LOGGER.error("Error executing SQL query: {}. Error: {}", sqlBuilder, e.getMessage());
            throw new SQLException("Error executing SQL query");
        }
        LOGGER.info("OUT EncryptMessageDAO.addMessage after successfully adding into database");
    }

    /**
     * This DAO method retrieves the message id and encrypted message from the database of the given message id.
     *
     * @param messageId id of the message that needs to be retrieved.
     * @return EncryptedMessageResponsePOJO object containing the message id and encrypted message.
     * @throws SQLException If an error occurs during database operation.
     */
    public EncryptedMessageResponsePOJO getEncryptedMessageById(long messageId) throws SQLException {
        LOGGER.info("IN EncryptMessageDAO.getEncryptedMessageById with messageId: {}", messageId);
        EncryptedMessageResponsePOJO encryptedMessageResponsePOJO;
        StringBuilder sqlBuilder = new StringBuilder("SELECT id,")
                .append(" encrypted_message")
                .append(" FROM ").append(PUBLIC_SCHEMA).append(".message m")
                .append(" WHERE id = ?");

        try {
            LOGGER.info("Executing SQL query: {}", sqlBuilder);
            encryptedMessageResponsePOJO = jdbcTemplate.queryForObject(sqlBuilder.toString(), (rs, numRow) -> {
                EncryptedMessageResponsePOJO encryptedMessageResponsePOJO1 = new EncryptedMessageResponsePOJO();
                encryptedMessageResponsePOJO1.setMessageId(rs.getString("id"));
                encryptedMessageResponsePOJO1.setEncryptedMessage(rs.getString("encrypted_message"));
                return encryptedMessageResponsePOJO1;
            }, messageId);
        } catch (DataAccessException e) {
            LOGGER.error("Error executing SQL query: {}. Error: {}", sqlBuilder, e.getMessage());
            throw new SQLException("Error executing SQL query");
        }
        LOGGER.info("OUT EncryptMessageDAO.getEncryptedMessageById with response: {}", encryptedMessageResponsePOJO);
        return encryptedMessageResponsePOJO;
    }

    /**
     * This DAO method is to check whether the message id is present in the database or not.
     *
     * @param messageId id of the message that needs to be validated.
     * @return true if id exists else false.
     * @throws SQLException If an error occurs during database operation.
     */
    public Boolean isMessageIdValid(long messageId) throws SQLException{
        LOGGER.info("IN EncryptMessageDAO.isMessageIdValid with messageId: {}", messageId);
        Boolean isValid;
        StringBuilder sqlBuilder = new StringBuilder("SELECT EXISTS (")
                .append("SELECT 1")
                .append(" FROM ").append(PUBLIC_SCHEMA).append(".message m")
                .append(" WHERE id = ?) AS is_valid");

        try {
            LOGGER.info("Executing SQL query: {}", sqlBuilder);
            isValid = jdbcTemplate.queryForObject(sqlBuilder.toString(), Boolean.class, messageId);
        } catch (DataAccessException e) {
            LOGGER.error("Error executing SQL query: {}. Error: {}", sqlBuilder, e.getMessage());
            throw new SQLException("Error executing SQL query");
        }
        LOGGER.info("OUT EncryptMessageDAO.isMessageIdValid with response: {}", isValid);
        return isValid;
    }
}
