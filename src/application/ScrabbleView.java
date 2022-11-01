package application;

import application.Logic.Board;
import application.Logic.Play;
import application.Logic.Player;
import application.Logic.TileBag;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ScrabbleView extends BorderPane
{
	public ScrabbleBoard scrabbleBoard;
	public BestMoves bestMoves;
	private Board board;
	
	private Player bot;
	private Player player;
	
	private Hand botHand;
	public Hand playerHand;
	
	public ScoreboardContainer scoreboard;
	
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
		
		this.setLeft(buildLeftPanel());
		
		bestMoves = new BestMoves(this, scrabbleBoard, player, playerHand);
		this.setRight(bestMoves);
		BorderPane.setMargin(bestMoves, new Insets(30, 30, 30, 30));
		
		update();
	}
	
	private VBox buildLeftPanel()
	{
		Button newGameButton = new Button("Reset game");
		newGameButton.setOnAction((event) -> {
			board = initGame();
			this.setCenter(buildCenterPanel());
			update();
		});
		
		VBox left = new VBox();
		scoreboard = new ScoreboardContainer(bot, player);
		
		left.getChildren().addAll(newGameButton, scoreboard);
		
		return left;
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
		
		Button submitWordButton = new Button();
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
		Image playImg = new Image("https://cdn-icons-png.flaticon.com/512/109/109197.png");
		ImageView submitImgView = new ImageView(playImg);
		submitImgView.setFitHeight(32);
		submitImgView.setFitWidth(32);
		submitWordButton.setGraphic(submitImgView);
		submitWordButton.setTooltip(new Tooltip("Submit current word"));
		
		Button resetWordButton = new Button();
		resetWordButton.setOnAction((event) -> {
			if (!isPlayersTurn) return;
			scrabbleBoard.resetCurrentMove();
			update();
		});
		Image resetImg = new Image("https://cdn-icons-png.flaticon.com/512/60/60690.png");
		ImageView resetImgView = new ImageView(resetImg);
		resetImgView.setFitHeight(32);
		resetImgView.setFitWidth(32);
		resetWordButton.setGraphic(resetImgView);
		resetWordButton.setTooltip(new Tooltip("Reset current word"));
		
		BorderPane bottomBox = new BorderPane();
		bottomBox.setCenter(playerHand);
		
		HBox buttons = new HBox();
		buttons.setAlignment(Pos.CENTER);
		buttons.setSpacing(10);
		buttons.getChildren().add(resetWordButton);
		buttons.getChildren().add(submitWordButton);
		bottomBox.setRight(buttons);
		
		Pane emptyPane = new Pane();
		emptyPane.setMinSize(96, 10);
		bottomBox.setLeft(emptyPane);
		
		centerPane.getChildren().add(bottomBox);
		
		return centerPane;
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
		
		scoreboard.updateScoreLabels();
	}
	
	public Board initGame()
	{
		Board board = new Board();
		
		TileBag bag = new TileBag();
		bot = new Player("Bot", 7, bag, board);
		player = new Player("You", 7, bag, board);
		
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