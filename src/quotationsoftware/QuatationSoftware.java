/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quotationsoftware;

import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import quotationsoftware.util.DbHandler;
import quotationsoftware.util.Keys;

/**
 *
 * @author sakit
 */
public class QuatationSoftware extends Application{
    
    @FXML private TabPane tabPane;

    private final ResourceBundle RB = ResourceBundle.getBundle("resources.bundles.Bundle", Locale.getDefault());
    public final static Properties PROP = new Properties();
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        //load the main fxml file
        Parent root = (Parent)FXMLLoader.load(getClass().getResource(Keys.MAIN_SCREEN), RB);
        
        //get tabpane and select the home window
        tabPane = (TabPane)root.getChildrenUnmodifiable().get(0);
        tabPane.getSelectionModel().clearAndSelect(6);
        
        try ( //load properties file
            InputStream in = getClass().getResourceAsStream("/resources/settings/preference")) {
            PROP.load(in);
        }
        
        //load the gui
        Scene scene = new Scene(root,1300,950);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/images/logo-small.png")));
        primaryStage.setTitle(RB.getString(Keys.PROG_NAME));   
        primaryStage.setOnCloseRequest(e->{
            DbHandler.shutdown();
            System.out.println("database shutdowned");
        });
        primaryStage.show();
    }
 
    public static void main(String args[]){
        launch(args);
    }
}
