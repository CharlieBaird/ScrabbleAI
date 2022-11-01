package application;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import application.Logic.Board;
import application.Logic.Bonus;
import application.Logic.Play;
import application.Logic.Player;
import application.Logic.ScrabblePointsComparator;
import application.Logic.Tile;
import application.Logic.Word;
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
	private ScrabbleView view;
	
	ScrabbleTileParent[][] tiles;
	
	ScrabbleTileParent hoveredTile;
	
	ArrayList<ScrabbleTileParent> inProgressTiles;
	
	private Board board;
	
	public ScrabbleBoard(ScrabbleView view, Board board)
	{
		this.view = view;
		this.inProgressTiles = new ArrayList<>();
		this.board = board;
		this.tiles = new ScrabbleTileParent[15][15];
		
		this.setStyle("-fx-background-color: #DDDDDD;");
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
	
	// Construct play and then play it
	public int submitWord(Player player)
	{
		if (inProgressTiles.isEmpty()) return 0;
		
		// Make board dupe for verification
        Tile[][] boardDupe = new Tile[15][15];
        for (int i = 0; i < 15; i++)
        {
            for (int j = 0; j < 15; j++)
            {
                Tile baseTile = board.getBoard()[i][j];
                boardDupe[i][j] = new Tile(baseTile.getValue(), baseTile.getBonus());
            }
        }
        
        for (ScrabbleTileParent tileParent : inProgressTiles)
        {
        	ScrabbleTile tile = tileParent.child;
        	
        	boardDupe[tile.x][tile.y].setValue(tile.containedChar);
        }
        
        // Get all words on the board. If null, one of the new words in invalid.
        ArrayList<Word> allNewWords = board.getAllWordsOnBoard(boardDupe, true);
        if (allNewWords == null) return 0;

        // If reached here, play the word and add points to total
        ArrayList<Word> newWords = board.getNewWords(allNewWords);
        
        // Remove used letters from player's hand
        for (ScrabbleTileParent tileParent : inProgressTiles)
        {
            for (int i = 0; i < player.hand.length; i++)
            {
                // Remove first instance
                if (player.hand[i] != null && player.hand[i] == tileParent.child.containedChar)
                {
                	player.hand[i] = null;
                    break;
                }
            }
        }
        
        // Update board
        for (ScrabbleTileParent tileParent : inProgressTiles)
        {
        	ScrabbleTile tile = tileParent.child;
        	
        	board.getBoard()[tile.x][tile.y].setValue(tile.containedChar);
        }
        
        board.currentWords = allNewWords;
        
        // Add points to player's total
        int points = board.computePoints(board.getBoard(), newWords);
        player.points += points;

        player.refreshHand();

        board.displayBoard();
        
        // Reset vars
        hoveredTile = null;
        inProgressTiles.clear();
        view.bestMoves.reset();
        
        view.isPlayersTurn = false;
        
        return points;
	}
	
	public void resetCurrentMove()
	{
		view.playerHand.update();
		
		hoveredTile = null;
		inProgressTiles.clear();
		
		for (int i=0; i<15; i++)
		{
			for (int j=0; j<15; j++)
			{
				Tile tile = board.getBoard()[i][j];
				if (tile.getValue() != tiles[i][j].child.containedChar)
				{
					tiles[i][j].child.update(tile);
				}
			}
		}
						
	}
	
	public boolean tryPlayTile(ArrayList<Point> possiblePoints, Hand hand, TileInHand tile)
	{
		if (!view.isPlayersTurn) return false;
		
		for (int i=0; i<15; i++)
		{
			for (int j=0; j<15; j++)
			{
				tiles[i][j].resetBorder();
			}
		}
		
		if (hoveredTile != null && possiblePoints != null && hoveredTile.child.containedChar == '_')
		{
			// Only empty if can be played anywhere
			if (!possiblePoints.isEmpty())
			{
				// Check if location of hovered tile is valid
				boolean valid = false;
				for (Point p : possiblePoints)
				{
					if (p.x == hoveredTile.child.x && p.y == hoveredTile.child.y)
					{
						valid = true;
					}
				}
				
				if (!valid) return false;
			}
			
			// Put tile on board
			hoveredTile.child.update(new Tile(tile.getChar(), Bonus.NONE));
			inProgressTiles.add(hoveredTile);
			
			return true;
		}
		
		return false;
	}
	
	public void previewPlay(Play play, Hand usedHand)
	{
		resetCurrentMove();
		
		ArrayList<Character> usedChars = playWord(play);
		
		for (int i=0; i<15; i++)
		{
			for (int j=0; j<15; j++)
			{
				tiles[i][j].child.update(tiles[i][j].child.containedChar);
			}
		}
		
		for (char c : usedChars)
		{
			usedHand.hideChar(c);
		}
	}
	
	// Puts word on board, returns used characters
    public ArrayList<Character> playWord(Play play)
    {
        ArrayList<Character> usedChars = new ArrayList<>();

        if (!play.isVertical())
        {
            for (int i = 0; i < play.getWord().length(); i++)
            {
                int x = play.getxOrigin();
                int y = play.getyOrigin() + i;
                if (tiles[x][y].child.containedChar != play.getWord().charAt(i))
                {
                	inProgressTiles.add(tiles[x][y]);
                	tiles[x][y].child.containedChar = play.getWord().charAt(i);
                    usedChars.add(play.getWord().charAt(i));
                }

            }
        }

        else
        {
            for (int i = 0; i < play.getWord().length(); i++)
            {
                int x = play.getxOrigin() + i;
                int y = play.getyOrigin();
                if (tiles[x][y].child.containedChar != play.getWord().charAt(i))
                {
                	inProgressTiles.add(tiles[x][y]);
                	tiles[x][y].child.containedChar = play.getWord().charAt(i);
                    usedChars.add(play.getWord().charAt(i));
                }
            }
        }

        return usedChars;
    }
	
	public ArrayList<Point> paintPossibleTiles()
	{
		if (!view.isPlayersTurn) return new ArrayList<>();
		
		// If length is 0, just return.
		if (inProgressTiles.isEmpty()) return new ArrayList<>();
		
		// Create list of possible points
		ArrayList<Point> possiblePoints = new ArrayList<>();
		
		Point up = null;
		Point right = null;
		Point down = null;
		Point left = null;
		
		// If length is 1, could either be left or right
		if (inProgressTiles.size() == 1)
		{
			int x = inProgressTiles.get(0).child.x;
			int y = inProgressTiles.get(0).child.y;
			
			// Paint up
			up = paintPossible(x-1, y, 0);
			
			// Paint right
			right = paintPossible(x, y+1, 1);
			
			// Paint down
			down = paintPossible(x+1, y, 2);
			
			// Paint left
			left = paintPossible(x, y-1, 3);
		}
		
		// If length is more than 1, than must be horizontal or vertical
		else
		{
			boolean isVertical = inProgressTiles.get(0).child.y - inProgressTiles.get(1).child.y == 0;
			
			int x = inProgressTiles.get(0).child.x;
			int y = inProgressTiles.get(0).child.y;
			
			if (isVertical)
			{
				// Paint up
				up = paintPossible(x-1, y, 0);
				
				// Paint down
				down = paintPossible(x+1, y, 2);
			}
			
			else
			{
				// Paint right
				right = paintPossible(x, y+1, 1);
				
				// Paint left
				left = paintPossible(x, y-1, 3);
			}
		}
		
		if (up != null) possiblePoints.add(up);
		if (down != null) possiblePoints.add(down);
		if (right != null) possiblePoints.add(right);
		if (left != null) possiblePoints.add(left);
		
		return possiblePoints;
	}
	
	private Point paintPossible(int x, int y, int direction)
	{
		// Check bounds
		if (x < 0 || x > 14 || y < 0 || y > 14) return null;
		
		// Check if tile already there
		ScrabbleTileParent tile = tiles[x][y];
		if (tile.child.containedChar == '_')
		{
			tile.setHighlightedBorder();
			return new Point(x,y);
		}
		
		// If tile already there, loop recursively until either
		// out of bounds or a tile is placeable
		else
		{
			switch (direction)
			{
				case 0:
					return paintPossible(x-1, y, 0);
				case 1:
					return paintPossible(x, y+1, 1);
				case 2:
					return paintPossible(x+1, y, 2);
				case 3:
					return paintPossible(x, y-1, 3);
			}
		}
		
		return null;
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
				else
				{
					tiles[i][j].child.reinit();
				}
			}
		}
	}
}

