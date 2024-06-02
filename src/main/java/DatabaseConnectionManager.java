import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionManager {
    private final String url;
    private final Properties priorities;

    public DatabaseConnectionManager(String host, String databaseName, String username, String password, String port){
        this.url = "jdbc:postgresql://"+host+":"+port+"/"+databaseName;
        this.priorities = new Properties();
        this.priorities.setProperty("user", username);
        this.priorities.setProperty("password", password);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.url, this.priorities);
    }
}
