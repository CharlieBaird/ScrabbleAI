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
		this.setCenter(builderCenterPanel());
		
		Button nextMoveButton = new Button("Next move");
		nextMoveButton.setOnAction((event) -> {
			bot.playBestPlay();
			update();
		});
		
		this.setBottom(nextMoveButton);
		
		Button newGameButton = new Button("Reset game");
		newGameButton.setOnAction((event) -> {
			board = initGame();
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
		
		return centerPane;
	}
	
	private void update()
	{
		scrabbleBoard.update(board);
		botHand.update();
	}
	
	public Board initGame()
	{
		Board board = new Board();
//		board.setDefaultWords();
		
		TileBag bag = new TileBag();
		bot = new Player(7, bag, board);
		
		botHand = new Hand(bot);
		
		return board;
	}
}
