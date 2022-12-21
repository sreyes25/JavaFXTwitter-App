package views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import app_data_centers.DataCenter;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.Posts;
import view_extention.DictionaryScene;

public class PostImage {

	@SuppressWarnings("unused")
	private StackPane feed;
	private DataCenter app;
	private Window stage;
	private String img = null;

	public PostImage() {
		app = DataCenter.getInstance();
		this.feed = App.getFeed();
	}

	public Pane postImage() {
		BorderPane content = new BorderPane();
		content.getStyleClass().add("post");

		VBox topBox = new VBox();
		topBox.getStyleClass().addAll("content", "post-top-corners");
		Button exitBtn = new Button("X");
		exitBtn.getStyleClass().add("btn-exit");
		HBox btnBox = new HBox(exitBtn);

		HBox labelBox = new HBox();
		Label label = new Label("Post An Image");
		label.getStyleClass().add("label-black");
		labelBox.getChildren().add(label);
		btnBox.getStyleClass().addAll("left-alignment-exit");
		topBox.getChildren().addAll(btnBox, labelBox);

		exitBtn.setOnAction(e -> {
			App.setDisablePostBtn(false);
			App.goBack();
		});

		TextArea textArea = new TextArea();
		textArea.setPrefHeight(70);
		textArea.setPromptText("Caption");
		HBox textBox = new HBox(textArea);
		textBox.getStyleClass().add("horizontal-padding");

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

		HBox spellCheckBox = new HBox(spellCheck);
		spellCheckBox.getStyleClass().addAll("right-alignment");

		HBox postButtonBox = new HBox();
		Button post = new Button("Post Image");
		post.getStyleClass().add("btn-post-pane");
		postButtonBox.getChildren().add(post);

		Image addNew = null;
		try {
			addNew = new Image(new FileInputStream(new File("src/images/img.png")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ImageView addNewView = new ImageView(addNew);
		addNewView.getStyleClass().add("post-image-view");
		addNewView.setFitHeight(200);
		addNewView.setFitWidth(200);

		HBox imageHBox = new HBox(addNewView);
		imageHBox.getStyleClass().addAll("horizontal-padding", "cursor");
		VBox scroll = new VBox(imageHBox);

		// Creating a Group object
		String dir = System.getProperty("user.dir");
		imageHBox.setOnMouseClicked(e -> {
			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(new File(dir));
			File selected = fc.showOpenDialog(stage);
			if (selected != null) {
				img = selected.getPath();
				Image newImage = null;
				try {
					newImage = new Image(new FileInputStream(new File(img)));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				addNewView.setFitHeight(330);
				addNewView.setFitWidth(330);
				addNewView.setPreserveRatio(true);
				addNewView.setImage(newImage);
			}
		});

		ScrollPane contentScroll = new ScrollPane(scroll);
		contentScroll.setFitToWidth(true);

		VBox centerScroll = new VBox(contentScroll);
		centerScroll.getStyleClass().addAll("content", "black");

		VBox bottomBox = new VBox(textBox, spellCheckBox, postButtonBox);
		bottomBox.getStyleClass().add("no-padding");

		content.setTop(topBox);
		content.setCenter(centerScroll);
		content.setBottom(bottomBox);

		spellCheck.setDisable(true);
		post.setDisable(true);
		
		textArea.setOnKeyTyped(e -> {
			post.setDisable(!(textArea.getLength() > 0));
			spellCheck.setDisable(!(textArea.getLength() > 0));
		});
		textArea.setOnKeyPressed(e -> {
			spellCheck.getStyleClass().remove("check-red-btn");
			spellCheck.getStyleClass().remove("check-green-btn");
			spellCheck.getStyleClass().add("check-btn");
		});

		spellCheck.setOnAction(e -> {
			/* Spell Check */
			String text = textArea.getText();
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

		post.setOnAction(e -> {

			if (img != null && !textArea.getText().equals("")) {
				Posts newPost = null;

				try {
					newPost = app.addImagePost(img, textArea.getText());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				Home.setHomeContent(PostBoxBuilder.createPost(newPost));
				Profile.setMyPostContent(PostBoxBuilder.createPost(newPost));
				Explore.setExploreContent(PostBoxBuilder.createPost(newPost));
				textArea.clear();
				img = null;
				App.setDisablePostBtn(false);
				App.goBack();
			}
		});

		BorderPane container = new BorderPane();
		container.getStyleClass().add("content-post");

		Pane marginTop = new Pane();
		marginTop.getStyleClass().addAll("popup-margin-top-bottom");
		Pane marginRight = new Pane();
		marginRight.getStyleClass().addAll("margin-right-left");
		Pane marginBottom = new Pane();
		marginBottom.getStyleClass().addAll("popup-margin-top-bottom");
		Pane marginLeft = new Pane();
		marginLeft.getStyleClass().addAll("margin-right-left");

		container.setTop(marginTop);
		container.setRight(marginRight);
		container.setBottom(marginBottom);
		container.setLeft(marginLeft);
		container.setCenter(content);

		return container;
	}

}
