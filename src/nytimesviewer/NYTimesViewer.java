/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nytimesviewer;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import static nytimesviewer.Switchable.getControllerByName;

/**
 *
 * @author Professor Wergeles
 */
public class NYTimesViewer extends Application {

    @Override
    public void start(Stage stage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewsViewer.fxml"));
//        Parent root = (Parent)loader.load();
//        NewsViewerController controller = (NewsViewerController)loader.getController();
//        
//        Scene scene = new Scene(root);
//        
//        stage.setScene(scene);
//        stage.show();
//        
//        controller.ready(stage);
//        
//        Switchable.scene = scene;

////         // UI if SceneManager can't switch to a Scene
        HBox root = new HBox();
        root.setPrefSize(600, 400);
        root.setAlignment(Pos.CENTER);
        Text message = new Text("This is a failure!");
        message.setFont(Font.font(STYLESHEET_MODENA, 32));
        root.getChildren().add(message);
        
        // create Scene and init UI to show failure in case switch fails
        Scene scene = new Scene(root);
        
        Switchable.scene = scene;
        Switchable.switchTo("NewsViewer");
        
        NewsViewerController controller = (NewsViewerController)getControllerByName("NewsViewer");
        
        
        stage.setScene(scene);
        stage.show();
        
        controller.ready(stage);
        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}