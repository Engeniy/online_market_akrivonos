package ru.mail.krivonos.al.repository;

import java.sql.Connection;

public interface GenericRepository {

    Connection getConnection();
}
