package model;

import java.io.Serializable;
import java.util.HashSet;

@SuppressWarnings("serial")
public class Dictionary implements Serializable {
	private HashSet<String> dictionary;

	public Dictionary(int size) {
		dictionary = new HashSet<String>(size);
	}

	public void add(String word) {
		dictionary.add(word);
	}
	
	public void delete(String word) {
		dictionary.remove(word);
	}

	public boolean spellCheck(String word) {
		return dictionary.contains(word);
	}
}
