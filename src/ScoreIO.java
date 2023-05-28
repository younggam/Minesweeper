import java.io.*;
import java.util.*;

public class ScoreIO {
	public static String beginnerPath = System.getProperty("user.dir") + "\\Beginner.txt";
	public static String intermediatePath = System.getProperty("user.dir") + "\\Intermediate.txt";
	public static String hardPath = System.getProperty("user.dir") + "\\Hard.txt";

	public static ArrayList<Long> readScores(String fileName) throws IOException {
		var scores = new ArrayList<Long>();
		var file = new File(fileName);
		if (!file.exists())
			file.createNewFile();
		var reader = new BufferedReader(new FileReader(file));

		String line;
		while ((line = reader.readLine()) != null) {
			long score = Long.parseLong(line);
			scores.add(score);
		}

		reader.close();
		return scores;
	}

	public static void writeScores(ArrayList<Long> scores, String fileName) throws IOException {
		var writer = new BufferedWriter(new FileWriter(fileName));

		for (long score : scores) {
			writer.write(String.valueOf(score));
			writer.newLine();
		}

		writer.close();
	}
}
