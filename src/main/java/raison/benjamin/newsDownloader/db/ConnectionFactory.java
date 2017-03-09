package raison.benjamin.newsDownloader.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static ConnectionFactory connectionFactory = null;
    
    private final String dbURL = "jdbc:mysql://localhost:3306/web?useSSL=true";
    private final String dbUser = "root";
    private final String dbPwd = "";
    
    private Connection connection = null;
    
    private ConnectionFactory() {
    }
    
    public static ConnectionFactory getInstance() {
        if (connectionFactory == null) {
            connectionFactory = new ConnectionFactory();
        }
        return connectionFactory;
    }
    
    public Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(dbURL, dbUser, dbPwd);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
    
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
