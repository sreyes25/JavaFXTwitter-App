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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import javafx.scene.shape.Rectangle;
import model.TableRow;
import model.Posts;
import model.User;

public class ViewUserProfile {
	private DataCenter app = DataCenter.getInstance();
	private StackPane feed = App.getFeed();
	private Label followersLabel;
	private Label followingLabel;
	private static VBox usersPostContent;
	private static int load;

	public Pane profile(User user) {
		BorderPane container = new BorderPane();
		container.getStyleClass().addAll("content", "all-corners");
		
		HBox labelBox = new HBox();
		Label back = new Label("Back");
		back.getStyleClass().addAll("label-black-clicking", "font-lg");
		labelBox.getStyleClass().addAll("top-corners", "left-alignment-exit");
		labelBox.getChildren().add(back);

		back.setOnMouseClicked(e -> {
			App.goBack();
		});

		VBox topBox = new VBox();
		topBox.setSpacing(5);
		topBox.setPadding(new Insets(0, 0, 0, 0));
		Color color = user.getAccount().getProfileBanner().getFXColor();
		
		topBox.setBackground(
				new Background(new BackgroundFill(color, new CornerRadii(8, 8, 0, 0, false), Insets.EMPTY)));
		;
		
		VBox content = new VBox();
		content.getStyleClass().addAll("no-padding", "content");

		String userPfp = user.getAccount().getProfileImage();
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

		Button pfpBtn = new Button();
		pfpBtn.getStyleClass().add("profile-pfp-btn");

		pfpBtn.setGraphic(imageView);

		HBox pfpBox = new HBox(pfpBtn);
		HBox profileBox = new HBox();
		profileBox.setSpacing(20);
		Label username = new Label("@" + user.getUsername());
		followingLabel = new Label("Following: " + user.getAccount().getFollowing().size());
		followersLabel = new Label("Followers: " + user.getAccount().getFollowers().size());
		followingLabel.getStyleClass().add("label-click");
		followersLabel.getStyleClass().add("label-click");
		username.getStyleClass().add("label-click");

		profileBox.getChildren().addAll(username, followingLabel, followersLabel);

		// Actions
		followingLabel.setOnMouseClicked(e -> {
			feed.getChildren().add(following(user));
		});

		followersLabel.setOnMouseClicked(e -> {
			feed.getChildren().add(followers(user));
		});

		ScrollPane contentScroll = new ScrollPane();
		contentScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		contentScroll.setVbarPolicy(ScrollBarPolicy.NEVER);

		usersPostContent = new VBox();
		usersPostContent.getStyleClass().addAll("content", "no-padding");

		LinkedList<Posts> posts = app.loadUserPosts(user);

		load = posts.size();
		int onFirstLoad = 0;

		if (load > 13) {
			onFirstLoad = load - (load - 12);
		}

		for (int i = posts.size() - 1; i >= onFirstLoad; i--) {
			usersPostContent.getChildren().add(PostBoxBuilder.createPost(posts.get(i)));
			load = i;
		}

		Button more = new Button("•••");
		if (onFirstLoad != 0) {
			usersPostContent.getChildren().add(more);
		}

		more.setOnAction(e -> {
			ObservableList<Node> list = usersPostContent.getChildren();
			usersPostContent.getChildren().remove(list.size() - 1);

			for (int i = load - 1; i >= 0; i--) {
				usersPostContent.getChildren().add(PostBoxBuilder.createPost(posts.get(i)));
				load = i;
			}
			if (load == 0) {
				more.setText("No More");
			}
			usersPostContent.getChildren().add(more);
		});

		contentScroll.setContent(usersPostContent);
		contentScroll.setFitToWidth(true);

		Button follow = new Button("Follow");
		follow.getStyleClass().add("follow-profile-btn");
		if (app.getAccountDetails().getFollowing().containsKey(user.getUsername())) {
			follow.setText("Following");
			follow.getStyleClass().remove("follow-profile-btn");
			follow.getStyleClass().add("following-profile-btn");
		}

		follow.setOnAction(e -> {
			if (follow.getText().equals("Following")) {
				follow.setText("Follow");
				follow.getStyleClass().remove("following-profile-btn");
				follow.getStyleClass().add("follow-profile-btn");
				app.unfollowUser(user.getUsername());
				int count = user.getAccount().getFollowers().size();
				followersLabel.setText("Followers: " + count);
			} else {
				follow.setText("Following");
				follow.getStyleClass().remove("follow-profile-btn");
				follow.getStyleClass().add("following-profile-btn");
				app.followUser(user.getUsername());
				int count = user.getAccount().getFollowers().size();
				followersLabel.setText("Followers: " + count);
			}
		});

		HBox buttonBox = new HBox(follow);
		buttonBox.getStyleClass().addAll("padding-bottom-one");

		content.getChildren().addAll(contentScroll);

		topBox.getChildren().addAll(labelBox, pfpBox, profileBox, buttonBox);

		container.setTop(topBox);
		container.setCenter(content);

		HBox labelBox1 = new HBox();
		Label title1 = new Label("");
		title1.getStyleClass().add("font-tiny");
		labelBox1.getStyleClass().addAll("bottom-corners");
		labelBox1.getChildren().add(title1);
		container.setBottom(labelBox1);
		return container;

	}

