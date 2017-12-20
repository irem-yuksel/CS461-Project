package minigma;

public class Solver {
	char[][] initial;
	String[] hints;
	Object[][] props;
	int[] sLoc;
	int lengths[];

	public Solver(String[] hints, int[] sLoc, int[] lengths) {
		this.hints = hints;
		this.sLoc = sLoc;
		this.lengths = lengths;
		props = new Object[10][300];
		char[][] initial = new char[5][5];
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				initial[i][j] = Character.MIN_VALUE;
			}
		}
		
		Finder f = new Finder();
		Solver solver;
		try {
			for (int i = 0; i < 10; i++) {
				System.out.println("Solving " + (i + 1) + "th hint");
				props[i] = f.findSolutions(hints[i], lengths[i]);
				for (Object o : props[i]) {
					System.out.println(o.toString());
				}
			}

		} catch (Exception e) {
		}
		solve(initial, 0);

	}

	public void solve(char[][] board, int clue) {
		if (boardFull(board)) {
			// print board
			System.out.println("Done");

			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					System.out.print(board[i][j]);
					System.out.println();
				}
			}
			// System.out.println(board[]);

		} else {
			int sX = sLoc[clue] % 5;
			int sY = sLoc[clue] / 5;

			if (clue < 5) // across
			{
				for (int i = 0; i < lengths[clue]; i++) {
					for (int k = 0; k < 300; k++) {
						if (board[sX][sY + i] != Character.MIN_VALUE
								&& board[sX][sY + i] == props[clue][k].toString().charAt(i)) {
							board[sX][sY + i] = ((PossibleSolution) props[clue][k]).getWord().charAt(i);
							System.out.println(board[sX][sY + i] + "fffffffff");
							solve(board, clue++);
						} else
							return;
					}
				}
			}
			if (clue >= 5) // down
			{
				for (int i = 0; i < lengths[clue]; i++) {
					for (int k = 0; k < 300; k++) {

						if (board[sX + i][sY] != Character.MIN_VALUE
								&& board[sX][sY + i] == props[clue][k].toString().charAt(i)) {
							board[sX + i][sY] = ((PossibleSolution) props[clue][k]).getWord().charAt(i);
							solve(board, clue++);
							System.out.println(board[sX][sY + i] + "dddd");
						} else
							return;
					}
				}
			}
		}

	}

	public boolean boardFull(char[][] b) {
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++)
				if (b[i][j] == Character.MIN_VALUE )
					return false;
		return true;
	}

	public char[][] getResultBoard(){
		return initial;
	}
}
