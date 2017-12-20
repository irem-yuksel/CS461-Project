package minigma;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.JTextComponent;

public final class UserInterface extends JPanel implements ActionListener {

	private static final String JButton = null;
	String url = "https://www.nytimes.com/crosswords/game/mini?page=mini&type=mini&date=&_r=0";
	final String DATE_FORMAT_NOW = "dd-MM-yyyy";
	Document document = Jsoup.connect(url).get();
	String html = document.html();
	Document doc = Jsoup.parse(html);
	int fromUp = 130, fromLeft = 350, width = 70;
	JTextArea[] clues = new JTextArea[10];
	JLabel title, across, down;
	JButton solve, reveal, reset, right, left, reset2, quit;
	JPanel clueAcross = new JPanel();
	JPanel clueDown = new JPanel();
	String[] clueArray = new String[10];
	ArrayList<Point> points;
	String[] myArr;
	String[] words;
	BufferedImage originalPuzzle;
	ArrayList<Integer> arrayBlack, arrayWhite, availableList;
	String dayname = "04-10", isReveal = "f";
	boolean isOnline, isSolve = false;
	int isSolving = -1;
	JButton[] rectangles;
	Rectangle selected;
	char c;
	double x, y;
	char[] enteredChars;
	JButton button11, button12, button13, button14, button15, button21, button22, button23, button24, button25;
	JButton button31, button32, button33, button34, button35, button41, button42, button43, button44, button45;
	JButton button51, button52, button53, button54, button55;
	MyKeyListener keyListener;
	boolean toSolve = false;
	Object[][] predictions;
	Finder f;
	int[] lengths;
	int[] startingLocations;
	boolean found = false;
	boolean completed = false;		
	char[][] finalBoard;
	Solver solver;
	
