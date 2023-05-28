import java.util.Random;

// Game의 전반적인 진행과 로직을 관리
public class GameManager {
	// 지뢰가 놓일 Tile을 모아놓은 보드
	private static Tile[][] board;
	private static int rows;
	private static int columns;
	// 총 지뢰의 개수
	private static int mines;

	// 지뢰 유무와 상관없이 드러난 Tile의 개수
	private static int revealedes;
	// 깃발이 세워진 Tile의 개수
	private static int flags;

	// 시작한 기준 시간
	private static long startTime;
	// 경과 시간
	private static long playTime;
	// 시간 타이머용 쓰레드
	private static Thread timeThread;

	// 게임 종료 여부
	private static boolean gameOver;
	// 보드 초기화 됐는지 여부
	private static boolean isBoardInitialized;

	// 시작하기 전 생성자에서 사이즈와 지뢰 개수 지정, 난이도 정보 받음
	public static void initialize(DifficultyPreset preset) {
		// 보드 정보 반영
		rows = preset.rows;
		columns = preset.columns;
		mines = preset.mines;
		board = new Tile[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				board[i][j] = new Tile();
			}
		}

		// 기타 값들 초기화
		revealedes = 0;
		flags = 0;
		playTime = 0;
		gameOver = false;
		isBoardInitialized = false;

		// 타이머를 지속적으로 작동시키기 위한 새 쓰레드, 실행하는건 아님
		timeThread = new Thread(() -> {
			// 게임 종료되면 중단
			while (!gameOver)
				GameManager.updateTime();
		});

		// UI쪽도 일부 초기화
		SetupUI.updateMines(mines);
	}

	// UI가 초기화 때, TileAction을 설정해주기. initialize이후 호출 보장되어야 함
	public static void setTileAction(TileAction tileAction) {
		var tile = board[tileAction.getRow()][tileAction.getColumn()];
		tile.setTileAction(tileAction);
		tileAction.setTile(tile);
	}

	// 외부에서 gameOver여부 알고싶을때
	public static boolean isGameOver() {
		return gameOver;
	}

	// 외부에서 playTime알고 싶을 때,
	public static long getPlayTime() {
		return playTime;
	}

	// 게임이 초기화되고, 이후 최초 클릭 시 호출되어 게임 시작 및 보드 초기화
	private static void initializeBoard(int excludedRow, int excludedCol) {
		isBoardInitialized = true;

		// 타이머 쓰레드 작동
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
			// 클릭한 지점, 이전에 지뢰가 배치된 Tile은 제외
			if (row != excludedRow && col != excludedCol && !board[row][col].isMine()) {
				board[row][col].SetMine();
				count++;
			}
		}

		// 주변 지뢰 개수 계산 후 저장
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				// 모든 보드 위의 점들에 대해 지뢰가 아닌 Tile에서 주변의 지뢰의 개수를 센다
				if (!board[i][j].isMine()) {
					int countMines = countSurroundingMines(i, j);
					// 이 과정을 거치면 주변에 있는 지뢰 개수를 타일에 저장한다.
					board[i][j].setAdjacents(countMines);
				}
			}
		}
	}

	// 특정 지점에 대해서 주변 지뢰의 개수를 센다.
	private static int countSurroundingMines(int row, int col) {
		int count = 0;
		for (int i = row - 1; i <= row + 1; i++) {
			for (int j = col - 1; j <= col + 1; j++) {
				// 일단 보드 안에 있으면서 지뢰가 있으면 센다.
				if (i >= 0 && i < rows && j >= 0 && j < columns && board[i][j].isMine()) {
					count++;
				}
			}
		}
		return count;
	}

	// 특정 좌표의 타일을 드러낸다
	public static void revealTile(int row, int col) {
		// 첫 클릭 시 보드 초기화
		if (!isBoardInitialized)
			initializeBoard(row, col);

		// 기본 로직 실행
		revealTileInner(row, col);

		// 승리 여부를 체크
		if (checkWin())
			onGameOver(true);
	}

	// 특정 좌표와 그 주변 8칸의 타일을 드러낸다.(더블 클릭)
	public static void revealSurroundingTiles(int row, int col) {
		// 첫 클릭 시 보드 초기화
		if (!isBoardInitialized)
			initializeBoard(row, col);

		// 이중 반복문 탈출용
		var stopRecursion = false;
		for (int i = row - 1; i <= row + 1; i++) {
			for (int j = col - 1; j <= col + 1; j++) {
				// 기본 내부 로직 실행
				stopRecursion = revealTileInner(i, j);
				if (stopRecursion)
					break;
			}
			if (stopRecursion)
				break;
		}
	}

	// 특정 좌표의 타일을 드러내는 로직. 반환값 게임 종료 여부
	private static boolean revealTileInner(int row, int col) {
		// 주어진 좌표가 보드 밖이면 무시
		if (row < 0 || row >= rows || col < 0 || col >= columns)
			return false;
		// 해당 타일에 깃발이 있거나, 이미 드러났으면 무시
		var tile = board[row][col];
		if (tile.isRevealed() || tile.hasFlag())
			return false;

		// Tile이 드러났음을 표시
		revealedes++;
		tile.setRevealed();

		// 지뢰를 드러내면 패배
		if (tile.isMine()) {
			onGameOver(false);
			return true;
		}

		// 주변이 지뢰가 하나도 없으면 연쇄 드러내기
		if (tile.hasNoAdjacents()) {
			// 이중 반복문 탈출용
			var stopRecursion = false;
			// 주변 8칸에 대해 재귀호출을 한다. 단, 패배 시 정지
			for (int i = row - 1; i <= row + 1; i++) {
				for (int j = col - 1; j <= col + 1; j++) {
					// 재귀
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

	// 특정 좌표에 깃발을 표시한다
	public static void setFlag(int row, int column) {
		var tile = board[row][column];
		tile.setFlag();

		if (tile.hasFlag())
			flags++;
		else
			flags--;
		// UI쪽도 반영
		SetupUI.updateMines(mines - flags);
	}

	// 타이머를 업대이트 한다. 개별 쓰레드 분리 권장
	private static void updateTime() {
		int prevTime = (int) playTime / 1000;
		// 시작 시간을 기준으로 경과 시간 계산
		playTime = System.currentTimeMillis() - startTime;
		int newTime = (int) playTime / 1000;

		// 필요 이상의 UI 최신화 방지 (초가 바뀔 때만 필요)
		if (prevTime != newTime)
			// UI 최신화
			SetupUI.updateTime(newTime);
	}

	// 승리 여부 최신화
	private static boolean checkWin() {
		// 찾아지지 않은 cell, 즉 10x10에서 100에서 시작해서 지뢰 10개를 제외한 90개의 타일을 다 드러내면
		// revealedes와 mines의 값이 같아진다.
		return rows * columns - revealedes == mines;
	}

	// 게임 종료 시, 모든 타일을 드러낸다
	private static void onGameOver(boolean won) {
		// 게임 종료시 UI동작
		SetupUI.gameReset(won);
		gameOver = true;
		// 보드 전체에
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				var tile = board[i][j];
				// 이미 드러났거나, 지뢰가 있는 깃발은 냅두기
				if (tile.isRevealed() || (tile.isMine() && tile.hasFlag()))
					continue;
				tile.setRevealed(won);
			}
		}
		// 이겼으면 클리어 시간 저장
		if (won)
			ScoreRanking.save(playTime);
	}
}
