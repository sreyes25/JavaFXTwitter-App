package model;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import views.Explore;
import views.Home;
import views.PostMessage;
import views.Profile;

public class Navagation {
	private StackPane feed;

	public Navagation(StackPane feed) {
		this.feed = feed;
	}

	public Pane addPane(String str) {

		if (str.equals("Home")) {
			Home home = new Home(feed);
			return home.home();
		} else if (str.equals("Explore")) {
			Explore explore = new Explore(feed);
			return explore.explore();
		} else if (str.equals("Profile")) {
			Profile profile = new Profile(feed);
			return profile.profile();
		} else if (str.equals("Post")) {
			PostMessage post = new PostMessage(feed);
			return post.post();
		} else
			return null;
	}
	public Pane editPostPane(Posts str) {
		PostMessage post = new PostMessage(feed);
		return post.editPost(str);
	}

}
