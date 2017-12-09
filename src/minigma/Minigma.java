package minigma;

import java.io.IOException;

public class Minigma {
	
	UserInterface inter;

	public Minigma() throws IOException{
		boolean isOnline= true;
		this.inter = new UserInterface(isOnline);
	}

	public static void main(String[] args) throws IOException{
		new Minigma();
	}
}
