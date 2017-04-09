package li.resonance.newsDownloader.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class ConnectionFactory {
    private static ConnectionFactory connectionFactory = null;
    
    private final String dbURL = "jdbc:mysql://localhost:3306/resonance?useSSL=true" +
                                 "&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false" +
                                 "&serverTimezone=UTC";
    private String dbUser;
    private String dbPwd;
    
    private Connection connection = null;
    
    private ConnectionFactory() {
        File cfg = new File("db.cfg");
        if (!cfg.exists()) {
            cfg = new File("/home/benji/java/db.cfg");
        }
        try (Scanner scan = new Scanner(cfg)) {
            String[] parts = scan.nextLine().split(";");
            dbUser = parts[0];
            dbPwd = parts[1];
        } catch (Exception e) {
            System.err.println("Failed to read database configuration!\n");
            e.printStackTrace();
            System.exit(1);
        }
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
