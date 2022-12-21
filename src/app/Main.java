package app;

import app_data_centers.DataCenter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import views.Login;

public class Main extends Application {

	private static final double WIDTH = 400;
	private static final double HEIGHT = 550;

	@Override
	public void start(Stage stage) throws Exception {
		Login login = new Login();
		Pane root = login.display();
		Scene scene = new Scene(root, WIDTH, HEIGHT);
		scene.getStylesheets().add("css/login.css");
		stage.setScene(scene);
		stage.setTitle("Login");
		stage.setMinHeight(HEIGHT);
		stage.setMinWidth(WIDTH);
		stage.show();
	}

	public static void main(String[] args) {
		try {
			DataCenter.getInstance();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Application.launch(args);
	}

}
