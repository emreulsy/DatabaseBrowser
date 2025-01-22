import com.sun.jdi.connect.spi.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println("Driver loaded");
        Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/java");
        ((java.sql.Connection) connection).setAutoCommit(true);

        System.out.println("Database connected ");

        Statement statement = ((java.sql.Connection) connection).createStatement(
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE
        );

        String query = "select* from course";
        ResultSet resultSet = statement.executeQuery(query);

        System.out.println("Before update");

        resultSet.absolute(2);
        resultSet.updateString("state","New State");
    }
}