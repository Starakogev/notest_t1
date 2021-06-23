package com.starakogev.notes.jdbc;

import java.sql.*;

public class TestConnection {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/notes_t1";
    static final String USER = "postgres";
    static final String PASS = "1234";

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement statement = connection.createStatement()) {
            if (connection != null) {
                System.out.println("You successfully connected to database now");
            } else {
                System.out.println("Failed to make connection to database");
            }
            String sql = "SELECT * FROM notes_schema.notes";
            final ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String title = resultSet.getString("name");

                System.out.println("id: " + id);
                System.out.println("title: " + title);

                resultSet.getMetaData();
            }

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
        }

    }
}
