package application;

import application.Logic.Board;
import application.Logic.Tile;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class ScrabbleBoard extends GridPane
{
	
	ScrabbleTile[][] tiles;
	
	public ScrabbleBoard(Board board)
	{
		this.setStyle("-fx-background-color: #DDDDDD;");
		tiles = new ScrabbleTile[15][15];
		
		for (int i=0; i<15; i++)
		{
			for (int j=0; j<15; j++)
			{
				ScrabbleTile tile = new ScrabbleTile(board.getBoard()[i][j]);
				tiles[i][j] = tile;
				this.add(tile, j, i);
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
	
	public ScrabbleTile(Tile tile)
	{
		containedChar = '_';
		this.setOpacity(0.7);
		bonusLabel = "";
		
		switch (tile.getBonus())
		{
			case DOUBLE_LETTER:
				this.setStyle("-fx-background-color: #C8DCDE;");
				bonusLabel = "DOUBLE LETTER SCORE";
				break;
			case DOUBLE_WORD:
				this.setStyle("-fx-background-color: #FFB5A7;");
				bonusLabel = "DOUBLE WORD SCORE";
				break;
			case TRIPLE_LETTER:
				this.setStyle("-fx-background-color: #419BAE;");
				bonusLabel = "TRIPLE LETTER SCORE";
				break;
			case TRIPLE_WORD:
				this.setStyle("-fx-background-color: #FE6550;");
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
		
		this.getChildren().add(label);
	}

	public void update(Tile tile)
	{
		this.setStyle("-fx-background-color: #E79C64;");
		label.setText(String.valueOf(tile.getValue()));
		label.setFont(new Font("Arial", 26));
		label.setTextAlignment(TextAlignment.CENTER);
		label.layoutXProperty().bind(this.widthProperty().subtract(label.widthProperty()).divide(2));
		label.layoutYProperty().bind(this.heightProperty().subtract(label.heightProperty()).divide(2));
	}
}