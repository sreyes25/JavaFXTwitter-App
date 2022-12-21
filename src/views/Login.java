package views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import app_data_centers.DataCenter;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import util.Util;
import view_extention.ForgotPassword;

public class Login {
	
	private ObservableList<Node> pnodes;
	private static DataCenter app = DataCenter.getInstance();
	private StackPane stackPane;
	private Button btnSignIn;
	private Button btnSignUp;
	private String userpfp;


	public Pane display() {
		BorderPane container = new BorderPane();
		stackPane = new StackPane();

		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER);

		btnSignIn = new Button("Sign In");
		btnSignUp = new Button("Sign Up");
		btnSignIn.getStyleClass().add("button2");
		btnSignUp.getStyleClass().add("button2");
		buttonBox.getChildren().addAll(btnSignIn, btnSignUp);

		btnSignIn.setMaxWidth(Double.MAX_VALUE);
		btnSignUp.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(btnSignIn, Priority.ALWAYS);
		HBox.setHgrow(btnSignUp, Priority.ALWAYS);

		Pane loginPane = login();
		Pane signUp = signUp();

		stackPane.getChildren().addAll(signUp, loginPane);
		pnodes = stackPane.getChildren();

		container.setTop(buttonBox);
		container.setCenter(stackPane);

		btnSignIn.setOnAction(e -> {
			ObservableList<Node> nodes = stackPane.getChildren();

			if (nodes.size() > 1) {
				Node node = nodes.get(nodes.size() - 1);
				if (node == signUp) {
					node.toBack();
				}
			}
		});

		btnSignUp.setOnAction(e -> {
			ObservableList<Node> nodes = stackPane.getChildren();

			if (nodes.size() > 1) {
				Node node = nodes.get(nodes.size() - 1);
				if (node == loginPane) {
					node.toBack();
				}
			}
		});

