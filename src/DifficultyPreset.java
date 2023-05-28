//미리 정해둔 난이도 별 게임 정보
public class DifficultyPreset {
	// final로 불변성 보장, private은 그냥current만 쓰여서 public일 이유가 없어서
	private static final DifficultyPreset Beginner = new DifficultyPreset(9, 9, 10);
	private static final DifficultyPreset Intermediate = new DifficultyPreset(16, 16, 40);
	private static final DifficultyPreset Expert = new DifficultyPreset(30, 16, 99);

	// private으로 설정하여 외부의 임의 set 방지용
	private static DifficultyPreset current = Beginner;

	// final로 불변성 보장
	public final int columns;
	public final int rows;
	public final int mines;

	// 현재 어떤 난이도인지 간단하게 비교할 수 있게
	private static int difficulty;

	private DifficultyPreset(int columns, int rows, int mines) {
		this.columns = columns;
		this.rows = rows;
		this.mines = mines;
	}

	// 외부에서 사용하는 현재 게임 난이도 얻기
	public static DifficultyPreset getCurrent() {
		return current;
	}

	// 난이도 변경
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

	// 현재 무슨 난이도 인지
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
