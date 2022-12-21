package views;

import java.util.LinkedList;

import app_data_centers.DataCenter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.TableRow;
import model.Navagation;
import model.Posts;
import model.User;

public class Explore extends Navagation {
	private DataCenter app;
	private static VBox exploreContent;
	private static int load;
	private static Pane explore;

	public Explore(StackPane feed) {
		super(feed);
		app = DataCenter.getInstance();
	}

	public Pane explore() {

		BorderPane container = new BorderPane();
		container.getStyleClass().addAll("content", "all-corners");

		HBox content = new HBox();
		content.setSpacing(5);

		exploreContent = new VBox();
		exploreContent.getStyleClass().addAll("no-padding");
		exploreContent.setPrefWidth(800);
		exploreContent.setMinWidth(200);

		ScrollPane contentScroll = new ScrollPane();
		contentScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		contentScroll.setVbarPolicy(ScrollBarPolicy.NEVER);

		LinkedList<Posts> globalPosts = app.getGlobal();

		load = globalPosts.size();

		int onFirstLoad = 0;
		if (load > 10) {
			onFirstLoad = load - 10;
		}

		for (int i = globalPosts.size() - 1; i >= onFirstLoad; i--) {

			exploreContent.getChildren().add(PostBoxBuilder.createPost(globalPosts.get(i)));

			if (i == onFirstLoad) {
				load = i;
			}
		}

		Button more = new Button("•••");
		if (load > 0) {
			exploreContent.getChildren().add(more);
		}

		more.setOnAction(e -> {
			ObservableList<Node> list = exploreContent.getChildren();
			exploreContent.getChildren().remove(list.size() - 1);

			int onLoad = 0;
			if (load > 10) {
				onLoad = load - 10;
			}

			for (int i = load - 1; i >= onLoad; i--) {
				exploreContent.getChildren().add(PostBoxBuilder.createPost(globalPosts.get(i)));
				load = i;
			}

			if (load == 0) {
				more.setText("No More");
				more.setDisable(true);
			}
			exploreContent.getChildren().add(more);
		});

		contentScroll.setContent(exploreContent);
		contentScroll.setFitToWidth(true);
		contentScroll.getStyleClass().add("rounded-scroll-pane");

		content.getChildren().addAll(contentScroll, exploreUsers());

		Label title = new Label("");
		title.getStyleClass().add("font-tiny");
		HBox textBox = new HBox(title);
		textBox.getStyleClass().addAll("spacing", "top-corners");

		container.setTop(textBox);
		container.setCenter(content);
		explore = container;
		return container;
	}

	@SuppressWarnings("unchecked")
	public Pane exploreUsers() {

		VBox content = new VBox();
		content.getStyleClass().addAll("soft");
		
		
		TextField search = new TextField();
		search.getStyleClass().add("search-text-field");
		HBox searchBox = new HBox(search);
		search.setPromptText(" Search Users");

		HBox labelBox = new HBox();
		Label label = new Label("Discover Users");
		label.getStyleClass().add("label-black");
		labelBox.getChildren().add(label);

		HBox followerBox = new HBox();

		ObservableList<TableRow> tvData = FXCollections.observableArrayList();
		TableView<TableRow> tableView = new TableView<>();
		tableView.setPlaceholder(new Label("no users"));
		tableView.getStyleClass().add("explore");
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableView.setEditable(true);
		tableView.setMinWidth(220);
		tableView.setPrefHeight(1000);

		TableColumn<TableRow, String> col0 = new TableColumn<>("User");
		col0.setCellValueFactory(new PropertyValueFactory<>("user"));
		TableColumn<TableRow, String> col1 = new TableColumn<>("Action");
		col1.setCellValueFactory(new PropertyValueFactory<>("button"));

		tableView.getColumns().addAll(col0, col1);

		LinkedList<String> users = app.discoverUsers();

		for (int i = 0; i < users.size(); i++) {
			String user = users.get(i);
			Label userLb = new Label(users.get(i));
			userLb.getStyleClass().add("font-sm");
			TableRow row = new TableRow(userLb);
			Button button = row.getButton();
			button.setText("Follow");
			button.getStyleClass().add("follow-btn");

			tvData.add(row);

			userLb.setOnMouseClicked(e -> {
				User userSerach = app.userSearch(userLb.getText());
				ViewUserProfile viewUser = new ViewUserProfile();
				App.setFeed(viewUser.profile(userSerach));
			});

			row.getButton().setOnAction(e -> {
				if (row.getButton().getText().equals("Following")) {
					row.getButton().setText("Follow");
					row.getButton().getStyleClass().remove("unfollow-btn");
					row.getButton().getStyleClass().add("follow-btn");
					app.unfollowUser(user);

				} else if (row.getButton().getText().equals("Follow")) {
					app.followUser(user);
					row.getButton().setText("Following");
					row.getButton().getStyleClass().remove("follow-btn");
					row.getButton().getStyleClass().add("unfollow-btn");
				}
			});
		}
		
		tableView.setItems(tvData);

		FilteredList<TableRow> filteredData = new FilteredList<TableRow>(tvData, b -> true);
		
		search.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(myTableRow -> {
				
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				String toLowerCase = newValue.toLowerCase();
				if (myTableRow.getUser().getText().toLowerCase().indexOf(toLowerCase) != -1) {
					return true;
				} else
					return false;
			});
		});
		
		SortedList<TableRow> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tableView.comparatorProperty());
		
		tableView.setItems(sortedData);
		
		followerBox.getChildren().add(tableView);
		content.getChildren().addAll(labelBox, searchBox, followerBox);

		return content;
	}

	public static void setExploreContent(Pane p) {
		exploreContent.getChildren().add(0, p);
	}
	
	public static void deletePost(Pane parent) {
		exploreContent.getChildren().remove(parent);
	}

	public static Pane getExplore() {
		return explore;
	}

}
