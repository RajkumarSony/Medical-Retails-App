
package controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author the.bugs.cracker
 */
public class Controllers extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/forms/page_login.fxml"));
        
        Scene scene = new Scene(root); 
        stage.setScene(scene);
        stage.getIcons().add(new Image("/icons/Departments_48px.png"));
        stage.setTitle("Login Form : Medical Retails App");
        //stage.initStyle(StageStyle.UNDECORATED);// Hide Titlebar
        //stage.resizableProperty().setValue(false); Desable Maximise button
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
