import java.util.Scanner;
import java.util.Random;

public class GameManager {
	private static Tile[][] board;
	private static int rows;
	private static int columns;
	private static int mines;

	private static int founds;
	private static int flags;
	private static float playTime;

	private static boolean gameOver;
	private static boolean isBoardInitialized;

	public static void initialize(DifficultyPreset preset) {
		rows = preset.rows; // 시작하기 전 생성자로 사이즈와 지뢰 개수 지정, 이걸로 난이도 조절 가능
		columns = preset.columns;
		mines = preset.mines;
		board = new Tile[rows][columns];

		founds = 0;
		flags = 0;
		playTime = 0;
		gameOver = false;
		isBoardInitialized = false;

		SetupUI.updateMines(mines);
		SetupUI.updateTime(playTime);
		SetupUI.initializeReset();

		// 보드 초기화 (먼저 0으로 채우기)
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				board[i][j] = new Tile();
			}
		}
	}

	public static void subscribeTileActionReset(TileAction tileAction) {
		var tile = board[tileAction.getRow()][tileAction.getColumn()];
		tile.setTileAction(tileAction);
		tileAction.setTile(tile);
	}

	public static boolean isGameOver() {
		return gameOver;
	}

	private static void initializeBoard(int excludedRow, int excludedCol) {
		isBoardInitialized = true;
		Random random = new Random(); // 랜덤 객체 생성
		random.setSeed(System.currentTimeMillis()); // 시드 설정을 따로 하지 않음

		// 지뢰 배치
		int count = 0;
		// mine의 개수만큼 random하게 배치
		while (count < mines) {
			int row = (int) (random.nextInt(rows));
			int col = (int) (random.nextInt(columns));
			if (row != excludedRow && col != excludedCol && !board[row][col].isMine()) {
				board[row][col].SetMine();
				count++;
			}
		}

		// 주변 지뢰 개수 계산
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (!board[i][j].isMine()) { // 모든 grid의 점들에 대해 지뢰가 아닌 주변의 지뢰의 개수를 센다
					int countMines = countSurroundingMines(i, j);
					board[i][j].setAdjacents(countMines); // board에서 처음에 빈 칸은 0이고 지뢰는 -1인데, 이 과정을 거치면 주변에 있는 지뢰 개수로
															// board가 변한다.
				}
			}
		}
	}

	private static int countSurroundingMines(int row, int col) { // 자기를 둘러싼 cell에 대해서 지뢰의 개수를 찾아 그 값을 return 한다.
		int count = 0;
		for (int i = row - 1; i <= row + 1; i++) {
			for (int j = col - 1; j <= col + 1; j++) {
				if (i >= 0 && i < rows && j >= 0 && j < columns && board[i][j].isMine()) {
					count++;
				}
			}
		}
		return count;
	}

	public static void revealTile(int row, int col) {
		if (!isBoardInitialized)
			initializeBoard(row, col);
		revealTileInner(row, col);

		updateState();
	}

	public static void revealSurroundingTiles(int row, int col) {
		var stopRecursion = false;
		for (int i = row - 1; i <= row + 1; i++) {
			for (int j = col - 1; j <= col + 1; j++) {
				revealTileInner(i, j);
				if (stopRecursion)
					break;
			}
			if (stopRecursion)
				break;
		}
	}

	private static boolean revealTileInner(int row, int col) {
		if (row < 0 || row >= rows || col < 0 || col >= columns) 
			return false;
		var tile = board[row][col];
		if (tile.isFound() || tile.hasFlag())
			return false;

		founds++;
		tile.setFound();
		if (tile.isMine()) {
			onLose();
			return true;
		}

		if (tile.hasNoAdjacents()) {
			// 주변 8칸에 대해 재귀호출을 한다. 여기서 위에 있는 if 조건을 만족하지 못하는 것(1. board의 범위를 벗어남 2. 이미 밝혀진
			// cell임)은 found가 false인 상태가 유지된다.
			var stopRecursion = false;
			for (int i = row - 1; i <= row + 1; i++) {
				for (int j = col - 1; j <= col + 1; j++) {
					stopRecursion = revealTileInner(i, j);
					if (stopRecursion)
						break;
				}
				if (stopRecursion)
					break;
			}
		}
		return false;
	}

	public static void setFlag(int row, int column) {
		var tile = board[row][column];
		var prev = tile.hasFlag();
		tile.setFlag();
		var cur = tile.hasFlag();
		if (prev != cur) {
			if (cur)
				flags++;
			else
				flags--;
			SetupUI.updateMines(mines - flags);
		}
	}

	private static boolean checkWin() {
		// 찾아지지 않은 cell, 즉 10x10에서 100에서 시작해서 지뢰 10개를 제외한 90개의 cell을 다 찾으면 count와
		// mines의 값이 같아진다.
		return rows * columns - founds == mines;
	}

	private static void updateState() {
		if (checkWin())
			onWin();
	}

	private static void onGameOver(boolean won) {
		gameOver = true;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				var tile = board[i][j];
				if (tile.isFound() || (won && tile.hasFlag()))
					continue;
				tile.setFound(won);
			}
		}
	}

	private static void onLose() {
		SetupUI.gameReset(false);
		onGameOver(false);
	}

	private static void onWin() {
		SetupUI.gameReset(true);
		onGameOver(true);
	}
}
