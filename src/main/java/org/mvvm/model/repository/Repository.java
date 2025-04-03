package org.mvvm.model.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Repository {
    private static final String URL = "jdbc:mysql://localhost:3306/filmproduction_database";
    private static final String USER = "root";
    private static final String PASSWORD = "*MYSQL*boss2003";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
