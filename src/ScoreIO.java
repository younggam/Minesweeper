import java.io.*;
import java.util.*;

// 점수에 대한 읽기, 쓰기 동작 및 기능을 정의
public class ScoreIO {
	// 점수가 저장되는 파일 경로 정의
	public static final String beginnerPath = System.getProperty("user.dir") + "\\Beginner.txt";
	public static final String intermediatePath = System.getProperty("user.dir") + "\\Intermediate.txt";
	public static final String hardPath = System.getProperty("user.dir") + "\\Hard.txt";

	// 주어진 파일 경로에서 점수 읽기
	public static ArrayList<Long> readScores(String fileName) throws IOException {
		var file = new File(fileName);
		if (!file.exists())
			file.createNewFile();
		var reader = new BufferedReader(new FileReader(file));

		// 한줄 한줄 읽어서 Long(ms)으로 변환
		var scores = new ArrayList<Long>();
		String line;
		while ((line = reader.readLine()) != null) {
			long score = Long.parseLong(line);
			scores.add(score);
		}

		reader.close();
		return scores;
	}

	// 주어진 파일 경로에서 점수 쓰기
	public static void writeScores(ArrayList<Long> scores, String fileName) throws IOException {
		var writer = new BufferedWriter(new FileWriter(fileName));

		// 한줄 한줄 Long(ms)을 string으로 변환
		for (long score : scores) {
			writer.write(String.valueOf(score));
			writer.newLine();
		}

		writer.close();
	}
}
