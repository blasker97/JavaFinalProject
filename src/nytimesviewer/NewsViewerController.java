/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nytimesviewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * FXML Controller class
 *
 * @author Brian L.
 * 
 * @references
 *      1) http://stackoverflow.com/questions/26227786/loading-urls-in-javafx-webview-is-crashing-the-jvm
 */
public class NewsViewerController extends Switchable implements Initializable, TimeStamp {

    private Stage stage;
    
    private NYTNewsManager newsManager;
    ArrayList<NYTNewsStory> stories;
    
    @FXML
    private TextField searchTextField;
    
    @FXML
    private ListView newsListView;
    
    @FXML
    private WebView webView;
    
    
    
    private String searchString = "University of Missouri";
    private WebEngine webEngine;
    ObservableList<String> newsListItems;
    
    private String webString = "";
    private String headline;
    
    
    public boolean tableCreated = false;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    
    public void ready(Stage stage) {
        this.stage = stage;
        webEngine = webView.getEngine();
        newsManager = new NYTNewsManager();
        newsListItems = FXCollections.observableArrayList();
        
        searchTextField.setText(searchString);
        loadNews();
        
        newsListView.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
                    if((int)new_val < 0 || (int)new_val > (stories.size() -1)){
                        return;
                    }
                    
                    NYTNewsStory story = stories.get((int) new_val);
                    webString = story.webUrl;
                    headline = story.headline;
                    webEngine.load(story.webUrl);
                });
       
    }
    
    private void loadNews() {
        
        try {
            newsManager.load(searchString);
            
        } catch (Exception ex) {
            Logger.getLogger(NewsViewerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        stories = newsManager.getNewsStories();
        newsListItems.clear();
        
        for(NYTNewsStory story : stories){
            newsListItems.add(story.headline);
        }
        
        newsListView.setItems(newsListItems);
        
        if(stories.size() > 0){
            newsListView.getSelectionModel().select(0);
            newsListView.getFocusModel().focus(0);
            newsListView.scrollTo(0);
        }
            
            // The above is used to tell the list view to select, focus on, and
            // scroll to the first item which will cause the listener to treat
            // this item as being selected.
            // Below is the way I could just tell the webView's webEngine to display
            // the first story.  ...which I don't need to do if the listener on
            // the list view is told the first item is selected.
            /*
            if (stories.size() > 0) {
            webEngine.load(stories.get(0).webUrl);
            }
            */
        
    }
    
    @FXML
    private void handleSearch(ActionEvent event) {
        if (searchTextField.getText().equals("")) {
            displayErrorAlert("The search field cannot be blank. Enter one or more search words.");
            return;
        }
        searchString = searchTextField.getText();
        loadNews();
    }
    
    @FXML
    private void handleUpdate(ActionEvent event) {
        loadNews();
    }
    
    @FXML
    private void handleAbout(ActionEvent event) {
        displayAboutAlert();
    }
    
    @FXML
    private void handleReview(ActionEvent event){
        if(webString.equals("")){
            displayErrorAlert("No article selected. Please select an article to review.");
        }
        else{
            displayReviewAlert();
        }
    }

    @FXML
    private void handleSwitchable(ActionEvent event){
        Switchable.switchTo("ReviewPage");
    }
    
    private void displayErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error!");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void displayExceptionAlert(Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception");
        alert.setHeaderText("An Exception Occurred!");
        alert.setContentText("An exception occurred.  View the exception information below by clicking Show Details.");

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }
    
    private void displayAboutAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("New York Times Viewer and Reviewer");
        alert.setContentText("This application was developed by Brian Lasker, using and developing on the api implementation by Proffesor Wergeles");
        
        TextArea textArea = new TextArea("The New York Times API is used to obtain a news feed.  Developer information is available at http://developer.nytimes.com. ");
        textArea.appendText("The goal of this project was to extend the implementation of the New York Times api "
                + "so that the user can keep track and record the articles that they have read.  "
                + "It includes a rating and description so the user can select articles they liked in the past and read the summary they wrote."
                + " The article reviewer contains sorting features that allow the user to sort by all but the description charcteristic of the review they wrote.");
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
    
    private void displayReviewAlert(){
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Article Review");
        dialog.setHeaderText("Rate based on content quality and enjoyment");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Enter", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField title = new TextField();
        title.setPromptText("Title Summary");
        TextField url  = new TextField();
        url.setText(webString);
        TextField rating = new TextField();
        TextField desc = new TextField();
        desc.setPrefHeight(150);
        desc.setPrefWidth(400);
        desc.setAlignment(Pos.TOP_LEFT);
       
        

        grid.add(new Label("Title:"), 0, 0);
        grid.add(title, 1, 0);
        grid.add(new Label("URL:"), 0, 1);
        grid.add(url, 1, 1);
        grid.add(new Label("Rating(1-5):"), 0, 2);
        grid.add(rating, 1, 2);
        grid.add(new Label("Description(Optional):"), 0, 3);
        grid.add(desc, 1, 3);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        title.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> title.requestFocus());