	@SuppressWarnings("unchecked")
	private Pane following(User user) {

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
		tv.setPlaceholder(new Label(user.getUsername() + " is not following any users"));
		tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tv.setEditable(true);
		tv.setPrefWidth(700);
		TableColumn<TableRow, String> col0 = new TableColumn<>("User");
		col0.setCellValueFactory(new PropertyValueFactory<>("user"));
		TableColumn<TableRow, String> col1 = new TableColumn<>("Action");
		col1.setCellValueFactory(new PropertyValueFactory<>("button"));
		tv.getColumns().addAll(col0, col1);

		TreeMap<String, User> following = user.getAccount().getFollowing();
		Iterator<User> iterator = following.values().iterator();

		while (iterator.hasNext()) {

			User user1 = iterator.next();

			Label userLb = new Label(user1.getUsername());
			userLb.getStyleClass().add("font-sm");

			userLb.setOnMouseClicked(e -> {
				if (!userLb.getText().equals(app.getUserDetails().getUsername())) {
					User userSerach = app.userSearch(userLb.getText());
					ViewUserProfile viewUser = new ViewUserProfile();
					App.setFeed(viewUser.profile(userSerach));
				}
			});

			TableRow row = new TableRow(userLb);
			tvData.add(row);

			if (!user1.getUsername().equals(app.getUserDetails().getUsername())) {
				Button button = new Button("Following");
				button.getStyleClass().add("unfollow-dark-btn");
				row.setButton(button);

				row.getButton().setOnAction(e -> {

					if (row.getButton().getText().equals("Following")) {
						row.getButton().setText("Follow");
						row.getButton().getStyleClass().remove("unfollow-dark-btn");
						row.getButton().getStyleClass().add("follow-dark-btn");
						app.unfollowUser(user1.getUsername());
						int count = app.getAccountDetails().getFollowing().size();
						followingLabel.setText("Following: " + count);
					} else if (row.getButton().getText().equals("Follow")) {
						app.followUser(user1.getUsername());
						row.getButton().setText("Following");
						row.getButton().getStyleClass().remove("follow-dark-btn");
						row.getButton().getStyleClass().add("unfollow-dark-btn");
						int count = app.getAccountDetails().getFollowing().size();
						followingLabel.setText("Following: " + count);
					}

				});
			}
			else {
				row.getButton().setVisible(false);
			}
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
	private Pane followers(User user) {
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
		tv.setPlaceholder(new Label(user.getUsername() + " has no followers"));
		tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tv.setEditable(true);
		tv.setPrefWidth(700);

		// table colunm
		TableColumn<TableRow, String> col0 = new TableColumn<>("User");
		col0.setCellValueFactory(new PropertyValueFactory<>("user"));
		TableColumn<TableRow, String> col1 = new TableColumn<>("Action");
		col1.setCellValueFactory(new PropertyValueFactory<>("button"));

		tv.getColumns().addAll(col0, col1);
		TreeMap<String, User> following = user.getAccount().getFollowers();
		Iterator<User> iterator = following.values().iterator();

		while (iterator.hasNext()) {
			User user1 = iterator.next();
			Label userLb = new Label(user1.getUsername());
			userLb.getStyleClass().add("font-sm");
			userLb.setOnMouseClicked(e -> {
				if (!userLb.getText().equals(app.getUserDetails().getUsername())) {
					User userSerach = app.userSearch(userLb.getText());
					ViewUserProfile viewUser = new ViewUserProfile();
					App.setFeed(viewUser.profile(userSerach));
				}
			});

			TableRow row = new TableRow(userLb);
			tvData.add(row);
			if (!user1.getUsername().equals(app.getUserDetails().getUsername())) {
				Button button;
				if (app.getAccountDetails().getFollowing().containsKey(user1.getUsername())) {
					button = new Button("Following");
					button.getStyleClass().add("unfollow-dark-btn");
				} else {
					button = new Button("Follow");
					button.getStyleClass().add("follow-dark-btn");
				}

				row.setButton(button);

				row.getButton().setOnAction(e -> {
					if (row.getButton().getText().equals("Following")) {
						row.getButton().setText("Follow");
						row.getButton().getStyleClass().remove("unfollow-dark-btn");
						row.getButton().getStyleClass().add("follow-dark-btn");
						app.unfollowUser(user1.getUsername());
						int count = app.getAccountDetails().getFollowing().size();
						followingLabel.setText("Following: " + count);
					} else if (row.getButton().getText().equals("Follow")) {
						row.getButton().setText("Following");
						row.getButton().getStyleClass().remove("follow-dark-btn");
						row.getButton().getStyleClass().add("unfollow-dark-btn");
						app.followUser(user1.getUsername());
						int count = app.getAccountDetails().getFollowing().size();
						followingLabel.setText("Following: " + count);
					}
				});
			}
			else {
				row.getButton().setVisible(false);
			}
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
}
