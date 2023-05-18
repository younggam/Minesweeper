import java.awt.event.*;

import javax.swing.*;

import resources.IconLoader;

public class MineAction implements MouseListener {
	private final JButton button;

	private boolean flagged;

	public MineAction(JButton button) {
		button.setIcon(IconLoader.boardDefaultIcon);
		this.button = button;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		switch (e.getClickCount()) {
		case 1:
			if (SwingUtilities.isRightMouseButton(e))
				flag();
			else if (!flagged)
				reveal();
			break;
		case 2:
			if (!flagged)
				chording();
			break;
		default:
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	private void reveal() {
		button.setIcon(IconLoader.boardPressedIcon);
		button.setPressedIcon(IconLoader.boardPressedIcon);
	}

	private void chording() {

	}

	private void flag() {
		if (flagged) {
			flagged = false;
			GameState.removeFlag();
			button.setIcon(IconLoader.boardDefaultIcon);
			button.setPressedIcon(IconLoader.boardPressedIcon);
		} else {
			flagged = true;
			GameState.addFlag();
			button.setIcon(IconLoader.boardFlagIcon);
			button.setPressedIcon(IconLoader.boardFlagIcon);
		}
	}
}
