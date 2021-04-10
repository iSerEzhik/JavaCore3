package chat.serverside.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

final class Connect {
     static final String DRIVER = "com.mysql.jdbc.Driver";
     static final String DB = "jdbc:mysql://localhost:8082/myUsers";
     static final String USER = "root";
     static final String PASSWORD = "root";
     private static  Connection connection;
       private Connect(){

       }
       public static Connection getConnection() throws SQLException, ClassNotFoundException {
           if (connection == null){
               connection = setConnection();
           }
           return connection;
       }
       private static Connection setConnection() throws ClassNotFoundException, SQLException {
           Class.forName(DRIVER);
           return DriverManager.getConnection(DB,USER,PASSWORD);
       }
}
