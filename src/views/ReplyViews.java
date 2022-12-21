package views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import app_data_centers.DataCenter;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Posts;
import util.Util;
import view_extention.DictionaryScene;

public class ReplyViews {
	
	private DataCenter app;
	
	public ReplyViews() {
		app = DataCenter.getInstance();
	}
	
	public Pane viewComments(Posts post) {
		BorderPane container = new BorderPane();
		container.getStyleClass().addAll("content", "all-corners");
	
		Button arrow = new Button("Back");
		arrow.getStyleClass().add("arrow");
		Button arrowButton = new Button();
		arrowButton.setGraphic(arrow);
		arrowButton.getStyleClass().addAll("arrow-btn");
		
		Label title = new Label("Replies");
		title.getStyleClass().add("font-xlg");
		HBox titleBox = new HBox(title);
		
		HBox topBox = new HBox(arrowButton,titleBox);
		topBox.getStyleClass().addAll("no-vertical-alinment", "padding-top-left", "spacing");
		
		
		ScrollPane contentScroll = new ScrollPane();
		contentScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		contentScroll.setVbarPolicy(ScrollBarPolicy.NEVER);
		Pane postTopBox = PostBoxBuilder.createTopPostBoxForReplies(post);
		
		VBox repliesBox = new VBox(postTopBox);
		
		LinkedList<Posts> replies = post.getComments();
	
		for (int i = 0; i < replies.size(); i++) {
			Pane repliesMsg = PostBoxBuilder.createRepliesPosts(replies.get(i));
			repliesBox.getChildren().add(repliesMsg);
		}
		
		contentScroll.setContent(repliesBox);
		contentScroll.setFitToWidth(true);
		
		arrowButton.setOnAction(e -> {
			App.goBack();
		});
		
		container.setTop(topBox);
		container.setCenter(contentScroll);
		
		return container;
	}

	public Pane reply(Posts replyTo, int count ,Label commentCount, ImageView commentImageView, Button commentBtn) {
		BorderPane contentBP = new BorderPane();
		contentBP.getStyleClass().addAll("round-all","black", "padding-sides");
		
		Button exitBtn = new Button("X");
		exitBtn.getStyleClass().add("btn-exit-comment");
		HBox btnBox = new HBox(exitBtn);
		btnBox.getStyleClass().addAll("left-alignment-exit");
		
		exitBtn.setOnAction(e -> {
			App.goBack();
			try {
				commentImageView.setImage(new Image(new FileInputStream(new File("src/images/comment.png"))));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			App.setDisablePostBtn(false);
			commentBtn.setId("comment");
		});

		Label user = new Label("@" + replyTo.getUsername());
		user.getStyleClass().add("label-white");
		Label time = new Label(" · " + Util.getPostTime(replyTo.getTimestamp()));
		time.setAlignment(Pos.CENTER_LEFT);
		time.getStyleClass().add("label-white");
		Tooltip date = new Tooltip(Util.getPostDate(replyTo.getTimestamp()));
		time.setTooltip(date);

		Label message = new Label(replyTo.getMessage());
		message.getStyleClass().add("label-white");
		HBox messageBox = new HBox(message);

		HBox top = new HBox(user, time);
		VBox postBox = new VBox(top, messageBox);
		top.getStyleClass().addAll("padding-content", "no-vertical-alinment");
		messageBox.getStyleClass().addAll("no-vertical-alinment", "padding-content");

		VBox topContent = new VBox(btnBox, postBox);
		topContent.getStyleClass().addAll("padding-bottom");
		
		TextArea textArea = new TextArea();
		textArea.setPromptText("Reply");
		HBox textBox = new HBox(textArea);
		textBox.getStyleClass().addAll("padding-text-area");
		
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
		//checkImageView.setPreserveRatio(true);
		
		Button spellCheck = new Button();
		spellCheck.getStyleClass().add("check-btn");
		spellCheck.setGraphic(checkImageView);
		Tooltip checkTip = new Tooltip("Check Spelling");
		spellCheck.setTooltip(checkTip);
		
		HBox spellCheckBox = new HBox(spellCheck);
		spellCheckBox.getStyleClass().addAll("padding-text-area-spell-check");
		
		VBox centerContent = new VBox(textBox, spellCheckBox);
		centerContent.getStyleClass().add("no-padding");
		
		
		HBox bottomBox = new HBox();
		Button post = new Button("Post");
		post.getStyleClass().add("btn-post-pane");
		bottomBox.getChildren().add(post);
		
		
		contentBP.setTop(topContent);
		contentBP.setCenter(centerContent);
		contentBP.setBottom(bottomBox);
		
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
			if(misspelled != null) {
			spellCheck.getStyleClass().add("check-red-btn");
			
			DictionaryScene.display(misspelled); //display window
			
			}else {
			spellCheck.getStyleClass().add("check-green-btn");
			}
		});

		post.setOnAction(e -> {
			int newCount = count;
			newCount++;
			commentCount.setText(String.valueOf(newCount));
		 
			Posts reply = null;
			try {
				reply = app.addReply(replyTo, textArea.getText());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Home.setHomeContent((PostBoxBuilder.createPost(reply)));
			Profile.setMyPostContent(PostBoxBuilder.createPost(reply));
			Explore.setExploreContent(PostBoxBuilder.createPost(reply));
			textArea.clear();
			App.setDisablePostBtn(false);
			App.goBack();
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
