package ru.otus.javabootcamp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostgreClient {

  private final static Logger logger = LoggerFactory.getLogger(PostgreClient.class);
  private Connection connection = null;

  public void connect() {
    String driverClass = "org.postgresql.Driver";
    String serverUrl = "jdbc:postgresql://localhost:5432/otus";
    try {
      Class.forName(driverClass);
      logger.info(serverUrl);
      connection = DriverManager.getConnection(serverUrl);
    } catch (ClassNotFoundException | SQLException e) {
      logger.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public void close() {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        logger.error(e.getMessage());
      }
    }
  }

  public ResultSet executeSelect(String sql) {
    if (connection == null) {
      logger.error("Connection is null");
      throw new RuntimeException("Connection is null");
    }
    try {
      Statement statement = connection.createStatement();
      logger.info("executing query {}", sql);
      return statement.executeQuery(sql);

    } catch (SQLException e) {
      logger.error(String.format("error when execute sql: %s", sql), e);
      throw new RuntimeException(String.format("error when execute sql: %s", sql));
    }
  }
}