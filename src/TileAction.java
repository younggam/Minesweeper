import java.awt.event.*;

import javax.swing.*;

import resources.IconLoader;

public class TileAction implements MouseListener {
	private final JButton button;
	private final int row, column;

	private Tile tile;

	public TileAction(JButton button, int row, int column) {
		button.setIcon(IconLoader.boardDefaultIcon);
		this.button = button;
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public void setTile(Tile tile) {
		this.tile = tile;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (GameManager.isGameOver())
			return;

		switch (e.getClickCount()) {
		case 1:
			if (SwingUtilities.isRightMouseButton(e))
				GameManager.setFlag(row, column);
			else
				GameManager.revealTile(row, column);
			break;
		case 2:
			GameManager.revealSurroundingTiles(row, column);
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

	public void reveal() {
		ImageIcon icon = tile.isMine() ? IconLoader.boardMinePressedIcon
				: tile.getAdjacents() == 0 ? IconLoader.boardPressedIcon
						: IconLoader.boardNumberIcons[tile.getAdjacents()];
		button.setIcon(icon);
		button.setPressedIcon(icon);
	}

	public void revealGameOver(boolean won) {
		ImageIcon icon = tile.isMine() ? won ? IconLoader.boardDefaultIcon : IconLoader.boardMineIcon
				: tile.getAdjacents() == 0 ? IconLoader.boardPressedIcon
						: IconLoader.boardNumberIcons[tile.getAdjacents()];
		button.setIcon(icon);
		button.setPressedIcon(icon);
	}

	public void setFlag() {
		if (tile.hasFlag()) {
			button.setIcon(IconLoader.boardFlagIcon);
			button.setPressedIcon(IconLoader.boardFlagIcon);
		} else {
			button.setIcon(IconLoader.boardDefaultIcon);
			button.setPressedIcon(IconLoader.boardPressedIcon);
		}
	}
}
