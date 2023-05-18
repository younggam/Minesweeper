package resources;

import javax.swing.ImageIcon;

public class IconLoader {
	public static ImageIcon[] boardNumberIcons = new ImageIcon[9];
	public static ImageIcon boardDefaultIcon;
	public static ImageIcon boardPressedIcon;
	public static ImageIcon boardFlagIcon;
	public static ImageIcon boardMineIcon;
	public static ImageIcon boardMinePressedIcon;

	public static ImageIcon resetDefaultIcon;
	public static ImageIcon resetPressedIcon;
	public static ImageIcon resetWinIcon;
	public static ImageIcon resetLoseIcon;

	public static ImageIcon scoreEmptyIcon;
	public static ImageIcon[] scoreNumberIcons = new ImageIcon[10];

	public static void Load() {
		var c = IconLoader.class;

		for (int i = 1; i < 9; i++)
			boardNumberIcons[i] = new ImageIcon(c.getResource("board/Board " + i + ".png"));
		boardDefaultIcon = new ImageIcon(c.getResource("board/Board Default.png"));
		boardPressedIcon = new ImageIcon(c.getResource("board/Board Pressed.png"));
		boardNumberIcons[0] = boardPressedIcon;
		boardFlagIcon = new ImageIcon(c.getResource("board/Board Flag.png"));
		boardMineIcon = new ImageIcon(c.getResource("board/Board Mine.png"));
		boardMinePressedIcon = new ImageIcon(c.getResource("board/Board Mine Pressed.png"));

		resetDefaultIcon = new ImageIcon(c.getResource("reset/Reset Default.png"));
		resetPressedIcon = new ImageIcon(c.getResource("reset/Reset Pressed.png"));
		resetWinIcon = new ImageIcon(c.getResource("reset/Reset Win.png"));
		resetLoseIcon = new ImageIcon(c.getResource("reset/Reset Lose.png"));

		for (int i = 0; i < 10; i++)
			scoreNumberIcons[i] = new ImageIcon(c.getResource("score/Score " + i + ".png"));
		scoreEmptyIcon = new ImageIcon(c.getResource("score/Score Empty.png"));
	}
}
