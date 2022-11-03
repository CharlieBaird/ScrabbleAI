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
	
	public Scoreboard userScoreboard;
	public Scoreboard botScoreboard;
	
	boolean isPlayersTurn = false;
	
	public ScrabbleView()
	{
		init();
	}
	
	private void init()
	{
		board = initGame();
		
		// Build center vBox
		this.setCenter(buildCenterPanel());
		this.setRight(buildRightPanel());
		this.setLeft(buildLeftPanel());
		
		update();
	}
	
	private Pane buildRightPanel()
	{
		bestMoves = new BestMoves(this, scrabbleBoard, player, playerHand);
		BorderPane.setMargin(bestMoves, new Insets(5, 0, 0, 5));
		return bestMoves;
	}
	
	private HBox buildLeftPanel()
	{
		HBox hbox = new HBox();
		Pane pane = new Pane();
		pane.setMinSize(5, 100);
		hbox.getChildren().add(pane);
		return hbox;
	}
	
	private VBox buildCenterPanel()
	{
		VBox centerPane = new VBox();
		
		scrabbleBoard = new ScrabbleBoard(this, board);
		scrabbleBoard.setMinSize(705, 705);
		
		userScoreboard = new Scoreboard(player);
		userScoreboard.setAlignment(Pos.CENTER);
		botHand = new Hand(scrabbleBoard, bot, true);
		botScoreboard = new Scoreboard(bot);
		botScoreboard.setAlignment(Pos.CENTER);
		
		BorderPane topPanel = new BorderPane();
		topPanel.setLeft(userScoreboard);
		topPanel.setCenter(botHand);
		topPanel.setRight(botScoreboard);
		
		centerPane.getChildren().add(topPanel);
		
		centerPane.getChildren().add(scrabbleBoard);
		
		playerHand = new Hand(scrabbleBoard, player, false);
		
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
			bestMoves.unhighlightAll();
			update();
		});
		Image resetImg = new Image("https://cdn-icons-png.flaticon.com/512/60/60690.png");
		ImageView resetImgView = new ImageView(resetImg);
		resetImgView.setFitHeight(32);
		resetImgView.setFitWidth(32);
		resetWordButton.setGraphic(resetImgView);
		resetWordButton.setTooltip(new Tooltip("Reset current word"));
		
		Button trashHandButton = new Button();
		trashHandButton.setOnAction((event) -> {
			if (!isPlayersTurn) return;
			player.trashHand();
			update();
			isPlayersTurn = false;
			update();
		});
		Image trashImg = new Image("https://cdn-icons-png.flaticon.com/512/1017/1017530.png");
		ImageView trashImgView = new ImageView(trashImg);
		trashImgView.setFitHeight(32);
		trashImgView.setFitWidth(32);
		trashHandButton.setGraphic(trashImgView);
		trashHandButton.setTooltip(new Tooltip("Trash current hand, skip turn"));
		
		Button newGameButton = new Button();
		newGameButton.setOnAction((event) -> {
			init();
		});
		Image resetImage = new Image("https://www.shareicon.net/data/512x512/2016/03/02/727533_button_512x512.png");
		ImageView resetImageView = new ImageView(resetImage);
		resetImageView.setFitHeight(32);
		resetImageView.setFitWidth(32);
		newGameButton.setGraphic(resetImageView);
		newGameButton.setTooltip(new Tooltip("Reset game"));
		
		BorderPane bottomBox = new BorderPane();
		bottomBox.setCenter(playerHand);
		
		HBox buttons = new HBox();
		buttons.setAlignment(Pos.CENTER);
		buttons.setSpacing(10);
		buttons.getChildren().add(trashHandButton);
		buttons.getChildren().add(resetWordButton);
		buttons.getChildren().add(submitWordButton);
		bottomBox.setRight(buttons);
		
		HBox leftButtons = new HBox();
		leftButtons.setAlignment(Pos.CENTER);
		Pane emptyPane = new Pane();
		emptyPane.setMinSize(122, 10);
		leftButtons.getChildren().addAll(newGameButton, emptyPane);
		bottomBox.setLeft(leftButtons);
		
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
		
		botScoreboard.updateScoreLabel();
		userScoreboard.updateScoreLabel();
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