package com.greatkapital.messageservice.dao;

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
}
