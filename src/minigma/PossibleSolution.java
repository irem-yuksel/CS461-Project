package minigma;

public class PossibleSolution {
	
	//properties
	private String word;
	private int count;
	
	public PossibleSolution(String word)
	{
		setWord(word);
		count = 0; 
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public String toString()
	{
		return getWord() + "," + getCount();
		
	}
}
