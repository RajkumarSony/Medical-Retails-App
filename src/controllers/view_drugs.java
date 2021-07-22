
package controllers;

import connection.DbConnect;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.ModelTable;

/**
 *
 * @author the.bugs.cracker
 */
public class view_drugs implements Initializable {

    @FXML
    private Button Settings;
    @FXML
    private TableView<ModelTable> table_view;
    @FXML
    private TableColumn<ModelTable, String> medicine_id;
    @FXML
    private TableColumn<ModelTable, String> medicine_name;
    @FXML
    private TableColumn<ModelTable, String> company;
    @FXML
    private TableColumn<ModelTable, String> batch_number;
    @FXML
    private TableColumn<ModelTable, LocalDate> mfg_date;
    @FXML
    private TableColumn<ModelTable, LocalDate> expiry_date;
    @FXML
    private TableColumn<ModelTable, String> quantity;
    @FXML
    private TableColumn<ModelTable, Float> price;
    @FXML
    private TextField tf_search;
    @FXML
    private Label user_name;
    
    ObservableList<ModelTable> oblist = FXCollections.observableArrayList();
    private ObservableList<ModelTable> filteredData = FXCollections.observableArrayList();
      
    private static String id;
    
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
    private void btn_modify(ActionEvent event) throws IOException {
        
        ModelTable select = table_view.getSelectionModel().getSelectedItem();
        int selectedIndex = table_view.getSelectionModel().getSelectedIndex();
        
        if(selectedIndex >= 0){
            id = select.getMedicine_id();
            Parent root = FXMLLoader.load(getClass().getResource("/forms/update_drugs.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.getIcons().add(new Image("/icons/Departments_48px.png"));
            stage.setTitle("Update Drugs : Medical Retails App");
            stage.show();
            Stage nStage = (Stage) Settings.getScene().getWindow();
            nStage.close();
        }
        else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR:");
            alert.setHeaderText("No selection was made!");
            alert.setContentText("You have not selected an drug to update. Please try again.");
            alert.showAndWait();
        }
    }

    @FXML
    private void btn_delete(ActionEvent event)throws SQLException {
        
        ModelTable select = table_view.getSelectionModel().getSelectedItem();
//        System.out.println(select.getMedicine_id());        
        int selectedIndex = table_view.getSelectionModel().getSelectedIndex();

        if(selectedIndex >= 0){
            Connection con = DbConnect.initializeDatabase();
            String query = "DELETE FROM drugs WHERE medicine_id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, select.getMedicine_id());
            pst.execute();
            pst.close();
            con.close();
            table_view.getItems().remove(selectedIndex);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message");
            alert.setHeaderText("Awesome!");
            alert.setContentText("Medicine has beed deleted from the list successfully!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR:");
            alert.setHeaderText("No selection was made!");
            alert.setContentText("You have not selected an drug to delete. Please try again.");
            alert.showAndWait();
        }
    }

    @FXML
    private void btn_checkout(ActionEvent event) {
        ModelTable select = table_view.getSelectionModel().getSelectedItem();
        int selectedIndex = table_view.getSelectionModel().getSelectedIndex();
        
        if(selectedIndex >= 0){
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Message");
            dialog.setHeaderText("Plaese, Enter the quantity!");
            dialog.setContentText("Quantity to purchage:");
        
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                System.out.println("Quantity to purchage: " + result.get());
                String quantity = select.getQuantity();
                
                int qty = Integer.parseInt(quantity);
                int pqty = Integer.parseInt(result.get());
                
                if(qty > pqty){
                    qty = qty - pqty;
                    System.out.println(qty);
                    try {
                        Connection con = DbConnect.initializeDatabase();
                        String query ="update drugs set quantity = ? where medicine_id = ?";
                        PreparedStatement st = con.prepareStatement(query);
                        st.setInt(1, qty);
                        st.setString(2, select.getMedicine_id());
                        st.executeUpdate();
                        st.close();
                        con.close();
                        table_view.getItems().removeAll(oblist);
                        loadTable();
                        addToBasket(select, pqty);
                    } catch (SQLException ex) {
                        Logger.getLogger(view_drugs.class.getName()).log(Level.SEVERE, null, ex);
                    }   
                }
            }
        }
        else{
            System.out.println("error: hello");
        }
    }
    
