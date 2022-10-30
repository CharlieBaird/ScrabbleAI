package application;

import application.Logic.Board;
import application.Logic.Play;
import application.Logic.Player;
import application.Logic.TileBag;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ScrabbleView extends BorderPane
{
	private ScrabbleBoard scrabbleBoard;
	private Board board;
	
	private Player bot;
	private Player player;
	
	private Hand botHand;
	private Hand playerHand;
	
	boolean isPlayersTurn = false;
	
	public ScrabbleView()
	{
		board = initGame();
		
		// Build center vBox
		this.setCenter(buildCenterPanel());
		
		Button nextMoveButton = new Button("Next move");
		nextMoveButton.setOnAction((event) -> {
			bot.playBestPlay();
			update();
		});
		
		this.setBottom(nextMoveButton);
		
		Button newGameButton = new Button("Reset game");
		newGameButton.setOnAction((event) -> {
			board = initGame();
			this.setCenter(buildCenterPanel());
			update();
		});
		
		this.setLeft(newGameButton);
		
		this.setRight(buildRightPanel());
		
		update();
	}
	
	private VBox buildCenterPanel()
	{
		VBox centerPane = new VBox();
		
		scrabbleBoard = new ScrabbleBoard(this, board);
		scrabbleBoard.setMinSize(705, 705);
		
		botHand = new Hand(scrabbleBoard, bot);
		centerPane.getChildren().add(botHand);
		
		centerPane.getChildren().add(scrabbleBoard);

		playerHand = new Hand(scrabbleBoard, player);
		centerPane.getChildren().add(playerHand);
		
		return centerPane;
	}
	
	private VBox buildRightPanel()
	{
		VBox rightPane = new VBox();
		
		Button submitWordButton = new Button("Submit word");
		submitWordButton.setOnAction((event) -> {
			if (!isPlayersTurn) return;
			
			int points = scrabbleBoard.submitWord(player);
			if (points == 0)
			{
				scrabbleBoard.resetCurrentMove();
				update();
				
			}
			else
			{
				isPlayersTurn = false;
				update();
			}
		});
		
		Button resetWordButton = new Button("Reset word");
		resetWordButton.setOnAction((event) -> {
			if (!isPlayersTurn) return;
			scrabbleBoard.resetCurrentMove();
			update();
		});
		
		Button giveBestMovesButton = new Button("Find my best move");
		giveBestMovesButton.setOnAction((event) -> {
			if (!isPlayersTurn) return;
			Play play = player.getBestPlay();
			System.out.println(play);
		});
		
		rightPane.getChildren().add(submitWordButton);
		rightPane.getChildren().add(resetWordButton);
		rightPane.getChildren().add(giveBestMovesButton);
		
		return rightPane;
	}
	
	private void update()
	{
		scrabbleBoard.update(board);
		botHand.update();
		playerHand.update();
		
		if (!isPlayersTurn)
		{
			bot.playBestPlay();
			isPlayersTurn = true;
			update();
		}
	}
	
	public Board initGame()
	{
		Board board = new Board();
		
		TileBag bag = new TileBag();
		bot = new Player(7, bag, board);
		player = new Player(7, bag, board);
		
		isPlayersTurn = userGoesFirst();
		if (!isPlayersTurn)
		{
			bot.playBestPlay();
			isPlayersTurn = true;
		}
		
		return board;
	}
	
	private boolean userGoesFirst = false;
	private boolean userGoesFirst()
	{
		ButtonType yesButton = new ButtonType("Yes", ButtonData.YES);
		ButtonType noButton = new ButtonType("No", ButtonData.NO);
		Alert alert = new Alert(Alert.AlertType.NONE, "Would you like to go first?", yesButton, noButton);
		alert.setTitle("Scrabble");
		alert.showAndWait().ifPresent(type -> {
		        if (type == yesButton) {
		        	userGoesFirst = true;
		        }
		        else if (type == noButton) {
		        	userGoesFirst = false;
		        }
		        else {
		        	userGoesFirst = false;
		        }
		});
		
		return userGoesFirst;
	}
}