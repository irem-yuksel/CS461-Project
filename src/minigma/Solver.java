package minigma;

public class Solver {
	char[][] initial;
	String[] hints;
	Object[][] props;
	int[] sLoc;
	int lengths[];
	char[][][] result;
	
	public Solver(String[] hints, int[] sLoc, int[] lengths) {
		this.hints = hints;
		this.sLoc = sLoc;
		this.lengths = lengths;
		props = new Object[10][];
		for(int i = 0; i < 10; i++) {
			props[i] = new Object[300];
		}
		
		char[][] initial = new char[5][];
		for(int i = 0; i < 5; i++) {
			initial[i] = new char[5];
		}
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				initial[i][j] = '-';
			}
		}
		
		result = new char[32][][];
		for(int i = 0; i < 32; i++) {
			result[i] = new char[5][];
			for(int j = 0; j <5;j++) {
				result[i][j] = new char[5];
			}
		}
		
		Finder f = new Finder();
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
//		solve(initial, 0);

	}

	public void solve(char[][] board, int clue) {
//		if (boardFull(board)) {
//			// print board
//			System.out.println("Done");
//		} else {
//			int sX = sLoc[clue] % 5;
//			int sY = sLoc[clue] / 5;
			int sY, sX;
			char[][][] arr = new char[32][][];

			for(int j = 0; j < 32; j ++) { //across
				arr[j] = new char[5][];
				for(int i = 0; i < 5; i++) {
					arr[j][i] = new char[5];
					for(int k = 0; k < 5; k++) {
//						arr[j][i][k] = props[i][k].toString().charAt(k);
					}
				}
			}
			
			result = arr;

				
//				for (int i = 0; i < lengths[j]; i++) {
//					for (int k = 0; k < props[j].length; k++) {
//						if (board[sX][sY + i] == '-'
//								|| board[sX][sY + i] == props[j][k].toString().charAt(i)) {
//							board[sX][sY + i] = ((PossibleSolution) props[j][k]).getWord().charAt(i);
//						}
//					}
//				}
				
			
//			if (clue < 5) // down
//			{
//				for (int i = 0; i < lengths[clue]; i++) {
//					for (int k = 0; k < props[clue].length; k++) {
//						if (board[sX][sY + i] == '-'
//								|| board[sX][sY + i] == props[clue][k].toString().charAt(i)) {
//							board[sX][sY + i] = ((PossibleSolution) props[clue][k]).getWord().charAt(i);
//							System.out.println(board[sX][sY + i] + "fffffffff");
//						} else
//							return;
//					}
//					solve(board, clue+1);
//
//				}
//			}
//			if (clue >= 5) // across
//			{
//				for (int i = 0; i < lengths[clue]; i++) {
//					for (int k = 0; k < props[clue].length; k++) {
//
//						if (board[sX + i][sY] == '-'
//								|| board[sX + i][sY] == props[clue][k].toString().charAt(i)) {
//							board[sX + i][sY] = ((PossibleSolution) props[clue][k]).getWord().charAt(i);
//							solve(board, ++clue);
//							System.out.println(board[sX + i][sY] + "dddd");
//						} else
//							return;
//					}
//				}
//			}
//		}

	}

	public boolean boardFull(char[][] b) {
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++)
				if ( b[i][j] == '-' ) 
					return false;
		initial = b;
		return true;
	}

	public char[][][] getResultBoard(){
		return result;
	}
}
