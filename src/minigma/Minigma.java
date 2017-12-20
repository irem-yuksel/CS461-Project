package minigma;

import java.io.IOException;

public class Minigma {
	
	UserInterface inter;

	public Minigma() throws IOException{
		boolean isOnline= false;
		if(isOnline)
			System.out.println("Getting todays puzzle");
		else
			System.out.println("Getting older puzzle");

		this.inter = new UserInterface(isOnline);
	}

	public static void main(String[] args) throws IOException{
		new Minigma();
	}
}