class ScrabbleTileParent extends VBox implements Comparable<ScrabbleTileParent>
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

	@Override
	public int compareTo(ScrabbleTileParent arg0) {
		ScrabbleTile other = arg0.child;
		
		// Check horizontal
		if (child.x == other.x)
		{
			return child.x - other.x;
		}
		
		return child.y - other.y;
	}
}

class ScrabbleTile extends Pane
{
	public char containedChar;
	public String bonusLabel;
	Label label;
	Label pointsLabel;
	ScrabblePointsComparator comparator;
	Tile tile;
	
	int x;
	int y;
	
	public ScrabbleTile(Tile tile, ScrabblePointsComparator comparator, int x, int y)
	{
		this.comparator = comparator;
		this.containedChar = '_';
		this.bonusLabel = "";
		
		this.x = x;
		this.y = y;
		
		this.tile = tile;
		
		init(tile);
	}
	
	private void init(Tile tile)
	{
		this.setOpacity(0.7);
		
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
			case NONE:
				this.setStyle(getStyleString("TRANSPARENT"));
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
	
	public void reinit()
	{
		this.getChildren().clear();
		init(tile);
	}
	
	public String getStyleString(String backgroundColor)
	{
		return "-fx-border-width: 24px;" +
                "-fx-border-radius: 5px;" + 
                "-fx-border-color: " + backgroundColor + ";";
	}

	public void update(Tile tile)
	{
		char c = tile.getValue();
		if (c != '_')
		{
			this.setStyle(getStyleString("#E79C64"));
			
			containedChar = c;
			label.setText(String.valueOf(c));
			label.setFont(new Font("Arial", 26));
			label.setTextAlignment(TextAlignment.CENTER);
			label.layoutXProperty().bind(this.widthProperty().subtract(label.widthProperty()).divide(2));
			label.layoutYProperty().bind(this.heightProperty().subtract(label.heightProperty()).divide(2));
			
			int points = comparator.map.getOrDefault((char) ((int) c + 32), 0);
			pointsLabel.setText(String.valueOf(points));
			pointsLabel.setFont(new Font("Arial", 12));
			pointsLabel.setTextAlignment(TextAlignment.RIGHT);
			pointsLabel.setTranslateX(points == 10 ? 30 : 36);
		}
		
		// Otherwise reset to original state
		else
		{
			this.getChildren().clear();
			this.containedChar = '_';
			init(tile);
		}
	}
	
	public void update(char c)
	{
		if (c != '_')
		{
			this.setStyle(getStyleString("#E79C64"));
			
			containedChar = c;
			label.setText(String.valueOf(c));
			label.setFont(new Font("Arial", 26));
			label.setTextAlignment(TextAlignment.CENTER);
			label.layoutXProperty().bind(this.widthProperty().subtract(label.widthProperty()).divide(2));
			label.layoutYProperty().bind(this.heightProperty().subtract(label.heightProperty()).divide(2));
			
			int points = comparator.map.getOrDefault((char) ((int) c + 32), 0);
			pointsLabel.setText(String.valueOf(points));
			pointsLabel.setFont(new Font("Arial", 12));
			pointsLabel.setTextAlignment(TextAlignment.RIGHT);
			pointsLabel.setTranslateX(points == 10 ? 30 : 36);
		}
		
		// Otherwise reset to original state
		else
		{
			this.getChildren().clear();
			this.containedChar = '_';
			reinit();
		}
	}
}