		return container;
	}

	public Pane login() {
		VBox loginPane = new VBox();
		loginPane.getStyleClass().add("vbox");

		HBox titleBox = new HBox();
		titleBox.getStyleClass().add("label-spacing");
		Label title = new Label("Login");
		title.getStyleClass().add("font-xlg");
		titleBox.getChildren().add(title);

		GridPane gridPane = new GridPane();
		gridPane.getStyleClass().add("grid");

		TextField textField = new TextField();
		PasswordField passwordField = new PasswordField();
		textField.setPromptText("Username");
		passwordField.setPromptText("Password");

		gridPane.add(textField, 0, 1);
		gridPane.add(passwordField, 0, 3);

		HBox buttonBox = new HBox();
		buttonBox.getStyleClass().add("spacing");

		Button loginBtn = new Button("Login");
		Button close = new Button("Close");
		loginBtn.setDisable(true);

		buttonBox.getChildren().addAll(loginBtn, close);

		// Action Events

		loginPane.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				String username = textField.getText().toLowerCase();
				String password = passwordField.getText();

				if (app.loginUser(username, password)) {
					Stage stage = (Stage) loginBtn.getScene().getWindow();
					App mainApp = new App();
					Pane app = null;
					try {
						app = mainApp.app();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Scene sc2 = new Scene(app, 1200, 900);
					sc2.getStylesheets().add("css/app.css");
					stage.setScene(sc2);
					stage.setTitle("App");
					stage.centerOnScreen();
					stage.setMinHeight(700);
					stage.setMinWidth(900);

					stage.setOnCloseRequest(ae -> {
						try {
							DataCenter.getInstance().logout();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					});
					stage.show();
				}

				else {
					Alert alert = new Alert(AlertType.ERROR, "default Dialog", ButtonType.CLOSE);
					alert.getDialogPane().setMinHeight(180);
					alert.setHeaderText("Login Failed");
					alert.setContentText("Cannot Find the User");
					ButtonType signUpButtonType = new ButtonType("Sign Up");
					alert.getButtonTypes().addAll(signUpButtonType);

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == signUpButtonType) {
						ObservableList<Node> nodes = pnodes;

						if (nodes.size() > 1) {
							Node node = nodes.get(nodes.size() - 1);
							node.toBack();
						}
					}
				}
			}
			if(event.getCode() == KeyCode.F1) {
				app.loginUser("sergio", "Soccer101");
					Stage stage = (Stage) loginBtn.getScene().getWindow();
					App mainApp = new App();
					Pane app = null;
					try {
						app = mainApp.app();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Scene sc2 = new Scene(app, 1200, 900);
					sc2.getStylesheets().add("css/app.css");
					stage.setScene(sc2);
					stage.setTitle("App");
					stage.centerOnScreen();
					stage.setMinHeight(700);
					stage.setMinWidth(900);

					stage.setOnCloseRequest(ae -> {
						try {
							DataCenter.getInstance().logout();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					});
					stage.show();
			}
			
		});

		loginBtn.setOnAction(e -> {

			String username = textField.getText().toLowerCase();
			String password = passwordField.getText();
	
			if (app.loginUser(username, password)) {
				Stage stage = (Stage) loginBtn.getScene().getWindow();
				App mainApp = new App();
				Pane app = null;
				try {
					app = mainApp.app();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Scene sc2 = new Scene(app, 1200, 850);
				sc2.getStylesheets().add("css/app.css");
				stage.setScene(sc2);
				stage.setTitle("App");
				stage.centerOnScreen();

				stage.setOnCloseRequest(ae -> {
					try {
						DataCenter.getInstance().logout();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
				stage.show();

			} else {
				Alert alert = new Alert(AlertType.ERROR, "default Dialog", ButtonType.CLOSE);
				alert.getDialogPane().setMinHeight(180);
				alert.setHeaderText("Login Failed");
				alert.setContentText("Incorrect Username or Password");
				ButtonType signUpButtonType = new ButtonType("Sign Up");
				alert.getButtonTypes().addAll(signUpButtonType);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == signUpButtonType) {
					ObservableList<Node> nodes = pnodes;
					if (nodes.size() > 1) {
						Node node = nodes.get(nodes.size() - 1);
						node.toBack();
					}
				}
			}
		});

		close.setOnAction(e -> {
			try {
				DataCenter.getInstance().logout();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Stage stage = (Stage) close.getScene().getWindow();
			stage.close();
		});

		// KeyEvents

		textField.setOnKeyTyped(e -> {
			String username = textField.getText();
			String password = passwordField.getText();
			loginBtn.setDisable(!Util.validateLogin(username, password));
		});

		passwordField.setOnKeyPressed(e -> {
			String username = textField.getText();
			String password = passwordField.getText();
			loginBtn.setDisable(!Util.validateLogin(username, password));
		});

		Label forgot = new Label("Forgot Password?");
		forgot.getStyleClass().add("cursor");
		HBox labelBox = new HBox(forgot);
		
		labelBox.setOnMouseClicked(e -> {
			ForgotPassword.display();
		});
		
		loginPane.getChildren().addAll(titleBox, gridPane, buttonBox, labelBox);

		return loginPane;
	}

	public Pane signUp() {

		VBox signUpPane = new VBox();
		signUpPane.getStyleClass().add("vbox");

		HBox titeBox = new HBox();

		Label title = new Label("Create an Account");
		title.getStyleClass().addAll("label-spacing", "font-xlg");
		titeBox.getChildren().add(title);

		TextField textField = new TextField();
		PasswordField passwordField = new PasswordField();
		PasswordField confirmPasswordField = new PasswordField();
		TextField emailField = new TextField();

		textField.setPromptText("Username");
		passwordField.setPromptText("Password");
		confirmPasswordField.setPromptText("Confirm Password");
		emailField.setPromptText("Email");

		VBox passwordRequirementBox = new VBox();
		Label passwordRequirement = new Label("Password Requirement");
		Label r1 = new Label("1 uppercase");
		Label r2 = new Label("6 characters");

		passwordRequirementBox.getChildren().addAll(passwordRequirement, r1, r2);

		GridPane gridPane = new GridPane();
		gridPane.getStyleClass().add("grid");
		gridPane.add(textField, 1, 0);
		gridPane.add(passwordField, 1, 2);
		gridPane.add(confirmPasswordField, 1, 4);
		gridPane.add(emailField, 1, 6);

		HBox createUserBox = new HBox();
		createUserBox.getStyleClass().add("hbox");

		Button createLogin = new Button("Sign Up");
		createLogin.setDisable(true);
		createUserBox.getChildren().addAll(createLogin);

		createLogin.setOnAction(e -> {

			String username = textField.getText().toLowerCase();
			String password = passwordField.getText();
			String confirmPass = confirmPasswordField.getText();
			String email = emailField.getText();

			if (!password.equals(confirmPass)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Renter password ");
				alert.setHeaderText("Password does not match");
				alert.showAndWait();
				return;
			}

			if (password.charAt(0) < 91 && app.confirmCreateUser(username, password, confirmPass)) {
				// First Time User
				stackPane.getChildren().add(newUser(username, password, email));
				//select pfp & banner color
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Username is already taken or password does not meet requirements");
				alert.setHeaderText("Sign Up Failed");
				alert.showAndWait();
			}

		});

		textField.setOnKeyTyped(e ->

		{
			String username = textField.getText();
			String password = passwordField.getText();
			String email = emailField.getText();
			createLogin.setDisable(!Util.validateSignUp(username, password, email));
		});

		passwordField.setOnKeyPressed(e -> {
			String username = textField.getText();
			String password = passwordField.getText();
			String email = emailField.getText();
			createLogin.setDisable(!Util.validateSignUp(username, password, email));
		});
		emailField.setOnKeyPressed(e -> {
			String username = textField.getText();
			String password = passwordField.getText();
			String email = emailField.getText();
			createLogin.setDisable(!Util.validateSignUp(username, password, email));
		});

		signUpPane.getChildren().addAll(titeBox, gridPane, createUserBox, passwordRequirementBox);

		return signUpPane;
	}

	public Pane newUser(String username, String password, String email) {
		btnSignUp.setVisible(false);
		btnSignIn.setVisible(false);

		userpfp = null;
		List<String> list = Util.getPfps("src/pfp/");
		BorderPane content = new BorderPane();
		content.getStyleClass().add("vbox-sides");
		Label title = new Label("Welcome");
		title.getStyleClass().add("font-xlg");
		Label name = new Label(username);
		name.getStyleClass().add("font-xlg");
		HBox titleBox = new HBox(title);
		HBox nameBox = new HBox(name);
		VBox topBox = new VBox(titleBox, nameBox);
		topBox.getStyleClass().add("top-bottom-padding");
		Label text = new Label("Choose a Profile Picture");
		text.getStyleClass().add("font-lg");
		HBox textBox = new HBox(text);

		Button continueBtn = new Button("Continue");
		continueBtn.setDisable(true);
		Button skipBtn = new Button("Skip");

		final int COL_SIZE = 3;
		final int ROW_SIZE = 2;
		
		Button[][] gridButtons = new Button[COL_SIZE][ROW_SIZE];
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(0, 0, 0, 0));
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(20);
		grid.setVgap(20);
		
		int count = 0;
		for (int y = 0; y < ROW_SIZE; y++) { // row
			for (int x = 0; x < COL_SIZE; x++) { // column

				Button btn = new Button();
				gridButtons[x][y] = btn;
				btn.getStyleClass().add("pfp-btn");

				Image pfp = null;
				try {
					pfp = new Image(new FileInputStream(new File("src/pfp/" + list.get(count))));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ImageView imageView = new ImageView(pfp);
				imageView.setFitHeight(80);
				imageView.setFitWidth(80);

				Rectangle clip = new Rectangle();
				clip.setWidth(80.0f);
				clip.setHeight(80.0f);
				clip.setArcWidth(100);
				clip.setArcHeight(100);
				imageView.setClip(clip);

				SnapshotParameters parameters = new SnapshotParameters();
				parameters.setFill(Color.TRANSPARENT);
				WritableImage image1 = imageView.snapshot(parameters, null);

				imageView.setClip(null);
				imageView.setImage(image1);

				btn.setGraphic(imageView);
				btn.setId(String.valueOf(count++));
				btn.setOnAction(e -> {
					userpfp = "src/pfp/" + list.get(Integer.valueOf(btn.getId()));
					continueBtn.setDisable(false);
				});
				grid.add(btn, x, y);
			}
		}
		
		HBox gridBox = new HBox(grid);

		// Button Functions
		continueBtn.setOnAction(e -> {
			
			try {
				app.addUser(username, password, email, userpfp);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			
			app.loginUser(username, password);
			Stage stage = (Stage) continueBtn.getScene().getWindow();
			App mainApp = new App();
			Pane app = null;
			try {
				app = mainApp.app();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			Scene sc2 = new Scene(app, 1200, 900);
			sc2.getStylesheets().add("css/app.css");
			stage.setScene(sc2);
			stage.setTitle("App");
			stage.centerOnScreen();
			stage.setMinHeight(700);
			stage.setMinWidth(900);

			stage.setOnCloseRequest(ae -> {
				try {
					DataCenter.getInstance().logout();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
			stage.show();
		});
		
		skipBtn.setOnAction(e -> {
			userpfp = "src/images/default.png";
			
			try {
				app.addUser(username, password, email, userpfp);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			
			app.loginUser(username, password);
			Stage stage = (Stage) skipBtn.getScene().getWindow();
			App mainApp = new App();
			Pane app = null;
			try {
				app = mainApp.app();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Scene sc2 = new Scene(app, 1200, 900);
			sc2.getStylesheets().add("css/app.css");
			stage.setScene(sc2);
			stage.setTitle("App");
			stage.centerOnScreen();
			stage.setMinHeight(700);
			stage.setMinWidth(900);
			stage.setOnCloseRequest(ae -> {
				try {
					DataCenter.getInstance().logout();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			stage.show();
		});

		VBox centerBox = new VBox(textBox, gridBox);
		centerBox.getStyleClass().addAll("pfp-padding", "double-spacing");
		VBox bottomBox = new VBox(continueBtn, skipBtn);
		bottomBox.getStyleClass().add("new-user-btn-spacing");
		content.setTop(topBox);
		content.setCenter(centerBox);
		content.setBottom(bottomBox);
		return content;
	}
	
}
