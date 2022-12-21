package util;

import java.io.File;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import model.CreateDictionary;
import model.Dictionary;
import model.Posts;

public class Util {
	
	public static String getPostTime(Calendar timestamp) {
		Calendar current = Calendar.getInstance();
		String currentTimeStamp = "";
		
		int days = (int) Duration.between(timestamp.toInstant(), current.toInstant()).toDays();
		currentTimeStamp = days + "d";
		
		if (days < 1) {
			int hours = (int) Duration.between(timestamp.toInstant(), current.toInstant()).toHoursPart();
			currentTimeStamp = hours + "h";
			if (hours < 1) {
				int minutes = (int) Duration.between(timestamp.toInstant(), current.toInstant()).toMinutesPart();
				currentTimeStamp = minutes + "m";
				if (minutes < 1) {
					int seconds = (int) Duration.between(timestamp.toInstant(), current.toInstant()).toSecondsPart();
					currentTimeStamp = seconds + "s";
				}
			}
		}
		return currentTimeStamp;
	}
	
	public static String getPostDate(Calendar timestamp) {
		Timestamp time = new Timestamp(timestamp.getTimeInMillis());
		return time.toString();
	}
	
	public static int getLikeCount(Posts post) {
		return post.getLikes().size();
	}
	
	public static Dictionary importDictionary() throws Exception {
		CreateDictionary dic = new CreateDictionary();
		Dictionary dictionary = dic.loadData();
		return dictionary;
	}
	
	public static void updateDictionary(Dictionary dictionary) throws Exception {
		CreateDictionary dic = new CreateDictionary();
		dic.updateDictionary(dictionary);
	}
	
	public static List<String> getPfps(String path) {
		List<String> pfps = new LinkedList<String>();
		File mainFolder = new File(path);
		File[] list = mainFolder.listFiles();
		for (int i = 0; i < list.length; i++) {
			if (list[i].isFile() && (!list[i].getName().equals(".DS_Store"))) {
				pfps.add(list[i].getName());
			}
		}
		return pfps;
	}
	
	public static int binarySearch(LinkedList<Posts> posts, String id) {
		
		int high = posts.size();
		int low = 0;

		while (low <= high) {
			int mid = (int) Math.ceil( 0 + (double)(high - low) / 2);

			if (posts.get(mid).getId().equals(id)) {
				return mid;
			}

			if (Integer.valueOf(posts.get(mid).getId()) < Integer.valueOf(id)) {
				low = mid + 1;
			}

			else {
				high = mid - 1;
			}
		}
		return -1;
		
	}
	
	public static boolean validateTextAreas(String text) {
		return text.length() > 0;
	}

	public static boolean validateSignUp(String username, String password, String email) {
		return username.length() >= 3 && password.length() >= 5 && email.length() >= 5;
	}

	public static boolean validateLogin(String username, String password) {
		return username.length() >= 3 && password.length() >= 5;
	}
	
}
