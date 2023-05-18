import javax.swing.JButton;

public class GameState {
	private static JButton[][] mines;
	
	private static int minesCount;
	private static int flags;
	private static float playTime;

	public static void initialize(int width, int height) {
		GameState.minesCount = width * height;
		GameState.flags = 0;
		GameState.playTime = 0;
	}

	public static int displayMines() {
		return Math.max(minesCount - flags, 0);
	}

	public static void addFlag() {
		flags += 1;
	}

	public static void removeFlag() {
		flags -= 1;
	}
}
