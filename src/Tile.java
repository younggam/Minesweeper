
public class Tile {
	private boolean mine;
	private int adjacents;
	private boolean found;
	private boolean flag;

	private TileAction tileAction;

	public Tile() {
	}

	public boolean isMine() {
		return mine;
	}

	public void SetMine() {
		mine = true;
	}

	public int getAdjacents() {
		return adjacents;
	}

	public void setAdjacents(int adjacents) {
		this.adjacents = adjacents;
	}

	public boolean hasNoAdjacents() {
		return adjacents == 0;
	}

	public boolean isFound() {
		return found;
	}

	public void setFound() {
		found = true;
		tileAction.reveal();
	}

	public void setFound(boolean won) {
		found = true;
		tileAction.revealGameOver(won);
	}

	public boolean hasFlag() {
		return flag;
	}

	public void setFlag() {
		flag = !flag;
		tileAction.setFlag();
	}

	public void setTileAction(TileAction tileAction) {
		this.tileAction = tileAction;
	}
}
