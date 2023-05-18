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

		SetupUI.updateMines(GameState.minesCount);
		SetupUI.updateTime(GameState.playTime);
		SetupUI.initializeReset();
	}

	public static void addFlag() {
		flags += 1;
	}

	public static void removeFlag() {
		flags -= 1;
	}

	public static void forEachMines(MineConsumer mineConsumer) {
		for (int i = 0; i < mines.length; i++) {
			var minesRow = mines[i];
			for (int j = 0; j < minesRow.length; j++)
				mineConsumer.cons();
		}
	}

	public static void forEachMineAdjacents(int r, int c, MineConsumer minesConsumer) {
		for (int i = r - 1; i <= r + 1; i++) {
			for (int j = c - 1; j <= c + 1; j++) {
				if (i == r && j == c)
					continue;
				minesConsumer.cons();
			}
		}
	}

	private static void updateState() {
		SetupUI.updateMines(GameState.minesCount);
	}

	private static void onLose() {
		SetupUI.gameReset(false);
	}

	private static void onWin() {
		SetupUI.gameReset(true);
	}
}