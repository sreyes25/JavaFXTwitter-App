package views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import app_data_centers.DataCenter;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Navagation;

public class App {
	private DataCenter app;
	private static StackPane feed;
	private static Navagation home;
	private static Navagation explore;
	private static Navagation profile;
	private static Navagation post;
	private static Button postBtn;
	private static Button pfpBtn;
	private static Label paneTitle;

	public Pane app() throws FileNotFoundException {

		app = DataCenter.getInstance();
		BorderPane mainBorderPane = new BorderPane();
		mainBorderPane.getStyleClass().add("black");
		BorderPane top = new BorderPane();
		top.getStyleClass().add("black");
		paneTitle = new Label("Home");
		paneTitle.getStyleClass().add("label-app-title");
		HBox appTitleBox = new HBox(paneTitle);
		Button logoutBtn = new Button("Logout");
		logoutBtn.getStyleClass().add("btn-logout");
		HBox btnBox = new HBox(logoutBtn);
		btnBox.getStyleClass().addAll("right-alignment-logout");

		Image image = null;
		String pfp = app.getUserDetails().getAccount().getProfileImage();

		try {
			image = new Image(new FileInputStream(new File(pfp)));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ImageView imageView = new ImageView(image);
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
		WritableImage roundImg = imageView.snapshot(parameters, null);

		imageView.setClip(null);
		imageView.setImage(roundImg);

		pfpBtn = new Button();
		pfpBtn.getStyleClass().add("pfp-btn");
		pfpBtn.setGraphic(imageView);

		HBox pfpBox = new HBox(pfpBtn);
		pfpBox.getStyleClass().addAll("pfp-corner");

		pfpBtn.setOnMouseClicked(e -> {
			if (feed.getChildren().size() < 2) {
				profile = new Profile(feed);
				feed.getChildren().add(((Profile) profile).accountSettings());
			} else {
				feed.getChildren().remove(1);
			}
		});

		logoutBtn.setOnAction(e -> {
			try {
				app.logout();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			Stage stage = (Stage) logoutBtn.getScene().getWindow();
			stage.close();

			Stage stage1 = (Stage) logoutBtn.getScene().getWindow();
			Login login = new Login();
			Pane root = login.display();
			Scene sc2 = new Scene(root, 400, 550);
			sc2.getStylesheets().add("css/login.css");
			stage1.setScene(sc2);
			stage1.setTitle("Login");
			stage.setMinHeight(550);
			stage.setMinWidth(400);
			stage1.setOnCloseRequest(ae -> {
			});
			stage1.show();
		});
		top.setLeft(pfpBox);
		top.setCenter(appTitleBox);
		top.setRight(btnBox);

		mainBorderPane.setTop(top);
		BorderPane appBorderPane = new BorderPane();
		appBorderPane.setCenter(feed());
		appBorderPane.setLeft(navigation());
		mainBorderPane.setCenter(appBorderPane);

		return mainBorderPane;
	}

	private Pane feed() {

		BorderPane container = new BorderPane();
		container.getStyleClass().add("feed");

		feed = new StackPane();
		home = new Home(feed);
		explore = new Explore(feed);
		profile = new Profile(feed);
		post = new PostMessage(feed);
		/* Pre Load */
		home.addPane("Home");
		profile.addPane("Profile");
		explore.addPane("Explore");

		feed.getChildren().add(home.addPane("Home")); // Default Pane

//		paneTitle = new Label("Home");
//		paneTitle.getStyleClass().add("label-app-title");
//		HBox appTitleBox = new HBox(paneTitle);
//		container.setTop(appTitleBox);
		container.setCenter(feed);
		return container;
	}

	private VBox navigation() throws FileNotFoundException {
		VBox content = new VBox();
		content.getStyleClass().add("vbox-sidePanel");

		String[] itemName = { "Home", "Explore", /* "Notifications", "Messages", "Bookmarks", */ "Profile" };
		Navagation[] paneNode = { home, explore, profile };

		for (int i = 0; i < paneNode.length; i++) {
			content.getChildren().add(navItems(paneNode[i], itemName[i]));
		}

		postBtn = new Button("Post");
		postBtn.getStyleClass().add("btn-post-nav");
		postBtn.setOnAction(e -> {
			feed.getChildren().add(post.addPane("Post"));
			postBtn.setDisable(true);
		});
		HBox postBox = new HBox(postBtn);
		content.getChildren().add(postBox);
		return content;
	}

	private HBox navItems(Navagation navItem, String name) throws FileNotFoundException {
		HBox content = new HBox();
		Button navButton = new Button(name);

		navButton.setOnAction(e -> {
			paneTitle.setText(name);
			postBtn.setDisable(false);
			feed.getChildren().add(navItem.addPane(name));
			feed.getChildren().remove(0);
		});

		content.getChildren().addAll(navButton);
		return content;
	}

	public static void setPfp(Image image) {
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(80);
		imageView.setFitWidth(80);
		imageView.isPreserveRatio();

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
		pfpBtn.setGraphic(imageView);
	}

	public static void setDisablePostBtn(boolean setDisable) {
		postBtn.setDisable(setDisable);
	}

	public static StackPane getFeed() {
		return feed;
	}

	public static void goBack() {
		feed.getChildren().remove(feed.getChildren().size() - 1);
	}

	public static void setFeed(Pane pane) {
		feed.getChildren().add(pane);
	}

}
