import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

// 점수 읽기 쓰기를 실행하고, 점수 순위를 관리
public class ScoreRanking {
	// 난이도 별 점수 순위(오름차순)
	private static ArrayList<Long> beginnerScores;
	private static ArrayList<Long> intermediateScores;
	private static ArrayList<Long> expertScores;

	// 외부에서의 임의 접근을 막고, 직접 String으로 변환해 데이터 전
	public static String[] getExpertScoresString() {
		return getScoresStringInner(expertScores);
	}

	public static String[] getIntermediateScoresString() {
		return getScoresStringInner(intermediateScores);
	}

	public static String[] getBeginnerScoresString() {
		return getScoresStringInner(beginnerScores);
	}

	// 점수를 문자열 포매팅으로 읽을만하게 만들기
	private static String[] getScoresStringInner(ArrayList<Long> scores) {
		var scoresString = new String[scores.size()];

		for (int i = 0; i < scores.size(); i++) {
			var score = scores.get(i);
			scoresString[i] = String.format("#%03d:  %02dm:%02ds:%03dms", i, score / 1000 / 60, score / 1000 % 60,
					score % 1000);
		}
		return scoresString;
	}

	// 점수를 읽어서 저장
	public static void load() {
		try {
			// 난이도별 file에 접근해 읽기를 한다.
			// 파일에서 점수 읽어오기
			beginnerScores = ScoreIO.readScores(ScoreIO.beginnerPath);
			// 점수를 오름차순으로 정렬
			Collections.sort(beginnerScores);
			// 파일에서 점수 읽어오기
			intermediateScores = ScoreIO.readScores(ScoreIO.intermediatePath);
			// 점수를 오름차순으로 정렬
			Collections.sort(intermediateScores);
			// 파일에서 점수 읽어오기
			expertScores = ScoreIO.readScores(ScoreIO.hardPath);
			// 점수를 오름차순으로 정렬
			Collections.sort(expertScores);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	// 새로운 점수 기록을 추가하고 순위 정리 후, 전체 기록을 파일에 저장
	public static void save(long newScore) {
		try {
			// 난이도에 따라 file에 접근해 쓰기를 한다.
			if (DifficultyPreset.isBeginner() && beginnerScores != null) {
				// 유저의 새로운 점수 추가
				beginnerScores.add(newScore);
				// 점수를 오름차순으로 정렬
				Collections.sort(beginnerScores);
				// 정렬된 점수를 파일에 쓰기
				ScoreIO.writeScores(beginnerScores, ScoreIO.beginnerPath);
			} else if (DifficultyPreset.isIntermediate() && intermediateScores != null) {
				// 유저의 새로운 점수 추가
				intermediateScores.add(newScore);
				// 점수를 오름차순으로 정렬
				Collections.sort(intermediateScores);
				// 정렬된 점수를 파일에 쓰기
				ScoreIO.writeScores(intermediateScores, ScoreIO.intermediatePath);
			} else if (DifficultyPreset.isHard() && expertScores != null) {
				// 유저의 새로운 점수 추가
				expertScores.add(newScore);
				// 점수를 오름차순으로 정렬
				Collections.sort(expertScores);
				// 정렬된 점수를 파일에 쓰기
				ScoreIO.writeScores(expertScores, ScoreIO.hardPath);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