	public UserInterface(boolean isOnline) throws IOException {

		this.isOnline = isOnline;
		points = new ArrayList<>();
		words = new String[10];
		enteredChars = new char[25];
		finalBoard = new char[5][5];
		
		setLayout(null);
		setBackground(new Color(130, 130, 130));
		JFrame frame = new JFrame();
		title = new JLabel("MINIGMA");
		title.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 50));
		title.setForeground(Color.white);
		title.setBounds(400, 0, 500, 120);
		rectangles = new JButton[25];

		across = new JLabel("Across");
		across.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 25));
		across.setForeground(Color.BLACK);
		across.setAlignmentX(Component.CENTER_ALIGNMENT);

		reveal = new JButton("Reveal");
		reveal.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 25));
		reveal.setForeground(Color.white);
		reveal.setBackground(Color.WHITE);
		reveal.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		reveal.setFocusPainted(false);
		reveal.setBounds(1200, 400, 100, 50);
		reveal.addActionListener(this);

		reset = new JButton("Reset");
		reset.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 25));
		reset.setForeground(Color.white);
		reset.setBackground(Color.WHITE);
		reset.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		reset.setFocusPainted(false);
		reset.setBounds(1200, 500, 100, 50);
		reset.addActionListener(this);

		down = new JLabel("Down");
		down.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 25));
		down.setForeground(Color.BLACK);
		down.setAlignmentX(Component.CENTER_ALIGNMENT);

		solve = new JButton("Solve");
		solve.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 25));
		solve.setForeground(Color.white);
		solve.setBackground(Color.WHITE);
		solve.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		solve.setFocusPainted(false);
		solve.setBounds(fromLeft + 460, fromUp + 235, 100, 50);
		solve.addActionListener(this);

		reset2 = new JButton("Reset");
		reset2.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 25));
		reset2.setForeground(Color.white);
		reset2.setBackground(Color.WHITE);
		reset2.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		reset2.setFocusPainted(false);
		reset2.setBounds(fromLeft + 460, fromUp + 290, 100, 50);
		reset2.addActionListener(this);

		right = new JButton("-->");
		right.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 20));
		right.setForeground(Color.white);
		right.setBackground(Color.WHITE);
		right.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		right.setFocusPainted(false);
		right.setBounds(fromLeft + 370, fromUp + 130, 50, 50);
		right.addActionListener(this);

		left = new JButton("<--");
		left.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 20));
		left.setForeground(Color.white);
		left.setBackground(Color.WHITE);
		left.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		left.setFocusPainted(false);
		left.setBounds(fromLeft - 60, fromUp + 130, 50, 50);
		left.addActionListener(this);

		quit = new JButton("Quit");
		quit.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 20));
		quit.setForeground(Color.white);
		quit.setBackground(Color.WHITE);
		quit.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		quit.setFocusPainted(false);
		quit.setBounds(1150, 600, 100, 50);
		quit.addActionListener(this);

		clueAcross.setLayout(new BoxLayout(clueAcross, BoxLayout.Y_AXIS));
		clueAcross.setBackground(Color.white);
		clueAcross.setBounds(50, fromUp + 360, 500, 300);
		clueAcross.add(across);

		clueDown.setLayout(new BoxLayout(clueDown, BoxLayout.Y_AXIS));
		clueDown.setBackground(Color.white);
		clueDown.setBounds(580, fromUp + 360, 500, 300);
		clueDown.add(down);

		myArr = getClues();

		for (int i = 0; i < 5; i++) {
			clueAcross.add(Box.createRigidArea(new Dimension(0, 3)));
			clues[i] = new JTextArea(setClue(myArr, i));
			clues[i].setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 20));
			clues[i].setForeground(Color.BLACK);
			clues[i].setBackground(Color.white);
			clues[i].setWrapStyleWord(true);
			clues[i].setLineWrap(true);
			clueAcross.add(clues[i]);
		}

		for (int i = 5; i < 10; i++) {
			clueDown.add(Box.createRigidArea(new Dimension(0, 3)));
			clues[i] = new JTextArea(setClue(myArr, i));
			clues[i].setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 20));
			clues[i].setForeground(Color.BLACK);
			clues[i].setBackground(Color.white);
			clues[i].setWrapStyleWord(true);
			clues[i].setLineWrap(true);
			clueDown.add(clues[i]);
		}

		add(quit);
		add(reveal);
		// add(reset);
		add(solve);
		add(reset2);
		add(title);
		// add(right);
		// add(left);
		add(clueAcross);
		add(clueDown);

		rectangles[0] = button11;
		rectangles[1] = button12;
		rectangles[2] = button13;
		rectangles[3] = button14;
		rectangles[4] = button15;
		rectangles[5] = button21;
		rectangles[6] = button22;
		rectangles[7] = button23;
		rectangles[8] = button24;
		rectangles[9] = button25;
		rectangles[10] = button31;
		rectangles[11] = button32;
		rectangles[12] = button33;
		rectangles[13] = button34;
		rectangles[14] = button35;
		rectangles[15] = button41;
		rectangles[16] = button42;
		rectangles[17] = button43;
		rectangles[18] = button44;
		rectangles[19] = button45;
		rectangles[20] = button51;
		rectangles[21] = button52;
		rectangles[22] = button53;
		rectangles[23] = button54;
		rectangles[24] = button55;

		setArrayBlack();
		keyListener = new MyKeyListener();

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (!arrayBlack.contains((i * 5) + j + 1)) {
					rectangles[(i * 5) + j] = new JButton("");
					rectangles[(i * 5) + j].setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 20));
					rectangles[(i * 5) + j].setForeground(Color.black);
					rectangles[(i * 5) + j].setBackground(Color.WHITE);
					rectangles[(i * 5) + j].setBorder(BorderFactory.createLineBorder(Color.WHITE));
					rectangles[(i * 5) + j].setFocusPainted(false);
					rectangles[(i * 5) + j].setBounds(fromLeft + j * width, fromUp + i * width, width, width);
					rectangles[(i * 5) + j].addActionListener(this);
					rectangles[(i * 5) + j].addKeyListener(keyListener);
				} else
					rectangles[i * 5 + j] = null;
			}
		}

		for (int i = 0; i < 25; i++) {
			if (rectangles[i] != null)
				add(rectangles[i]);
		}
		
		solver = new Solver(myArr, startingLocations, lengths);

		frame.add(this);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);
		frame.setTitle("Minigma");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

	}

	@Override
	public void paintComponent(Graphics page) {
		try {
			super.paintComponent(page);
			if (!isOnline) {
				try {
					originalPuzzle = ImageIO.read(new File("resource/" + dayname + isReveal + ".png"));
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			else {
				dayname = "14-12";
			}

			page.drawImage(originalPuzzle, 1000, 50, 400, 300, this);
			Graphics2D g2 = (Graphics2D) page;
			g2.setStroke(new BasicStroke(3));

			// draw puzzle board
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					g2.setColor(Color.WHITE);
					page.fillRect(fromLeft + j * width, fromUp + i * width, width, width);
					g2.setColor(Color.BLACK);
					page.drawRect(fromLeft + j * width, fromUp + i * width, width, width);
				}
			}

			// paint black squares
			setArrayBlack();
			g2.setColor(Color.black);
			int k, m;
			for (int i = 0; i < arrayBlack.size(); i++) {
				if (arrayBlack.get(i) % 5 == 0) {
					k = arrayBlack.get(i) / 5;
					m = 5;
				} else {
					k = arrayBlack.get(i) / 5 + 1;
					m = arrayBlack.get(i) % 5;
				}
				page.fillRect(fromLeft + (m - 1) * width, fromUp + (k - 1) * width, width, width);
			}

			// write clue numbers
			setAvailableSquareList();
			g2.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 22));
			g2.setColor(Color.black);
			arrayWhite = new ArrayList<>();

			startingLocations = new int[availableList.size()+2];
			lengths = new int[availableList.size()+2];
			
			for (int i = 1; i < 26; i++) {
				if (!arrayBlack.contains(i)) {
					arrayWhite.add(i);
				}
			}
			for (int i = 0; i < availableList.size(); i++) {
				if (availableList.get(i) % 5 == 0) {
					k = availableList.get(i) / 5;
					m = 5;
				} else {
					k = availableList.get(i) / 5 + 1;
					m = availableList.get(i) % 5;
				}
				page.drawString("" + (i + 1), fromLeft + (m - 1) * width + 10, fromUp + (k - 1) * width + 20);

				// System.out.println((i+1) + "c");
				// System.out.println(m-1);
				// System.out.println(k-1);

				// startingLocations[i] = ((m-1)*5) + (k-1);
			}
			//
			// for(int i = 0; i < startingLocations.length; i++) {
			// for( int j = startingLocations[j]; j < 5; j++) {
			// if()
			// }
			// length[i] =
			// }

			if (dayname.equals("11-10")) {
				// across
				startingLocations[0] = 0;
				startingLocations[1] = 5;
				startingLocations[2] = 10;
				startingLocations[3] = 15;
				startingLocations[4] = 20;

				// down
				startingLocations[5] = 0;
				startingLocations[6] = 1;
				startingLocations[7] = 2;
				startingLocations[8] = 3;
				startingLocations[9] = 4;


				//across
				lengths[0] = 5;
				lengths[1] = 5;
				lengths[2] = 5;
				lengths[3] = 5;
				lengths[4] = 5;
				
				//down
				lengths[5] = 5;
				lengths[6] = 5;
				lengths[7] = 5;
				lengths[8] = 5;
			}
			else {
				// across
				startingLocations[0] = 1;
				startingLocations[1] = 6;
				startingLocations[2] = 10;
				startingLocations[3] = 15;
				startingLocations[4] = 20;

				// down
				startingLocations[5] = 1;
				startingLocations[6] = 2;
				startingLocations[7] = 3;
				startingLocations[8] = 4;
				startingLocations[9] = 10;

				//across
				lengths[0] = 4;
				lengths[1] = 4;
				lengths[2] = 5;
				lengths[3] = 5;
				lengths[4] = 5;
				
				//down
				lengths[5] = 5;
				lengths[6] = 5;
				lengths[7] = 5;
				lengths[8] = 3;
				lengths[9] = 3;
			}
			
			predictions = new Object[availableList.size()][300];

			g2.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 13));
			g2.setFont(new java.awt.Font("Times New Roman", Font.ITALIC, 45));
			if (isSolving == -1) {
				page.drawString("", fromLeft + 460, fromUp + 370);
			} else if (isSolving == 0) {
				page.drawString("Solving...", fromLeft + 460, fromUp + 370);
			} else if (isSolving == 1) {
				page.drawString("Solved!", fromLeft + 460, fromUp + 385);
			}

			g2.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 25));

			if (!isOnline) {
				page.drawString(dayname + "-2017", 30, 50);
			} else {
				page.drawString(now(), 30, 50);
			}

			// repaint();
		} catch (IOException ex) {
			Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
		}
		
