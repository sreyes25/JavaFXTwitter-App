package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;


public class CreateDictionary {
	Dictionary dictionary;
	File txt = new File("dictionary.txt");
	File dat = new File("dictionary.dat");

	public CreateDictionary() throws Exception {
		if (!dat.exists()) {
			importDictionary();
			saveData();
		}
	}

	private void importDictionary() throws Exception {
		long count = Files.lines(txt.toPath()).count();
		int size = (int) count * 2;
		dictionary = new Dictionary(size);
		BufferedReader reader = new BufferedReader(new FileReader("dictionary.txt"));
		String line = null;
		while ((line = reader.readLine()) != null) {
			dictionary.add(line);
		}
		reader.close();
	}

	public void saveData() throws FileNotFoundException, IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dat));
		oos.writeObject(dictionary);
		oos.close();
	}

	public Dictionary loadData() {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dat))) {
			dictionary = (Dictionary) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			e.getStackTrace();
		}
		return dictionary;
	}
	
	public void updateDictionary(Dictionary dic) throws FileNotFoundException, IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dat));
		oos.writeObject(dic);
		oos.close();
	}
}
