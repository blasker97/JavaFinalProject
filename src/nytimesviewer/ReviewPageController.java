/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nytimesviewer;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Brian L
 */
public class ReviewPageController extends Switchable implements Initializable {
    
    private Stage stage;
    
    public ObservableList<Review> reviews;
    
    
    @FXML
    private TableView<Review> reviewTable;
    
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    
    @FXML
    private void handleSwitchable(ActionEvent event){
        Switchable.switchTo("NewsViewer");
        
        NewsViewerController controller = (NewsViewerController)getControllerByName("NewsViewer");
        
        controller.ready(stage);
        
        
    }
    
    public void createTable(){
        TableColumn titleCol = new TableColumn("Title");
        titleCol.setCellValueFactory(
                new PropertyValueFactory<Review, String>("title"));
        titleCol.setPrefWidth(150);
        
        TableColumn dateCol = new TableColumn("Date");
        dateCol.setCellValueFactory(
                new PropertyValueFactory<Review, String>("timeStamp"));
        
        TableColumn ratingCol = new TableColumn("Rating");
        ratingCol.setCellValueFactory(
                new PropertyValueFactory<Review, String>("rating"));
        
        TableColumn urlCol = new TableColumn("Url");
        urlCol.setCellValueFactory(
               new PropertyValueFactory<Review, String>("url"));
        reviewTable.setItems(reviews);
        reviewTable.getColumns().addAll(titleCol, dateCol, ratingCol, urlCol);
    
    }
    
    public void addReview (Review review){
        reviews.add(review);
    }
    
    @FXML
    private void readDescription(ActionEvent event){
        NewsViewerController controller = (NewsViewerController)getControllerByName("NewsViewer");
        
        if(controller.tableCreated == true){
            if(reviews.isEmpty() == false){

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(reviews.get(reviewTable.getSelectionModel().getFocusedIndex()).getTitle() + " review");
                alert.setHeaderText("Viewed at " + reviews.get(reviewTable.getSelectionModel().getFocusedIndex()).getTimeStamp());
                alert.setContentText("Url: " + reviews.get(reviewTable.getSelectionModel().getFocusedIndex()).getUrl());

                TextArea textArea = new TextArea(reviews.get(reviewTable.getSelectionModel().getFocusedIndex()).getDesc());
                textArea.setEditable(false);
                textArea.setWrapText(true);
                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);

                GridPane expContent = new GridPane();
                expContent.setMaxWidth(Double.MAX_VALUE);
                expContent.add(textArea, 0, 0);

                alert.getDialogPane().setExpandableContent(expContent);

                alert.showAndWait();
            }
        }
        
    }
    
    @FXML
    private void deleteEntry(ActionEvent event){
        NewsViewerController controller = (NewsViewerController)getControllerByName("NewsViewer");
        if(controller.tableCreated == true){
            if(reviews.isEmpty() == false){
                reviews.remove(reviewTable.getSelectionModel().getFocusedIndex());
            }
        }
        
        
        
    }
}
