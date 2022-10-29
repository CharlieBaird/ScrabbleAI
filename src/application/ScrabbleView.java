package application;

import application.Logic.Board;
import application.Logic.Player;
import application.Logic.TileBag;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class ScrabbleView extends BorderPane
{
	ScrabbleBoard scrabbleBoard;
	Board board;
	Player bot;
	
	public ScrabbleView()
	{
		board = initGame();
		
		scrabbleBoard = new ScrabbleBoard(board);
		scrabbleBoard.setMinSize(705, 705);
		this.setCenter(scrabbleBoard);
		
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
	}
	
	public Board initGame()
	{
		Board board = new Board();
		board.setDefaultWords();
		
		TileBag bag = new TileBag();
		bot = new Player(7, bag, board);
		
		return board;
	}
	
	public void update()
	{
		scrabbleBoard.update(board);
	}
}
