
public class ScoreRanking {
	private static int[] expertScores = new int[] { 120, 32, 123, 40, 10, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
	private static int[] intermediateScores = new int[] { 120, 32, 123, 40 };
	private static int[] beginnerScores = new int[] { 120, 32, 123, 40 };

	public static String[] getExpertScoresString() {
		return getScoresStringInner(expertScores);
	}

	public static String[] getIntermediateScoresString() {
		return getScoresStringInner(intermediateScores);
	}

	public static String[] getBeginnerScoresString() {
		return getScoresStringInner(beginnerScores);
	}

	// 점수에 문자열 포매팅으로 읽을만하게 만들기
	private static String[] getScoresStringInner(int[] scores) {
		var scoresString = new String[scores.length];

		for (int i = 0; i < scores.length; i++) {
			var score = scores[i];
			scoresString[i] = String.format("#%03d:  %02d:%02d:%03ds", i, score / 1000 / 60, score / 1000 % 60,
					score % 1000);
		}
		return scoresString;
	}
}
