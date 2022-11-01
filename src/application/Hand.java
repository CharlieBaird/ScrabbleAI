package application;

import java.awt.Point;
import java.util.ArrayList;

import application.Logic.Player;
import application.Logic.ScrabblePointsComparator;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Hand extends HBox
{
	private Player player;
	private ScrabblePointsComparator comparator;
	private ScrabbleBoard scrabbleBoard;
	
	public Hand(ScrabbleBoard scrabbleBoard, Player linkedPlayer)
	{
		this.scrabbleBoard = scrabbleBoard;
		this.comparator = new ScrabblePointsComparator();
		this.setMinHeight(50);
		this.player = linkedPlayer;
		this.setAlignment(Pos.CENTER);
		this.setSpacing(5);
	}
	
	public void update()
	{
		this.getChildren().clear();
		
		for (Character c : player.getHand())
		{
			if (c == null) continue;
			
			int points = comparator.map.getOrDefault((char) ((int) c + 32), 0);
			
			TileInHand tile = new TileInHand(scrabbleBoard, this, c, points);
			
			this.getChildren().add(tile);
		}
	}
	
	public void hideChar(char c)
	{
		for (int i=0; i<this.getChildren().size(); i++)
		{
			TileInHand tile = (TileInHand) this.getChildren().get(i);
			
			if (tile.character == c)
			{
				this.getChildren().remove(tile);
				break;
			}
		}
	}
}

class TileInHand extends Pane
{
	private ScrabbleBoard scrabbleBoard;
	private Hand hand;
	public char character;
	
	private ArrayList<Point> possiblePoints;
	
	public TileInHand(ScrabbleBoard scrabbleBoard, Hand hand, Character c, int points)
	{
		this.scrabbleBoard = scrabbleBoard;
		this.hand = hand;
		this.character = c;
		
		// Set tile styles
		this.setMinSize(40, 40);
		this.setMaxSize(40, 40);
		this.setOpacity(0.7);
		resetStyle();
		
		// Add main character laben in center
		Label label = new Label(String.valueOf(c));
		label.setFont(new Font("Arial", 26));
		label.setTextAlignment(TextAlignment.CENTER);
		label.layoutXProperty().bind(this.widthProperty().subtract(label.widthProperty()).divide(2));
		label.layoutYProperty().bind(this.heightProperty().subtract(label.heightProperty()).divide(2));
		
		// Add supertext points label in top right
		Label pointsLabel = new Label(String.valueOf(points));
		pointsLabel.setFont(new Font("Arial", 12));
		pointsLabel.setTextAlignment(TextAlignment.RIGHT);
		pointsLabel.setTranslateX(points == 10 ? 24 : 30);
		
		// Add listeners
		this.addEventFilter(
            MouseEvent.MOUSE_PRESSED,
            new EventHandler<MouseEvent>() {
                public void handle(final MouseEvent mouseEvent)
                {
                	// Paint possible tiles for this tile to go on
                	possiblePoints = scrabbleBoard.paintPossibleTiles();
            		selectedStyle();
                }
            });
		this.addEventFilter(
            MouseEvent.MOUSE_RELEASED,
            new EventHandler<MouseEvent>() {
                public void handle(final MouseEvent mouseEvent)
                {
                	// Add asynchronous sleep to let mouse entered event catch up
                	new Thread(() -> {
                	    try {
							Thread.sleep(10);
							Platform.runLater(() -> {
								resetStyle();
			                	play();
							});
						} catch (InterruptedException e) {}
                	}).start();;
                }
            });
		this.addEventFilter(
	            MouseEvent.MOUSE_DRAGGED,
	            new EventHandler<MouseEvent>() {
	                public void handle(final MouseEvent mouseEvent)
	                {
	                }
	            });
		this.getChildren().add(label);
		this.getChildren().add(pointsLabel);
	}
	
	public void play()
	{
		// If returns true, successfully played tile
		if (scrabbleBoard.tryPlayTile(possiblePoints, hand, this))
		{
			hand.getChildren().remove(this);
		}
	}
	
	private void resetStyle()
	{
		this.setStyle( 
                "-fx-border-width: 20px;" +
                "-fx-border-radius: 5px;" + 
                "-fx-border-color: #E79C64;");
	}
	
	private void selectedStyle()
	{
		this.setStyle( 
                "-fx-border-width: 20px;" +
                "-fx-border-radius: 5px;" + 
                "-fx-border-color: #df7e34;");
	}
	
	public char getChar()
	{
		return this.character;
	}
}
