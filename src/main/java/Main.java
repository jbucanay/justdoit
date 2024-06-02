import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

public class Main {
    public static void main(String[] args) {
//        POSTGRES_PSWD=123456;POSTGRES_UNM=postgres;POSTGRES_DB=postgres
        String dbName = System.getenv("POSTGRES_DB");
        String uname = System.getenv("POSTGRES_UNM");
        String pwd = System.getenv("POSTGRES_PSWD");
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",dbName, uname , pwd, "5432");
        try {
            Connection connection = dcm.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM TODO");
            while (resultSet.next()){
                System.out.println(resultSet.getInt(1));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        Utility utility = new Utility();
        utility.userInteraction();
    }
}
