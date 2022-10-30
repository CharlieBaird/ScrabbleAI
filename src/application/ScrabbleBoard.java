package application;

import java.util.ArrayList;

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
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class ScrabbleBoard extends GridPane
{
	
	ScrabbleTileParent[][] tiles;
	
	ScrabbleTileParent hoveredTile;
	
	ArrayList<ScrabbleTileParent> inProgressTiles;
	
	public ScrabbleBoard(Board board)
	{
		inProgressTiles = new ArrayList<>();
		
		this.setStyle("-fx-background-color: #DDDDDD;");
		tiles = new ScrabbleTileParent[15][15];
		ScrabblePointsComparator comparator = new ScrabblePointsComparator();
		
		for (int i=0; i<15; i++)
		{
			for (int j=0; j<15; j++)
			{
				ScrabbleTileParent tile = new ScrabbleTileParent(board.getBoard()[i][j], comparator, i, j);
				
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
	
	public boolean tryPlayTile(Hand hand, TileInHand tile)
	{
		if (hoveredTile != null && hoveredTile.child.containedChar == '_')
		{
			// Put tile on board
			hoveredTile.child.update(new Tile(tile.getChar(), Bonus.NONE));
			inProgressTiles.add(hoveredTile);
			
			for (int i=0; i<15; i++)
			{
				for (int j=0; j<15; j++)
				{
					tiles[i][j].resetBorder();
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	public void paintPossibleTiles()
	{
		// If length is 0, just return.
		if (inProgressTiles.isEmpty()) return;
		
		// If length is 1, could either be left or right
		if (inProgressTiles.size() == 1)
		{
			int x = inProgressTiles.get(0).child.x;
			int y = inProgressTiles.get(0).child.y;
			
			// Paint up
			paintPossible(x-1, y, 0);
			
			// Paint right
			paintPossible(x, y+1, 1);
			
			// Paint down
			paintPossible(x+1, y, 2);
			
			// Paint left
			paintPossible(x, y-1, 3);
		}
		
		// If length is more than 1, than must be horizontal or vertical
		else
		{
			
		}
		
	}
	
	private void paintPossible(int x, int y, int direction)
	{
		// Check bounds
		if (x < 0 || x > 14 || y < 0 || y > 14) return;
		
		// Check if tile already there
		ScrabbleTileParent tile = tiles[x][y];
		if (tile.child.containedChar == '_')
		{
			tile.setHighlightedBorder();
		}
		
		// If tile already there, loop recursively until either
		// out of bounds or a tile is placeable
		else
		{
			switch (direction)
			{
				case 0:
					paintPossible(x-1, y, 0);
					break;
				case 1:
					paintPossible(x, y+1, 1);
					break;
				case 2:
					paintPossible(x+1, y, 2);
					break;
				case 3:
					paintPossible(x, y-1, 3);
					break;
			}
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
					tiles[i][j].child.update(matrix[i][j]);
				}
			}
		}
	}
}

class ScrabbleTileParent extends VBox
{
	public ScrabbleTile child;
	
	public ScrabbleTileParent(Tile tile, ScrabblePointsComparator comparator, int x, int y)
	{
		child = new ScrabbleTile(tile, comparator, x, y);
		this.setMinSize(47, 47);
		this.setBackground(Background.EMPTY);
		this.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, 
	            BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
		
		this.getChildren().add(child);
	}
	
	public void setHighlightedBorder()
	{
		setBorder(new Border(new BorderStroke(Color.BLACK, 
	            BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
	}
	
	public void resetBorder()
	{
		setBorder(new Border(new BorderStroke(Color.TRANSPARENT, 
	            BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
	}
}

class ScrabbleTile extends Pane
{
	public char containedChar;
	public String bonusLabel;
	Label label;
	Label pointsLabel;
	ScrabblePointsComparator comparator;
	
	int x;
	int y;
	
	public ScrabbleTile(Tile tile, ScrabblePointsComparator comparator, int x, int y)
	{
		this.comparator = comparator;
		containedChar = '_';
		this.setOpacity(0.7);
		bonusLabel = "";
		
		this.x = x;
		this.y = y;
		
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
	
	public String getStyleString(String backgroundColor)
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