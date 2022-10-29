package application;

import application.Logic.Board;
import application.Logic.Player;
import application.Logic.TileBag;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ScrabbleView extends BorderPane
{
	private ScrabbleBoard scrabbleBoard;
	private Board board;
	private Player bot;
	private Hand botHand;
	
	public ScrabbleView()
	{
		board = initGame();
		
		// Build center vBox
		VBox centerPane = new VBox();
		
		botHand = new Hand(bot);
		centerPane.getChildren().add(botHand);
		
		scrabbleBoard = new ScrabbleBoard(board);
		scrabbleBoard.setMinSize(705, 705);
		centerPane.getChildren().add(scrabbleBoard);
		
		this.setCenter(centerPane);
		
		Button nextMoveButton = new Button("Next move");
		nextMoveButton.setOnAction((event) -> {
			bot.playBestPlay();
			update();
		});
		
		this.setBottom(nextMoveButton);
		
		Button newGameButton = new Button("Reset game");
		newGameButton.setOnAction((event) -> {
			board = initGame();
			scrabbleBoard = new ScrabbleBoard(board);
			scrabbleBoard.setMinSize(705, 705);
			this.setCenter(scrabbleBoard);
			update();
		});
		
		this.setLeft(newGameButton);
		
		update();
	}
	
	private void update()
	{
		scrabbleBoard.update(board);
		botHand.update();
	}
	
	public Board initGame()
	{
		Board board = new Board();
		board.setDefaultWords();
		
		TileBag bag = new TileBag();
		bot = new Player(7, bag, board);
		
		return board;
	}
}
