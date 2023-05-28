import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.IconLoader;

// 점수 표시용 UI에 자주 호출되는 코드를 따로 분리해서 모음
public class ScoreUI extends JPanel {
	private JLabel place100;// 100의 자리
	private JLabel place10;// 10의 자리
	private JLabel place1;// 1의 자리

	public ScoreUI() {
		super();
		// 수평으로 배치
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		place100 = new JLabel(IconLoader.scoreNumberIcons[0]);
		add(place100);
		place10 = new JLabel(IconLoader.scoreNumberIcons[0]);
		add(place10);
		place1 = new JLabel(IconLoader.scoreNumberIcons[0]);
		add(place1);
	}

	// 최신화된 점수에 따라 UI표시도 수정
	public void update(int newScore) {
		// 0~999 사이로 제한
		newScore = Math.min(Math.max(newScore, 0), 999);

		// UI 점수판 변경
		place100.setIcon(IconLoader.scoreNumberIcons[newScore / 100]);
		newScore %= 100;
		place10.setIcon(IconLoader.scoreNumberIcons[newScore / 10]);
		newScore %= 10;
		place1.setIcon(IconLoader.scoreNumberIcons[newScore]);
	}
}
