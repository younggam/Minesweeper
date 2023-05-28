//미리 정해둔 난이도 별 게임 정보
public class DifficultyPreset implements Cloneable {
	private static final DifficultyPreset Beginner = new DifficultyPreset(9, 9, 10);
	private static final DifficultyPreset Intermediate = new DifficultyPreset(16, 16, 40);
	private static final DifficultyPreset Expert = new DifficultyPreset(30, 16, 99);

	private static DifficultyPreset current = Beginner;

	public final int columns;
	public final int rows;
	public final int mines;

	private static int difficulty;

	private DifficultyPreset(int columns, int rows, int mines) {
		this.columns = columns;
		this.rows = rows;
		this.mines = mines;
	}

	@Override
	public DifficultyPreset clone() {
		return new DifficultyPreset(columns, rows, mines);
	}

	// 외부에서 사용하는 현재 게임 난이도 정보
	public static DifficultyPreset current() {
		return current.clone();
	}

	public static void setAsBeginner() {
		current = Beginner;
		difficulty = 0;
	}

	public static void setAsIntermediate() {
		current = Intermediate;
		difficulty = 1;
	}

	public static void setAsExpert() {
		current = Expert;
		difficulty = 2;
	}

	public static boolean isBeginner() {
		return difficulty == 0;
	}

	public static boolean isIntermediate() {
		return difficulty == 1;
	}

	public static boolean isHard() {
		return difficulty == 2;
	}
}
