public class DifficultyPreset {
	private static final DifficultyPreset Beginner = new DifficultyPreset(9, 9, 10);
	private static final DifficultyPreset Intermediate = new DifficultyPreset(16, 16, 40);
	private static final DifficultyPreset Expert = new DifficultyPreset(30, 16, 99);

	public static DifficultyPreset current = Beginner;

	public final int width;
	public final int height;
	public final int mines;

	private DifficultyPreset(int width, int height, int mines) {
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
