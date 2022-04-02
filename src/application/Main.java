package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;



public class Main extends Application {
	@Override
	public void start(Stage stage) throws IOException {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
	        Scene scene = new Scene(fxmlLoader.load());
	        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
	        stage.setTitle("Hello!");
	        stage.setScene(scene);
	        stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
