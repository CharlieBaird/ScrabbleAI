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
	
	public ScrabbleView(Board board)
	{
		this.board = board;
		
		scrabbleBoard = new ScrabbleBoard(board);
		scrabbleBoard.setMinSize(705, 705);
		this.setCenter(scrabbleBoard);
		
		TileBag bag = new TileBag();
		Player player = new Player(7, bag, board);
		
		Button nextMoveButton = new Button("Next move");
		nextMoveButton.setOnAction((event) -> {
		  player.playBestPlay();
		  update();
		});
		
		this.setBottom(nextMoveButton);
	}
	
	public void update()
	{
		scrabbleBoard.update(board);
	}
}
