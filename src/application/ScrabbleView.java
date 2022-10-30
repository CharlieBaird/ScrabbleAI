package application;

import application.Logic.Board;
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
	
	public ScrabbleView()
	{
		board = initGame(true);
		
		// Build center vBox
		this.setCenter(builderCenterPanel());
		
		Button nextMoveButton = new Button("Next move");
		nextMoveButton.setOnAction((event) -> {
			bot.playBestPlay();
			update();
		});
		
		this.setBottom(nextMoveButton);
		
		Button newGameButton = new Button("Reset game");
		newGameButton.setOnAction((event) -> {
			initGame(false);
			this.setCenter(builderCenterPanel());
			update();
		});
		
		this.setLeft(newGameButton);
		
		update();
	}
	
	private VBox builderCenterPanel()
	{
		VBox centerPane = new VBox();
		
		botHand = new Hand(bot);
		centerPane.getChildren().add(botHand);
		
		scrabbleBoard = new ScrabbleBoard(board);
		scrabbleBoard.setMinSize(705, 705);
		centerPane.getChildren().add(scrabbleBoard);

		playerHand = new Hand(player);
		centerPane.getChildren().add(playerHand);
		
		return centerPane;
	}
	
	private void update()
	{
		scrabbleBoard.update(board);
		botHand.update();
		playerHand.update();
	}
	
	public Board initGame(boolean firstGame)
	{
		Board board = new Board();
		
		TileBag bag = new TileBag();
		bot = new Player(7, bag, board);
		player = new Player(7, bag, board);
		
		boolean userGoesFirst = userGoesFirst();
		if (!userGoesFirst)
		{
			bot.playBestPlay();
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