import java.util.Random;

public class GameManager {
	private static Tile[][] board;
	private static int rows;
	private static int columns;
	private static int mines;

	private static int founds;
	private static int flags;

	private static long startTime;
	private static long playTime;
	private static Thread timeThread;

	private static boolean gameOver;
	private static boolean isBoardInitialized;

	// 시작하기 전 생성자에서 사이즈와 지뢰 개수 지정, 난이도 정보 받음
	public static void initialize(DifficultyPreset preset) {
		rows = preset.rows;
		columns = preset.columns;
		mines = preset.mines;
		board = new Tile[rows][columns];

		founds = 0;
		flags = 0;
		playTime = 0;
		gameOver = false;
		isBoardInitialized = false;
		// 타이머를 지속적으로 작동시키기 위한 새 쓰레드
		timeThread = new Thread(() -> {
			while (!gameOver)
				GameManager.updateTime();
		});

		SetupUI.updateMines(mines);
		SetupUI.updateTime((int) playTime);
		SetupUI.updateResetButton();

		// 보드 초기화
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				board[i][j] = new Tile();
			}
		}
	}

	// UI가 바꼈을 때, TileAction을 설정해주기
	public static void setTileAction(TileAction tileAction) {
		var tile = board[tileAction.getRow()][tileAction.getColumn()];
		tile.setTileAction(tileAction);
		tileAction.setTile(tile);
	}

	public static boolean isGameOver() {
		return gameOver;
	}

	public static long getPlayTime() {
		return playTime;
	}

	// 게임이 초기화되고, 최초 클릭 시 게임 시작 및 보드 구성
	private static void initializeBoard(int excludedRow, int excludedCol) {
		isBoardInitialized = true;

		// 타이머 작동
		startTime = System.currentTimeMillis();
		timeThread.start();

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
					board[i][j].setAdjacents(countMines); // 이 과정을 거치면 주변에 있는 지뢰 개수를 타일에 저장한다.
				}
			}
		}
	}

	private static int countSurroundingMines(int row, int col) { // 자기를 둘러싼 타일에 대해서 지뢰의 개수를 찾아 그 값을 return 한다.
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

	// 특정 좌표의 타일을 드러낸다
	public static void revealTile(int row, int col) {
		if (!isBoardInitialized)
			initializeBoard(row, col);
		revealTileInner(row, col);

		// 승리 조건을 확인한다
		if (checkWin())
			onWin();
	}

	// 특정 좌표 주변 8칸도 타일을 드러낸다
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
		// 주어진 좌표가 보드 밖이면 무시
		if (row < 0 || row >= rows || col < 0 || col >= columns)
			return false;
		// 해당 타일에 깃발이 있거나, 이미 드러났으면 무시
		var tile = board[row][col];
		if (tile.isFound() || tile.hasFlag())
			return false;

		founds++;
		tile.setFound();
		// 마인을 드러내면 패배
		if (tile.isMine()) {
			onLose();
			return true;
		}

		if (tile.hasNoAdjacents()) {
			// 주변 8칸에 대해 재귀호출을 한다. 단, 패배 시 정지
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

	// 특정좌표에 깃발을 꽂는다
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

	// 타이머를 업대이트 한다. 개별 쓰레드 전용
	private static void updateTime() {
		int prevTime = (int) playTime / 1000;
		playTime = System.currentTimeMillis() - startTime;
		int newTime = (int) playTime / 1000;
		if (prevTime != newTime)
			SetupUI.updateTime(newTime);
	}

	private static boolean checkWin() {
		// 찾아지지 않은 cell, 즉 10x10에서 100에서 시작해서 지뢰 10개를 제외한 90개의 타일을 다 찾으면 founds와
		// mines의 값이 같아진다.
		return rows * columns - founds == mines;
	}

	// 게임 오버 시, 모든 타일을 드러낸다
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
		if (won)
			ScoreRanking.save(playTime);
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
