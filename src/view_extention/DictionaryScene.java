package view_extention;

import java.util.List;
import java.util.TreeSet;

import app_data_centers.DataCenter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DictionaryScene {
	private static TreeSet<String> selected;
	private static DataCenter app;

	public static void display(List<String> misspelled) {

		selected = new TreeSet<String>();
		app = DataCenter.getInstance();
		
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);

		Label title = new Label("Add To Dictionary");
		HBox titleBox = new HBox(title);

		Button add = new Button("Add");
		add.getStyleClass().add("dictionary-btn");

		Button close = new Button("Close");
		close.getStyleClass().add("dictionary-btn");

		close.setOnAction(e -> {
			stage.close();
		});

		HBox buttonBox = new HBox(close, add);
		buttonBox.getStyleClass().add("spacing");
		BorderPane content = new BorderPane();
		content.getStyleClass().add("padding");

		ScrollPane contentScroll = new ScrollPane();
		contentScroll.setContent(wordChooser(misspelled));
		contentScroll.setFitToWidth(true);
		contentScroll.getStyleClass().add("rounded-scroll-pane");
		VBox scroll = new VBox(contentScroll);

		content.setTop(titleBox);
		content.setCenter(scroll);
		content.setBottom(buttonBox);

		add.setOnAction(e -> {

			if(selected.size() > 0) {
			try {
				app.updateDictionary(selected);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}
			selected = new TreeSet<>();
			stage.close();
			
		});

		Scene scene = new Scene(content, 250, 150);
		scene.getStylesheets().add("css/app.css");
		stage.setTitle("Dictionary");
		stage.setScene(scene);
		stage.setMinHeight(300);
		stage.setMinWidth(180);
		stage.showAndWait();

	}

	public static Pane wordChooser(List<String> misspelled) {

		VBox btnBox = new VBox();
		for (String word : misspelled) {
			if (word.trim().length() > 0) {
				RadioButton rBtn = new RadioButton(word);
				btnBox.getChildren().add(rBtn);
				rBtn.getStyleClass().add("red-radio-button");
				rBtn.setSelected(true);
				selected.add(rBtn.getText());

				rBtn.selectedProperty().addListener(new ChangeListener<Boolean>() {
					@Override
					public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected,
							Boolean isNowSelected) {
						if (isNowSelected) {
							selected.add(rBtn.getText());
						} else {
							selected.remove(rBtn.getText());
						}
					}
				});

			}
		}
		btnBox.setSpacing(15);
		btnBox.getStyleClass().addAll("radio-btn-alignment");

		return btnBox;
	}
}
