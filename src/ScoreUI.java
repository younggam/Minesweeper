import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.IconLoader;

public class ScoreUI extends JPanel {
	private JLabel place100;
	private JLabel place10;
	private JLabel place1;

	public ScoreUI() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		place100 = new JLabel(IconLoader.scoreNumberIcons[0]);
		add(place100);
		place10 = new JLabel(IconLoader.scoreNumberIcons[0]);
		add(place10);
		place1 = new JLabel(IconLoader.scoreNumberIcons[0]);
		add(place1);
	}

	public void update(int newScore) {
		newScore = Math.min(Math.max(newScore, 0), 999);
		
		place100.setIcon(IconLoader.scoreNumberIcons[newScore / 100]);
		newScore %= 100;
		place10.setIcon(IconLoader.scoreNumberIcons[newScore / 10]);
		newScore %= 10;
		place1.setIcon(IconLoader.scoreNumberIcons[newScore]);
	}
}
