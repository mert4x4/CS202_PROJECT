import java.sql.*;

public class DBConnection {
    final static String url = "jdbc:mysql://localhost:3306/cs202Project";
    final static String user = "root";
    final static String password = "mert2002";

    DBConnection(){

    }

    public Connection getConnection(){
        Connection c = null;
        try{
            c = DriverManager.getConnection(url, user, password);
        } catch(Exception e){
            System.out.println("error!");
        }

        return c;
    }



}