    private void loadTable(){
        try {
            Connection con = DbConnect.initializeDatabase();
            ResultSet rs = con.createStatement().executeQuery("SELECT `medicine_id`, `medicine_name`, `company_name`, `batch_number`, `mfg_date`, `expiry_date`, `quantity`, `price` FROM `drugs`;");
            
            while(rs.next()){
                oblist.add(new ModelTable(rs.getString("medicine_id"), rs.getString("medicine_name"),
                        rs.getString("company_name"), rs.getString("batch_number"), rs.getDate("mfg_date"), 
                        rs.getDate("expiry_date"), rs.getString("quantity"), rs.getFloat("price")));            
            }
            rs.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(view_drugs.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        medicine_id.setCellValueFactory(new PropertyValueFactory<>("medicine_id"));
        medicine_name.setCellValueFactory(new PropertyValueFactory<>("medicine_name"));
        company.setCellValueFactory(new PropertyValueFactory<>("company_name"));
        batch_number.setCellValueFactory(new PropertyValueFactory<>("batch_number"));
        mfg_date.setCellValueFactory(new PropertyValueFactory<>("mfg_date"));
        expiry_date.setCellValueFactory(new PropertyValueFactory<>("expiry_date"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        table_view.setItems(oblist);
    }
    
    private void addToBasket(ModelTable select, int pqty){
        
        String medicine_id = select.getMedicine_id();
        String medicine_name = select.getMedicine_name();
        String company_name = select.getCompany_name();
        String batch_number = select.getBatch_number();
        Date mfg_date = select.getMfg_date();
        Date expiry_date = select.getExpiry_date();
        float price = select.getPrice();
        float total = pqty * price;
        
        try{
            Connection con = DbConnect.initializeDatabase();
            PreparedStatement st = con.prepareStatement("insert into basket values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            
            st.setString(1, medicine_id); 
            st.setString(2, medicine_name); 
            st.setString(3, company_name);
            st.setString(4, batch_number); 
            st.setDate(5, mfg_date);
            st.setDate(6, expiry_date);
            st.setInt(7, pqty); 
            st.setFloat(8, price); 
            st.setFloat(9, total);
                
            st.executeUpdate();
                
            st.close(); 
            con.close();
                
            System.out.println("medicine has beed added in basker!");
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message");
            alert.setHeaderText("Awesome!");
            alert.setContentText("Medicine has beed added in basket successfully!");
            alert.showAndWait();
        }
        catch(SQLException ex){
            Logger.getLogger(Page_signup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static String send_medicineId(){
        return id;
    }

//=============== Search Portion ========================//
    
    @FXML
    private void start_search(MouseEvent event) {
        
        table_view.setItems(filteredData);
        tf_search.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                
                updateFilteredData();
            }
        });
    }
    
    private void updateFilteredData() {
        
        filteredData.clear();      
        for (ModelTable p : oblist) {
            if (matchesFilter(p)) {
                filteredData.add(p);
            }
        }  
        // Must re-sort table after items changed
        repplyTableSortOrder();
    }
    
    private boolean matchesFilter(ModelTable mtable) {
        
        String filterString = tf_search.getText();
        if (filterString == null || filterString.isEmpty()) {
            // No filter --> Add all.
            return true;
        }
        String lowerCaseFilterString = filterString.toLowerCase();
        
        if (mtable.getMedicine_id().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
            return true;
        } else if (mtable.getMedicine_name().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
            return true;
        } else if (mtable.getCompany_name().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
            return true;
        } else if (mtable.getBatch_number().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
            return true;
        }
        
        return false; // Does not match
    }
    
    private void repplyTableSortOrder() {
        ArrayList<TableColumn<ModelTable, ?>> sortOrder = new ArrayList<>(table_view.getSortOrder());
        table_view.getSortOrder().clear();
        table_view.getSortOrder().addAll(sortOrder);
    }
}
