package views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import app_data_centers.DataCenter;
import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.Navagation;
import model.Posts;
import view_extention.DictionaryScene;

public class Home extends Navagation {
	private StackPane feed;
	private DataCenter app;
	private static VBox homeContent;
	private static Pane home;
	private static int load;

	public Home(StackPane feed) {
		super(feed);
		app = DataCenter.getInstance();
		this.feed = feed;
	}

	public Pane home() {
		BorderPane container = new BorderPane();
		container.getStyleClass().addAll("content", "all-corners");
		homeContent = new VBox();
		homeContent.getStyleClass().addAll("no-padding");

		ScrollPane contentScroll = new ScrollPane();
		contentScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		contentScroll.setVbarPolicy(ScrollBarPolicy.NEVER);
		// Title
		HBox titleBox = new HBox();
		Label title = new Label("");
		title.getStyleClass().add("font-tiny");
		titleBox.getChildren().add(title);

		LinkedList<Posts> myFeed = app.getMyFeed();
		load = myFeed.size();

		int onFirstLoad = 0;
		if (load > 10) {
			onFirstLoad = load - 10;
		}

		for (int i = myFeed.size() - 1; i >= onFirstLoad; i--) {
			homeContent.getChildren().add(PostBoxBuilder.createPost(myFeed.get(i)));

			if (i == onFirstLoad) {
				load = i;
			}
		}

		Button more = new Button("•••");
		if (load > 0) {
			homeContent.getChildren().add(more);
		}

		more.setOnAction(e -> {

			ObservableList<Node> list = homeContent.getChildren();
			homeContent.getChildren().remove(list.size() - 1);

			int onLoad = 0;
			if (load > 10) {
				onLoad = load - 10;
			}

			for (int i = load - 1; i >= onLoad; i--) {
				
				homeContent.getChildren().add(PostBoxBuilder.createPost(myFeed.get(i)));

				load = i;
			}

			if (load == 0) {
				more.setText("No More");
				more.setDisable(true);
			}
			homeContent.getChildren().add(more);
		});

		contentScroll.setContent(homeContent);
		contentScroll.setFitToWidth(true);
		contentScroll.getStyleClass().add("rounded-scroll-pane");

		HBox textBox = new HBox();
		textBox.getStyleClass().addAll("message-box", "spacing", "bottom-corners");

		Button addMedia = new Button("+");
		addMedia.getStyleClass().add("add-media-btn");
		Tooltip imgTip = new Tooltip("Add Image");
		addMedia.setTooltip(imgTip);

		TextField textField = new TextField();
		textField.setPromptText("Message");

		addMedia.setOnMouseClicked(e -> {
			App.setDisablePostBtn(true);
			PostImage post = new PostImage();
			feed.getChildren().add(post.postImage());
		});

		Image check = null;
		try {
			check = new Image(new FileInputStream(new File("src/images/check.png")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ImageView checkImageView = new ImageView(check);
		checkImageView.setFitHeight(25);
		checkImageView.setFitWidth(25);

		Button spellCheck = new Button();
		spellCheck.getStyleClass().add("check-btn");
		spellCheck.setGraphic(checkImageView);
		Tooltip checkTip = new Tooltip("Check Spelling");
		spellCheck.setTooltip(checkTip);

		spellCheck.setDisable(true);
		textField.setOnKeyTyped(e -> {
			if (textField.getLength() == 0) {
				spellCheck.getStyleClass().remove("check-red-btn");
				spellCheck.getStyleClass().remove("check-green-btn");
				spellCheck.getStyleClass().add("check-btn");
			}
			spellCheck.setDisable(!(textField.getLength() > 0));
		});

		textField.setOnKeyPressed(event -> {
			spellCheck.getStyleClass().remove("check-red-btn");
			spellCheck.getStyleClass().remove("check-green-btn");
			spellCheck.getStyleClass().add("check-btn");

			if (event.getCode() == KeyCode.ENTER && !textField.getText().equals("")) {
				Posts post = null;
				try {
					post = app.addMessagePost(textField.getText());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				homeContent.getChildren().add(0, PostBoxBuilder.createPost(post));
				textField.clear();
				spellCheck.getStyleClass().remove("check-red-btn");
				spellCheck.getStyleClass().remove("check-green-btn");
				spellCheck.getStyleClass().add("check-btn");
			}

			if (event.getCode() == KeyCode.SPACE) {
				List<String> misspelled = null;
				try {
					misspelled = app.spellCheckPost(textField.getText());
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				if (misspelled != null) {
					spellCheck.getStyleClass().remove("check-btn");
					spellCheck.getStyleClass().add("check-red-btn");
				} else {
					spellCheck.getStyleClass().remove("check-btn");
					spellCheck.getStyleClass().add("check-green-btn");
				}
			}
			if (event.getCode() == KeyCode.BACK_SPACE) {
				List<String> misspelled = null;
				try {
					misspelled = app.spellCheckPost(textField.getText());
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				if (misspelled != null) {
					spellCheck.getStyleClass().remove("check-btn");
					spellCheck.getStyleClass().add("check-red-btn");
				} else {
					spellCheck.getStyleClass().remove("check-btn");
					spellCheck.getStyleClass().add("check-green-btn");
				}
			}

			else if (!textField.getText().equals("")) {
				PauseTransition pt = new PauseTransition(Duration.seconds(1));
				pt.setOnFinished(e -> {
					spellCheck.getStyleClass().remove("check-red-btn");
					spellCheck.getStyleClass().remove("check-green-btn");
					spellCheck.getStyleClass().add("check-btn");
					List<String> misspelled = null;
					try {
						misspelled = app.spellCheckPost(textField.getText());
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					if (misspelled != null) {
						spellCheck.getStyleClass().remove("check-btn");
						spellCheck.getStyleClass().add("check-red-btn");
					} else {
						spellCheck.getStyleClass().remove("check-btn");
						spellCheck.getStyleClass().add("check-green-btn");
					}
				});
				pt.play();
			}
		});

		spellCheck.setOnAction(e -> {
			/* Spell Check */
			String text = textField.getText();
			List<String> misspelled = null;
			try {
				misspelled = app.spellCheckPost(text);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if (misspelled != null) {
				spellCheck.getStyleClass().add("check-red-btn");
				DictionaryScene.display(misspelled); // display window

			} else {
				spellCheck.getStyleClass().add("check-green-btn");
			}
		});

		textBox.getChildren().addAll(addMedia, textField, spellCheck);
		container.setTop(titleBox);
		container.setCenter(contentScroll);
		container.setBottom(textBox);

		home = container;
		return container;
	}

	public static void setHomeContent(Pane p) {
		homeContent.getChildren().add(0, p);
	}
	public static void deletePost(Pane parent) {
		homeContent.getChildren().remove(parent);
	}

	public static Pane getHome() {
		return home;
	}

}
