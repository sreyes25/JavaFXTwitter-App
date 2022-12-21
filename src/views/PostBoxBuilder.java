package views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

import app_data_centers.DataCenter;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Posts;
import model.User;
import util.Util;

public class PostBoxBuilder {
	private static DataCenter app = DataCenter.getInstance();

	public static Pane createPost(Posts posts) {
		VBox reply = null;
		VBox postBox = new VBox();
		String username = posts.getUsername();

		if (posts.getReplyingToPost() != null) {
			Posts replyingToPost = posts.getReplyingToPost();
			String replyingToUsername = replyingToPost.getUsername();
			
			reply = new VBox();
			HBox repliedBox = new HBox();
			HBox replyingToBox = new HBox();
			HBox replyingToMessageBox = new HBox();

			Label replied = new Label(username + " replied to");
			replied.getStyleClass().add("label-replied");

			repliedBox.getChildren().add(replied);
			repliedBox.getStyleClass().addAll("padding-content", "no-vertical-alinment");

			Label replyingToName = new Label("@" + replyingToUsername);
			replyingToName.getStyleClass().add("label-click");
			HBox replyingToNameBox = new HBox(replyingToName);
			Label replyTotime = new Label(" ·  " + Util.getPostTime(replyingToPost.getTimestamp()));
			HBox replyTotimeTimeBox = new HBox(replyTotime);
			replyTotime.setAlignment(Pos.CENTER_LEFT);
			Tooltip date = new Tooltip(Util.getPostDate(replyingToPost.getTimestamp()));
			replyTotime.setTooltip(date);
			
			replyingToBox.getChildren().addAll(pfp(replyingToUsername), replyingToNameBox, replyTotimeTimeBox);
			replyingToBox.getStyleClass().addAll("padding-content", "no-vertical-alinment");

			replyingToBox.setOnMouseClicked(e -> {
				if (!replyingToUsername.equals(app.getUserDetails().getUsername())) {
					User user = app.userSearch(replyingToUsername);
					ViewUserProfile viewUser = new ViewUserProfile();
					App.setFeed(viewUser.profile(user));
				}
			});

			Label replyingToMessage = new Label(replyingToPost.getMessage());
			replyingToMessage.getStyleClass().add("padding-left");
			replyingToMessageBox.getChildren().add(replyingToMessage);
			replyingToMessageBox.getStyleClass().addAll("no-vertical-alinment", "reply-padding");
			
			replyingToMessageBox.setOnMouseClicked(e -> {
				ReplyViews viewReplies = new ReplyViews();
				App.setFeed(viewReplies.viewComments(replyingToPost));
			});

			reply.getChildren().addAll(repliedBox, replyingToBox, replyingToMessageBox);
			
			if (replyingToPost.getImage() != null) {
				reply.getChildren().addAll(createImageBox(replyingToPost));
			}
			reply.getStyleClass().addAll("reply-postbox");
			postBox.getChildren().add(reply);
			
		}

		Label name = new Label("@" + username);
		HBox nameBox = new HBox(name);
		name.getStyleClass().add("label-click");
		Label time = new Label(" ·  " + Util.getPostTime(posts.getTimestamp()));
		HBox timeBox = new HBox(time);
		time.setAlignment(Pos.CENTER_LEFT);
		Tooltip date = new Tooltip(Util.getPostDate(posts.getTimestamp()));
		time.setTooltip(date);

		HBox top = new HBox(pfp(username), nameBox, timeBox);

		if (!username.equals(app.getUserDetails().getUsername())) {
			Button userAction = new Button();
			time.setText(time.getText()+"  · ");
			
			if (app.getUserDetails().getAccount().getFollowing().containsKey(username)) {
				userAction.setText("Following");
				userAction.getStyleClass().add("post-following-btn");
			} else {
				userAction.setText("Follow");
				userAction.getStyleClass().add("post-follow-btn");
			}
	
			userAction.setOnAction(e ->{
				if(userAction.getText().equals("Following")) {
					app.unfollowUser(username);
					userAction.setText("Follow");
					userAction.getStyleClass().remove("post-following-btn");
					userAction.getStyleClass().add("post-follow-btn");
					
				}else {
					app.followUser(username);
					userAction.setText("Following");
					userAction.getStyleClass().remove("post-follow-btn");
					userAction.getStyleClass().add("post-following-btn");
				}
			});
			
			HBox box = new HBox(userAction);
			top.getChildren().add(box);

		}

		VBox topBox = new VBox(top);
		topBox.getStyleClass().add("center");
		Label message = new Label(posts.getMessage());
		message.getStyleClass().add("padding-left");
		HBox messageBox = new HBox(message);

		postBox.getChildren().addAll(topBox, messageBox);

		if (posts.getImage() != null) {
			postBox.getChildren().addAll(createImageBox(posts));
		}

		HBox bottom = bottomButtons(posts);

		top.setOnMouseClicked(e -> {
			if (!username.equals(app.getUserDetails().getUsername())) {
				User user = app.userSearch(username);
				ViewUserProfile viewUser = new ViewUserProfile();
				App.setFeed(viewUser.profile(user));
			}
		});
		messageBox.setOnMouseClicked(e -> {
					ReplyViews viewReplies = new ReplyViews();
					App.setFeed(viewReplies.viewComments(posts));
				});
		postBox.getChildren().addAll(bottom);

		top.getStyleClass().addAll("padding-content", "no-vertical-alinment");
		messageBox.getStyleClass().addAll("no-vertical-alinment", "padding-content");
		bottom.getStyleClass().addAll("padding-bottom", "spacing-tripel");
		postBox.getStyleClass().addAll("global-post");

		

		return postBox;
	}

