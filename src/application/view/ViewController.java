
package application.view;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;

import application.model.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * 
 * @author Kunal Pednekar (ksp101)
 * @author Rachana Thanawala (rt468)
 *
 */
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
	@FXML
	Button cancel;
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
			Song song;
			String line = scanner.nextLine();
			String splits[] = line.split(", ");
			if(splits.length==2)
			song = new Song(splits[0],splits[1],"","");
			else if(splits.length==3)
				song = new Song(splits[0],splits[1],splits[2],"");
			else
				song = new Song(splits[0],splits[1],splits[2],splits[3]);
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
		{

			cancel.setVisible(false);
			okay.setVisible(false);
			sTitle.setText("");
			sArtist.setText("");
			sAlbum.setText("");
			sYear.setText("");
			return;
		}
			
		Song s = songList.getSelectionModel().getSelectedItem();
		// update the VBox to show the details of the song that is selected
		sTitle.setText("\t"+ s.getTitle());
		sArtist.setText("\t"+s.getArtist());
		sAlbum.setText("\t"+s.getAlbum());
		sYear.setText("\t"+s.getYear());
		// remove visibility for cancel and done buttons
		cancel.setVisible(false);
		okay.setVisible(false);
	}
	/** Updates the file with the changes made in the list by the user
	 * @param obsList the ArrayList that we will write to the file 
	 * @throws IOException 
	 */
	private void writeToFile(ObservableList<Song> obsList) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
	//	System.out.println("In function call writeToFile");
		// overwrite the whole file
		for (Song s : obsList) {
			writer.write(s.getTitle()+", "+s.getArtist()+", "+s.getAlbum()+", "+s.getYear());
			writer.newLine();
		}
		writer.close();
	}
	/**
	 * ActionEvent triggered function that adds a song to the song library
	 * The cancel and done button appear when the user triggers the add button
	 * The song details are cleared for the user to enter new song details. 
	 * Checks are made to ensure user does not enter repetitions and atleast the title and the artist of the song is entered
	 * The song is added to the list in alphabetical order when the user presses done
	 * The newly entered song is selected.
	 * The done and cancel button disappear
	 * @throws FileNotFoundException when the file to append the newly added song does not exist
	 */
	public void addSong(ActionEvent evt) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setContentText("Enter Details to add a new song");

		alert.showAndWait();

		sTitle.setText("");
		sArtist.setText("");
		sAlbum.setText("");
		sYear.setText("");
		
		cancel.setVisible(true);
		okay.setVisible(true);
		
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
							if(song1.getTitle().toLowerCase().compareTo(song2.getTitle().toLowerCase())==0)
								return song1.getArtist().toLowerCase().compareTo(song2.getArtist().toLowerCase());
							else
							return song1.getTitle().toLowerCase().compareTo(song2.getTitle().toLowerCase());
						}

					});
					if (obsList.size() == 1) {
						songList.getSelectionModel().select(0);
					}
					else
					{
						int i = 0;
						for(Song s: obsList)
						{

							if(s == s2)
							{
								songList.getSelectionModel().select(i);
								break;
							}

							i++;
						}
					}
					displayDetails();
					// persist changes
					try {
						writeToFile(obsList);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
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
		cancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				displayDetails();
			}
		});
	}


	/**
	 * ActionEvent triggered function that edits a song in the song library
	 * if list is empty alert box pops up displaying appropriate message
	 * cancel and done buttons appear when the user triggers the edit button
	 * once the user enters the edited song and presses ok, the changes reflect in the list 
	 * the song is placed in the list in alphabetically order
	 * the cancel and ok buttons disappear
	 * @throws FileNotFoundException when the file to append the newly added song does not exist
	 */
	public void editSong(ActionEvent evt) {

		if (obsList.isEmpty())
		{
			//System.out.println(songList.getSelectionModel().getSelectedIndex());
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning Dialog");
			alert.setContentText("List is empty. No song to edit");
			alert.showAndWait();
			displayDetails();
			return;
		}
		
		Song s1 = songList.getSelectionModel().getSelectedItem();
		
		cancel.setVisible(true);
		okay.setVisible(true);
		
		okay.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String error;
				Song s4 = songList.getSelectionModel().getSelectedItem();
			if(sTitle.getText().trim().toLowerCase().equals(s4.getTitle().toLowerCase())&&sArtist.getText().trim().toLowerCase().equals(s4.getArtist().toLowerCase())&&sAlbum.getText().trim().toLowerCase().equals(s4.getAlbum().toLowerCase())&&sYear.getText().trim().toLowerCase().equals(s4.getYear().toLowerCase()))
					error = "no changes made";
			else if(sTitle.getText().trim().toLowerCase().equals(s4.getTitle().toLowerCase())&&sArtist.getText().trim().toLowerCase().equals(s4.getArtist().toLowerCase()))
				error= ValidityforEdit(sTitle.getText().trim(), sArtist.getText().trim(), sAlbum.getText().trim(),sYear.getText().trim());
				else
				error= Validity(sTitle.getText().trim(), sArtist.getText().trim(), sAlbum.getText().trim(),sYear.getText().trim());
				if(error.equalsIgnoreCase("no error")) {
					s1.setTitle(sTitle.getText());
					s1.setArtist(sArtist.getText());
					s1.setAlbum(sAlbum.getText());
					s1.setYear(sYear.getText());
					FXCollections.sort(obsList, new Comparator<Song>() {
						@Override
						public int compare(Song song1, Song song2) {
							if(song1.getTitle().toLowerCase().compareTo(song2.getTitle().toLowerCase())==0)
								return song1.getArtist().toLowerCase().compareTo(song2.getArtist().toLowerCase());
							else
							return song1.getTitle().toLowerCase().compareTo(song2.getTitle().toLowerCase());
							}

					});
					displayDetails();
					// persist changes
					try {
						writeToFile(obsList);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning Dialog");
					alert.setContentText(error);
					alert.showAndWait();
				}
			}
		});
		
		cancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				sTitle.setText(s1.getTitle());
				sArtist.setText(s1.getArtist());
				sAlbum.setText(s1.getAlbum());
				sYear.setText(s1.getYear());
				displayDetails();
			}
		});
	}
	/**
	 * ActionEvent triggered function that deletes a song from the song library
	 * if list is empty, an alert is shown, displaying an appropriate message
	 * An alert also pops up to confirm if the suer wants to delete the song
	 * if the user presses ok, the song is deleted
	 * @throws FileNotFoundException when the file to append the newly added song does not exist
	 */
	public void deleteSong(ActionEvent evt) {

		// show dialog only if list is empty
		if (obsList.isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning Dialog");
			alert.setHeaderText("Cannot delete song");
			alert.setContentText("List is empty or no song selected");
			alert.showAndWait();
			return;
		} 
		else 
		{
			// remove song from ArrayList and update ListView
			Song s = songList.getSelectionModel().getSelectedItem();
			int index = songList.getSelectionModel().getSelectedIndex();
			if (obsList.contains(s)) 
			{
				Alert alert = 
						new Alert(AlertType.INFORMATION);
				alert.setTitle("Delete Item");
				alert.setHeaderText(
						"Press OK to confirm or the cross to cancel");

				String content = "Are you sure you want to delete " + s.getTitle() + "?";

				alert.setContentText(content);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.isPresent()) 
				{
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
					songList.setItems(obsList);
				}		
			}
			
		}
	
		// persist changes
		try {
			writeToFile(obsList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**Checks if the input entered by the user is valid or not
	 * 
	 * @param t is the title of the song. The title must be present.
	 * @param ar is the artist of the song. The artist of the song must be present
	 * @param al is the album of the song
	 * @param yr is the year of the song. The year must be only integers and only 4 characters long
	 * Checks for repetition by comparing title and artist
	 * @return appropriate message if above conditions are not met. Else return no error
	 */
	public String Validity(String t, String ar, String al, String yr)
	{
		//checks if user has entered title and artist
		if(t.isEmpty()||ar.isEmpty())
			return "Title or Artist cannot be left empty";
		//checks if the characters in the year are all digits
		else if((!yr.trim().isEmpty()) && (!yr.trim().matches("[0-9]+")))
			return "Year must only contain numbers.";
		//checks if the year has only 4 digits
		else if ((!yr.trim().isEmpty())&&(yr.trim().length() != 4))
			return "Year must be 4 digits long.";
		//compares the title and artist to ensure no repetitions exist
		else if(!UniqueFields(t, ar))
			return "Title and Artist already exist";
		else
			return "no error";
	}

	/**Checks if the input entered by the user is valid or not
	 * 
	 * @param t is the title of the song. The title must be present.
	 * @param ar is the artist of the song. The artist of the song must be present
	 * @param al is the album of the song
	 * @param yr is the year of the song. The year must be only integers and only 4 characters long
	 * Does not compare artist and title since they are equal to the selected song
	 * @return appropriate message if above conditions are not met. Else return no error
	 */

	public String ValidityforEdit(String t, String ar, String al, String yr)
	{
		//checks if user has entered title and artist
		if(t.isEmpty()||ar.isEmpty())
			return "Title or Artist cannot be left empty";
		//checks if the characters in the year are all digits
		else if((!yr.trim().isEmpty()) && (!yr.trim().matches("[0-9]+")))
			return "Year must only contain numbers.";
		//checks if the year has only 4 digits
		else if ((!yr.trim().isEmpty())&&(yr.trim().length() != 4))
			return "Year must be 4 digits long.";
		//compares the title and artist to ensure no repetitions exist
		else
			return "no error";
	}

	/** Check for repetitions by comparing title and artist. Strings are converted to lower case to make the comparison case-insensitive
	 * 
	 * @param t is the title of the song
	 * @param ar is the artist of the song
	 * @return false if the song already exists and true if the song does not exist
	 */
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
