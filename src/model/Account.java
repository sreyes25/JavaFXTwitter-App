package model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.TreeMap;

import javafx.scene.paint.Color;

@SuppressWarnings("serial")
public class Account implements Serializable{

	private String profileImage;
	private String name;
	private SerializableColor profileBanner; 
	private TreeMap<String, User> followers;
	private TreeMap<String, User> following;
	private LinkedList<Posts> myPosts;
	private LinkedList<Posts> myFeed;
	private int popularity;
	
	public Account(String username) {
		name = username;
		profileBanner = new SerializableColor(Color.web("#DEDFDD",1));
		followers = new TreeMap<String, User>();
		following = new TreeMap<String, User>();
		myPosts = new LinkedList<>();
		myFeed = new LinkedList<>();
		profileImage = null;
		popularity = 0;
	}

	public TreeMap<String, User> getFollowers() {
		return followers;
	}

	public void setFollowers(TreeMap<String, User> followers) {
		this.followers = followers;
	}

	public TreeMap<String, User> getFollowing() {
		return following;
	}

	public void setFollowing(TreeMap<String, User> follows) {
		this.following = follows;
	}

	public LinkedList<Posts> getMyPosts() {
		return myPosts;
	}

	public void setMyPosts(LinkedList<Posts> myPosts) {
		this.myPosts = myPosts;
	}

	public LinkedList<Posts> getMyFeed() {
		return myFeed;
	}

	public void setMyFeed(LinkedList<Posts> myPostFeed) {
		this.myFeed = myPostFeed;
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	

	public SerializableColor getProfileBanner() {
		return profileBanner;
	}

	public void setProfileBanner(Color profileBanner) {
		this.profileBanner = new SerializableColor(profileBanner);
	}

	@Override
	public String toString() {
		return "Account Details [name="+name+"follower=" + followers + ", following=" + following + ", post=" + myPosts + ", popularity="
				+ popularity + "]";
	}	
	
}
