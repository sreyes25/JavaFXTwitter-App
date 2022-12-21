package model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.TreeSet;

@SuppressWarnings("serial")
public class Posts implements Serializable {

	private Calendar timestamp;
	private String pfp;
	private Posts replyingToPost;
	private String username;
	private String message;
	private String image;
	private String id;
	private LinkedList<Posts> comments;
	private TreeSet<String> likes;
	
	public Posts() {
	}
	public Posts(String pfp, String username, String image, String message) {
		this.setReplyingToPost(null);
		this.pfp = pfp;
		this.username = username;
		this.timestamp = Calendar.getInstance();
		this.image = image;
		this.message = message;
		this.comments = new LinkedList<Posts>();
		this.id = null;
		this.likes = new TreeSet<String>();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Calendar getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public LinkedList<Posts> getComments() {
		return comments;
	}

	public void setComments(LinkedList<Posts> comments) {
		this.comments = comments;
	}

	public TreeSet<String> getLikes() {
		return likes;
	}

	public void setLikes(TreeSet<String> likes) {
		this.likes = likes;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getPfp() {
		return pfp;
	}

	public void setPfp(String pfp) {
		this.pfp = pfp;
	}

	public Posts getReplyingToPost() {
		return replyingToPost;
	}
	public void setReplyingToPost(Posts replyingToPost) {
		this.replyingToPost = replyingToPost;
	}

	@Override
	public String toString() {
		return "Posts [pfpf="+pfp+"username=" + username + ", timestamp=" + timestamp.get(Calendar.DATE) + ", message=" + message + ", image=" + image
				+ ", comments=" + comments + ", id=" + id + "]";
	}
}
