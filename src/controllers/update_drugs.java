
package controllers;

import connection.DbConnect;
import impl.com.calendarfx.view.NumericTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author the.bugs.cracker
 */
public class update_drugs implements Initializable {

    @FXML
    private Button Settings;
    @FXML
    private Label user_name;
    @FXML
    private TextField tf_medicine_id;
    @FXML
    private TextField tf_medicine_name;
    @FXML
    private TextField tf_company_name;
    @FXML
    private TextField tf_batch_number;
    @FXML
    private DatePicker dp_mfg_date;
    @FXML
    private DatePicker dp_expiry_date;
    @FXML
    private NumericTextField tf_quantity;
    @FXML
    private TextField tf_price_per_unit;
    @FXML
    private TextField tf_total_price;

    String code;
    float price, total;
    int status = 0;
    
    private String medicine_id;

    @Override
    public void initialize(URL url, ResourceBundle rb) {      
        
        user_name.setText(controllers.Dashboard.send_Uname());
        medicine_id = controllers.view_drugs.send_medicineId(); 
        loadData();
        tf_medicine_id.setText(medicine_id);
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
    private void btn_back(ActionEvent event) throws IOException {
        
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
    private void btn_Update(ActionEvent event) {
        
        String medicine_name = tf_medicine_name.getText();
        String company_name =  tf_company_name.getText();
        String batch_number = tf_batch_number.getText();
        LocalDate mfg_date = dp_mfg_date.getValue();
        LocalDate expiry_date = dp_expiry_date.getValue();
        String quantity = tf_quantity.getText();
        Float price = Float.parseFloat(tf_price_per_unit.getText());
        
        System.out.println("hello");
        if (isValid()){
            if(status == 1){
                if(mfg_date.compareTo(expiry_date) < 0){
                    try {
                        Connection con = DbConnect.initializeDatabase();
                        PreparedStatement st = con.prepareStatement("update drugs set medicine_name=?, company_name=?, "
                                + "batch_number=?, mfg_date=?, expiry_date=?, quantity=?, price=?, total_price=?"
                                + "where medicine_id='"+medicine_id+"'");
             
                        st.setString(1, medicine_name); 
                        st.setString(2, company_name);
                        st.setString(3, batch_number); 
                        st.setDate(4, java.sql.Date.valueOf(mfg_date));
                        st.setDate(5, java.sql.Date.valueOf(expiry_date));
                        st.setString(6, quantity); 
                        st.setFloat(7, price); 
                        st.setFloat(8, total);
                
                        st.executeUpdate();
                
                        st.close(); 
                        con.close();
                
                        System.out.println("Update Successfully!");
                
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Message");
                        alert.setHeaderText("Awesome!");
                        alert.setContentText("Medicine's informations are updated in database successfully!");
                        alert.showAndWait();
                        
                        Parent root = FXMLLoader.load(getClass().getResource("/forms/view_drugs.fxml"));
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.getIcons().add(new Image("/icons/Departments_48px.png"));
                        stage.setTitle("View Drugs : Medical Retails App");
                        stage.show();
                        Stage nStage = (Stage) Settings.getScene().getWindow();
                        nStage.close();
                
                    } catch (SQLException ex) {
                        System.out.println("SQL Server not connected!");
                
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR:");
                        alert.setHeaderText("Already Existed!");
                        alert.setContentText("Your given informations are already exist, please try again!");
                        alert.show();
                    } catch (IOException ex) {
                        Logger.getLogger(add_drugs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR:");
                    alert.setHeaderText("Invalid Date!");
                    alert.setContentText("Your given expire date is not valid, please try again!");
                    alert.show();
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR:");
                alert.setHeaderText("Please, Calculate!");
                alert.setContentText("Please, calculate first then try again!");
                alert.show();
            }
        } 
    }

    @FXML
    private void btn_Reset(ActionEvent event) {
        loadData();
    }

    @FXML
    private void btn_Calculate(ActionEvent event) {
        
        int quant = Integer.parseInt(tf_quantity.getText()); 
        price = Float.parseFloat(tf_price_per_unit.getText());
        total = Float.parseFloat(""+price * quant);
        
        tf_total_price.setText(""+total);
        status = 1;
    }
    
    private void loadData(){
        try {
            Connection con = DbConnect.initializeDatabase();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM `drugs` WHERE `medicine_id` = '"+medicine_id+"'");
            
            while(rs.next()){
                medicine_id = rs.getString("medicine_id");
                tf_medicine_name.setText(rs.getString("medicine_name"));
                tf_company_name.setText(rs.getString("company_name"));
                tf_batch_number.setText(rs.getString("batch_number"));
                
                java.sql.Date mfg_date = rs.getDate("mfg_date");
                dp_mfg_date.setValue(mfg_date.toLocalDate());
                java.sql.Date expiry_date = rs.getDate("expiry_date");
                dp_expiry_date.setValue(expiry_date.toLocalDate());
                
                tf_quantity.setText(rs.getString("quantity"));
                tf_price_per_unit.setText(String.valueOf(rs.getFloat("price")));
                rs.getFloat("total_price");
            }
            rs.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(view_drugs.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean isValid() {
        boolean valid = true;
        if (tf_medicine_name.getText().isEmpty() || tf_company_name.getText().isEmpty() ||
                tf_batch_number.getText().isEmpty() || tf_quantity.getText().isEmpty() ||
                dp_mfg_date.getValue() == null || dp_mfg_date.getValue() == null ||
                tf_price_per_unit.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Message");
            alert.setHeaderText("All fields are required!");
            alert.setContentText("All text fields are required. please fill all text field!");
            alert.show();
            valid = false;
        }
        return valid;
    }
}
