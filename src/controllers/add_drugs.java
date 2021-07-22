
package controllers;

import connection.DbConnect;
import impl.com.calendarfx.view.NumericTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
public class add_drugs implements Initializable {

    @FXML
    private Button Settings;
    @FXML
    private TextField tf_medicine_id;
    @FXML
    private TextField tf_medicine_name;
    @FXML
    private TextField tf_company_name;
    @FXML
    private TextField tf_batch_number;
    @FXML
    private DatePicker dp_expiry_date;
    @FXML
    private DatePicker dp_mfg_date;
    @FXML
    private NumericTextField tf_quantity;
    @FXML
    private TextField tf_price_per_unit;
    @FXML
    private TextField tf_total_price;
    @FXML
    private Label user_name;
    
    String code;
    float price, total;
    int status = 0;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        user_name.setText(controllers.Dashboard.send_Uname());
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789"; 

	StringBuilder sb = new StringBuilder(8); 
	for (int i = 0; i < 8; i++) { 
            int index = (int)(AlphaNumericString.length() * Math.random()); 
            sb.append(AlphaNumericString .charAt(index)); 
	}
	code = sb.toString();
        tf_medicine_id.setText(""+code);
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
    private void btn_Insert(ActionEvent event) {
        
        String medicine_id = code;
        
        String medicine_name = tf_medicine_name.getText();
        String company_name =  tf_company_name.getText();
        String batch_number = tf_batch_number.getText();
        LocalDate mfg_date = dp_mfg_date.getValue();
        LocalDate expiry_date = dp_expiry_date.getValue();
        String quantity = tf_quantity.getText();
        
        System.out.println("hello");
        if (isValid()){
            if(status == 1){
                if(mfg_date.compareTo(expiry_date) < 0){
                    try {
                        Connection con = DbConnect.initializeDatabase();
                        PreparedStatement st = con.prepareStatement("insert into drugs values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            
                        st.setString(1, medicine_id); 
                        st.setString(2, medicine_name); 
                        st.setString(3, company_name);
                        st.setString(4, batch_number); 
                        st.setDate(5, java.sql.Date.valueOf(mfg_date));
                        st.setDate(6, java.sql.Date.valueOf(expiry_date));
                        st.setString(7, quantity); 
                        st.setFloat(8, price); 
                        st.setFloat(9, total);
                
                        st.executeUpdate();
                
                        st.close(); 
                        con.close();
                
                        System.out.println("Insert Successfully!");
                
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Message");
                        alert.setHeaderText("Awesome!");
                        alert.setContentText("Medicine's informations are stored in database successfully!");
                        alert.showAndWait();
                        
                        Parent root = FXMLLoader.load(getClass().getResource("/forms/Dashboard.fxml"));
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.getIcons().add(new Image("/icons/Departments_48px.png"));
                        stage.setTitle("Dashboard : Medical Retails App");
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
    private void btn_Cancel(ActionEvent event) {
        
        tf_medicine_name.setText("");
        tf_company_name.setText("");
        tf_batch_number.setText("");
        dp_mfg_date.setValue(null);
        dp_expiry_date.setValue(null);
        tf_quantity.setText("");
        tf_price_per_unit.setText("");
        tf_total_price.setText("");
        
    }
    
    private boolean isValid() {
        boolean valid = true;
        if (tf_medicine_name.getText().isEmpty() || tf_company_name.getText().isEmpty() ||
                tf_batch_number.getText().isEmpty() || tf_quantity.getText().isEmpty() ||
                dp_mfg_date.getValue() == null || dp_mfg_date.getValue() == null ||
                tf_price_per_unit.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR:");
            alert.setHeaderText("All fields are required!");
            alert.setContentText("All text fields are required. please fill all text field!");
            alert.show();
            valid = false;
        }
        return valid;
    }

    @FXML
    private void btn_Calculate(ActionEvent event) {
        
        int quant = Integer.parseInt(tf_quantity.getText()); 
        price = Float.parseFloat(tf_price_per_unit.getText());
        total = Float.parseFloat(""+price * quant);
        
        tf_total_price.setText(""+total);
        status = 1;
    }

}
