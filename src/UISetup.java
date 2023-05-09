import java.awt.*;
import javax.swing.*;

public class UISetup {
	public UISetup() {
		var mainFrame = new JFrame();
		mainFrame.setTitle("Minesweeper");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(256,320);

		setupMenuBar(mainFrame);
		setupMainUIPanel(mainFrame);
		setupGameBoardPanel(mainFrame);

		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	public static void setupMenuBar(JFrame parent) {
		var menuBar = new JPanel();

		var difficultyButton = new JMenuItem("Difficulty");
		menuBar.add(difficultyButton);

		var scoreBoardButton = new JMenuItem("Score Board");
		menuBar.add(scoreBoardButton);

		parent.add(menuBar);
	}

	public static void setupMainUIPanel(JFrame parent) {
		var mainUIPanel = new JPanel();

		setupTimeUIPanel(mainUIPanel);

		var resetButton = new JButton("Reset");
		mainUIPanel.add(resetButton);

		setupMinesUIPanel(mainUIPanel);

		parent.add(mainUIPanel);
	}

	public static void setupTimeUIPanel(JPanel parent) {
		var timeUIPanel = new JPanel();

		timeUIPanel.add(new JLabel("Time Elapsed"));

		var timeLabel = new JLabel();
		timeUIPanel.add(timeLabel);

		parent.add(timeUIPanel);
	}

	public static void setupMinesUIPanel(JPanel parent) {
		var minesUIPanel = new JPanel();

		minesUIPanel.add(new JLabel("Mines Remaining"));

		var minesLabel = new JLabel();
		minesUIPanel.add(minesLabel);

		parent.add(minesUIPanel);
	}

	public static void setupGameBoardPanel(JFrame parent) {
		var gameBoardPanel = new JPanel();

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				var mineButton = new JButton();
				gameBoardPanel.add(mineButton);
			}
		}

		parent.add(gameBoardPanel);
	}
}
