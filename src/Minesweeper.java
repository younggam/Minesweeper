public class Minesweeper {
	public static void main(String[] args) {
		new SetupUI();
		GameState.initialize(DifficultyPreset.current.height, DifficultyPreset.current.width);
		while(true) {
			
		}
	}
}

import java.util.Scanner;
import java.util.Random;


public class Minesweeper {
    private int[][] board;
    private boolean[][] found;
    private boolean[][] mine_cell;
    private int rows;
    private int columns;
    private int mines;
     
    
    public Minesweeper(int rows, int columns, int mines) {
        this.rows = rows; // 시작하기 전 생성자로 사이즈와 지뢰 개수 지정, 이걸로 난이도 조절 가능
        this.columns = columns;
        this.mines = mines;
        board = new int[rows][columns]; // board에서 지뢰는 -1, 지뢰가 아닌 경우 근처 지뢰의 개수를 보여준다. 
        found = new boolean[rows][columns]; // 사용자가 찾은 경우(그 점의 정보를 알 때)True, 정보를 모를 때 False 
        mine_cell = new boolean[rows][columns];
    }

    public void start() {
        Scanner sc = new Scanner(System.in);
       // 보드 초기화
        initializeBoard(); // 일단 board 초기화 
        
        // 게임 루프
        boolean gameOver = false;
        while (!gameOver) {
            // 보드 출력
            printBoard();

            // 사용자 입력 받기
            System.out.print("row column 순으로 입력하시오: ");
            int row = sc.nextInt();
            int col = sc.nextInt();
            
            // 선택한 칸 열기
            foundCell(row, col);

            // 게임 종료 여부 확인
            if (found[row][col]) {
                if (board[row][col] == -1) {
                    System.out.println("Game Over! You hit a mine!");
                    gameOver = true;
                } else if (checkWin()) {
                    System.out.println("Congratulations! You win!");
                    gameOver = true;
                }
            }
        }
    }

    private void initializeBoard() { 
       Random random = new Random(); //랜덤 객체 생성
        random.setSeed(System.currentTimeMillis()); // 시드 설정을 따로 하지 않음
        
        // 보드 초기화 (먼저 0으로 채우기)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                board[i][j] = 0;
                found[i][j] = false;
                mine_cell[i][j] = false;
            }
        }

        // 지뢰 배치
        int count = 0; 
        // mine의 개수만큼 random하게 배치 
        while (count < mines) {
            int row = (int) (random.nextInt(rows));
            int col = (int) (random.nextInt(columns));
            if (board[row][col] != -1) {
                board[row][col] = -1;
                mine_cell[row][col] = true; // mine_found 에서는 지뢰가 있는 곳이 1, 지뢰가 없는 곳은 0 
                count++;
            }
        }

        // 주변 지뢰 개수 계산
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (board[i][j] != -1) { // 모든 grid의 점들에 대해 지뢰가 아닌 주변의 지뢰의 개수를 센다
                    int countMines = countSurroundingMines(i, j);
                    board[i][j] = countMines; // board에서 처음에 빈 칸은 0이고 지뢰는 -1인데, 이 과정을 거치면 주변에 있는 지뢰 개수로 board가 변한다. 
                }
            }
        }
    }

    private int countSurroundingMines(int row, int col) { // 자기를 둘러싼 cell에 대해서 지뢰의 개수를 찾아 그 값을 return 한다. 
        int count = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < rows && j >= 0 && j < columns && board[i][j] == -1) {
                    count++;
                }
            }
        }
        return count;
    }

    private void foundCell(int row, int col) { 
        if (row < 0 || row >= rows || col < 0 || col >= columns || found[row][col]) {
            return;
        }

        found[row][col] = true;

        if (board[row][col] == 0) {
            // 주변 8칸에 대해 재귀호출을 한다. 여기서 위에 있는 if 조건을 만족하지 못하는 것(1. board의 범위를 벗어남 2. 이미 밝혀진 cell임)은 found가 false인 상태가 유지된다. 
            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = col - 1; j <= col + 1; j++) {
                    foundCell(i, j);
                }
            }
        }
    }

    private boolean checkWin() { // 
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (!found[i][j]) { // 찾아지지 않은 cell, 즉 10x10에서 100에서 시작해서 지뢰 10개를 제외한 90개의 cell을 다 찾으면 count와 mines의 값이 같아진다. 
                    count++;
                }
            }
        }
        return count == mines;
    }

    private void printBoard() {
        System.out.println("Current board:");
        System.out.print("   ");
        for (int i=0; i< columns; i++) {
           System.out.print(i+" ");
        }
        System.out.print("\n");
        
        for (int i = 0; i < rows; i++) {
           if(i >= 10) System.out.print(i+" ");
           else System.out.print(i+"  ");
            for (int j = 0; j < columns; j++) {
                if (found[i][j]) { // 이미 found 된 각각의 cell들은 보여준다. 
                    if (board[i][j] == -1) { // 지뢰 
                        System.out.print("* "); // 지뢰는 *로 표현
                    } else { 
                       if(j>=10) System.out.print(board[i][j] + "  "); // 깐 부분은 근처 지뢰의 개수로 표현한다.
                       else System.out.print(board[i][j] + " "); // 깐 부분은 근처 지뢰의 개수로 표현한다.
                    }
                } else { // 밝혀지지 않은 cell은 .으로 표현
                   if(j>=10) System.out.print(".  "); // 깐 부분은 근처 지뢰의 개수로 표현한다.
                   else System.out.print(". "); // 깐 부분은 근처 지뢰의 개수로 표현한다. 
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
       Scanner sc = new Scanner(System.in);
       
       System.out.print("난이도 선택(Easy, Medium, Hard): ");
       String level = sc.nextLine();
       if(level.equals("Easy")) {
          Minesweeper game = new Minesweeper(9, 9, 10);
          game.start();
       }
       else if(level.equals("Medium")) {
          Minesweeper game = new Minesweeper(16, 16, 40);
          game.start();
       }
       else if(level.equals("Hard")) {
          Minesweeper game = new Minesweeper(16, 30, 99);
          game.start();
       }
    }
}
