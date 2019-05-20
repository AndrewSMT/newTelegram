package Database;

import java.sql.*;

public class Connect {

    private static Connection con;
    private static Statement st;


    public static Statement getSt() {
        return st;
    }



    public static void openCon() throws ClassNotFoundException, SQLException {

       String URL = "jdbc:mysql://localhost/messenger?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String USER = "root";
        String PASSWORD = "12345cs123";
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(URL, USER, PASSWORD);
        st = con.createStatement();
    }
}
