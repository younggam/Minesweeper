import java.awt.event.*;

import javax.swing.*;

import resources.IconLoader;

public class MineAction implements MouseListener {
	private final JButton button;
	private final int row, column;

	private boolean flagged;

	public MineAction(JButton button, int row, int column) {
		button.setIcon(IconLoader.boardDefaultIcon);
		this.button = button;
		this.row = row;
		this.column = column;
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
		// foreachMineAdjacent
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
