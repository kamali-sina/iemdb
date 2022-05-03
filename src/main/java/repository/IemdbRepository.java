package repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class IemdbRepository {
    private static IemdbRepository instance;

    public static IemdbRepository getInstance() {
        if (instance == null) {
            try {
                instance = new IemdbRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in IemdbRepository.create query.");
            }
        }
        return instance;
    }

    private IemdbRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        con.setAutoCommit(false);
        Statement stmt = con.createStatement();
        stmt.addBatch("CREATE TABLE IF NOT EXISTS Actors (\n" +
                "    id INT,\n" +
                "    name VARCHAR(100),\n" +
                "    birthDate VARCHAR(100),\n" +
                "    nationality VARCHAR(100),\n" +
                "    image VARCHAR(500),\n" +
                "    PRIMARY KEY(id)\n" +
                ");");
        stmt.addBatch("CREATE TABLE IF NOT EXISTS Movies (\n" +
                "    id INT,\n" +
                "    name VARCHAR(100),\n" +
                "    summery VARCHAR(1000),\n" +
                "    releaseDate VARCHAR(100),\n" +
                "    director VARCHAR(100),\n" +
                "    writers VARCHAR(100),\n" +
                "    imdbRate FLOAT,\n" +
                "    duration INT,\n" +
                "    ageLimit INT,\n" +
                "    image VARCHAR(500),\n" +
                "    coverImage VARCHAR(500),\n" +
                "    PRIMARY KEY(id)\n" +
                ");");
        stmt.addBatch("CREATE TABLE IF NOT EXISTS Users(\n" +
                "    email VARCHAR(100),\n" +
                "    password VARCHAR(100),\n" +
                "    name VARCHAR(100),\n" +
                "    nickname VARCHAR(100),\n" +
                "    birthDate VARCHAR(100),\n" +
                "    PRIMARY KEY(email)\n" +
                ");");
        int[] updateCounts = stmt.executeBatch();
        stmt.close();
        con.close();
//        TODO: Complete this
//        fillTables();
    }
}
