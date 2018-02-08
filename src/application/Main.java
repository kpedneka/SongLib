package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import application.view.ViewController;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("view/GUI.fxml"));
			BorderPane root = (BorderPane)loader.load();
			ViewController controller = loader.getController();
			controller.start(primaryStage);
			Scene scene = new Scene(root, 400, 300);
			primaryStage.setTitle("Song Library");
			primaryStage.setScene(scene);
			primaryStage.setResizable(true);
			primaryStage.show(); 
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
