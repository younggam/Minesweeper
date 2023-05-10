import java.awt.*;
import javax.swing.*;

public class UISetup {
	public static Color defaultColor;

	public UISetup() {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			defaultColor = new Color(UIManager.getColor("control").getRGB());
		} catch (Exception e) {
			e.printStackTrace();
		}

		var mainFrame = new JFrame();
		mainFrame.setTitle("Minesweeper");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);
		mainFrame.setSize(320, 320);

		mainFrame.add(setupMainPanel());

		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	public static JPanel setupMainPanel() {
		var mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		var layoutConstraints = new GridBagConstraints();

		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 0;
		mainPanel.add(setupMenuBar(), layoutConstraints);

		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 1;
		mainPanel.add(setupMainUIPanel(), layoutConstraints);

		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 2;
		mainPanel.add(setupGameBoardPanel(), layoutConstraints);

		return mainPanel;
	}

	public static JPanel setupMenuBar() {
		var menuBarPanel = new JPanel();
		menuBarPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		var difficultyButton = new JButton("Difficulty");
		difficultyButton.setBorder(BorderFactory.createEmptyBorder(3, 8, 2, 8));
		difficultyButton.setBackground(defaultColor);
		menuBarPanel.add(difficultyButton);

		var scoreBoardButton = new JButton("Score Board");
		scoreBoardButton.setBorder(BorderFactory.createEmptyBorder(3, 8, 2, 8));
		scoreBoardButton.setBackground(defaultColor);
		menuBarPanel.add(scoreBoardButton);

		return menuBarPanel;
	}

	public static JPanel setupMainUIPanel() {
		var mainUIPanel = new JPanel();
		mainUIPanel.setBorder(BorderFactory.createLoweredBevelBorder());

		mainUIPanel.add(setupTimeUIPanel());

		var resetButton = new JButton("Reset");
		mainUIPanel.add(resetButton);

		mainUIPanel.add(setupMinesUIPanel());

		return mainUIPanel;
	}

	public static JPanel setupTimeUIPanel() {
		var timeUIPanel = new JPanel();
		timeUIPanel.setLayout(new BoxLayout(timeUIPanel, BoxLayout.Y_AXIS));

		var timeLabel = new JLabel("Time Elapsed");
		timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		timeUIPanel.add(timeLabel);

		var timeCountLabel = new JLabel("10");
		timeCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		timeUIPanel.add(timeCountLabel);

		return timeUIPanel;
	}

	public static JPanel setupMinesUIPanel() {
		var minesUIPanel = new JPanel();
		minesUIPanel.setLayout(new BoxLayout(minesUIPanel, BoxLayout.Y_AXIS));

		var minesLabel = new JLabel("Mines Remaining");
		minesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		minesUIPanel.add(minesLabel);

		var minesCountLabel = new JLabel("10");
		minesCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		minesUIPanel.add(minesCountLabel);

		return minesUIPanel;
	}

	public static JPanel setupGameBoardPanel() {
		var gameBoardPanel = new JPanel();
		gameBoardPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		gameBoardPanel.setLayout(new GridLayout(10, 10));

		var buttonBorder = BorderFactory.createRaisedBevelBorder();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				var mineButton = new JButton();
				mineButton.setBorder(buttonBorder);
				gameBoardPanel.add(mineButton);
			}
		}

		return gameBoardPanel;
	}
}
