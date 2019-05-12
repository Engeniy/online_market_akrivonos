package ru.mail.krivonos.al.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.mail.krivonos.al.repository.GenericRepository;
import ru.mail.krivonos.al.repository.exceptions.DatabaseConnectionException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class GenericRepositoryImpl implements GenericRepository {

    private static final String DATABASE_CONNECTION_ERROR_MESSAGE = "Can't create connection to database.";

    private static final Logger logger = LoggerFactory.getLogger(GenericRepositoryImpl.class);

    @Autowired
    private DataSource dataSource;

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseConnectionException(DATABASE_CONNECTION_ERROR_MESSAGE, e);
        }
    }
}