//		String[] hints;
//		hints = getClues();
		if (toSolve) {
			finalBoard = solver.getResultBoard();
		}

		
		
//			Solver solver;
//			try {
//				if(!found) {
//					for (int i = 0; i < hints.length; i++) {
//						System.out.println("Solving " + (i + 1) + "th hint");
//						predictions[i] = f.findSolutions(hints[i], lengths[i]);
//						for(Object o : predictions[i]) {
//							System.out.println(o.toString());
//						}
//						if(i == 9) {
//							found = true;
//						}
//					}
//					solver = new Solver(finalBoard, predictions, startingLocations, lengths);
//				}
//					
//				
////				if(found && !completed) {
////					int hintNo = 0;
////					for (int i = 0; i < 5; i++) {
////						for (int j = 0; j < 5; j++) {
////							for(Object o : predictions[hintNo]) {
////								for(int a = 0 ; a < o.toString().length(); a++) {
////									//put things on finalBoard
////									//fromLeft + j * width, fromUp + i * width, width, width
////								}
////							}
////						}
////					}
////					completed = true;
////				}
//				
//			}catch (Exception e) {
//			}
//
//			// f.findSolutions("One who's deep in the weeds of policy",4);
//
//		}

	}

	@Override
	public void actionPerformed(ActionEvent event) {

		Object source = event.getSource();
		System.out.println("dd" + source.toString());

		if (source == rectangles[0]) {
			rectangles[0].setText(c + "");
			enteredChars[0] = c;
			c = (Character) null;
		}
		if (source == rectangles[1]) {
			rectangles[1].setText(c + "");
			enteredChars[1] = c;
			c = (Character) null;
		}
		if (source == rectangles[2]) {
			rectangles[2].setText(c + "");
			enteredChars[2] = c;
			c = (Character) null;
		}
		if (source == rectangles[3]) {
			rectangles[3].setText(c + "");
			enteredChars[3] = c;
			c = (Character) null;
		}
		if (source == rectangles[4]) {
			rectangles[4].setText(c + "");
			enteredChars[4] = c;
			c = (Character) null;
		}
		if (source == rectangles[5]) {
			rectangles[5].setText(c + "");
			enteredChars[5] = c;
			c = (Character) null;
		}
		if (source == rectangles[6]) {
			rectangles[6].setText(c + "");
			enteredChars[6] = c;
			c = (Character) null;
		}
		if (source == rectangles[7]) {
			rectangles[7].setText(c + "");
			enteredChars[7] = c;
			c = (Character) null;
		}
		if (source == rectangles[8]) {
			rectangles[8].setText(c + "");
			enteredChars[8] = c;
			c = (Character) null;
		}
		if (source == rectangles[9]) {
			rectangles[9].setText(c + "");
			enteredChars[9] = c;

			c = (Character) null;
		}
		if (source == rectangles[10]) {
			rectangles[10].setText(c + "");
			enteredChars[10] = c;

			c = (Character) null;
		}
		if (source == rectangles[11]) {
			rectangles[11].setText(c + "");
			enteredChars[11] = c;

			c = (Character) null;
		}
		if (source == rectangles[12]) {
			rectangles[12].setText(c + "");
			enteredChars[12] = c;

			c = (Character) null;
		}
		if (source == rectangles[13]) {
			rectangles[13].setText(c + "");
			enteredChars[13] = c;

			c = (Character) null;
		}
		if (source == rectangles[14]) {
			rectangles[14].setText(c + "");
			enteredChars[14] = c;

			c = (Character) null;
		}
		if (source == rectangles[15]) {
			rectangles[15].setText(c + "");
			enteredChars[15] = c;

			c = (Character) null;
		}
		if (source == rectangles[16]) {
			rectangles[16].setText(c + "");
			enteredChars[16] = c;

			c = (Character) null;
		}
		if (source == rectangles[17]) {
			rectangles[17].setText(c + "");
			enteredChars[17] = c;

			c = (Character) null;
		}
		if (source == rectangles[18]) {
			rectangles[18].setText(c + "");
			enteredChars[18] = c;

			c = (Character) null;
		}
		if (source == rectangles[19]) {
			rectangles[19].setText(c + "");
			enteredChars[19] = c;

			c = (Character) null;
		}
		if (source == rectangles[20]) {
			rectangles[20].setText(c + "");
			enteredChars[20] = c;

			c = (Character) null;
		}
		if (source == rectangles[21]) {
			rectangles[21].setText(c + "");
			enteredChars[21] = c;

			c = (Character) null;
		}
		if (source == rectangles[22]) {
			rectangles[22].setText(c + "");
			enteredChars[22] = c;

			c = (Character) null;
		}
		if (source == rectangles[23]) {
			rectangles[23].setText(c + "");
			enteredChars[23] = c;

			c = (Character) null;
		}
		if (source == rectangles[24]) {
			rectangles[24].setText(c + "");
			enteredChars[24] = c;

			c = (Character) null;
		}

		if (source == quit) {
			Object[] options = { "Close", "Cancel" };
			int n = JOptionPane.showOptionDialog(null, "What do you want to do?", "Choose an option!",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.DEFAULT_OPTION, null, options, options[1]);
			if (n == 0) {
				((Window) getRootPane().getParent()).dispose();
			} else if (n == 1) {
			}
		}
		if (source == right) {
			isSolving = -1;
			isReveal = "f";
			for (int i = 0; i < 10; i++) {
				words[i] = null;
			}
			if ("02-10".equals(dayname)) {
				dayname = "03-10";
			} else if ("03-10".equals(dayname)) {
				dayname = "04-10";
			} else if ("04-10".equals(dayname)) {
				dayname = "05-10";
			} else if ("05-10".equals(dayname)) {
				dayname = "10-10";
			} else if ("10-10".equals(dayname)) {
				dayname = "11-10";
			} else if ("11-10".equals(dayname)) {
				dayname = "13-10";
			} else if ("13-10".equals(dayname)) {
				dayname = "16-10";
			} else if ("16-10".equals(dayname)) {
				dayname = "23-10";
			} else if ("23-10".equals(dayname)) {
				dayname = "02-10";
			}
			try {
				myArr = getClues();
				setAvailableSquareList();
			} catch (IOException ex) {
				Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
			}
			for (int i = 0; i < 5; i++) {
				clues[i].setText("	" + setClue(myArr, i));
			}
			for (int i = 5; i < 10; i++) {
				clues[i].setText("	" + setClue(myArr, i));
			}
		}
		if (source == left) {
			isSolving = -1;
			isReveal = "f";
			for (int i = 0; i < 10; i++) {
				words[i] = null;
			}

			if ("02-10".equals(dayname)) {
				dayname = "23-10";
			} else if ("23-10".equals(dayname)) {
				dayname = "16-10";
			} else if ("16-10".equals(dayname)) {
				dayname = "13-10";
			} else if ("13-10".equals(dayname)) {
				dayname = "11-10";
			} else if ("11-10".equals(dayname)) {
				dayname = "10-10";
			} else if ("10-10".equals(dayname)) {
				dayname = "05-10";
			} else if ("05-10".equals(dayname)) {
				dayname = "04-10";
			} else if ("04-10".equals(dayname)) {
				dayname = "03-10";
			} else if ("03-10".equals(dayname)) {
				dayname = "02-10";
			}
			try {
				myArr = getClues();
				setAvailableSquareList();
			} catch (IOException ex) {
				Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
			}
			for (int i = 0; i < 5; i++) {
				clues[i].setText("	" + setClue(myArr, i));
			}
			for (int i = 5; i < 10; i++) {
				clues[i].setText("	" + setClue(myArr, i));
			}
		}
		if (source == reveal) {
			isReveal = "t";
		}
		if (source == reset) {
			isReveal = "f";
		}
		if (source == solve) {
			toSolve = true;
		}
	}

	// getting clues
	String[] getClues() throws IOException {
		int j = 0;
		String[] clues = new String[10];
		if (isOnline) {
//			String url = "https://www.nytimes.com/crosswords/game/mini?page=mini&type=mini&date=&_r=0";
//			Document document = Jsoup.connect(url).get();
//			String html = document.html();
//			Document doc = Jsoup.parse(html);
//			Elements elements = doc.getElementsByClass("Clue-text--3lZl7");
//			Elements labels = doc.getElementsByClass("Clue-label--2IdMY");
//			for (Element e : elements) {
//				clues[j] = labels.get(j).text() + ") " + e.text();
//				j++;
//				if (j == 10) {
//					break;
//				}
//			}
			try {
				dayname = "14-12";
				File f = new File("resource/" + dayname + ".txt");
				FileReader fis = new FileReader(f);
				BufferedReader br = new BufferedReader(fis);

				String strLine;
				while ((strLine = br.readLine()) != null) {
					if (!strLine.equals("Across:") && !strLine.equals("Down:") && !strLine.equals("")) {
						clues[j] = strLine.substring(0, strLine.length());
						j++;
						if (j == 10) {
							break;
						}
					}
				}
				br.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		} else {
			try {
				File f = new File("resource/" + dayname + ".txt");
				FileReader fis = new FileReader(f);
				BufferedReader br = new BufferedReader(fis);

				String strLine;
				while ((strLine = br.readLine()) != null) {
					if (!strLine.equals("Across:") && !strLine.equals("Down:") && !strLine.equals("")) {
						clues[j] = strLine.substring(0, strLine.indexOf('/'));
						j++;
						if (j == 10) {
							break;
						}
					}
				}
				br.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
		return clues;
	}

	String setClue(String[] s, int i) {
		return s[i];
	}

	void setArrayBlack() throws IOException {
		arrayBlack = new ArrayList<>();
		if (isOnline) {
			Elements divs = doc.getElementsByAttribute("fill");
			int black = 0;
			int white = 0;
			for (Element div : divs) {
				if (div.toString().contains("none") && (black + white) < 25) {
					white++;
				}
				if (div.toString().contains("black") && (black + white) < 25) {
					black++;
					arrayBlack.add(black + white);
				}
			}
			try {
				PrintWriter writer = new PrintWriter("this.txt", "UTF-8");
				for (int i = 0; i < arrayBlack.size(); i++) {
					writer.println(arrayBlack.get(i));
				}
				writer.close();
			} catch (IOException e) {
			}

		} else {
			try {
				if(!isOnline) {
					File f = new File("resource/" + dayname + "-box.txt");
					FileReader fis = new FileReader(f);
					BufferedReader br = new BufferedReader(fis);

					String strLine;
					while ((strLine = br.readLine()) != null) {
						for (int j = 0; j < strLine.length(); j++) {
							if (strLine.charAt(j) == '1') {
								arrayBlack.add(j + 1);
							}
						}
					}
					br.close();
				}
				
			} catch (Exception e) {
			}
		}
	}

	// Getting available list places to put number into puzzle shape
	void setAvailableSquareList() throws IOException {
		setArrayWhite();
		availableList = new ArrayList<>();
		for (int j = 1; j < 11; j++) {
			int smallest = 0;
			int[] arr = getArr(j);
			for (int i = 4; i >= 0; i--) {
				if (arrayWhite.contains(arr[i])) {
					smallest = arr[i];
				}
			}
			if (!availableList.contains(smallest)) {
				availableList.add(smallest);
			}
		}
		Collections.sort(availableList);
	}

	// Getting row and column arrays
	int[] getArr(int num) {
		int first;
		int[] arr = new int[5];
		if (num < 6) {
			first = 5 * (num - 1) + 1;
			for (int i = 0; i < 5; i++) {
				arr[i] = first++;
			}
		} else {
			first = (num - 5);
			for (int i = 0; i < 5; i++) {
				arr[i] = first += 5;
				arr[i] = arr[i] - 5;
			}
		}
		return arr;
	}

	void setArrayWhite() throws IOException {
		arrayWhite = new ArrayList<>();
		for (int i = 0; i < 25; i++) {
			if (!arrayBlack.contains(i)) {
				arrayWhite.add(i);
			}
		}
	}

	String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

	public class MyKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			c = e.getKeyChar();
			System.out.println(c);
		}
	}
}
