import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.IconLoader;

// 점수 표시용 UI
public class ScoreUI extends JPanel {
	private JLabel place100;// 100의 자리
	private JLabel place10;// 10의 자리
	private JLabel place1;// 1의 자리

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
