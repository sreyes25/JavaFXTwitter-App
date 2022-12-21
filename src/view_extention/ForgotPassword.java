package view_extention;

import app_data_centers.DataCenter;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.Util;

public class ForgotPassword {
	private static DataCenter app;
	private static Stage stage;
	private static StackPane stack;

	public static void display() {
		
		
		app = DataCenter.getInstance();
		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		
		stack = new StackPane(enterUsername());
		Scene scene = new Scene(stack, 370, 200);
		scene.getStylesheets().add("css/login.css");
		stage.setTitle("Forgot Password");
		stage.setScene(scene);
		stage.setMinHeight(170);
		stage.setMinWidth(300);
		stage.showAndWait();
	}
	
	public static Pane enterUsername() {
		Label title = new Label("Enter Username");
		title.getStyleClass().add("font-lg");
		HBox titleBox = new HBox(title);
		titleBox.getStyleClass().add("padding");
		Button next = new Button("Next");
		next.getStyleClass().add("forgot-btn");
		next.setDisable(true);

		Button close = new Button("Close");
		close.getStyleClass().add("forgot-btn");

		HBox buttonBox = new HBox(close, next);
		buttonBox.getStyleClass().add("spacing");
		
		BorderPane content = new BorderPane();
		content.getStyleClass().add("padding");

		TextField textField = new TextField();
		textField.setOnKeyTyped(e -> {
			next.setDisable(!Util.validateTextAreas(textField.getText()));
		});
		
		close.setOnAction(e -> {
			stage.close();
		});
		next.setOnAction(e -> {
			if(app.userExists(textField.getText())) {
			stack.getChildren().add(enterEmail(textField.getText())); 
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("The username "+textField.getText()+" does not exist or is spelt incorrectly.");
				alert.setHeaderText("User Does Not Exist");
				alert.showAndWait();
			}
		});
		
		textField.setPrefWidth(100);
		HBox textBox = new HBox(textField);

		content.setTop(titleBox);
		content.setCenter(textBox);
		content.setBottom(buttonBox);
		return content;
	}
	
	public static Pane enterEmail(String username) {
		
		Label title = new Label("Enter Email");
		title.getStyleClass().add("font-lg");
		HBox titleBox = new HBox(title);
		titleBox.getStyleClass().add("padding");
		Button enter = new Button("Next");
		enter.getStyleClass().add("forgot-btn");
		enter.setDisable(true);

		Button back = new Button("Back");
		back.getStyleClass().add("forgot-btn");

		HBox buttonBox = new HBox(back, enter);
		buttonBox.getStyleClass().add("spacing");
		
		BorderPane content = new BorderPane();
		content.getStyleClass().addAll("padding", "background");

		TextField textField = new TextField();
		textField.setOnKeyTyped(e -> {
			enter.setDisable(!Util.validateTextAreas(textField.getText()));
		});
		
		back.setOnAction(e -> {
			stack.getChildren().remove((stack.getChildren().size()-1));
		});
		enter.setOnAction(e -> {
			if(app.confirmEmail(username, textField.getText())) {
			stack.getChildren().add(hint(username));
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("This email "+textField.getText()+" does not match the one to this account");
				alert.setHeaderText("Wrong Email");
				alert.showAndWait();
			}
		});
		
		textField.setPrefWidth(100);
		HBox textBox = new HBox(textField);

		content.setTop(titleBox);
		content.setCenter(textBox);
		content.setBottom(buttonBox);
		return content;
	}
	
	public static Pane hint(String username) {
		Label title = new Label("Password Hint");
		title.getStyleClass().add("font-lg");
		HBox titleBox = new HBox(title);
		titleBox.getStyleClass().add("padding");

		String password = app.userSearch(username).getPassword();
		char[] passwordArr = password.toCharArray();
		
		String passwordHint = "";
		for(int i = 0; i < passwordArr.length; i++) {
			if(i == 0) {
				passwordHint += passwordArr[i];
			}
			else if(i > passwordArr.length-3) {
				passwordHint += passwordArr[i];
			}
			else {
				passwordHint += "*";
			}
		}
		Label hint = new Label(passwordHint);
		hint.getStyleClass().add("font-lg");
		HBox textBox = new HBox(hint);
		
		Button close = new Button("Close");
		close.getStyleClass().add("forgot-btn");
		
		close.setOnAction(e -> {
			stage.close();
		});

		HBox buttonBox = new HBox(close);
		buttonBox.getStyleClass().add("spacing");
		
		BorderPane content = new BorderPane();
		content.getStyleClass().addAll("padding", "background");

		content.setTop(titleBox);
		content.setCenter(textBox);
		content.setBottom(buttonBox);
		return content;
	}

}
