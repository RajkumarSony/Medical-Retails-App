
package controllers;

import connection.DbConnect;
import static controllers.Page_signup.VALID_EMAIL_ADDRESS_REGEX;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author the.bugs.cracker
 */
public class Page_login implements Initializable {

    @FXML
    private TextField tf_password;
    @FXML
    private Button btn_login;
    @FXML
    private TextField tf_email;

    private static String session;
    String email, upass;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        setDefault();
    }    

    @FXML
    private void btnLogin(ActionEvent event) {
        
        email = tf_email.getText();
        upass = tf_password.getText();

        
        if(isValid() && isValidEmail()){
            try {
                Connection conn = DbConnect.initializeDatabase();
                String query = "Select * from users where email=? and upass=?";
                PreparedStatement pst = conn.prepareStatement(query);
                
                pst.setString(1, email);
                pst.setString(2, upass);
                ResultSet rs = pst.executeQuery();
         
                if (rs.next()) {
                    System.out.println("Login Successful.");
                    
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Message");
                    alert.setHeaderText("Hey, Congratulations!");
                    alert.setContentText("You are logged-in successfully!");
                    alert.showAndWait();
                    setSession();
                    loadData();
                    
                    Parent root = FXMLLoader.load(getClass().getResource("/forms/Dashboard.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.getIcons().add(new Image("/icons/Departments_48px.png"));
                    stage.setTitle("Dashboard : Medical Retails App");
                    stage.show();
                    Stage nStage = (Stage) btn_login.getScene().getWindow();
                    nStage.close();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR:");
                    alert.setHeaderText("Invalid Password!");
                    alert.setContentText("Your give email-id or password is wrong, please try again!");
                    alert.show();
                }
                rs.close();
                pst.close();
                conn.close();
                
            } catch (SQLException ex) {
                System.out.println("SQL Server not connected!");
            } catch (IOException ex) {
                Logger.getLogger(Page_login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void gotoSignup(ActionEvent event) {
        
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/forms/page_signup.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.getIcons().add(new Image("/icons/Departments_48px.png"));
            stage.setTitle("Signup Form : Medical Retails App");
            stage.show();
            Stage nStage = (Stage) btn_login.getScene().getWindow();
            nStage.close();
        } catch (IOException ex) {
            Logger.getLogger(Page_signup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean isValid() {
        boolean valid = true;
        if (tf_email.getText().isEmpty() || tf_password.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Message");
            alert.setHeaderText("All fields are required!");
            alert.setContentText("All text fields are required. please fill all text field!");
            alert.show();
            valid = false;
        }
        return valid;
    }
    
    private boolean isValidEmail() {
        boolean valid = false;
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(tf_email.getText());
        if (matcher.find()) {
            valid = true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Message");
            alert.setHeaderText("Ufff, Sorry!");
            alert.setContentText("Email address is not valid, please enter a valid email address!");
            alert.show();
            valid = false;
        }
        return valid;
    }
    
    private void loadData(){
        try {
            Connection con = DbConnect.initializeDatabase();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM `users` WHERE `email` = '"+email+"'");
            System.out.println(email);
           
            while(rs.next()){
                session = rs.getString("session");
            }
            System.out.println("session1: "+session);
            
        } catch (SQLException ex) {
            Logger.getLogger(Page_login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static String sendSession(){
        return session;
    }
    
    private void setSession(){
        Connection con = DbConnect.initializeDatabase();
        PreparedStatement st; 
        try {
            st = con.prepareStatement("update users set session=? where email='"+email+"'");
            st.setString(1, "yes"); 
            st.executeUpdate(); 
  
            st.close(); 
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Page_login.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    private void setDefault(){
        Connection con = DbConnect.initializeDatabase();
        PreparedStatement st; 
        try {
            st = con.prepareStatement("update users set session=?");
            st.setString(1, "no"); 
            st.executeUpdate(); 
  
            st.close(); 
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Page_login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
