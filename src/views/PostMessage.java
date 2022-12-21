package views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import app_data_centers.DataCenter;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.Navagation;
import model.Posts;
import view_extention.DictionaryScene;

public class PostMessage extends Navagation {

	private DataCenter app;

	public PostMessage(StackPane feed) {
		super(feed);
		app = DataCenter.getInstance();
	}

	public Pane post() {
		BorderPane contentBP = new BorderPane();
		contentBP.getStyleClass().addAll("post", "padding-sides");

		Button exitBtn = new Button("X");
		exitBtn.getStyleClass().add("btn-exit");
		HBox btnBox = new HBox(exitBtn);
		btnBox.getStyleClass().addAll("no-vertical-alinment");

		exitBtn.setOnAction(e -> {
			App.goBack();
			App.setDisablePostBtn(false);
		});

		HBox labelBox = new HBox();
		Label label = new Label("Create A New Post");
		label.getStyleClass().add("label-black");
		labelBox.getChildren().add(label);

		VBox topContent = new VBox(btnBox, labelBox);
		TextArea textArea = new TextArea();
		textArea.setPromptText("Hello World;");
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
		Tooltip commentTip = new Tooltip("Check Spelling");
		spellCheck.setTooltip(commentTip);

		HBox spellCheckBox = new HBox(spellCheck);
		spellCheckBox.getStyleClass().addAll("right-alignment");

		VBox centerBox = new VBox(textBox, spellCheckBox);
		centerBox.getStyleClass().add("no-padding");

		HBox buttonBox = new HBox();
		Button post = new Button("Post");
		post.getStyleClass().add("btn-post-pane");
		buttonBox.getChildren().addAll(post);

		contentBP.setTop(topContent);
		contentBP.setCenter(centerBox);
		contentBP.setBottom(buttonBox);

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
			Posts newPost = null;
			String text = textArea.getText();

			try {
				newPost = app.addMessagePost(text);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Home.setHomeContent((PostBoxBuilder.createPost(newPost)));
			Profile.setMyPostContent(PostBoxBuilder.createPost(newPost));
			Explore.setExploreContent(PostBoxBuilder.createPost(newPost));
			textArea.clear();
			App.goBack();
			App.setDisablePostBtn(false);
		});

		BorderPane container = new BorderPane();
		container.getStyleClass().add("content-post");
		Pane marginTop = new Pane();
		marginTop.getStyleClass().addAll("margin-top-bottom");
		Pane marginRight = new Pane();
		marginRight.getStyleClass().addAll("margin-right-left");
		Pane marginBottom = new Pane();
		marginBottom.getStyleClass().addAll("margin-top-bottom");
		Pane marginLeft = new Pane();
		marginLeft.getStyleClass().addAll("margin-right-left");

		container.setTop(marginTop);
		container.setRight(marginRight);
		container.setBottom(marginBottom);
		container.setLeft(marginLeft);
		container.setCenter(contentBP);

		return container;
	}

	public Pane editPost(Posts post) {
		BorderPane contentBP = new BorderPane();
		contentBP.getStyleClass().addAll("post", "padding-sides");

		Label cancel = new Label("Cancel");
		cancel.getStyleClass().addAll("black-label-clicking");
		HBox exitHBox = new HBox(cancel);
		exitHBox.getStyleClass().addAll("no-vertical-alinment");

		cancel.setOnMouseClicked(e -> {
			App.goBack();
			App.setDisablePostBtn(false);
		});

		HBox labelBox = new HBox();
		Label label = new Label("Edit Post");
		label.getStyleClass().add("label-black");
		labelBox.getChildren().add(label);

		VBox topContent = new VBox(exitHBox, labelBox);
		TextArea textArea = new TextArea(post.getMessage());
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
		// checkImageView.setPreserveRatio(true);

		Button spellCheck = new Button();
		spellCheck.getStyleClass().add("check-btn");
		spellCheck.setGraphic(checkImageView);
		Tooltip commentTip = new Tooltip("Check Spelling");
		spellCheck.setTooltip(commentTip);

		HBox spellCheckBox = new HBox(spellCheck);
		spellCheckBox.getStyleClass().addAll("right-alignment");

		VBox centerBox = new VBox(textBox, spellCheckBox);
		centerBox.getStyleClass().add("no-padding");

		HBox buttonBox = new HBox();
		Button save = new Button("Save");
		save.getStyleClass().add("btn-post-pane");
		buttonBox.getChildren().addAll(save);

		contentBP.setTop(topContent);
		contentBP.setCenter(centerBox);
		contentBP.setBottom(buttonBox);

		spellCheck.setDisable(true);
		save.setDisable(true);
		textArea.setOnKeyTyped(e -> {
			save.setDisable(!(textArea.getLength() > 0));
			spellCheck.setDisable(!(textArea.getLength() > 0));
		});
		textArea.setOnKeyPressed(e -> {
			spellCheck.getStyleClass().remove("check-red-btn");
			spellCheck.getStyleClass().remove("check-green-btn");
			spellCheck.getStyleClass().add("check-btn");
		});

		spellCheck.setOnAction(e -> {
			String textCheck = textArea.getText();
			List<String> misspelled = null;
			try {
				misspelled = app.spellCheckPost(textCheck);
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

		save.setOnAction(e -> {

			String editedMessage = textArea.getText();
			post.setMessage(editedMessage);
			textArea.clear();
			App.goBack();
			App.setDisablePostBtn(false);
		});

		BorderPane container = new BorderPane();
		container.getStyleClass().add("content-post");
		Pane marginTop = new Pane();
		marginTop.getStyleClass().addAll("margin-top-bottom");
		Pane marginRight = new Pane();
		marginRight.getStyleClass().addAll("margin-right-left");
		Pane marginBottom = new Pane();
		marginBottom.getStyleClass().addAll("margin-top-bottom");
		Pane marginLeft = new Pane();
		marginLeft.getStyleClass().addAll("margin-right-left");

		container.setTop(marginTop);
		container.setRight(marginRight);
		container.setBottom(marginBottom);
		container.setLeft(marginLeft);
		container.setCenter(contentBP);

		return container;
	}

}
