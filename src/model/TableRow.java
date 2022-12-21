package model;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TableRow {
	private Label user;
	private Button button;

	public TableRow(Label user) {
		this.user = user;
		button = new Button();
		button.getStyleClass().add("follow-btn");
	}

	public Label getUser() {
		return user;
	}

	public void setUser(Label user) {
		this.user = user;
	}

	public Button getButton() {
		return button;
	}

	public void setButton(Button button) {
		this.button = button;
	}
}
