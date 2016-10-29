/*
 * Nikhil Menon and Renard Tumbokon 
 */

package app;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.Controller;

public class SongLib extends Application {

	Stage mainStage;

    @Override
    public void start(Stage stage){
    	mainStage = stage; 
    	mainStage.setTitle("Nikhil & Renard Song Library");
    	
    	try{
	    	// load fxml
	    	FXMLLoader loader = new FXMLLoader();
	    	loader.setLocation(getClass().getResource("/view/SongLib.fxml"));
	    	AnchorPane pane = (AnchorPane) loader.load();
	        
	    	// load controller
	    	Controller controller = loader.getController();
	    	controller.setMainStage(mainStage); //main function in controller
	    	
	    	Scene scene = new Scene(pane, 700, 600);
	        mainStage.setScene(scene);
	        mainStage.show();
	        
    	} catch (IOException e){
    		e.printStackTrace();
    	}
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
