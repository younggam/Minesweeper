
public class GameState {
	private static final GameState Beginner = new GameState(9, 9, 10);
	private static final GameState Intermediate = new GameState(16, 16, 40);
	private static final GameState Expert = new GameState(30, 16, 99);

	public static GameState current = Beginner;

	public final int width;
	public final int height;
	public final int mines;

	private GameState(int width, int height, int mines) {
		this.width = width;
		this.height = height;
		this.mines = mines;
	}

	public static void setAsBeginner() {
		current = Beginner;
	}

	public static void setAsIntermediate() {
		current = Intermediate;
	}

	public static void setAsExpert() {
		current = Expert;
	}
}
