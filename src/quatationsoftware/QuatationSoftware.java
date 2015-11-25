/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quatationsoftware;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import quatationsoftware.util.Keys;

/**
 *
 * @author sakit
 */
public class QuatationSoftware extends Application{
    
    @FXML private TabPane tabPane;

    private final ResourceBundle rb = ResourceBundle.getBundle("resources.bundles.Bundle", Locale.getDefault());
    @Override
    public void start(Stage primaryStage) throws Exception {
        //load the main fxml file
        Parent root = (Parent)FXMLLoader.load(getClass().getResource("/resources/fxml/main.fxml"), rb);
        
        //get tabpane and select the home window
        tabPane = (TabPane)root.getChildrenUnmodifiable().get(0);
        tabPane.getSelectionModel().clearAndSelect(6);
        
        //load the gui
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1280);
        primaryStage.setMinHeight(800);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/images/logo-small.png")));
        primaryStage.setTitle(rb.getString(Keys.PROG_NAME));           
        primaryStage.show();
    }
 
    public static void main(String args[]){
        launch(args);
    }
}
