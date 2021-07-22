
package controllers;

import connection.DbConnect;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.ModelTable2;

/**
 *
 * @author the.bugs.cracker
 */
public class view_basket implements Initializable {

    @FXML
    private Button Settings;
    @FXML
    private TableView<ModelTable2> table_view;
    @FXML
    private TableColumn<ModelTable2, String> medicine_id;
    @FXML
    private TableColumn<ModelTable2, String> medicine_name;
    @FXML
    private TableColumn<ModelTable2, String> company_name;
    @FXML
    private TableColumn<ModelTable2, LocalDate> mfg_date;
    @FXML
    private TableColumn<ModelTable2, LocalDate> expiry_date;
    @FXML
    private TableColumn<ModelTable2, String> quantity;
    @FXML
    private TableColumn<ModelTable2, Float> price;
    @FXML
    private TableColumn<ModelTable2, Float> total_price;
    @FXML
    private Label net_balance;
    @FXML
    private Label user_name;
    
    ObservableList<ModelTable2> oblist2 = FXCollections.observableArrayList();
    
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
    private void gotoBack(ActionEvent event) throws IOException {
        
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

    @FXML
    private void btn_purchage(ActionEvent event) {
        try {
            Connection con = DbConnect.initializeDatabase();
            ResultSet rs = con.createStatement().executeQuery("SELECT `medicine_id`, `medicine_name`, `company_name`, `mfg_date`, `expiry_date`, `quantity`, `price`, `total_price` FROM `basket`;");
            
            while(rs.next()){
                gotoPurchage(rs.getString("medicine_id"),rs.getString("medicine_name"),rs.getString("company_name"),
                    rs.getDate("mfg_date"), rs.getDate("expiry_date"),rs.getString("quantity"),rs.getFloat("price"),
                    rs.getFloat("total_price"));
            }
            rs.close();
            con.close();
            table_view.getItems().removeAll(oblist2);
            remove_all();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message");
            alert.setHeaderText("Awesome!");
            alert.setContentText("Medicine has beed purchaged successfully!");
            alert.showAndWait();
        } catch (SQLException ex) {
            Logger.getLogger(view_drugs.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btn_remove(ActionEvent event) {
        
        ModelTable2 select = table_view.getSelectionModel().getSelectedItem();
        System.out.println(select.getMedicine_id());        
        int selectedIndex = table_view.getSelectionModel().getSelectedIndex();

        if(selectedIndex >= 0){
            try {
                Connection con = DbConnect.initializeDatabase();
                String query = "DELETE FROM basket WHERE medicine_id = ?";          
                PreparedStatement pst = con.prepareStatement(query);              
                pst.setString(1, select.getMedicine_id());
                pst.execute();
                pst.close();
                con.close();
                table_view.getItems().remove(selectedIndex);
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Message");
                alert.setHeaderText("Awesome!");
                alert.setContentText("Medicine has beed removed from the basket successfully!");
                alert.showAndWait();
            } catch (SQLException ex) {
                Logger.getLogger(view_basket.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR:");
            alert.setHeaderText("No selection was made!");
            alert.setContentText("You have not selected an item to remove. Please try again.");
            alert.showAndWait();
        }
    }
    
    private void loadTable(){
        float bal = (float) 0.0;
        try {
            Connection con = DbConnect.initializeDatabase();
            ResultSet rs = con.createStatement().executeQuery("SELECT `medicine_id`, `medicine_name`, `company_name`, `mfg_date`, `expiry_date`, `quantity`, `price`, `total_price` FROM `basket`;");
            
            while(rs.next()){
                oblist2.add(new ModelTable2(rs.getString("medicine_id"), rs.getString("medicine_name"), 
                        rs.getString("company_name"), rs.getDate("mfg_date"), rs.getDate("expiry_date"),
                        rs.getString("quantity"), rs.getFloat("price"), rs.getFloat("total_price"))); 
                bal += rs.getFloat("total_price");
            }
            rs.close();
            con.close();
            net_balance.setText(""+bal);
        } catch (SQLException ex) {
            Logger.getLogger(view_drugs.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        medicine_id.setCellValueFactory(new PropertyValueFactory<>("medicine_id"));
        medicine_name.setCellValueFactory(new PropertyValueFactory<>("medicine_name"));
        company_name.setCellValueFactory(new PropertyValueFactory<>("company_name"));
        mfg_date.setCellValueFactory(new PropertyValueFactory<>("mfg_date"));
        expiry_date.setCellValueFactory(new PropertyValueFactory<>("expiry_date"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        total_price.setCellValueFactory(new PropertyValueFactory<>("total_price"));
        
        table_view.setItems(oblist2);
    }

    private void gotoPurchage(String medicine_id, String medicine_name, String company_name, Date mfg_date,
            Date expiry_date, String quantity, Float price, Float total_price){
        try {
            System.out.println(price);
            Connection conn = DbConnect.initializeDatabase();
            PreparedStatement st = conn.prepareStatement("insert into purchage values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            LocalDate today = LocalDate.now();
            Date mfg = mfg_date;
            st.setDate(1, java.sql.Date.valueOf(today));
            st.setString(2, medicine_id);
            st.setString(3, medicine_name);
            st.setString(4, company_name);
            System.out.println(mfg);
            st.setDate(5, mfg_date);
            st.setDate(6, expiry_date);
            st.setString(7, quantity);
            st.setFloat(8, price);
            st.setFloat(9, total_price);
            System.out.println("Successful.");
            st.execute();
        } catch (SQLException ex) {
            Logger.getLogger(view_basket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void remove_all(){     
        try {
            Connection con = DbConnect.initializeDatabase();
            String query = "DELETE FROM basket";          
            PreparedStatement pst = con.prepareStatement(query);              
            pst.execute();
            pst.close();
            con.close();                
        } catch (SQLException ex) {
            Logger.getLogger(view_basket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
