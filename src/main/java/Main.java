import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

public class Main {
    public static void main(String[] args) {
        String dbName = System.getenv("POSTGRES_DB");
        String uname = System.getenv("POSTGRES_UNM");
        String pwd = System.getenv("POSTGRES_PSWD");
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",dbName, uname , pwd, "5432");
        Utility utility = new Utility();
        utility.userInteraction();
        try(Connection connection = dcm.getConnection()) {
            utility.rowInsert(connection);
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM TODO");
//            while (resultSet.next()){
//                System.out.println(resultSet.getInt(1));
//            }
        } catch (SQLException e){
            e.printStackTrace();
        }


    }
}
