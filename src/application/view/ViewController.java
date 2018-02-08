package application.view;

import application.model.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class ViewController {
	// our array of songs that we will display in the GUI
	private ObservableList<Song> songList;
	
	/**
	 * 
	 * @param primaryStage is the stage that is passed in from Main.java
	 */
	public void start(Stage primaryStage) {
		songList = FXCollections.observableArrayList();
	}
	// need to still add the following functions:
	// handle add
	// handle edit
	// handle delete
	
}
