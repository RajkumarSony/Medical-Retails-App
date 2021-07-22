
package controllers;

import connection.DbConnect;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.ModelTable3;

/**
 *
 * @author the.bugs.cracker
 */
public class sold_drugs implements Initializable {

    @FXML
    private Button Settings;
    @FXML
    private Label user_name;
    @FXML
    private TableView<ModelTable3> table_view;
    @FXML
    private TableColumn<ModelTable3, Date> purchage_date;
    @FXML
    private TableColumn<ModelTable3, String> medicine_id;
    @FXML
    private TableColumn<ModelTable3, String> medicine_name;
    @FXML
    private TableColumn<ModelTable3, String> company_name;
    @FXML
    private TableColumn<ModelTable3, Date> mfg_date;
    @FXML
    private TableColumn<ModelTable3, Date> expiry_date;
    @FXML
    private TableColumn<ModelTable3, String> quantity;
    @FXML
    private TableColumn<ModelTable3, Float> price;
    @FXML
    private TableColumn<ModelTable3, Float> total_price;

    ObservableList<ModelTable3> oblist = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        user_name.setText(controllers.Dashboard.send_Uname());
        loadTable();
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
    private void btn_ClearAll(ActionEvent event) throws IOException {
        remove_all();
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("/forms/Dashboard.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image("/icons/Departments_48px.png"));
        stage.setTitle("Dashboard : Medical Retails App");
        stage.show();
        Stage nStage = (Stage) Settings.getScene().getWindow();
        nStage.close();
    }
    
    private void loadTable(){
        try {
            Connection con = DbConnect.initializeDatabase();
            ResultSet rs = con.createStatement().executeQuery("SELECT `purchage_date`,`medicine_id`, `medicine_name`, `company_name`, `mfg_date`, `expiry_date`, `quantity`, `price`, `total_price` FROM `purchage`;");
            
            while(rs.next()){
                oblist.add(new ModelTable3(rs.getDate("purchage_date"), rs.getString("medicine_id"), rs.getString("medicine_name"),
                        rs.getString("company_name"), rs.getDate("mfg_date"), rs.getDate("expiry_date"), rs.getString("quantity"),
                        rs.getFloat("price"), rs.getFloat("total_price")));            
            }
            rs.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(view_drugs.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        purchage_date.setCellValueFactory(new PropertyValueFactory<>("purchage_date"));
        medicine_id.setCellValueFactory(new PropertyValueFactory<>("medicine_id"));
        medicine_name.setCellValueFactory(new PropertyValueFactory<>("medicine_name"));
        company_name.setCellValueFactory(new PropertyValueFactory<>("company_name"));
        mfg_date.setCellValueFactory(new PropertyValueFactory<>("mfg_date"));
        expiry_date.setCellValueFactory(new PropertyValueFactory<>("expiry_date"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        total_price.setCellValueFactory(new PropertyValueFactory<>("total_price"));
        table_view.setItems(oblist);
    }
    
    private void remove_all(){     
       
        table_view.getItems().removeAll(oblist);

        try {
            Connection con = DbConnect.initializeDatabase();
            String query = "DELETE FROM purchage";          
            PreparedStatement pst = con.prepareStatement(query);              
            pst.execute();
            pst.close();
            con.close();                
        } catch (SQLException ex) {
            Logger.getLogger(view_basket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
