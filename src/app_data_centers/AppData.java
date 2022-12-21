package app_data_centers;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.TreeMap;
import model.Posts;
import model.User;

@SuppressWarnings("serial")
public class AppData implements Serializable{
	
	private TreeMap<String, User> users;
	private LinkedList<Posts> globalPosts;
	private long userCount;
	private long postCount;
	
	public AppData() {
		users = new TreeMap<String, User>();
		globalPosts = new LinkedList<>();
		userCount = 0;
		postCount = 0;
	}
	
	/* Insert */
	
	public void addUser(String username, String password,String email, String pfp) {
		User user = new User(username, password, email);
		if(pfp == null) {
			user.getAccount().setProfileImage("src/pfp/pfp0.jpeg");
		}
		user.getAccount().setProfileImage(pfp);
		user.setId(String.valueOf(userCount++));
		users.put(username, user);
	}
	
	public Posts addPost(String pfp, String username, String img, String message) {
		Posts post = new Posts(pfp, username, img, message);
		post.setId(String.valueOf(postCount++));
		globalPosts.add(post);
		return post;
	}
	
	/* Remove */
	
	/* Searching */
	
	public User findUser(String username) {
		User user = users.get(username);
		if (user != null) {
			return user;
		}
		return user;
	}
	
	/*Getters*/
	
	public TreeMap<String, User> getUsers() {
		return users;
	}

	public void setUsers(TreeMap<String, User> users) {
		this.users = users;
	}

	public LinkedList<Posts> getGlobalPosts() {
		return globalPosts;
	}
	

	public void setGlobalPosts(LinkedList<Posts> globalPosts) {
		this.globalPosts = globalPosts;
	}

	public boolean userExists(String username) {
		return users.containsKey(username);
	}
}
