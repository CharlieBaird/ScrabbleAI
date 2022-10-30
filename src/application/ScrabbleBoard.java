package application;

import application.Logic.Board;
import application.Logic.Bonus;
import application.Logic.ScrabblePointsComparator;
import application.Logic.Tile;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class ScrabbleBoard extends GridPane
{
	
	ScrabbleTile[][] tiles;
	
	ScrabbleTile hoveredTile;
	
	public ScrabbleBoard(Board board)
	{
		this.setStyle("-fx-background-color: #DDDDDD;");
		tiles = new ScrabbleTile[15][15];
		ScrabblePointsComparator comparator = new ScrabblePointsComparator();
		
		for (int i=0; i<15; i++)
		{
			for (int j=0; j<15; j++)
			{
				ScrabbleTile tile = new ScrabbleTile(board.getBoard()[i][j], comparator);
				
				// Add listener for mouse entered
				tile.addEventFilter(
		            MouseEvent.MOUSE_ENTERED,
		            new EventHandler<MouseEvent>() {
		                public void handle(final MouseEvent mouseEvent)
		                {
		                	// Update the board as this is the selected node
		                	hoveredTile = tile;
		                }
		            });
				
				tiles[i][j] = tile;
				this.add(tile, j, i);
			}
		}
		
		// Add listener for mouse exited entire scrabble board
		this.addEventFilter(
            MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
                public void handle(final MouseEvent mouseEvent)
                {
                	// Update the board as this is the selected node
                	hoveredTile = null;
                }
            });
	}
	
	public void tryPlayTile(Hand hand, TileInHand tile)
	{
		if (hoveredTile != null && hoveredTile.containedChar == '_')
		{
			hoveredTile.update(new Tile(tile.getChar(), Bonus.NONE));
		}
	}
	
	public void update(Board board)
	{
		Tile[][] matrix = board.getBoard();
		for (int i=0; i<15; i++)
		{
			for (int j=0; j<15; j++)
			{
				if (matrix[i][j].getValue() != '_')
				{
					tiles[i][j].update(matrix[i][j]);
				}
			}
		}
	}
}

class ScrabbleTile extends Pane
{
	public char containedChar;
	public String bonusLabel;
	Label label;
	Label pointsLabel;
	ScrabblePointsComparator comparator;
	
	public ScrabbleTile(Tile tile, ScrabblePointsComparator comparator)
	{
		this.comparator = comparator;
		containedChar = '_';
		this.setOpacity(0.7);
		bonusLabel = "";
		
		switch (tile.getBonus())
		{
			case DOUBLE_LETTER:
				this.setStyle(getStyleString("#C8DCDE"));
				bonusLabel = "DOUBLE LETTER SCORE";
				break;
			case DOUBLE_WORD:
				this.setStyle(getStyleString("#FFB5A7"));
				bonusLabel = "DOUBLE WORD SCORE";
				break;
			case TRIPLE_LETTER:
				this.setStyle(getStyleString("#419BAE"));
				bonusLabel = "TRIPLE LETTER SCORE";
				break;
			case TRIPLE_WORD:
				this.setStyle(getStyleString("#FE6550"));
				bonusLabel = "TRIPLE WORD SCORE";
				break;
			default:
				break;
		}
		
		this.setMinSize(47, 47);
		
		label = new Label(bonusLabel);
		label.setWrapText(true);
		label.setMaxWidth(47);
		label.setFont(new Font("Arial", 9));
		label.layoutYProperty().bind(this.heightProperty().subtract(label.heightProperty()).divide(2));
		label.setTextAlignment(TextAlignment.CENTER);
		
		pointsLabel = new Label();
		
		this.getChildren().add(label);
		this.getChildren().add(pointsLabel);
	}
	
	private String getStyleString(String backgroundColor)
	{
		return "-fx-border-width: 24px;" +
                "-fx-border-radius: 5px;" + 
                "-fx-border-color: " + backgroundColor + ";";
	}

	public void update(Tile tile)
	{
		this.setStyle(getStyleString("#E79C64"));
		
		containedChar = tile.getValue();
		label.setText(String.valueOf(tile.getValue()));
		label.setFont(new Font("Arial", 26));
		label.setTextAlignment(TextAlignment.CENTER);
		label.layoutXProperty().bind(this.widthProperty().subtract(label.widthProperty()).divide(2));
		label.layoutYProperty().bind(this.heightProperty().subtract(label.heightProperty()).divide(2));
		
		int points = comparator.map.getOrDefault((char) ((int) tile.getValue() + 32), 0);
		pointsLabel.setText(String.valueOf(points));
		pointsLabel.setFont(new Font("Arial", 12));
		pointsLabel.setTextAlignment(TextAlignment.RIGHT);
		pointsLabel.setTranslateX(points == 10 ? 30 : 36);
	}
}