package greer;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;

import java.awt.Color;

/*
 * TODO:
 * Implement Lichess Branch and connect to API
 * 50 move rule
 * 3 move repetition?
 * king only endgame stalemate
 * 
 * 
 * 
 * logic objects in bots might need to be created instead of passed through because of the way it stores pieces
 * update eval to deal with endgame scenarios
 * 
 * Issues:
 * 
 * 
 */

/**
 * @author Hunter Greer
 * The main entry class which creates the main window and allows the user to choose Local or Lichess 
 *
 * ***Note that all code is original and written by myself
 */
public class ProjectManager {

	private JFrame mainFrame;
	
	JComboBox<String> comboBox1 = null;
	JComboBox<String> comboBox2 = null;
	
	static Color color1S = new Color(175, 123, 63);
	static Color color2S = new Color(237, 215, 187);
	
	String FEN = null;
	
	Color color1 = color1S;
	Color color2 = color2S;
	
	Color[] colors = {color1, color2};
	
	boolean whiteSelected = true;
	
	JTextField textField = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProjectManager window = new ProjectManager();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application and initialize the window
	 */
	public ProjectManager() {
		createMainFrame();
	}
	
	/**
	 * Creates the main window
	 */
	public void createMainFrame() {
		mainFrame = new JFrame("Main Window");
		mainFrame.setBounds(100, 100, 450, 500);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		
		mainFrame.setVisible(true);
		
		displayHome();
	}

	/**
	 * Initialize the contents of the frame.
	 * Creates the buttons that the user will choose between
	 */
	private void displayHome() {
		
		JButton localOptionButton = new JButton("Local Game");
		localOptionButton.setBounds(102, 56, 224, 40);
		mainFrame.getContentPane().add(localOptionButton);
		
		JButton hostBotOptionButton = new JButton("Host CAP");
		hostBotOptionButton.setBounds(102, 110, 224, 40);
		mainFrame.getContentPane().add(hostBotOptionButton);
		
		localOptionButton.addActionListener(e -> localButtonPressed());
		hostBotOptionButton.addActionListener(e -> hostButtonPressed());
		
	}
	
	/**
	 * User clicked the local button. The page will be redirected to show local game options
	 */
	private void localButtonPressed() {
		clearFrame(mainFrame);
		
		JButton singlePlayerOptionButton = new JButton("Single Player");
		singlePlayerOptionButton.setBounds(102, 56, 224, 40);
		mainFrame.getContentPane().add(singlePlayerOptionButton);
		
		JButton twoPlayerOptionButton = new JButton("2-Player");
		twoPlayerOptionButton.setBounds(102, 106, 224, 40);
		mainFrame.getContentPane().add(twoPlayerOptionButton);
		
		//JButton analysisBoardOptionButton = new JButton("Analysis Board");
		//analysisBoardOptionButton.setBounds(102, 156, 224, 40);
		//mainFrame.getContentPane().add(analysisBoardOptionButton);
		
		//JButton boardEditorOptionButton = new JButton("Board Editor");
		//boardEditorOptionButton.setBounds(102, 156, 224, 40);
		//mainFrame.getContentPane().add(boardEditorOptionButton);
		
		JButton watchOptionButton = new JButton("Watch");
		watchOptionButton.setBounds(102, 156, 224, 40);
		mainFrame.getContentPane().add(watchOptionButton);
		
		JButton homeOptionButton = new JButton("Home");
		homeOptionButton.setBounds(102, 206, 224, 40);
		mainFrame.getContentPane().add(homeOptionButton);
		
		JButton settingsButton = new JButton("Settings");
		settingsButton.setBounds(152, 406, 124, 40);
		mainFrame.getContentPane().add(settingsButton);
		
		singlePlayerOptionButton.addActionListener(e -> singlePlayerButtonPressed());
		twoPlayerOptionButton.addActionListener(e -> twoPlayerButtonPressed());
		//analysisBoardOptionButton.addActionListener(e -> analysisBoardButtonPressed());
		//boardEditorOptionButton.addActionListener(e -> boardEditorButtonPressed());
		watchOptionButton.addActionListener(e -> watchButtonPressed());
		homeOptionButton.addActionListener(e -> homeButtonPressed());
		
		settingsButton.addActionListener(e -> settingsButtonPressed());
	}
	
