//미리 정해둔 난이도 별 게임 정보
public class DifficultyPreset {
	private static final DifficultyPreset Beginner = new DifficultyPreset(9, 9, 10);
	private static final DifficultyPreset Intermediate = new DifficultyPreset(16, 16, 40);
	private static final DifficultyPreset Expert = new DifficultyPreset(30, 16, 99);

	// 외부에서 사용하는 현재 게임 난이도 정보
	public static DifficultyPreset current = Beginner;

	public final int columns;
	public final int rows;
	public final int mines;

	private DifficultyPreset(int columns, int rows, int mines) {
		this.columns = columns;
		this.rows = rows;
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
