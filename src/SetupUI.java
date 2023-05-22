import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import resources.IconLoader;

public class SetupUI {
	public static Color defaultColor;

	private static JFrame mainFrame;
	private static JPanel mainPanel;
	private static ScoreUI timeUIPanel;
	private static JButton resetButton;
	private static ScoreUI minesUIPanel;
	private static JPanel gameBoardPanel;

	// 최초 게임 창 켜기
	public static void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			defaultColor = new Color(UIManager.getColor("control").getRGB());
		} catch (Exception e) {
			e.printStackTrace();
		}

		IconLoader.Load();

		mainFrame = new JFrame();
		mainFrame.setTitle("Minesweeper");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);
		mainFrame.getRootPane().setBorder(BorderFactory.createRaisedBevelBorder());

		mainFrame.add(setupMainPanel());

		mainFrame.setVisible(true);
		reset();
	}

	public static JPanel setupMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mainPanel.add(setupMenuBar());
		mainPanel.add(setupMainUIPanel());

		return mainPanel;
	}

	public static JPanel setupMenuBar() {
		var menuBarPanel = new JPanel();
		menuBarPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		// 클릭하면 난이도 선택 가능
		var difficultyMenuBar = new JMenuBar();
		difficultyMenuBar.setBorder(BorderFactory.createEmptyBorder(3, -6, 2, -6));
		difficultyMenuBar.setBackground(defaultColor);
		difficultyMenuBar.add(setupDifficultyMenu());
		menuBarPanel.add(difficultyMenuBar);

		// 클릭하면 점수판 팝업 나옴
		var scoreBoardButton = new JButton("Score Board");
		scoreBoardButton.addActionListener(e -> SetupUI.setupScoreBoardDialog());
		scoreBoardButton.setBorder(BorderFactory.createEmptyBorder(3, 0, 2, 0));
		scoreBoardButton.setBackground(defaultColor);
		menuBarPanel.add(scoreBoardButton);

		return menuBarPanel;
	}

	public static JMenu setupDifficultyMenu() {
		var difficultyMenu = new JMenu("Difficulty");

		// 클릭하면 난이도 바뀜
		var beginnerMenuItem = new JMenuItem("Beginner");
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

	public static JPanel setupMainUIPanel() {
		var mainUIPanel = new JPanel();
		mainUIPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		mainUIPanel.setLayout(new BoxLayout(mainUIPanel, BoxLayout.X_AXIS));

		// 남은 지뢰 개수 UI
		minesUIPanel = new ScoreUI();
		mainUIPanel.add(minesUIPanel);

		mainUIPanel.add(Box.createHorizontalGlue());

		// 클릭하면 게임 리셋
		resetButton = new JButton();
		resetButton.addActionListener(e -> reset());
		resetButton.setPreferredSize(new Dimension(24, 24));
		resetButton.setBorder(BorderFactory.createEmptyBorder());
		resetButton.setBackground(defaultColor);
		updateResetButton();
		mainUIPanel.add(resetButton);

		mainUIPanel.add(Box.createHorizontalGlue());

		// 경과 시간 UI
		timeUIPanel = new ScoreUI();
		mainUIPanel.add(timeUIPanel);

		return mainUIPanel;
	}

	public static JPanel setupGameBoardPanel() {
		var state = DifficultyPreset.current;

		gameBoardPanel = new JPanel();
		gameBoardPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		gameBoardPanel.setLayout(new GridLayout(state.rows, state.columns));

		// 그리드에다 타일 버튼 배치
		var buttonBorder = BorderFactory.createEmptyBorder();
		var buttonDimension = new Dimension(16, 16);
		for (int i = 0; i < state.rows; i++) {
			for (int j = 0; j < state.columns; j++) {
				var tileButton = new JButton();
				var tileAction = new TileAction(tileButton, i, j);
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

	// UI및 게임 초기화
	public static void reset() {
		if (gameBoardPanel != null)
			mainPanel.remove(gameBoardPanel);
		GameManager.initialize(DifficultyPreset.current);
		mainPanel.add(setupGameBoardPanel());
		mainFrame.pack();
	}

	public static ActionListener changeDifficulty(Runnable runnable) {
		return e -> {
			runnable.run();
			reset();
		};
	}

	private static void setupScoreBoardDialog() {
		var scoreBoardDialog = new JDialog(mainFrame, "Score Board", true);
		scoreBoardDialog.setLocationRelativeTo(mainFrame);

		var tabbedPane = new JTabbedPane(SwingConstants.LEFT);

		// 난이도 별 클리어 시간 나열
		var expertPanel = new JPanel();
		expertPanel.setLayout(new BoxLayout(expertPanel, BoxLayout.Y_AXIS));
		var expertScores = ScoreRanking.getExpertScoresString();
		for (int i = 0; i < expertScores.length; i++)
			expertPanel.add(new JLabel(expertScores[i]));
		tabbedPane.addTab("Expert", new JScrollPane(expertPanel));

		var intermediatePanel = new JPanel();
		intermediatePanel.setLayout(new BoxLayout(intermediatePanel, BoxLayout.Y_AXIS));
		var intermediateScores = ScoreRanking.getIntermediateScoresString();
		for (int i = 0; i < intermediateScores.length; i++)
			intermediatePanel.add(new JLabel(intermediateScores[i]));
		tabbedPane.addTab("Intermediate", new JScrollPane(intermediatePanel));

		var beginnerPanel = new JPanel();
		beginnerPanel.setLayout(new BoxLayout(beginnerPanel, BoxLayout.Y_AXIS));
		var beginnerScores = ScoreRanking.getBeginnerScoresString();
		for (int i = 0; i < beginnerScores.length; i++)
			beginnerPanel.add(new JLabel(beginnerScores[i]));
		tabbedPane.addTab("Beginner", new JScrollPane(beginnerPanel));

		tabbedPane.setPreferredSize(new Dimension(240, 192));
		scoreBoardDialog.add(tabbedPane, BorderLayout.CENTER);

		scoreBoardDialog.pack();
		scoreBoardDialog.setVisible(true);
	}

	public static void updateMines(int mines) {
		minesUIPanel.update(mines);
	}

	public static void updateTime(int time) {
		timeUIPanel.update(time);
	}

	public static void updateResetButton() {
		resetButton.setIcon(IconLoader.resetDefaultIcon);
		resetButton.setPressedIcon(IconLoader.resetPressedIcon);
	}

	public static void gameReset(boolean win) {
		if (win)
			resetButton.setIcon(IconLoader.resetWinIcon);
		else
			resetButton.setIcon(IconLoader.resetLoseIcon);

	}
}