	/**
	 * User clicks on the single player button. Opens options menu for the game
	 */
	private void singlePlayerButtonPressed() {
		System.out.println("Single Player Button Pressed");
		showOptionsMenu(true, true, true, "SP");
	}
	
	/**
	 * User clicks on the two-player button. Opens options menu for the game
	 */
	private void twoPlayerButtonPressed() {
		System.out.println("Two-Player Button Pressed");
		showOptionsMenu(false, false, true, "TP");
	}
	
	/**
	 * User clicks on the analysis board button. Opens analysis board
	 */
	private void analysisBoardButtonPressed() {
		System.out.println("Analysis Board Button Pressed");
	}
	
	/**
	 * User clicks on the board editor button. Opens board editor
	 */
	private void boardEditorButtonPressed() {
		System.out.println("Board Editor Button Pressed");
	}
	
	/**
	 * User clicks on the watch button. Opens options menu for the game
	 */
	private void watchButtonPressed() {
		System.out.println("Watch Button Pressed");
		showOptionsMenu(false, true, true, "W");
	}
	
	/**UNIMPLEMENTED
	 * User clicked the host button. The page will be redirected to login
	 */
	private void hostButtonPressed() {
		System.out.println("Unimplemented");
	}
	
	/**
	 * The user clicked the home button
	 */
	private void homeButtonPressed() {
		clearFrame(mainFrame);
		displayHome();
	}
	
	/**
	 * User clicks on the settings button
	 */
	private void settingsButtonPressed() {
		System.out.println("Settings button pressed");
		
		JFrame colorChooserWindow = new JFrame("Color Choosing");
		colorChooserWindow.setBounds(100, 100, 400, 350);
		colorChooserWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		colorChooserWindow.getContentPane().setLayout(null);
		
		JLabel label1 = new JLabel("Square 1"); // Text to be displayed
		label1.setBounds(160, 106, 50, 40);
		
		JLabel label2 = new JLabel("Square 2"); // Text to be displayed
		label2.setBounds(160, 156, 50, 40);
		
		JButton pickColor1 = new JButton();
		pickColor1.setBounds(220, 106, 50, 40);
		pickColor1.setBackground(color1);
		
		JButton pickColor2 = new JButton();
		pickColor2.setBounds(220, 156, 50, 40);
		pickColor2.setBackground(color2);
		
		JButton resetColors = new JButton("Reset Colors");
		resetColors.setBounds(100, 206, 200, 40);
		colorChooserWindow.getContentPane().add(resetColors);
		
		
		colorChooserWindow.getContentPane().add(pickColor1);
		colorChooserWindow.getContentPane().add(pickColor2);
		colorChooserWindow.getContentPane().add(label1);
		colorChooserWindow.getContentPane().add(label2);
		
		colorChooserWindow.setVisible(true);
		
		pickColor1.addActionListener(e -> openColorWindow(0, pickColor1));
		pickColor2.addActionListener(e -> openColorWindow(1, pickColor2));

		resetColors.addActionListener(e -> resetColors(pickColor1, pickColor2));
		
        
	}
	
	/**
	 * Resets color customization options to default
	 * @param button1 The first color button to change the color of
	 * @param button2 The second color button to change the color of
	 */
	private void resetColors(JButton button1, JButton button2) {
		colors[0] = color1S;
		colors[1] = color2S;
		button1.setBackground(color1);
		button2.setBackground(color2);
	}
	
	private void openColorWindow(int index, JButton buttonClicked) {
		
		colors[index] = JColorChooser.showDialog(null, "Choose a color", colors[index]);
		buttonClicked.setBackground(colors[index]);
		
	}
	