//         Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            
            if (dialogButton == loginButtonType) {
                
                for(int i =0; i < rating.getText().length(); i++){
                    char digit = rating.getText().charAt(i);
                    if(Character.isDigit(digit) == false){
                        String error = "Invalid rating. Try again with a number between 1-5";
                        displayErrorAlert(error);
                        return null;
                    }
                }
                
                if(rating.getText().isEmpty()){
                    String error = "Invalid rating. Try again with a number between 1-5";
                    displayErrorAlert(error);
                }
                else{
                    if(Integer.parseInt(rating.getText()) < 1 || Integer.parseInt(rating.getText()) > 5){
                        String error = "Invalid rating. Try again with a number between 1-5";
                        displayErrorAlert(error);
                    }
                    else{
                        return new Pair<>(title.getText(), rating.getText());
                    }
                }
            }
            return null;
        });


        Optional<Pair<String, String>> result = dialog.showAndWait();
        
        String timeStamp = handleTime();
        
        result.ifPresent(usernamePassword -> {
            Review review = new Review(timeStamp,usernamePassword.getKey(),webString,usernamePassword.getValue(),desc.getText());
//            System.out.println(review.title + "\n" + review.url + "\n" + review.timeStamp + "\n" + review.rating);
            Switchable.switchTo("ReviewPage");
            ReviewPageController controller = (ReviewPageController)getControllerByName("ReviewPage");

            if(tableCreated == false){
                controller.reviews = FXCollections.observableArrayList(review);
                controller.createTable();
            }
            else{
                controller.addReview(review);
            }
            
            
            
            tableCreated = true;
            
        });
        
        
        
    }

    @Override
     public String handleTime() {
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        return timeStamp;
    }
    
    @FXML
    public void handleOpen(ActionEvent event) {
        Switchable.switchTo("ReviewPage");
        
        ReviewPageController controller = (ReviewPageController)getControllerByName("ReviewPage");
        controller.reviews = readFile();
       
        if(tableCreated == false){
            controller.createTable();
            tableCreated = true;
        }
        
    }
    
    @FXML
    public void handleSave(ActionEvent event) throws IOException{
        ReviewPageController controller = (ReviewPageController)getControllerByName("ReviewPage");
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            if( ! file.getName().endsWith(".ser")){
                file = new File(file.getPath() + ".ser");
            }
        }
        //Path file = Files.createTempFile("testing", "ser");
        writeFile(controller.reviews, file);
    }
    
    private ObservableList<Review> readFile(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                FileInputStream fileIn = new FileInputStream(file.getPath());
                ObjectInputStream in = new ObjectInputStream(fileIn);
                
                List<Review> list = (List<Review>)in.readObject(); 
                
                in.close();
                fileIn.close();
                
                return FXCollections.observableList(list);
                
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(NewsViewerController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(NewsViewerController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(NewsViewerController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        return FXCollections.emptyObservableList();
    }
    
    
    private static void writeFile(ObservableList<Review> reviews, File file){
            try{
                FileOutputStream fileOut = new FileOutputStream(file.getPath());;    
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(new ArrayList<Review>(reviews));
                out.close();
                
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
             } catch (IOException ex) {
                ex.printStackTrace();
            }
            
        }
}