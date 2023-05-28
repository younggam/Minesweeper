import java.awt.event.*;

import javax.swing.*;

import resources.IconLoader;

// Tile관련 UI 동작 및 기능을 따로 분리함
// 1. Tile과 보드에서 값들이 변했을 때, 맞춰서 UI에 반영시켜줌
// 2. 유저 상호작용을 MouseListener를 통해 받아서 Tile과 보드에 전해줌
public class TileAction implements MouseListener {
	// 연결된 UI
	private final JButton button;
	// 보드상 위치, 불변
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

	// Tile과 연결용
	public void setTile(Tile tile) {
		this.tile = tile;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// 게임 종료 시 아무 반응 도 안함
		if (GameManager.isGameOver())
			return;

		switch (e.getClickCount()) {
		case 1:
			// 일반 클릭시
			if (SwingUtilities.isRightMouseButton(e))
				// 우클릭이면 깃발 조작
				GameManager.setFlag(row, column);
			else
				// 좌클릭이면 Tile 드러내기
				GameManager.revealTile(row, column);
			break;
		case 2:
			// 더블 클릭하면 주변 Tile도 드러내기
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

	// Tile이 드러났을 때의 UI동작(아이콘 변경)
	public void reveal() {
		ImageIcon icon = tile.isMine() ? IconLoader.boardMinePressedIcon
				: tile.getAdjacents() == 0 ? IconLoader.boardPressedIcon
						: IconLoader.boardNumberIcons[tile.getAdjacents()];
		button.setIcon(icon);
		button.setPressedIcon(icon);
	}

	// 게임 종료 시 Tile이 드러났을 때의 UI동작(아이콘 변경)
	public void revealGameOver(boolean won) {
		ImageIcon icon = tile.isMine() ? won ? IconLoader.boardDefaultIcon : IconLoader.boardMineIcon
				: tile.getAdjacents() == 0 ? IconLoader.boardPressedIcon
						: IconLoader.boardNumberIcons[tile.getAdjacents()];
		button.setIcon(icon);
		button.setPressedIcon(icon);
	}

	// 깃발이 표시 됐을 때의 UI동작(아이콘 변경)
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