	/**
	 * General function to show an options menu for selecting a game type.
	 * @param userPickColor does the user have the option to pick their color
	 * @param userSelectBot does the user have the option to select a bot to play against
	 * @param fenInput does the user have the option to input a FEN String
	 * @param gameType the type of fame being played: "SP" - Single Player, "TP" - Two Player, "AN" - Analysis, "BE" - Board Editor, "W" - Watch
	 */
	private void showOptionsMenu(boolean userPickColor, boolean userSelectBot, boolean fenInput, String gameType) {
		
		System.out.println("Options menu opened");
		JFrame optionsWindow = new JFrame("Options Window");
		optionsWindow.setBounds(100, 100, 400, 350);
		optionsWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		optionsWindow.getContentPane().setLayout(null);
		
		//create JButtons for color selection
		if(userPickColor) {
			JLabel label = new JLabel("Play as:"); // Text to be displayed
			label.setBounds(160, 55, 50, 40);
			
			JButton pickColor = new JButton();
			pickColor.setBounds(220, 55, 50, 40);
			optionsWindow.getContentPane().add(pickColor);
			optionsWindow.getContentPane().add(label);
			
			pickColor.addActionListener(e -> changeColorButtonPressed(pickColor));
			
		}
		//create dropdown for bot version selection
		if(userSelectBot) {
			
			System.out.println("User selcting Bot");
			
			JLabel bottomLabel = new JLabel("Bot selection:"); // Text to be displayed
			bottomLabel.setBounds(120, 105, 90, 40);
			
			// Create a string array for the dropdown options
	        String[] options = {"BotV1", "BotV2", "BotV3"};
	        
	        // Create a JComboBox with the options array
	        comboBox1 = new JComboBox<>(options);
	        comboBox1.setBounds(210, 105, 75, 40);
	        // Add the JComboBox to the JFrame
	        optionsWindow.add(comboBox1);
	        optionsWindow.add(bottomLabel);
	        
	        if(gameType.equals("W")) {
	        	
	        	JLabel topLabel = new JLabel("White Bot:"); // Text to be displayed
	        	topLabel.setBounds(120, 55, 90, 40);
	        	
				bottomLabel.setText("Black Bot:");
	        	comboBox1.setBounds(210, 55, 75, 40);
	        	
	        	comboBox2 = new JComboBox<>(options);
	        	comboBox2.setBounds(210, 105, 75, 40);
	        	
	        	optionsWindow.add(comboBox2);
	        	optionsWindow.add(topLabel);
	        	
	        }
					
		}
		//create input String for FEN String input
		if(fenInput) {
			
			textField = new JTextField(); // 20 is the initial width of the text field
			textField.setBounds(210, 155, 75, 40);
			
			JLabel fenInputLabel = new JLabel("FEN Paste:"); // Text to be displayed
			fenInputLabel.setBounds(120, 155, 90, 40);
        	
			optionsWindow.getContentPane().add(fenInputLabel);
			optionsWindow.getContentPane().add(textField);
			
		}
		
		JButton createGameButton = new JButton("Create Game");
		createGameButton.setBounds(100, 206, 200, 40);
		optionsWindow.getContentPane().add(createGameButton);
		
		
		optionsWindow.setVisible(true);
		
		
		
		
		createGameButton.addActionListener(e -> createGameButtonPressed(gameType, optionsWindow));
	}
	
	/**
	 * Creates the game and initializes the GameGUI
	 * @param gameType A string containing the type of game to be played
	 * @param optionsWindow The JFrame to destroy after creating the game
	 */
	private void createGameButtonPressed(String gameType, JFrame optionsWindow) {
		
		String selectedBot1 = null;
		String selectedBot2 = null;
		
		if(comboBox1 != null) {
			selectedBot1 = (String) comboBox1.getSelectedItem().toString();
		}
		
		if(comboBox2 != null) {
			selectedBot2 = (String) comboBox2.getSelectedItem().toString();
		}
		
		if(textField != null) {
			FEN = textField.getText();
		}
		
		GameGUI createGame = new GameGUI(this);
		createGame.createGame(gameType, selectedBot1, selectedBot2, whiteSelected, colors[0], colors[1], FEN);
		
		//once game is created, destroy the main frame
		destroyFrame(mainFrame);
		destroyFrame(optionsWindow);
	}
	
	/**
	 * Change color selected
	 * @param button The button to change the background of
	 */
	private void changeColorButtonPressed(JButton button) {
		if(whiteSelected) {
			whiteSelected = false;
			button.setBackground(Color.BLACK);
			
		}else {
			whiteSelected = true;
			button.setBackground(Color.WHITE);
		}
		
	}
	
	/**
	 * Function clears the main window. Used when returning to the home page
	 */
	private void clearFrame(JFrame frame) {
		frame.getContentPane().removeAll();
		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();
		resetProperties();
	}
	
	private void destroyFrame(JFrame frame) {
		frame.dispose();
		resetProperties();
	}
	
	private void resetProperties() {
		whiteSelected = true;
	}
}
