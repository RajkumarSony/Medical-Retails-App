
package controllers;

import connection.DbConnect;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author the.bugs.cracker
 */
public class Page_signup implements Initializable {

    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_email;
    @FXML
    private PasswordField tf_password;
    @FXML
    private PasswordField tf_confirm;
    @FXML
    private Button btn_signup;
    
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnSignup(ActionEvent event) {
        
        String uname = tf_username.getText();
        String email = tf_email.getText();
        String upass = tf_password.getText();
        String confm = tf_confirm.getText();
        
        if(isPassMatch() && isValid() && isValidEmail()){
            try {
                Connection con = DbConnect.initializeDatabase();
                PreparedStatement st = con.prepareStatement("insert into users values(?, ?, ?, ?)"); 
                
                st.setString(1, uname); 
                st.setString(2, email); 
                st.setString(3, upass);
                st.setString(4,"no");
                st.executeUpdate(); 
  
                st.close(); 
                con.close();
                
                System.out.println("Register Successful.");
                
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Message");
                alert.setHeaderText("Hey, Congratulations!");
                alert.setContentText("You are registered successfully!");
                alert.showAndWait();
                
                Parent root = FXMLLoader.load(getClass().getResource("/forms/page_login.fxml"));
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.getIcons().add(new Image("/icons/Departments_48px.png"));
                stage.setTitle("Login Form : Medical Retails App");
                stage.show();
                Stage nStage = (Stage) btn_signup.getScene().getWindow();
                nStage.close();
                
            } catch (SQLException ex) {
                System.out.println("SQL Server not connected!"+ex);
                
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR:");
                alert.setHeaderText("Already Existed!");
                alert.setContentText("Your given informations are already exist, please try again!");
                alert.show();
            } catch (IOException ex) {
                Logger.getLogger(Page_signup.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            System.out.println("Email address not valid");
        }      
    }

    @FXML
    private void gotoLogin(ActionEvent event) {
        
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/forms/page_login.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.getIcons().add(new Image("/icons/Departments_48px.png"));
            stage.setTitle("Login Form : Medical Retails App");
            stage.show();
            Stage nStage = (Stage) btn_signup.getScene().getWindow();
            nStage.close();
        } catch (IOException ex) {
            Logger.getLogger(Page_signup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean isValid() {
        boolean valid = true;
        if (tf_username.getText().isEmpty() || tf_email.getText().isEmpty() || tf_password.getText().isEmpty() || tf_confirm.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR:");
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
            alert.setTitle("ERROR:");
            alert.setHeaderText("Invalid Email-id!");
            alert.setContentText("Email address is not valid, please enter a valid email address!");
            alert.show();
            valid = false;
        }
        return valid;
    }
    
    private boolean isPassMatch() {
        boolean valid = false;
        if (tf_password.getText().matches(tf_confirm.getText())) {
            valid = true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR:");
            alert.setHeaderText("Invalid Password!");
            alert.setContentText("Please, Check the password and try again!");

            alert.showAndWait();
        }
        return valid;
    }
}
