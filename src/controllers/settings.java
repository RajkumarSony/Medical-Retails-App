
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author the.bugs.cracker
 */
public class settings implements Initializable {

    @FXML
    private Label user_name;
    @FXML
    private Button update;
    @FXML
    private TextField tf_user_name;
    @FXML
    private TextField tf_email_id;
    @FXML
    private PasswordField tf_password;
    @FXML
    private PasswordField tf_confirm_pass;

    String username;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        user_name.setText(controllers.Dashboard.send_Uname());
        username = user_name.getText();
        loadData();
    }    

    @FXML
    private void btn_back(ActionEvent event) throws IOException{
         
        Parent root = FXMLLoader.load(getClass().getResource("/forms/Dashboard.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image("/icons/Departments_48px.png"));
        stage.setTitle("Dashboard : Medical Retails App");
        stage.show();
        Stage nStage = (Stage) update.getScene().getWindow();
        nStage.close();
    }

    @FXML
    private void btn_Update(ActionEvent event) {
        
        String uname = tf_user_name.getText();
        String email = tf_email_id.getText();
        String upass = tf_password.getText();
        String confm = tf_confirm_pass.getText();
        
        if(isPassMatch() && isValid() && isValidEmail()){
            try {
                Connection con = DbConnect.initializeDatabase();
                PreparedStatement st = con.prepareStatement("update users set uname=?, email=?, upass=? where uname='"+username+"'"); 
                
                st.setString(1, uname); 
                st.setString(2, email); 
                st.setString(3, upass);
                st.executeUpdate(); 
  
                st.close(); 
                con.close();
                
                System.out.println("Update Successful.");
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Message");
                alert.setHeaderText("Hey, Congratulations!");
                alert.setContentText("Your informations are updated successfully!");
                alert.showAndWait();
                
                Parent root = FXMLLoader.load(getClass().getResource("/forms/Dashboard.fxml"));
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.getIcons().add(new Image("/icons/Departments_48px.png"));
                stage.setTitle("Dashboard : Medical Retails App");
                stage.show();
                Stage nStage = (Stage) update.getScene().getWindow();
                nStage.close();
                
            } catch (SQLException ex) {
                
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
    private void btn_Reset(ActionEvent event) {
        loadData();
    }
    
    @FXML
    private void btn_Logout(ActionEvent event) throws IOException {
                 
        Parent root = FXMLLoader.load(getClass().getResource("/forms/page_login.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image("/icons/Departments_48px.png"));
        stage.setTitle("Login Form : Medical Retails App");
        stage.show();
        Stage nStage = (Stage) update.getScene().getWindow();
        nStage.close();
    }
    
    @FXML
    private void deleteAccount(ActionEvent event) throws IOException {
        try {
            Connection con = DbConnect.initializeDatabase();
            String query = "DELETE FROM users where uname='"+username+"'";          
            PreparedStatement pst = con.prepareStatement(query);              
            pst.execute();
            pst.close();
            con.close();  
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message");
            alert.setHeaderText("Awesome!");
            alert.setContentText("Your account has been deleted successfully!");
            alert.showAndWait();
            
            Parent root = FXMLLoader.load(getClass().getResource("/forms/page_login.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.getIcons().add(new Image("/icons/Departments_48px.png"));
            stage.setTitle("Login Form : Medical Retails App");
            stage.show();
            Stage nStage = (Stage) update.getScene().getWindow();
            nStage.close();
        } catch (SQLException ex) {
            Logger.getLogger(view_basket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadData(){
        try {
            Connection con = DbConnect.initializeDatabase();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM `users` WHERE `uname` = '"+username+"'");
            
            while(rs.next()){
                tf_user_name.setText(rs.getString("uname"));
                tf_email_id.setText(rs.getString("email"));
                tf_password.setText(rs.getString("upass"));
            }
            rs.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(view_drugs.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean isValid() {
        boolean valid = true;
        if (tf_user_name.getText().isEmpty() || tf_email_id.getText().isEmpty() || tf_password.getText().isEmpty() || tf_confirm_pass.getText().isEmpty()) {
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
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(tf_email_id.getText());
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
        if (tf_password.getText().matches(tf_confirm_pass.getText())) {
            valid = true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR:");
            alert.setHeaderText("Invalid Password!");
            alert.setContentText("Please, Check the password and try again!");
            alert.showAndWait();
        }
        return valid;
    }
}
