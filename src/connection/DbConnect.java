
package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.scene.control.Alert;

/**
 *
 * @author the.bugs.cracker
 */
public class DbConnect {
            
    public static Connection initializeDatabase()
    { 
        Connection conn = null;
        
	String url = "jdbc:mysql://localhost:3306/medical"; 

	String user = "root"; 
	String pass = ""; 
	  
        try { 
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,user,pass); 
            System.out.println("Connected!");
            
        } catch (ClassNotFoundException ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR:");
            alert.setHeaderText("Missing JDBC Driver!");
            alert.setContentText("You need to import mysql-jdbc driver (.jar file)!");
            alert.showAndWait();
            
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR:");
            alert.setHeaderText("Database Not Connected!");
            alert.setContentText("You need to check database connection!");
            alert.showAndWait();
        } 
        return conn;
    } 
}
