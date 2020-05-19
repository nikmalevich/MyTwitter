package dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private static ConnectionPool instance = null;

    private ConnectionPool() {}

    public static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }

        return instance;
    }

    public Connection getConnection() throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/tutter");

        return dataSource.getConnection();
    }
}
