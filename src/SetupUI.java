import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import resources.IconLoader;

// 전반적인 UI 생성을 관리
public class SetupUI {
	// 현재 UI의 기본 색상
	public static Color defaultColor;

	// 게임 메인 창 Frame
	private static JFrame mainFrame;
	// 모든 UI가 배치될 Panel
	private static JPanel mainPanel;
	// 시간 점수 UI
	private static ScoreUI timeUIPanel;
	// 초기화 버튼
	private static JButton resetButton;
	// 지뢰 개수 점수 UI
	private static ScoreUI minesUIPanel;
	// 게임 보드 표시 Panel
	private static JPanel gameBoardPanel;

	// 초기화 됐는지 여부
	private static boolean isInitialized;

	// 최초 게임 창 켜기
	public static void initialize() {
		// 중복 실행 방지
		if (isInitialized)
			return;
		else
			isInitialized = true;

		// Swing자체 전체적인 UI 테마 설정
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			defaultColor = new Color(UIManager.getColor("control").getRGB());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// UI에 쓰일 아이콘들 로드
		IconLoader.Load();

		// Main Frame 설정
		mainFrame = new JFrame();
		mainFrame.setTitle("Minesweeper");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);
		mainFrame.getRootPane().setBorder(BorderFactory.createRaisedBevelBorder());

		// Main Panel 생성
		mainFrame.add(setupMainPanel());

