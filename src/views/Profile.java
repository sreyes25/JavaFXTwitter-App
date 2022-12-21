package views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;

import app_data_centers.DataCenter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.TableRow;
import model.Navagation;
import model.Posts;
import model.User;

public class Profile extends Navagation {

	private Window stage;
	private StackPane feed;
	private DataCenter app;
	private Label followersLabel;
	private Label followingLabel;
	private TextInputControl nameTextField;
	private TextField passwordTextField;
	private TextInputControl emailTextField;
	private static BorderPane profile;
	private static int load;
	private static VBox myPostContent;
	private static Button pfpBtn;

	public Profile(StackPane feed) {
		super(feed);
		this.feed = feed;
		app = DataCenter.getInstance();
	}

	public Pane profile() {
		BorderPane container = new BorderPane();
		container.getStyleClass().addAll("content", "all-corners");
		
		VBox topBox = new VBox();
		topBox.setSpacing(0);
		topBox.setPadding(new Insets(0, 0, 0, 0));
		
		Color color = app.getUserDetails().getAccount().getProfileBanner().getFXColor();
		topBox.setBackground(
				new Background(new BackgroundFill(color, new CornerRadii(8, 8, 0, 0, false), Insets.EMPTY)));
		;

		VBox colorBox = new VBox();
		
		Circle circle = new Circle(20, 20, 20);
		circle.getStyleClass().add("circ");

		circle.setStroke(javafx.scene.paint.Color.WHITE);
		circle.setStrokeWidth(4);

		ColorPicker colorPicker = new ColorPicker();
		colorPicker.setValue(color);
		colorPicker.setVisible(false);

		colorPicker.setOnAction(e -> {
			topBox.setBackground(new Background(
					new BackgroundFill(colorPicker.getValue(), new CornerRadii(8, 8, 0, 0, false), Insets.EMPTY)));
			;
			circle.setFill(colorPicker.getValue());
			app.getAccountDetails().setProfileBanner(colorPicker.getValue());
			colorPicker.setVisible(false);
		});

		circle.setOnMouseClicked(e -> {
			if(colorPicker.isVisible()) {
				colorPicker.setVisible(false);
			}
			else {
			colorPicker.setVisible(true);
			}
		});

		HBox colorChooser = new HBox(colorPicker, circle);
		colorChooser.setSpacing(10);
		colorChooser.getStyleClass().add("right-alignment");
		colorBox.getChildren().add(colorChooser);

		VBox content = new VBox();
		content.getStyleClass().addAll("no-padding", "content");

		String userPfp = app.getAccountDetails().getProfileImage();
		Image image = null;
		try {
			image = new Image(new FileInputStream(new File(userPfp)));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(110);
		imageView.setFitWidth(110);

		Rectangle clip = new Rectangle();
		clip.setWidth(110.0f);
		clip.setHeight(110.0f);
		clip.setArcWidth(130);
		clip.setArcHeight(130);
		imageView.setClip(clip);

		SnapshotParameters parameters = new SnapshotParameters();
		parameters.setFill(Color.TRANSPARENT);
		WritableImage image1 = imageView.snapshot(parameters, null);

		imageView.setClip(null);
		imageView.setImage(image1);

		pfpBtn = new Button();
		pfpBtn.getStyleClass().add("profile-pfp-btn");

		pfpBtn.setGraphic(imageView);

		// Creating a Group object
		pfpBtn.setOnMouseClicked(e -> {
			feed.getChildren().add(accountSettings());
		});

		HBox pfpBox = new HBox(pfpBtn);
		pfpBox.getStyleClass().addAll("padding-10");
		HBox profileBox = new HBox();
		profileBox.setSpacing(20);
		Label username = new Label("@" + app.getUserDetails().getUsername());

		followingLabel = new Label("Following: " + app.getAccountDetails().getFollowing().size());
		followersLabel = new Label("Followers: " + app.getAccountDetails().getFollowers().size());
		followingLabel.getStyleClass().add("label-click");
		followersLabel.getStyleClass().add("label-click");
		username.getStyleClass().add("label-click");

		profileBox.getChildren().addAll(username, followingLabel, followersLabel);

		// Actions
		followingLabel.setOnMouseClicked(e -> {
			feed.getChildren().add(following());
		});

		followersLabel.setOnMouseClicked(e -> {
			feed.getChildren().add(followers());
		});

		username.setOnMouseClicked(e -> {
			feed.getChildren().add(accountSettings());
		});

		ScrollPane contentScroll = new ScrollPane();
		contentScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		contentScroll.setVbarPolicy(ScrollBarPolicy.NEVER);

		myPostContent = new VBox();
		myPostContent.getStyleClass().addAll("content", "no-padding");

		LinkedList<Posts> myPosts = app.getMyPosts();
		load = myPosts.size();

		int onFirstLoad = 0;
		if (load > 10) {
			onFirstLoad = load - 10;
		}

		for (int i = myPosts.size() - 1; i >= onFirstLoad; i--) {
			myPostContent.getChildren().add(PostBoxBuilder.createPost(myPosts.get(i)));
			if (i == onFirstLoad) {
				load = i;
			}
		}

		Button more = new Button("•••");
		if (load > 0) {
			myPostContent.getChildren().add(more);
		}

		more.setOnAction(e -> {

			ObservableList<Node> list = myPostContent.getChildren();
			myPostContent.getChildren().remove(list.size() - 1);

			int onLoad = 0;
			if (load > 10) {
				onLoad = load - 10;
			}

			for (int i = load - 1; i >= onLoad; i--) {
				myPostContent.getChildren().add(PostBoxBuilder.createPost(myPosts.get(i)));
				load = i;
			}

			if (load == 0) {
				more.setText("No More");
				more.setDisable(true);
			}
			myPostContent.getChildren().add(more);
		});

		contentScroll.setContent(myPostContent);
		contentScroll.setFitToWidth(true);

		content.getChildren().addAll(contentScroll);
		topBox.getChildren().addAll(colorBox, pfpBox, profileBox);

		container.setTop(topBox);

		container.setCenter(content);

		HBox labelBox1 = new HBox();
		Label title1 = new Label("");
		title1.getStyleClass().add("font-tiny");
		labelBox1.getStyleClass().addAll("bottom-corners");
		labelBox1.getChildren().add(title1);
		container.setBottom(labelBox1);
		profile = container;
		return container;

	}

	@SuppressWarnings("unchecked")
	private Pane following() {

		BorderPane content = new BorderPane();
		content.getStyleClass().add("follow-pane");

		Label cancel = new Label("Back");
		cancel.getStyleClass().addAll("white-label-clicking");
		HBox exitHBox = new HBox(cancel);
		exitHBox.getStyleClass().addAll("left-alignment-exit");

		cancel.setOnMouseClicked(e -> {
			App.goBack();
		});

		Label title = new Label("Following");
		title.getStyleClass().add("label-white");
		HBox titleBox = new HBox(title);

		VBox topBox = new VBox();
		topBox.getChildren().addAll(exitHBox, titleBox);

		HBox followingBox = new HBox();

		ObservableList<TableRow> tvData = FXCollections.observableArrayList();
		TableView<TableRow> tv = new TableView<>();
		tv.setPlaceholder(new Label("You are not following anyone"));
		tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tv.setEditable(true);
		tv.setPrefWidth(700);
		TableColumn<TableRow, String> col0 = new TableColumn<>("User");
		col0.setCellValueFactory(new PropertyValueFactory<>("user"));
		TableColumn<TableRow, String> col1 = new TableColumn<>("Action");
		col1.setCellValueFactory(new PropertyValueFactory<>("button"));
		tv.getColumns().addAll(col0, col1);

		TreeMap<String, User> following = app.getAccountDetails().getFollowing();
		Iterator<User> iterator = following.values().iterator();

		while (iterator.hasNext()) {
			User user = iterator.next();
			Label userLb = new Label(user.getUsername());
			userLb.getStyleClass().add("font-sm");
			
			userLb.setOnMouseClicked(e -> {
				User userSerach = app.userSearch(userLb.getText());
				ViewUserProfile viewUser = new ViewUserProfile();
				App.setFeed(viewUser.profile(userSerach));
			});

			TableRow x = new TableRow(userLb);
			tvData.add(x);
			Button button = new Button("Following");
			button.getStyleClass().add("unfollow-dark-btn");
			x.setButton(button);

			x.getButton().setOnAction(e -> {
				if (x.getButton().getText().equals("Following")) {
					x.getButton().setText("Follow");
					x.getButton().getStyleClass().remove("unfollow-dark-btn");
					x.getButton().getStyleClass().add("follow-dark-btn");
					app.unfollowUser(user.getUsername());
					int count = app.getAccountDetails().getFollowing().size();
					followingLabel.setText("Following: " + count);
				} else if (x.getButton().getText().equals("Follow")) {
					app.followUser(user.getUsername());
					x.getButton().setText("Following");
					x.getButton().getStyleClass().remove("follow-dark-btn");
					x.getButton().getStyleClass().add("unfollow-dark-btn");
					int count = app.getAccountDetails().getFollowing().size();
					followingLabel.setText("Following: " + count);
				}
			});
		}

		tv.setItems(tvData);
		followingBox.getChildren().add(tv);
		VBox centerBox = new VBox(followingBox);

		content.setTop(topBox);
		content.setCenter(centerBox);

		BorderPane container = new BorderPane();
		container.getStyleClass().add("content-post");
		Pane marginTop = new Pane();
		marginTop.getStyleClass().addAll("popup-margin-top-bottom");
		Pane marginRight = new Pane();
		marginRight.getStyleClass().addAll("popup-margin-right-left");
		Pane marginBottom = new Pane();
		marginBottom.getStyleClass().addAll("popup-margin-top-bottom");
		Pane marginLeft = new Pane();
		marginLeft.getStyleClass().addAll("popup-margin-right-left");

		container.setTop(marginTop);
		container.setRight(marginRight);
		container.setBottom(marginBottom);
		container.setLeft(marginLeft);
		container.setCenter(content);

		return container;
	}

	@SuppressWarnings("unchecked")
	private Pane followers() {
		BorderPane content = new BorderPane();
		content.getStyleClass().add("follow-pane");

		Label cancel = new Label("Back");
		cancel.getStyleClass().addAll("white-label-clicking");
		HBox exitHBox = new HBox(cancel);
		exitHBox.getStyleClass().addAll("left-alignment-exit");

		cancel.setOnMouseClicked(e -> {
			App.goBack();
		});

		Label title = new Label("Followers");
		title.getStyleClass().add("label-white");
		HBox titleBox = new HBox(title);

		VBox topBox = new VBox();
		topBox.getChildren().addAll(exitHBox, titleBox);

		HBox followerBox = new HBox();

		ObservableList<TableRow> tvData = FXCollections.observableArrayList();

		// Table view
		TableView<TableRow> tv = new TableView<>();
		tv.setPlaceholder(new Label("You have no followers"));
		tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tv.setEditable(true);
		tv.setPrefWidth(700);

		// table colunm
		TableColumn<TableRow, String> col0 = new TableColumn<>("User");
		col0.setCellValueFactory(new PropertyValueFactory<>("user"));
		TableColumn<TableRow, String> col1 = new TableColumn<>("Action");
		col1.setCellValueFactory(new PropertyValueFactory<>("button"));

		tv.getColumns().addAll(col0, col1);
		TreeMap<String, User> following = app.getAccountDetails().getFollowers();
		Iterator<User> iterator = following.values().iterator();

		while (iterator.hasNext()) {
			User user = iterator.next();

			Label userLb = new Label(user.getUsername());
			userLb.getStyleClass().add("font-sm");
			userLb.setOnMouseClicked(e -> {
				User userSerach = app.userSearch(userLb.getText());
				ViewUserProfile viewUser = new ViewUserProfile();
				App.setFeed(viewUser.profile(userSerach));
			});

			TableRow x = new TableRow(userLb);
			tvData.add(x);
			Button button;
			if (app.getAccountDetails().getFollowing().containsKey(user.getUsername())) {
				button = new Button("Following");
				button.getStyleClass().add("unfollow-dark-btn");
			} else {
				button = new Button("Follow");
				button.getStyleClass().add("follow-dark-btn");
			}

			x.setButton(button);

			x.getButton().setOnAction(e -> {
				if (x.getButton().getText().equals("Following")) {
					x.getButton().setText("Follow");
					x.getButton().getStyleClass().remove("unfollow-dark-btn");
					x.getButton().getStyleClass().add("follow-dark-btn");
					app.unfollowUser(user.getUsername());
					int count = app.getAccountDetails().getFollowing().size();
					followingLabel.setText("Following: " + count);
				} else if (x.getButton().getText().equals("Follow")) {
					x.getButton().setText("Following");
					x.getButton().getStyleClass().remove("follow-dark-btn");
					x.getButton().getStyleClass().add("unfollow-dark-btn");
					app.followUser(user.getUsername());
					int count = app.getAccountDetails().getFollowing().size();
					followingLabel.setText("Following: " + count);
				}

			});
		}

		tv.setItems(tvData);

		followerBox.getChildren().add(tv);
		VBox centerBox = new VBox(followerBox);

		content.setTop(topBox);
		content.setCenter(centerBox);

		BorderPane container = new BorderPane();
		container.getStyleClass().add("content-post");

		Pane marginTop = new Pane();
		marginTop.getStyleClass().addAll("popup-margin-top-bottom");
		Pane marginRight = new Pane();
		marginRight.getStyleClass().addAll("popup-margin-right-left");
		Pane marginBottom = new Pane();
		marginBottom.getStyleClass().addAll("popup-margin-top-bottom");
		Pane marginLeft = new Pane();
		marginLeft.getStyleClass().addAll("popup-margin-right-left");

		container.setTop(marginTop);
		container.setRight(marginRight);
		container.setBottom(marginBottom);
		container.setLeft(marginLeft);
		container.setCenter(content);

		return container;
	}

	public Pane accountSettings() {

		BorderPane contentBP = new BorderPane();
		contentBP.getStyleClass().add("follow-pane");
		ScrollPane contentScroll = new ScrollPane();
		contentScroll.setFitToWidth(true);

		VBox content = new VBox();

		Label cancel = new Label("Cancel");
		cancel.getStyleClass().addAll("white-label-clicking");
		HBox exitHBox = new HBox(cancel);
		exitHBox.getStyleClass().addAll("left-alignment-exit");

		cancel.setOnMouseClicked(e -> {
			App.goBack();
		});

		Image image = null;
		String userPfp = app.getUserDetails().getAccount().getProfileImage();

		try {
			image = new Image(new FileInputStream(new File(userPfp)));
		} catch (FileNotFoundException e1) {
			try {
				image = new Image(new FileInputStream(new File("src/images/default.png")));
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}

		
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(110);
		imageView.setFitWidth(110);

		Rectangle clip = new Rectangle();
		clip.setWidth(110.0f);
		clip.setHeight(110.0f);
		clip.setArcWidth(130);
		clip.setArcHeight(130);
		imageView.setClip(clip);

		SnapshotParameters parameters = new SnapshotParameters();
		parameters.setFill(Color.TRANSPARENT);
		WritableImage image1 = imageView.snapshot(parameters, null);

		imageView.setClip(null);
		imageView.setImage(image1);

		Button pfpBtn = new Button();
		pfpBtn.getStyleClass().add("account-pfp-btn");

		pfpBtn.setGraphic(imageView);

		// Creating a Group object
		String dir = System.getProperty("user.dir");
		pfpBtn.setOnMouseClicked(e -> {
			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(new File(dir + "/src/pfp"));
			File selected = fc.showOpenDialog(stage);
			if (selected != null) {
				String newPfp = selected.getPath();
				Image newImage = null;
				try {
					newImage = new Image(new FileInputStream(new File(newPfp)));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				imageView.setImage(newImage);
				imageView.setClip(clip);
				WritableImage roundImg = imageView.snapshot(parameters, null);
				imageView.setClip(null);
				imageView.setImage(roundImg);
				pfpBtn.setGraphic(imageView);
				app.getUserDetails().getAccount().setProfileImage(newPfp);
				App.setPfp(newImage);
				setPfp(newImage);
				
			}
		});

		HBox pfpBox = new HBox(pfpBtn);
		pfpBox.getStyleClass().add("padding");
		Label title = new Label("Edit Profile");
		title.getStyleClass().add("label-white");
		HBox titleBox = new HBox(title);
		titleBox.getStyleClass().add("padding");
		Label name = new Label("Name");
		name.getStyleClass().addAll("label-white", "font-med", "padding-right");
		Label email = new Label("Email");
		email.getStyleClass().addAll("label-white", "font-med", "padding-right");
		Label password = new Label("Password");
		password.getStyleClass().addAll("label-white", "font-med", "padding-right");

		VBox labelBox = new VBox(name, email, password);
		labelBox.getStyleClass().addAll("spacing-double", "left-alignment");

		nameTextField = new TextField();
		nameTextField.setEditable(false);
		nameTextField.setPromptText(app.getUserDetails().getAccount().getName());
		nameTextField.getStyleClass().add("small-text-field");

		nameTextField.setOnMouseClicked(e -> {
			feed.getChildren().add(Username());
		});

		emailTextField = new TextField();
		emailTextField.setEditable(false);
		emailTextField.setPromptText(app.getUserDetails().getEmail());
		emailTextField.getStyleClass().add("small-text-field");

		emailTextField.setOnMouseClicked(e -> {
			feed.getChildren().add(Email());
		});

		passwordTextField = new TextField();
		passwordTextField.setEditable(false);
		String hiddenPassword = "";
		char[] hiddenLength = app.getUserDetails().getPassword().toCharArray();

		for (int i = 0; i < hiddenLength.length; i++) {
			hiddenPassword += "*";
		}

		passwordTextField.setPromptText(hiddenPassword);
		passwordTextField.getStyleClass().add("small-text-field");

		passwordTextField.setOnMouseClicked(e -> {
			feed.getChildren().add(EnterPassword(passwordTextField.getPromptText()));
		});

		VBox textFieldBox = new VBox(nameTextField, emailTextField, passwordTextField);
		textFieldBox.getStyleClass().add("spacing");

		HBox editProfileBox = new HBox(labelBox, textFieldBox);

		contentScroll.setContent(editProfileBox);

		content.getChildren().addAll(contentScroll);
		content.getStyleClass().add("no-vertical-alinment");

		VBox topBox = new VBox();
		topBox.getChildren().addAll(exitHBox, titleBox, pfpBox);

		contentBP.setTop(topBox);
		contentBP.setCenter(content);

		BorderPane container = new BorderPane();
		container.getStyleClass().add("content-post");
		Pane marginTop = new Pane();
		marginTop.getStyleClass().addAll("popup-margin-top-bottom");
		Pane marginRight = new Pane();
		marginRight.getStyleClass().addAll("popup-margin-right-left");
		Pane marginBottom = new Pane();
		marginBottom.getStyleClass().addAll("popup-margin-top-bottom");
		Pane marginLeft = new Pane();
		marginLeft.getStyleClass().addAll("popup-margin-right-left");

		container.setTop(marginTop);
		container.setRight(marginRight);
		container.setBottom(marginBottom);
		container.setLeft(marginLeft);
		container.setCenter(contentBP);
		return container;
	}

	public Pane Username() {
		BorderPane content = new BorderPane();
		content.getStyleClass().add("follow-pane");

		Label cancel = new Label("Back");
		cancel.getStyleClass().addAll("white-label-clicking");
		HBox exitHBox = new HBox(cancel);
		exitHBox.getStyleClass().addAll("left-alignment-exit");

		cancel.setOnMouseClicked(e -> {
			App.goBack();
		});

		Label title = new Label("Edit Name");
		title.getStyleClass().add("label-white");
		HBox titleBox = new HBox(title);

		VBox topBox = new VBox();
		topBox.getChildren().addAll(exitHBox, titleBox);

		Label label = new Label("Change Your Name");
		label.getStyleClass().addAll("label-white");
		HBox labelBox = new HBox(label);

		TextField nameTextField = new TextField();
		nameTextField.setPromptText(app.getUserDetails().getAccount().getName());
		nameTextField.getStyleClass().add("small-text-field");
		HBox textFieldBox = new HBox(nameTextField);

		Button save = new Button("Save");

		save.setOnAction(e -> {
			String newUsername = nameTextField.getText();
				this.nameTextField.setPromptText(newUsername);
				app.changeUsername(newUsername);
				App.goBack();
		
		});

		save.getStyleClass().add("white-btn");
		HBox buttonBox = new HBox(save);
		VBox bottomBox = new VBox(buttonBox);

		VBox centerBox = new VBox(labelBox, textFieldBox);
		centerBox.getStyleClass().addAll("edit-profile-box", "spacing");

		content.setTop(topBox);
		content.setCenter(centerBox);
		content.setBottom(bottomBox);

		BorderPane container = new BorderPane();
		container.getStyleClass().add("transparent");
		Pane marginTop = new Pane();
		marginTop.getStyleClass().addAll("popup-margin-top-bottom");
		Pane marginRight = new Pane();
		marginRight.getStyleClass().addAll("popup-margin-right-left");
		Pane marginBottom = new Pane();
		marginBottom.getStyleClass().addAll("popup-margin-top-bottom");
		Pane marginLeft = new Pane();
		marginLeft.getStyleClass().addAll("popup-margin-right-left");
		container.setTop(marginTop);
		container.setRight(marginRight);
		container.setBottom(marginBottom);
		container.setLeft(marginLeft);
		container.setCenter(content);
		return container;
	}

	public Pane Email() {
		BorderPane content = new BorderPane();
		content.getStyleClass().add("follow-pane");

		Label cancel = new Label("Back");
		cancel.getStyleClass().addAll("white-label-clicking");
		HBox exitHBox = new HBox(cancel);
		exitHBox.getStyleClass().addAll("left-alignment-exit");

		cancel.setOnMouseClicked(e -> {
			App.goBack();
		});

		Label title = new Label("Edit Email");
		title.getStyleClass().add("label-white");
		HBox titleBox = new HBox(title);

		VBox topBox = new VBox();
		topBox.getChildren().addAll(exitHBox, titleBox);

		Label label = new Label("Change Your Email");
		label.getStyleClass().addAll("label-white");
		HBox labelBox = new HBox(label);

		TextField emailTextField = new TextField();
		emailTextField.setPromptText(app.getUserDetails().getEmail());
		emailTextField.getStyleClass().add("small-text-field");
		HBox textFieldBox = new HBox(emailTextField);

		Button save = new Button("Save");
		save.setOnAction(e -> {
			String newEmail = emailTextField.getText();
			this.emailTextField.setPromptText(newEmail);
			app.getUserDetails().setEmail(newEmail);
			App.goBack();
		});

		save.getStyleClass().add("white-btn");
		HBox buttonBox = new HBox(save);
		VBox bottomBox = new VBox(buttonBox);

		VBox centerBox = new VBox(labelBox, textFieldBox);
		centerBox.getStyleClass().addAll("edit-profile-box", "spacing");

		content.setTop(topBox);
		content.setCenter(centerBox);
		content.setBottom(bottomBox);

		BorderPane container = new BorderPane();
		container.getStyleClass().add("transparent");
		Pane marginTop = new Pane();
		marginTop.getStyleClass().addAll("popup-margin-top-bottom");
		Pane marginRight = new Pane();
		marginRight.getStyleClass().addAll("popup-margin-right-left");
		Pane marginBottom = new Pane();
		marginBottom.getStyleClass().addAll("popup-margin-top-bottom");
		Pane marginLeft = new Pane();
		marginLeft.getStyleClass().addAll("popup-margin-right-left");
		container.setTop(marginTop);
		container.setRight(marginRight);
		container.setBottom(marginBottom);
		container.setLeft(marginLeft);
		container.setCenter(content);

		return container;
	}

	private Pane EnterPassword(String hiddenPassword) {
		BorderPane content = new BorderPane();
		content.getStyleClass().add("follow-pane");

		Label cancel = new Label("Back");
		cancel.getStyleClass().addAll("white-label-clicking");
		HBox exitHBox = new HBox(cancel);
		exitHBox.getStyleClass().addAll("left-alignment-exit");

		cancel.setOnMouseClicked(e -> {
			App.goBack();
		});

		Label title = new Label("Edit Password");
		title.getStyleClass().add("label-white");
		HBox titleBox = new HBox(title);

		VBox topBox = new VBox();
		topBox.getChildren().addAll(exitHBox, titleBox);

		Label label = new Label("Enter Password");
		label.getStyleClass().addAll("label-white");
		HBox labelBox = new HBox(label);

		PasswordField passwordTextField = new PasswordField();
		passwordTextField.setPromptText(hiddenPassword);
		passwordTextField.getStyleClass().add("small-text-field");
		HBox textFieldBox = new HBox(passwordTextField);

		Button enter = new Button("Enter");

		enter.setOnAction(e -> {
			String password = passwordTextField.getText();
			if (app.getUserDetails().getPassword().equals(password)) {
				feed.getChildren().add(Password(password));
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Wrong password");
				alert.setHeaderText("Try again");
				alert.showAndWait();
			}
		});

		enter.getStyleClass().add("white-btn");
		HBox buttonBox = new HBox(enter);
		VBox bottomBox = new VBox(buttonBox);

		VBox centerBox = new VBox(labelBox, textFieldBox);
		centerBox.getStyleClass().addAll("edit-profile-box", "spacing");

		content.setTop(topBox);
		content.setCenter(centerBox);
		content.setBottom(bottomBox);

		BorderPane container = new BorderPane();
		container.getStyleClass().add("transparent");
		Pane marginTop = new Pane();
		marginTop.getStyleClass().addAll("popup-margin-top-bottom");
		Pane marginRight = new Pane();
		marginRight.getStyleClass().addAll("popup-margin-right-left");
		Pane marginBottom = new Pane();
		marginBottom.getStyleClass().addAll("popup-margin-top-bottom");
		Pane marginLeft = new Pane();
		marginLeft.getStyleClass().addAll("popup-margin-right-left");
		container.setTop(marginTop);
		container.setRight(marginRight);
		container.setBottom(marginBottom);
		container.setLeft(marginLeft);
		container.setCenter(content);

		return container;
	}

	public Pane Password(String password) {
		BorderPane content = new BorderPane();
		content.getStyleClass().add("follow-pane");

		Label back = new Label("Back");
		back.getStyleClass().addAll("white-label-clicking");
		HBox exitHBox = new HBox(back);
		exitHBox.getStyleClass().addAll("left-alignment-exit");

		back.setOnMouseClicked(e -> {
			App.goBack();
			App.goBack();
		});

		Label title = new Label("Edit Password");
		title.getStyleClass().add("label-white");
		HBox titleBox = new HBox(title);

		VBox topBox = new VBox();
		topBox.getChildren().addAll(exitHBox, titleBox);

		Label label = new Label("Change Your Password");
		label.getStyleClass().addAll("label-white");
		HBox labelBox = new HBox(label);

		TextField passwordTextField = new TextField();
		passwordTextField.setPromptText(password);
		passwordTextField.getStyleClass().add("small-text-field");
		HBox textFieldBox = new HBox(passwordTextField);

		Button save = new Button("Save");
		save.setOnAction(e -> {
			String newPassword = passwordTextField.getText();
			app.getUserDetails().setPassword(newPassword);
			App.goBack();
			App.goBack();
		});

		save.getStyleClass().add("white-btn");
		HBox buttonBox = new HBox(save);
		VBox bottomBox = new VBox(buttonBox);

		VBox centerBox = new VBox(labelBox, textFieldBox);
		centerBox.getStyleClass().addAll("edit-profile-box", "spacing");

		content.setTop(topBox);
		content.setCenter(centerBox);
		content.setBottom(bottomBox);

		BorderPane container = new BorderPane();
		container.getStyleClass().add("transparent");
		Pane marginTop = new Pane();
		marginTop.getStyleClass().addAll("popup-margin-top-bottom");
		Pane marginRight = new Pane();
		marginRight.getStyleClass().addAll("popup-margin-right-left");
		Pane marginBottom = new Pane();
		marginBottom.getStyleClass().addAll("popup-margin-top-bottom");
		Pane marginLeft = new Pane();
		marginLeft.getStyleClass().addAll("popup-margin-right-left");
		container.setTop(marginTop);
		container.setRight(marginRight);
		container.setBottom(marginBottom);
		container.setLeft(marginLeft);
		container.setCenter(content);

		return container;
	}

	public static void setPfp(Image image) {
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(110);
		imageView.setFitWidth(110);

		Rectangle clip = new Rectangle();
		clip.setWidth(110.0f);
		clip.setHeight(110.0f);
		clip.setArcWidth(130);
		clip.setArcHeight(130);
		imageView.setClip(clip);

		SnapshotParameters parameters = new SnapshotParameters();
		parameters.setFill(Color.TRANSPARENT);
		WritableImage image1 = imageView.snapshot(parameters, null);

		imageView.setClip(null);
		imageView.setImage(image1);
		
		pfpBtn.setGraphic(imageView);
	}
	public static void setMyPostContent(Pane p) {
		myPostContent.getChildren().add(0, p);
	}
	
	
	public static void deletePost(Pane parent) {
		myPostContent.getChildren().remove(parent);
	}


	public static Pane getProfile() {
		return profile;
	}

}
