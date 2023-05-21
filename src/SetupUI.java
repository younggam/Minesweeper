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

	public SetupUI() {
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

		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	public static JPanel setupMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mainPanel.add(setupMenuBar());
		mainPanel.add(setupMainUIPanel());
		mainPanel.add(setupGameBoardPanel());

		return mainPanel;
	}

	public static JPanel setupMenuBar() {
		var menuBarPanel = new JPanel();
		menuBarPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		var difficultyMenuBar = new JMenuBar();
		difficultyMenuBar.setBorder(BorderFactory.createEmptyBorder(3, -6, 2, -6));
		difficultyMenuBar.setBackground(defaultColor);
		difficultyMenuBar.add(setupDifficultyMenu());
		menuBarPanel.add(difficultyMenuBar);

		var scoreBoardButton = new JButton("Score Board");
		scoreBoardButton.addActionListener(SetupUI::setupScoreBoardDialog);
		scoreBoardButton.setBorder(BorderFactory.createEmptyBorder(3, 0, 2, 0));
		scoreBoardButton.setBackground(defaultColor);
		menuBarPanel.add(scoreBoardButton);

		return menuBarPanel;
	}

	public static JMenu setupDifficultyMenu() {
		var difficultyMenu = new JMenu("Difficulty");

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

		minesUIPanel = new ScoreUI();
		mainUIPanel.add(minesUIPanel);

		mainUIPanel.add(Box.createHorizontalGlue());

		resetButton = new JButton();
		resetButton.setPreferredSize(new Dimension(24, 24));
		resetButton.setBorder(BorderFactory.createEmptyBorder());
		resetButton.setBackground(defaultColor);
		initializeReset();
		mainUIPanel.add(resetButton);

		mainUIPanel.add(Box.createHorizontalGlue());

		timeUIPanel = new ScoreUI();
		mainUIPanel.add(timeUIPanel);

		return mainUIPanel;
	}

	public static JPanel setupGameBoardPanel() {
		var state = DifficultyPreset.current;

		gameBoardPanel = new JPanel();
		gameBoardPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		gameBoardPanel.setLayout(new GridLayout(state.height, state.width));

		var buttonBorder = BorderFactory.createEmptyBorder();
		var buttonDimension = new Dimension(16, 16);
		for (int i = 0; i < state.height; i++) {
			for (int j = 0; j < state.width; j++) {
				var mineButton = new JButton();
				var mineAction = new MineAction(mineButton, i, j);
				mineButton.addMouseListener(mineAction);
				mineButton.setPreferredSize(buttonDimension);
				mineButton.setBorder(buttonBorder);
				mineButton.setBackground(defaultColor);
				gameBoardPanel.add(mineButton);
			}
		}

		return gameBoardPanel;
	}

	public static void setupScoreBoardDialog(ActionEvent e) {
		JOptionPane.showMessageDialog(mainFrame, "Hello?", "Score Board", JOptionPane.PLAIN_MESSAGE);
	}

	public static ActionListener changeDifficulty(Runnable runnable) {
		return e -> {
			mainPanel.remove(gameBoardPanel);
			runnable.run();
			GameState.initialize(DifficultyPreset.current.width, DifficultyPreset.current.height);
			mainPanel.add(setupGameBoardPanel());
			mainFrame.pack();
		};
	}

	public static void updateMines(int mines) {
		minesUIPanel.update(mines);
	}

	public static void updateTime(float time) {
		timeUIPanel.update((int) time);
	}

	public static void initializeReset() {
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