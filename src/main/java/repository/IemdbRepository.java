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
//        TODO: complete table creation
        stmt.addBatch("");
        stmt.addBatch("");
        stmt.addBatch("");
        int[] updateCounts = stmt.executeBatch();
        stmt.close();
        con.close();
//        TODO: Complete this
//        fillTables();
    }
}
