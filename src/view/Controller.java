/*
 * Nikhil Menon and Renard Tumbokon 
 */

package view;

import java.util.Optional;

import app.Song;
import app.SongList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller {
	
	Stage mainStage;
	
	@FXML ListView<String> listView;
	private ObservableList<String> obsList;
	private SongList songList;
	
	@FXML private TextArea songDetailsText;
	
	// var name in java controller = fx:id in fxml layout
	@FXML private Button addButton;
	@FXML private Button editButton;
	@FXML private Button deleteButton;
	@FXML private Button cancelButton;
	
	@FXML private TextField titleField;
	@FXML private TextField artistField;
	@FXML private TextField albumField;
	@FXML private TextField yearField;
	
	public void setMainStage(Stage stage){
		// set main stage
		mainStage = stage; 
		
		// create ObservableList from ArrayList
		obsList = FXCollections.observableArrayList();
		
		// populate songs from file to obsList
		songList = new SongList(); // constructor reads file and makes list
		Song ptr = songList.front;
		while (ptr != null){
			obsList.add(ptr.title);
			ptr = ptr.next;
		}
		songList.printList(); //debug
		cleanListView(0); //auto select 0-th index		
	}
	
	private void cleanListView(int indexOfSelect){
		// sort list (note: may already be sorted)
		FXCollections.sort(obsList);
		
		// display list
		listView.setItems(obsList);
		
		// auto select the first item 
	    listView.getSelectionModel().select(indexOfSelect);
	    if (obsList.size() == 0){
	    	songDetailsText.setText("");
	    	return;
	    }
	    showDetails();
	    // listener for clicked item to showDetails() 
	    /* DOES NOT WORK ON SAME NAME
	    listView.getSelectionModel()
	    		.selectedItemProperty()
	    		.addListener(e -> showDetails(listView.getSelectionModel().getSelectedIndex()));	
	    		*/	
	}
	
	@FXML
	private void showDetails(){
		if (obsList.size() < 1){ //list is empty
			return;
		}
		int index = listView.getSelectionModel().getSelectedIndex();
		Song song = songList.findSong(index);
		
		// error: not found song
		if (song.equals(null)){
			System.out.println("Error: song not found to show details");
			return;
		}
		
		// found song
		songDetailsText.setText("Title: " + song.title +
								"\nArtist: " + song.artist +
								"\nAlbum: " + song.album +
								"\nYear: " + song.year);
	}
	
	private void alert(Alert.AlertType alertType, String title, String content){
		// note: alert type = AlertType.INFORMATION/CONFIRMATION/WARNING/ERROR
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(title);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	//if CANCEL button is clicked
	@FXML
	public void cancel(){
		titleField.clear();
		artistField.clear();
		albumField.clear();
		yearField.clear();
	}
	
	// if ADD button is clicked
	@FXML
	public void add(){
		String title = titleField.getText();
		String artist = artistField.getText();
		String album = albumField.getText();
		String year = yearField.getText(); 
		
		addDriver(title, artist, album, year);
	}
	private void addDriver(String title, String artist, String album, String year){	
		// error: required input is empty
		if (title.equals("") || artist.equals("") || title == null || artist == null){
			alert(AlertType.ERROR, "Invalid Input", "Title and Artist field are required");
			return;
		}
		
		boolean successAdd = songList.add(title, artist, album, year);
		
		//error: repeated song
		if (!successAdd){
			alert(AlertType.ERROR, "Duplicate", "Song (Title + Artist) already exists");
			return;
		}
	
		//successful add
		obsList.add(title);
		int newSongIndex = songList.findIndex(title, artist);
		if (newSongIndex != -1){ // safeguard; should always return non-negative
			cleanListView(newSongIndex);
		}	
	}
	
	// if EDIT button is clicked: Delete selected and add new node with "edited" values
	@FXML
	public void edit(){
		/* 
		 *  fxml inputs code here
		 */
		String title = titleField.getText();
		String artist = artistField.getText();
		String album = albumField.getText();
		String year = yearField.getText(); 
		
		editDriver(title, artist, album, year);
	}
	private void editDriver(String title, String artist, String album, String year){
		/*
		 *  communicate with info from fxml form (Text Input Dialog)
		 */
		//check if title+artist already exists
		if (songList.findIndex(title, artist) != -1){
			alert(AlertType.ERROR, "Duplicate", "Song (Title + Artist) already exists");
			return;
		}
		
		int indexToEdit = listView.getSelectionModel().getSelectedIndex(); // note: selected song MUST exist
		Song songToEdit = songList.findSong(indexToEdit);		
		
		// deletion part
		songList.delete(songToEdit.title, songToEdit.artist);
		obsList.remove(indexToEdit);
		
		// add part
		addDriver(title, artist, album, year);
	}
	
	// if DELETE button is clicked: Confirm then Delete
	@FXML
	public void delete(){
		int indexToDelete = listView.getSelectionModel().getSelectedIndex(); // note: selected song MUST exist
		Song songToDelete = songList.findSong(indexToDelete);
		
		// confirmation with cancel
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete");
		alert.setHeaderText("Confirm Deletion");
		alert.setContentText("Are you sure you want to delete '" + songToDelete.title + "' by " + songToDelete.artist + "?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() != ButtonType.OK){
			return; // delete is cancelled, else proceed
		}
		
		// deletion
		songList.delete(songToDelete.title, songToDelete.artist); // delete from linked list
		obsList.remove(indexToDelete); // delete from UI
		
		// fix obsList and choose selector
		if (indexToDelete < obsList.size()){
			cleanListView(indexToDelete); //deleted in middle index
		} else{
			cleanListView(obsList.size()-1); //deleted in last index in list
		}
	}
}
