package app_data_centers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import model.Account;
import model.Dictionary;
import model.Posts;
import model.User;
import util.Util;

public class DataCenter {

	/* User Util Class */

	private static DataCenter instance = null;
	private AppData app;
	private File appData;
	private User user;
	private Dictionary dictionary;
	private boolean dictionaryWasUpdated;

	private DataCenter() {
		appData = new File("app.dat");
		app = new AppData();
		loadAppData();
		dictionaryWasUpdated = false;
		try {
			dictionary = Util.importDictionary();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static DataCenter getInstance() {
		if (instance == null) {
			instance = new DataCenter();
		}
		return instance;
	}

	/* BackEnd */
	private void loadAppData() {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(appData))) {
			app = (AppData) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			e.getStackTrace();
		}
	}

	private void saveAppData() throws FileNotFoundException, IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(appData));
		oos.writeObject(app);
		oos.close();
	}

	public void logout() throws Exception {
		if (user != null) {
			user.getAccount().setMyFeed(new LinkedList<>());
			user.getAccount().setMyPosts(new LinkedList<>());
			user = null;
			if (dictionaryWasUpdated) {
				Util.updateDictionary(dictionary);
			}
			saveAppData();
			dictionaryWasUpdated = false;
		}
	}

	public void loadAccountInfo() {
		LinkedList<Posts> myFeed = user.getAccount().getMyFeed();
		LinkedList<Posts> myPosts = user.getAccount().getMyPosts();
		for (int i = 0; i < app.getGlobalPosts().size(); i++) {
			Posts post = app.getGlobalPosts().get(i);
			if (user.getAccount().getFollowing().containsKey(post.getUsername())) {
				myFeed.add(post);
			}
			if (post.getUsername().equals(user.getUsername())) {
				myPosts.add(post);
				myFeed.add(post);
			}
		}
	}

	public LinkedList<Posts> loadUserPosts(User user) {
		LinkedList<Posts> tempUsersPosts = new LinkedList<Posts>();
		for (int i = 0; i < app.getGlobalPosts().size(); i++) {
			Posts post = app.getGlobalPosts().get(i);
			if (post.getUsername().equals(user.getUsername())) {
				tempUsersPosts.add(post);
			}
		}
		return tempUsersPosts;
	}

	public void updateMyFeed() {
		LinkedList<Posts> myFeed = new LinkedList<>();
		LinkedList<Posts> myPosts = new LinkedList<>();

		for (int i = 0; i < app.getGlobalPosts().size(); i++) {
			Posts post = app.getGlobalPosts().get(i);
			if (user.getAccount().getFollowing().containsKey(post.getUsername())) {
				myFeed.add(post);
			}
			if (post.getUsername().equals(user.getUsername())) {
				myPosts.add(post);
				myFeed.add(post);
			}
		}
		user.getAccount().setMyFeed(myFeed);
		user.getAccount().setMyPosts(myPosts);
	}

	public void updateDictionary(TreeSet<String> words) throws Exception {
		for (String word : words) {
			dictionary.add(word);
		}
		dictionaryWasUpdated = true;
	}

	/* Create User */

	public boolean confirmCreateUser(String username, String password, String confirm) {
		User user = app.findUser(username);
		if (user != null) {
			return false;
		} else if (password.equals(confirm)) {
			return true;
		}
		return false;
	}

	public boolean confirmEmail(String username, String email) {
		User user = app.findUser(username);
		if (user != null && user.getEmail().equals(email)) {
			return true;
		}
		return false;
	}

	public boolean userExists(String username) {
		User user = app.findUser(username);
		if (user != null) {
			return true;
		} else {
			return false;
		}
	}

	public void changeUsername(String newUsername) {
		user.getAccount().setName(newUsername);
	}

	public void addUser(String username, String password, String email, String pfp) throws IOException {
		app.addUser(username, password, email, pfp);
		saveAppData();
	}

	public void deletePost(Posts post) {
		app.getGlobalPosts().remove(post);
		updateMyFeed();
	}

	/* Login Functions */

	public boolean loginUser(String username, String password) {

		User user = app.findUser(username);
		if (user == null) {
			return false;
		} else if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
			this.user = user;
			loadAccountInfo();
			return true;
		} else {
			return false;
		}
	}

	/* App Functions */

	public Posts addMessagePost(String message) throws FileNotFoundException, IOException {
		Posts post = app.addPost(user.getAccount().getProfileImage(), user.getUsername(), null, message);
		updateMyFeed();
		return post;
	}

	public Posts addImagePost(String img, String message) throws FileNotFoundException, IOException {
		Posts post = app.addPost(user.getAccount().getProfileImage(), user.getUsername(), img, message);
		updateMyFeed();
		return post;
	}

	public Posts addReply(Posts replyTo, String message) throws FileNotFoundException, IOException {
		Posts reply = app.addPost(user.getAccount().getProfileImage(), user.getUsername(), null, message);
		reply.setReplyingToPost(replyTo);
		replyTo.getComments().add(reply);
		updateMyFeed();
		return reply;
	}

	public void followUser(String username) {
		User user = app.findUser(username);
		if (!user.equals(this.user)) {
			this.user.getAccount().getFollowing().put(username, user);
			user.getAccount().getFollowers().put(this.user.getUsername(), this.user);
			updateMyFeed();
		}
	}

	public void unfollowUser(String username) {
		User user = app.findUser(username);

		if (!user.equals(this.user)) {
			this.user.getAccount().getFollowing().remove(username);
			user.getAccount().getFollowers().remove(this.user.getUsername());
			updateMyFeed();
		}
	}

	public void likePost(Posts post) {
		post.getLikes().add(user.getUsername());
	}

	public void unlikePost(Posts post) {
		post.getLikes().remove(this.user.getUsername());

	}

	public boolean isLiked(Posts post) {
		return post.getLikes().contains(this.user.getUsername());
	}

	/* User Functions */

	public LinkedList<String> discoverUsers() {
		LinkedList<String> users = new LinkedList<>();
		Iterator<User> i = app.getUsers().values().iterator();
		while (i.hasNext()) {
			User user = i.next();
			if (!this.user.getAccount().getFollowing().containsKey(user.getUsername())
					&& !user.getUsername().equals(this.user.getUsername())) {
				users.add(user.getUsername());
			}
		}
		return users;
	}

	public List<String> spellCheckPost(String text) throws Exception {
		List<String> list = new LinkedList<>();

		String sentence = text.replaceAll("[\\t\\n\\r]+", " ");

		String[] words = sentence.replaceAll("[^a-zA-Z ]", "").split("\\s+");

		for (int i = 0; i < words.length; i++) {
			if (!dictionary.spellCheck(words[i])) {
				if (!dictionary.spellCheck(words[i].toLowerCase())) {
					list.add(words[i]);
				}
			}
		}
		if (list.size() != 0) {
			return list;
		} else
			return null;
	}

	/* Getters */

	public Posts findPost(String id) {
		int idNumber = Integer.valueOf(id);
		LinkedList<Posts> posts = getGlobal();

		if (posts.get(idNumber).getId().equals(id)) {
			return posts.get(Integer.valueOf(id));
		} else {
			int index = Util.binarySearch(posts, id);
			return posts.get(index);
		}
	}

	public User userSearch(String username) {
		return app.findUser(username);
	}

	public User getUserDetails() {
		return user;
	}

	public Account getAccountDetails() {
		return user.getAccount();
	}

	public LinkedList<Posts> getGlobal() {
		return app.getGlobalPosts();
	}

	public LinkedList<Posts> getMyFeed() {
		return user.getAccount().getMyFeed();
	}

	public LinkedList<Posts> getMyPosts() {
		return user.getAccount().getMyPosts();
	}
}