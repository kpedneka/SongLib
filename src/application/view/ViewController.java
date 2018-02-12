//THINGS TO DO:
//refresh the list after editing a song
//select the newly added song after adding a song
//make all changes in the txt file

package application.view;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.Scanner;

import application.model.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ViewController {
	// our array of songs that we will display in the GUI
	private ObservableList<Song> obsList;
	private String filename = "src/songs.txt";
	@FXML
	private ListView<Song> songList;
	@FXML
	TextField sTitle, sArtist, sAlbum, sYear;
	@FXML
	Button editButton;
	@FXML
	Button okay;
	
	/**
	 * Initializes the ListView with song objects from a text file specified in the function
	 * @param primaryStage is the stage that is passed in from Main.java
	 * @throws FileNotFoundException when the file to initialize the list is not found
	 */
	public void start(Stage primaryStage) throws FileNotFoundException {
		obsList = FXCollections.observableArrayList();
		// scanner reads the file, creates a song object and adds it to our list
		Scanner scanner  =  new Scanner(new FileReader(filename));
		while(scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			String splits[] = line.split(", ");
			Song song = new Song(splits[0],splits[1], splits[2], splits[3]);
			obsList.add(song);
		}
		scanner.close();
		// sort our songs
		FXCollections.sort(obsList, new Comparator<Song>() {
			@Override
			public int compare(Song song1, Song song2) {
				return song1.getTitle().toLowerCase().compareTo(song2.getTitle().toLowerCase());
			}
			
		});
		// set the ListView to display our song list
		songList.setItems(obsList);
		// what does the cell factory do?
		songList.setCellFactory(new Callback<ListView<Song>, ListCell<Song>>(){
            @Override
            public ListCell<Song> call(ListView<Song> p) {
                ListCell<Song> cell = new ListCell<Song>(){
                    @Override
                    protected void updateItem(Song s, boolean bln) {
                        super.updateItem(s, bln);
                        if (s != null) {
                        	// here we are setting the song name and title that we see in the list
                            setText(s.getTitle()+", "+s.getArtist());
                        }
                        else if (s == null) {
                        	setText(null);
                        }
                    }
                };
                return cell;
            }
        });
		
		// select the first item
	    if (!obsList.isEmpty())
	    		songList.getSelectionModel().select(0);
	    // show the selected song
	    displayDetails();
	}
	/**
	 * Gets the selected song and updates the FXML Text boxes
	 * to display the details of the selected song.
	 */
	public void displayDetails() {
		if (songList.getSelectionModel().getSelectedIndex() < 0)
			return;
		Song s = songList.getSelectionModel().getSelectedItem();
		// update the VBox to show the details of the song that is selected
		sTitle.setText("\t"+ s.getTitle());
		sArtist.setText("\t"+s.getArtist());
		sAlbum.setText("\t"+s.getAlbum());
		sYear.setText("\t"+s.getYear());
	}
	/**
	 * ActionEvent triggered function that adds a song to the song library
	 * @throws FileNotFoundException when the file to append the newly added song does not exist
	 */
	public void addSong(ActionEvent evt) throws FileNotFoundException {
		Scanner scanner  =  new Scanner(new FileReader(filename));
		sTitle.setText("");
		sArtist.setText("");
		sAlbum.setText("");
		sYear.setText("");

		
		okay.setOnAction(new EventHandler<ActionEvent>()
				{
				@Override
				public void handle(ActionEvent e)
				{
					
					String error = Validity(sTitle.getText().trim(), sArtist.getText().trim(), sAlbum.getText().trim(),sYear.getText().trim());
					
					if(error.equalsIgnoreCase("no error"))
					{
					Song s2 = new Song(sTitle.getText().trim(), sArtist.getText().trim(), sAlbum.getText().trim(),sYear.getText().trim());
					obsList.add(s2);
					FXCollections.sort(obsList, new Comparator<Song>() {
						@Override
						public int compare(Song song1, Song song2) {
							return song1.getTitle().toLowerCase().compareTo(song2.getTitle().toLowerCase());
						}
						
					});
								
						displayDetails();
					}
					else
					{
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Warning Dialog");
						alert.setContentText(error);

						alert.showAndWait();
					}
						
				}
				});		
		scanner.close();
	}
	/**
	 * ActionEvent triggered function that edits a song in the song library
	 * @throws FileNotFoundException when the file to append the newly added song does not exist
	 */
	public void editSong(ActionEvent evt) throws FileNotFoundException {
		Scanner scanner  =  new Scanner(new FileReader(filename));
		
		if (obsList.isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning Dialog");
			alert.setContentText("List is empty. No song to edit");

			alert.showAndWait();
		} 
		Song s1 = songList.getSelectionModel().getSelectedItem();
		
		okay.setOnAction(new EventHandler<ActionEvent>()
				{
				@Override
				public void handle(ActionEvent e)
				{
					
					String error = Validity(sTitle.getText().trim(), sArtist.getText().trim(), sAlbum.getText().trim(),sYear.getText().trim());
					
					if(error.equalsIgnoreCase("no error"))
					{
					s1.setTitle(sTitle.getText());
					s1.setArtist(sArtist.getText());
					s1.setAlbum(sAlbum.getText());
					s1.setYear(sYear.getText());
					}
					else
					{
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Warning Dialog");
						alert.setContentText(error);

						alert.showAndWait();
					}
						
				}
				});		
		FXCollections.sort(obsList, new Comparator<Song>() {
			@Override
			public int compare(Song song1, Song song2) {
				return song1.getTitle().toLowerCase().compareTo(song2.getTitle().toLowerCase());
			}
			
		});
		displayDetails();
		// need to replace the old line in the file
		scanner.close();
	}
	/**
	 * ActionEvent triggered function that deletes a song from the song library
	 * @throws FileNotFoundException when the file to append the newly added song does not exist
	 */
	public void deleteSong(ActionEvent evt) throws FileNotFoundException {
		Scanner scanner = new Scanner(new FileReader(filename));

		// show dialog only if list is empty
		if (obsList.isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning Dialog");
			alert.setHeaderText("Cannot delete song");
			alert.setContentText("List is empty or no song selected");

			alert.showAndWait();
		} else {
			// remove song from ArrayList and update ListView
		Song s = songList.getSelectionModel().getSelectedItem();
			int index = songList.getSelectionModel().getSelectedIndex();
		if (obsList.contains(s)) {
				obsList.remove(s);
				if (obsList.isEmpty()) {
					   sTitle.setText("");
					   sArtist.setText("");
					   sAlbum.setText("");
					   sYear.setText("");
				   }
				  else if(index == obsList.size()-1)
				   {
					   songList.getSelectionModel().select(index--);
				   }
				   else
				   {
					   songList.getSelectionModel().select(index++);
				   }
				   displayDetails();
			}
			songList.setItems(obsList);
		}
		// need to delete the old line in the file, ensure no blank spaces
		// update ListView
		scanner.close();
	}
	
	//check if the input strings are valid
	public String Validity(String t, String ar, String al, String yr)
	{
		if(t.isEmpty()||ar.isEmpty())
			return "Title or Artist cannot be left empty";
		else if((!yr.trim().isEmpty()) && (!yr.trim().matches("[0-9]+")))
				   return "Year must only contain numbers.";
		else if ((!yr.trim().isEmpty())&&(yr.trim().length() != 4))
				   return "Year must be 4 digits long.";
		else if(!UniqueFields(t, ar))
			return "Title and Artist already exist";
		else
		return "no error";
	}
	
	//check for repetitions by comparing title and artist
	public boolean UniqueFields(String t, String ar)
	{
		for (Song s : obsList) 
			{	   
				   if (s.getTitle().toLowerCase().equals(t.toLowerCase()) && s.getArtist().toLowerCase().equals(ar.toLowerCase()))
					   return false;		   
			}
		return true;

	}
	
}