		mainFrame.setVisible(true);
		// UI들 생성 완료 후 본격 작동 시작
		reset();
	}

	// Main Panel 생성 및 구성
	public static JPanel setupMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// Menu Bar 생성
		mainPanel.add(setupMenuBar());
		// Main UI 생성
		mainPanel.add(setupMainUIPanel());

		return mainPanel;
	}

	// Menu Bar 생성 및 구성
	public static JPanel setupMenuBar() {
		var menuBarPanel = new JPanel();
		menuBarPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		// 난이도 설정용 Menu Bar 생성, 클릭하면 난이도 선택 가능
		var difficultyMenuBar = new JMenuBar();
		difficultyMenuBar.setBorder(BorderFactory.createEmptyBorder(3, -6, 2, -6));
		difficultyMenuBar.setBackground(defaultColor);
		// 난이도 설정용 Menu UI들 생성
		difficultyMenuBar.add(setupDifficultyMenu());
		menuBarPanel.add(difficultyMenuBar);

		// 지금껏 클리어 기록 점수판 표시 버튼 생성
		var scoreBoardButton = new JButton("Score Board");
		// 클릭시 점수판 팝업
		scoreBoardButton.addActionListener(e -> SetupUI.setupScoreBoardDialog());
		scoreBoardButton.setBorder(BorderFactory.createEmptyBorder(3, 0, 2, 0));
		scoreBoardButton.setBackground(defaultColor);
		menuBarPanel.add(scoreBoardButton);

		return menuBarPanel;
	}

	// 난이도 설정용 Difficulty Menu UI들 생성
	public static JMenu setupDifficultyMenu() {
		var difficultyMenu = new JMenu("Difficulty");

		// 클릭하면 난이도 바뀜
		var beginnerMenuItem = new JMenuItem("Beginner");
		// 클릭 시 호출할 함수 등록
		beginnerMenuItem.addActionListener(changeDifficulty(DifficultyPreset::setAsBeginner));
		difficultyMenu.add(beginnerMenuItem);

		var intermediateMenuItem = new JMenuItem("Intermediate");
		intermediateMenuItem.addActionListener(changeDifficulty(DifficultyPreset::setAsIntermediate));
		difficultyMenu.add(intermediateMenuItem);

		var expertMenuItem = new JMenuItem("Expert");
		expertMenuItem.addActionListener(changeDifficulty(DifficultyPreset::setAsExpert));
		difficultyMenu.add(expertMenuItem);

		return difficultyMenu;
	}

	// Main UI 생성 및 구성
	public static JPanel setupMainUIPanel() {
		var mainUIPanel = new JPanel();
		mainUIPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		mainUIPanel.setLayout(new BoxLayout(mainUIPanel, BoxLayout.X_AXIS));

		// 남은 지뢰 개수 점수 UI 생성
		minesUIPanel = new ScoreUI();
		mainUIPanel.add(minesUIPanel);

		mainUIPanel.add(Box.createHorizontalGlue());

		// 게임 리샛 버튼 생성
		resetButton = new JButton();
		// 클릭 시 호출될 함수 등록
		resetButton.addActionListener(e -> reset());
		resetButton.setPreferredSize(new Dimension(24, 24));
		resetButton.setBorder(BorderFactory.createEmptyBorder());
		resetButton.setBackground(defaultColor);
		mainUIPanel.add(resetButton);

		mainUIPanel.add(Box.createHorizontalGlue());

		// 경과 시간 점수 UI 생성
		timeUIPanel = new ScoreUI();
		mainUIPanel.add(timeUIPanel);

		return mainUIPanel;
	}

	// 게임 보드용 UI 생성 및 구
	public static JPanel setupGameBoardPanel() {
		// 현재 설정된 난이도 가져오기
		var state = DifficultyPreset.getCurrent();

		gameBoardPanel = new JPanel();
		gameBoardPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		gameBoardPanel.setLayout(new GridLayout(state.rows, state.columns));

		// 보드 각 Tile에다 버튼 배치
		var buttonBorder = BorderFactory.createEmptyBorder();
		var buttonDimension = new Dimension(16, 16);
		for (int i = 0; i < state.rows; i++) {
			for (int j = 0; j < state.columns; j++) {
				// 버튼 생성
				var tileButton = new JButton();
				// 클릭 시 호출될 동작
				var tileAction = new TileAction(tileButton, i, j);
				// Tile과 연결
				GameManager.setTileAction(tileAction);
				tileButton.addMouseListener(tileAction);
				tileButton.setPreferredSize(buttonDimension);
				tileButton.setBorder(buttonBorder);
				tileButton.setBackground(defaultColor);
				gameBoardPanel.add(tileButton);
			}
		}

		return gameBoardPanel;
	}

	// UI및 게임 재설정
	public static void reset() {
		// 원래 있던 게임 보드 UI없애기
		if (gameBoardPanel != null)
			mainPanel.remove(gameBoardPanel);

		// 게임 현재 나이도에 따라 초기화
		GameManager.initialize(DifficultyPreset.getCurrent());
		// UI도 초기화
		SetupUI.updateResetButton();
		SetupUI.updateTime(0);
		// 게임 보드 UI 다시 생성
		mainPanel.add(setupGameBoardPanel());
		mainFrame.pack();
	}

	// 난이도 변경 시 호출될 함수를 만드는 함수
	public static ActionListener changeDifficulty(Runnable runnable) {
		return e -> {
			// 난이도 변경용 함수가 등록될거임, 그거 실행
			runnable.run();
			// UI설정 및 게임 시
			reset();
		};
	}

	// 점수판 팝업용 다일러그 생성 및 구성
	private static void setupScoreBoardDialog() {
		var scoreBoardDialog = new JDialog(mainFrame, "Score Board", true);
		scoreBoardDialog.setLocationRelativeTo(mainFrame);

		var tabbedPane = new JTabbedPane(SwingConstants.LEFT);

		// Expert 난이도 점수판
		var expertPanel = new JPanel();
		expertPanel.setLayout(new BoxLayout(expertPanel, BoxLayout.Y_AXIS));
		// 점수 목록 가져오기 및 텍스트 표시
		var expertScores = ScoreRanking.getExpertScoresString();
		for (int i = 0; i < expertScores.length; i++)
			expertPanel.add(new JLabel(expertScores[i]));
		tabbedPane.addTab("Expert", new JScrollPane(expertPanel));

		// Intermediate 난이도 점수판
		var intermediatePanel = new JPanel();
		intermediatePanel.setLayout(new BoxLayout(intermediatePanel, BoxLayout.Y_AXIS));
		// 점수 목록 가져오기 및 텍스트 표시
		var intermediateScores = ScoreRanking.getIntermediateScoresString();
		for (int i = 0; i < intermediateScores.length; i++)
			intermediatePanel.add(new JLabel(intermediateScores[i]));
		tabbedPane.addTab("Intermediate", new JScrollPane(intermediatePanel));

		// Beginner 난이도 점수판
		var beginnerPanel = new JPanel();
		beginnerPanel.setLayout(new BoxLayout(beginnerPanel, BoxLayout.Y_AXIS));
		// 점수 목록 가져오기 및 텍스트 표시
		var beginnerScores = ScoreRanking.getBeginnerScoresString();
		for (int i = 0; i < beginnerScores.length; i++)
			beginnerPanel.add(new JLabel(beginnerScores[i]));
		tabbedPane.addTab("Beginner", new JScrollPane(beginnerPanel));

		tabbedPane.setPreferredSize(new Dimension(256, 192));
		scoreBoardDialog.add(tabbedPane, BorderLayout.CENTER);

		scoreBoardDialog.pack();
		scoreBoardDialog.setVisible(true);
	}

	// 지뢰 개수 점수 UI 최신화, 함부로 조작 못하게 점수만 받아서 전달
	public static void updateMines(int mines) {
		minesUIPanel.update(mines);
	}

	// 경과 시간 점수 UI 최신화, 함부로 조작 못하게 점수만 받아서 전달
	public static void updateTime(int time) {
		timeUIPanel.update(time);
	}

	// 리셋 버튼 아이콘 변경
	public static void updateResetButton() {
		resetButton.setIcon(IconLoader.resetDefaultIcon);
		resetButton.setPressedIcon(IconLoader.resetPressedIcon);
	}

	// 승리여부에 따라 UI동작 실행
	public static void gameReset(boolean win) {
		if (win) {
			// 클리어 시간을 포함한 승리 팝업
			var score = GameManager.getPlayTime();
			JOptionPane.showMessageDialog(mainFrame, String.format("You win!\nClear Time %02dm:%02ds:%03dms",
					score / 1000 / 60, score / 1000 % 60, score % 1000), "", JOptionPane.PLAIN_MESSAGE);
			// 리셋 버튼 아이콘 변경
			resetButton.setIcon(IconLoader.resetWinIcon);
		} else {
			// 패배 팝업
			JOptionPane.showMessageDialog(mainFrame, "", "You lose", JOptionPane.PLAIN_MESSAGE);
			// 리셋 버튼 아이콘 변경
			resetButton.setIcon(IconLoader.resetLoseIcon);
		}
	}
}
