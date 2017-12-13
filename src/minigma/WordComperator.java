package minigma;

import java.util.Comparator;

public class WordComperator implements Comparator<PossibleSolution> {

	@Override
	public int compare(PossibleSolution arg0, PossibleSolution arg1) {
		
		if(arg0.getCount() >= arg1.getCount() && arg0.getWord().compareTo(arg1.getWord()) != 0 )
			return -1;
		if(arg0.getCount() < arg1.getCount() && arg0.getWord().compareTo(arg1.getWord()) != 0)
			return 1;
		if(arg0.getCount() == arg1.getCount() && arg0.getWord().compareTo(arg1.getWord()) == 0)
			return 0;
		return 1;
	}
}
