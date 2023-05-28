// 보드의 한 부분의 주요 기능을 따로 분리해서 정의, UI와의 연결점이기도 함
public class Tile {
	// 지뢰가 있는지 여부
	private boolean mine;
	// 근접한 Tile의 지뢰 갯수
	private int adjacents;
	// Tile이 드러났는지 여부
	private boolean revealed;
	// 깃발이 표시됐는지 여부
	private boolean flag;

	// 연결된 UI동작
	private TileAction tileAction;

	public Tile() {
	}

	// 임의 조작 방지용
	public boolean isMine() {
		return mine;
	}

	// 지뢰를 설정하는 건 되도 취소는 안됨
	public void SetMine() {
		mine = true;
	}

	// 임의 조작 방지용
	public int getAdjacents() {
		return adjacents;
	}

	public void setAdjacents(int adjacents) {
		this.adjacents = adjacents;
	}

	// 주변에 지뢰가 하나도 없는지 여부
	public boolean hasNoAdjacents() {
		return adjacents == 0;
	}

	// 임의 조작 방지용. 특히 함께 실행되어야 하는 내부 로직 있음
	public boolean isRevealed() {
		return revealed;
	}

	// 드러내기, UI동작도 호출
	public void setRevealed() {
		revealed = true;
		tileAction.reveal();
	}

	// 게임 종료 시 드러내기, UI동작도 호출
	public void setRevealed(boolean won) {
		revealed = true;
		tileAction.revealGameOver(won);
	}

	// 임의 조작 방지용. 특히 함께 실행되어야 하는 내부 로직 있음
	public boolean hasFlag() {
		return flag;
	}

	// 깃발 표시/해제, UI동작도 호출
	public void setFlag() {
		flag = !flag;
		tileAction.setFlag();
	}

	// UI동작 연결용
	public void setTileAction(TileAction tileAction) {
		this.tileAction = tileAction;
	}
}
