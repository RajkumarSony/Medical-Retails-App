
package controllers;

import connection.DbConnect;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author the.bugs.cracker
 */
public class Dashboard implements Initializable {

    @FXML
    private Button Settings;
    @FXML
    private Label add_Drugs;
    @FXML
    private Label view_Drugs;
    @FXML
    private Label view_Basket;
    @FXML
    private Label view_Sold;
    @FXML
    private Label user_name;

    private static String session, username;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        session = controllers.Page_login.sendSession();
        System.out.println("session2: "+session);
        loadData();
    }    

    @FXML
    private void gotoSettings(ActionEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("/forms/settings.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image("/icons/Departments_48px.png"));
        stage.setTitle("Settings : Medical Retails App");
        stage.show();
        Stage nStage = (Stage) Settings.getScene().getWindow();
        nStage.close();
    }

    @FXML
    private void gotoAdd_Drugs(MouseEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("/forms/add_drugs.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image("/icons/Departments_48px.png"));
        stage.setTitle("Add Drugs : Medical Retails App");
        stage.show();
        Stage nStage = (Stage) Settings.getScene().getWindow();
        nStage.close();
    }

    @FXML
    private void gotoView_Drugs(MouseEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("/forms/view_drugs.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image("/icons/Departments_48px.png"));
        stage.setTitle("View Drugs : Medical Retails App");
        stage.show();
        Stage nStage = (Stage) Settings.getScene().getWindow();
        nStage.close();
    }

    @FXML
    private void gotoView_Basket(MouseEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("/forms/view_basket.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image("/icons/Departments_48px.png"));
        stage.setTitle("View Basket : Medical Retails App");
        stage.show();
        Stage nStage = (Stage) Settings.getScene().getWindow();
        nStage.close();
    }

    @FXML
    private void gotoView_Sold(MouseEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("/forms/sold_drugs.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image("/icons/Departments_48px.png"));
        stage.setTitle("Sold Drugs : Medical Retails App");
        stage.show();
        Stage nStage = (Stage) Settings.getScene().getWindow();
        nStage.close();
    }
    
    private void loadData(){
        try {
            Connection con = DbConnect.initializeDatabase();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM `users` WHERE `session` = '"+session+"'");
            
            while(rs.next()){
                username = rs.getString("uname");
                user_name.setText(username);
                System.out.println("username = "+username);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Page_login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static String send_Uname(){
        return username;
    }

    @FXML
    private void gotoProfile(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("https://www.facebook.com/mr.raj.official.profile"));
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