	public static Pane pfp(String username) {
		User user = app.userSearch(username);
		String pfp = user.getAccount().getProfileImage();

		Image image = null;
		try {
			image = new Image(new FileInputStream(new File(pfp)));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			try {
				image = new Image(new FileInputStream(new File("src/images/default.png")));
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			//e1.printStackTrace();
		}

		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(40);
		imageView.setFitWidth(40);
		imageView.isPreserveRatio();

		Rectangle clip = new Rectangle();
		clip.setWidth(40.0f);
		clip.setHeight(40.0f);
		clip.setArcWidth(40);
		clip.setArcHeight(40);
		imageView.setClip(clip);

		SnapshotParameters parameters = new SnapshotParameters();
		parameters.setFill(Color.TRANSPARENT);
		WritableImage image1 = imageView.snapshot(parameters, null);

		imageView.setClip(null);
		imageView.setEffect(new DropShadow(2, new Color(.3, .3, .3, 1)));
		imageView.setImage(image1);

		imageView.setOnMouseClicked(e -> {
			if (!username.equals(app.getUserDetails().getUsername())) {
				ViewUserProfile viewUser = new ViewUserProfile();
				App.setFeed(viewUser.profile(user));
			}
		});

		HBox imageBox = new HBox(imageView);
		return imageBox;
	}

	public static Pane createImageBox(Posts posts) {
		Image image = null;
		try {
			image = new Image(new FileInputStream(new File(posts.getImage())));
		} catch (FileNotFoundException e1) {
			try {
				image = new Image(new FileInputStream("src/images/img.png"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(350);
		imageView.setFitWidth(350);

		Rectangle clip = new Rectangle();
		clip.setWidth(350.0f);
		clip.setHeight(350.0f);
		clip.setArcWidth(20);
		clip.setArcHeight(20);
		imageView.setClip(clip);

		SnapshotParameters parameters = new SnapshotParameters();
		parameters.setFill(Color.TRANSPARENT);
		WritableImage image1 = imageView.snapshot(parameters, null);
		imageView.setClip(null);
		imageView.setEffect(new DropShadow(20, new Color(.3, .3, .3, 1)));
		imageView.setImage(image1);
		HBox imageBox = new HBox(imageView);

		return imageBox;
	}

	public static Pane createTopPostBoxForReplies(Posts posts) { //

		VBox reply = null;
		VBox postBox = new VBox();
		String username = posts.getUsername();

		if (posts.getReplyingToPost() != null) {
			Posts replyingToPost = posts.getReplyingToPost();
			String replyingToUsername = replyingToPost.getUsername();
			
			reply = new VBox();
			HBox repliedBox = new HBox();
			HBox replyingToBox = new HBox();
			HBox replyingToMessageBox = new HBox();

			Label replied = new Label(username + " replied to");
			replied.getStyleClass().add("label-replied");

			repliedBox.getChildren().add(replied);
			repliedBox.getStyleClass().addAll("padding-content", "no-vertical-alinment");

			Label replyingToName = new Label("@" + replyingToUsername);
			replyingToName.getStyleClass().add("label-click");
			HBox replyingToNameBox = new HBox(replyingToName);
			Label replyTotime = new Label(" ·  " + Util.getPostTime(replyingToPost.getTimestamp()));
			HBox replyTotimeTimeBox = new HBox(replyTotime);
			replyTotime.setAlignment(Pos.CENTER_LEFT);
			Tooltip date = new Tooltip(Util.getPostDate(replyingToPost.getTimestamp()));
			replyTotime.setTooltip(date);
			
			replyingToBox.getChildren().addAll(pfp(replyingToUsername), replyingToNameBox, replyTotimeTimeBox);
			replyingToBox.getStyleClass().addAll("padding-content", "no-vertical-alinment");

			replyingToBox.setOnMouseClicked(e -> {
				if (!replyingToUsername.equals(app.getUserDetails().getUsername())) {
					User user = app.userSearch(replyingToUsername);
					ViewUserProfile viewUser = new ViewUserProfile();
					App.setFeed(viewUser.profile(user));
				}
			});

			Label replyingToMessage = new Label(replyingToPost.getMessage());
			replyingToMessage.getStyleClass().add("padding-left");
			replyingToMessageBox.getChildren().add(replyingToMessage);
			replyingToMessageBox.getStyleClass().addAll("no-vertical-alinment", "reply-padding");

			replyingToMessageBox.setOnMouseClicked(e -> {
				ReplyViews viewReplies = new ReplyViews();
				App.setFeed(viewReplies.viewComments(replyingToPost));
			});
			
			reply.getChildren().addAll(repliedBox, replyingToBox, replyingToMessageBox);
			if (replyingToPost.getImage() != null) {
				reply.getChildren().addAll(createImageBox(replyingToPost));
			}
			reply.getStyleClass().addAll("reply-postbox");
			postBox.getChildren().add(reply);
			
		}

		Label name = new Label("@" + username);
		HBox nameBox = new HBox(name);
		name.getStyleClass().add("label-click");
		Label time = new Label(" · " + Util.getPostTime(posts.getTimestamp()));
		HBox timeBox = new HBox(time);
		time.setAlignment(Pos.CENTER_LEFT);
		Tooltip date = new Tooltip(Util.getPostDate(posts.getTimestamp()));
		time.setTooltip(date);

		HBox top = new HBox(pfp(username), nameBox, timeBox);
		VBox topBox = new VBox(top);
		topBox.getStyleClass().add("center");
		Label message = new Label(posts.getMessage());
		HBox messageBox = new HBox(message);

		postBox.getChildren().addAll(topBox, messageBox);

		if (posts.getImage() != null) {
			Image image = null;
			try {
				image = new Image(new FileInputStream(new File(posts.getImage())));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			ImageView imageView = new ImageView(image);
			imageView.setFitHeight(350);
			imageView.setFitWidth(350);

			Rectangle clip = new Rectangle();
			clip.setWidth(350.0f);
			clip.setHeight(350.0f);
			clip.setArcWidth(20);
			clip.setArcHeight(20);
			imageView.setClip(clip);

			SnapshotParameters parameters = new SnapshotParameters();
			parameters.setFill(Color.TRANSPARENT);
			WritableImage image1 = imageView.snapshot(parameters, null);

			imageView.setClip(null);
			imageView.setEffect(new DropShadow(20, new Color(.3, .3, .3, 1)));
			imageView.setImage(image1);

			HBox imageBox = new HBox(imageView);
			VBox imageVBox = new VBox(imageBox);
			postBox.getChildren().add(imageVBox);
		}

		HBox bottom = bottomButtons(posts);
		postBox.getChildren().add(bottom);

		top.getStyleClass().addAll("padding-content", "no-vertical-alinment");
		messageBox.getStyleClass().addAll("no-vertical-alinment", "padding-content");
		bottom.getStyleClass().addAll("padding-bottom", "spacing-tripel");
		postBox.getStyleClass().addAll("overlay", "padding-all");

		return postBox;
	}

	public static Pane createRepliesPosts(Posts posts) {

		String username = posts.getUsername();

		Label name = new Label("@" + username);
		HBox nameBox = new HBox(name);
		name.getStyleClass().add("label-click");
		Label time = new Label(" · " + Util.getPostTime(posts.getTimestamp()));
		HBox timeBox = new HBox(time);
		time.setAlignment(Pos.CENTER_LEFT);
		Tooltip date = new Tooltip(Util.getPostDate(posts.getTimestamp()));
		time.setTooltip(date);

		HBox top = new HBox(pfp(username), nameBox, timeBox);
		VBox topBox = new VBox(top);

		Label message = new Label(posts.getMessage());
		HBox messageBox = new HBox(message);

		top.setOnMouseClicked(e -> {
			if (!username.equals(app.getUserDetails().getUsername())) {
				User user = app.userSearch(username);
				ViewUserProfile viewUser = new ViewUserProfile();
				App.setFeed(viewUser.profile(user));
			}
		});
		
		messageBox.setOnMouseClicked(e -> {
					ReplyViews viewReplies = new ReplyViews();
					App.setFeed(viewReplies.viewComments(posts));
		});

		HBox bottomBox = bottomButtons(posts);
		VBox replyBox = new VBox(topBox, messageBox, bottomBox);

		top.getStyleClass().addAll("padding-content", "no-vertical-alinment");
		messageBox.getStyleClass().addAll("no-vertical-alinment", "padding-content");
		bottomBox.getStyleClass().addAll("padding-bottom", "spacing-tripel");
		replyBox.getStyleClass().addAll("padding-replies");

		return replyBox;

	}

	public static HBox bottomButtons(Posts posts) {
		int likes = 0;
		Image like = null;
		boolean isLiked = app.isLiked(posts);
		String likeId = "";

		if (isLiked == false) {
			likeId = "unliked";
			try {
				like = new Image(new FileInputStream(new File("src/images/like.png")));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			likeId = "liked";
			try {
				like = new Image(new FileInputStream(new File("src/images/unlike.png")));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		ImageView likeImageView = new ImageView(like);
		likeImageView.setFitHeight(30);
		likeImageView.setFitWidth(30);
		likeImageView.setPreserveRatio(true);

		Image comment = null;
		try {
			comment = new Image(new FileInputStream(new File("src/images/comment.png")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ImageView commentImageView = new ImageView(comment);
		commentImageView.setFitHeight(30);
		commentImageView.setFitWidth(30);
		commentImageView.setPreserveRatio(true);

		Button likeBtn = new Button();

		likeBtn.setId(likeId);
		Tooltip isLikedTooltip = new Tooltip("Like");
		likeBtn.setTooltip(isLikedTooltip);
		likeBtn.getStyleClass().add("like-btn");
		likeBtn.setGraphic(likeImageView);
		likes = Util.getLikeCount(posts);
		Label likeCount = new Label(String.valueOf(likes));

		likeBtn.setOnAction(e -> {
			int count = Integer.valueOf(likeCount.getText());

			if (likeBtn.getId().equals("unliked")) {
				try {
					likeImageView.setImage(new Image(new FileInputStream(new File("src/images/unlike.png"))));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				app.likePost(posts);
				count++;
				likeCount.setText(String.valueOf(count));
				likeBtn.setId("liked");
				isLikedTooltip.setText("Unlike");

			} else if (likeBtn.getId().equals("liked")) {
				try {
					likeImageView.setImage(new Image(new FileInputStream(new File("src/images/like.png"))));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				app.unlikePost(posts);
				count--;
				likeCount.setText(String.valueOf(count));
				likeBtn.setId("unliked");
				isLikedTooltip.setText("Like");
			}
		});

		Button commentBtn = new Button();
		commentBtn.getStyleClass().add("comment-btn");
		commentBtn.setGraphic(commentImageView);
		Tooltip commentTip = new Tooltip("Comment");
		commentBtn.setTooltip(commentTip);
		Label commentCount = new Label(String.valueOf(posts.getComments().size()));
		commentBtn.setId("comment");

		commentBtn.setOnAction(e -> {
			int count = Integer.valueOf(commentCount.getText());
			if (commentBtn.getId().equals("comment")) {
				try {
					commentImageView
							.setImage(new Image(new FileInputStream(new File("src/images/commentClicked.png"))));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				commentBtn.setId("commentClicked");
			}
			App.setDisablePostBtn(true);
			ReplyViews reply = new ReplyViews();
			App.setFeed(reply.reply(posts, count, commentCount, commentImageView, commentBtn));
		});

		Button moreBtn = new Button("•••");
		moreBtn.getStyleClass().add("more-btn");
		moreBtn.setTooltip(new Tooltip("More"));

		ContextMenu contextMenu = new ContextMenu();

		// Creating the menu Items for the context menu
		MenuItem edit = new MenuItem("Edit");
		MenuItem delete = new MenuItem("Delete");
		MenuItem follow = new MenuItem("Follow");
		MenuItem unfollow = new MenuItem("Unfollow");

		edit.setOnAction(e -> {
			PostMessage post = new PostMessage(App.getFeed());
			App.setDisablePostBtn(true);
			App.setFeed(post.editPostPane(posts));
		});

		delete.setOnAction(e -> {

			Alert alert = new Alert(AlertType.ERROR, "default Dialog", ButtonType.CANCEL);
			alert.getDialogPane().setMinHeight(180);
			alert.setHeaderText("Do want to delete this post?");
			alert.setContentText("Delete Post");
			ButtonType confirmDelete = new ButtonType("Delete");
			alert.getButtonTypes().addAll(confirmDelete);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == confirmDelete) {
				app.deletePost(posts);
				Pane postParent = (Pane) moreBtn.getParent().getParent();

				Home.deletePost(postParent);
				Profile.deletePost(postParent);
				Explore.deletePost(postParent);
			}
		});

		follow.setOnAction(e -> {
			app.followUser(posts.getUsername());
			contextMenu.getItems().remove(follow);
			contextMenu.getItems().add(unfollow);
		});
		unfollow.setOnAction(e -> {
			app.unfollowUser(posts.getUsername());
			contextMenu.getItems().remove(unfollow);
			contextMenu.getItems().add(follow);
		});

		if (posts.getUsername().equals(app.getUserDetails().getUsername())) {
			contextMenu.getItems().addAll(edit, delete);
		} else {
			if (app.getUserDetails().getAccount().getFollowing().containsKey(posts.getUsername())) {
				contextMenu.getItems().add(unfollow);
			} else {
				contextMenu.getItems().add(follow);
			}
		}

		moreBtn.setOnMouseClicked(e -> {
			Stage stage = (Stage) commentBtn.getScene().getWindow();
			contextMenu.show(stage, e.getScreenX() + 4, e.getScreenY() + 2);
		});

		HBox likeBox = new HBox(likeCount, likeBtn);
		likeBox.getStyleClass().add("spacing");
		HBox commentBox = new HBox(commentCount, commentBtn);
		commentBox.getStyleClass().add("spacing");

		HBox bottom = new HBox(likeBox, commentBox, moreBtn);
		return bottom;
	}

}
