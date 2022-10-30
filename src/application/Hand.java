package application;

import application.Logic.Player;
import application.Logic.ScrabblePointsComparator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Hand extends HBox
{
	private Player player;
	private ScrabblePointsComparator comparator;
	
	public Hand(Player linkedPlayer)
	{
		this.comparator = new ScrabblePointsComparator();
		this.setMinHeight(50);
		this.player = linkedPlayer;
		this.setAlignment(Pos.CENTER);
		this.setSpacing(5);
	}
	
	// TODO add each tile's points in the top right of the tile
	public void update()
	{
		this.getChildren().clear();
		
		for (Character c : player.getHand())
		{
			if (c == null) continue;
			
			Pane pane = new Pane();
			pane.setMinSize(40, 40);
			pane.setMaxSize(40, 40);
			pane.setOpacity(0.7);
			
			pane.setStyle( 
                    "-fx-border-width: 20px;" +
                    "-fx-border-radius: 5px;" + 
                    "-fx-border-color: #E79C64;");
			
			Label label = new Label(String.valueOf(c));
			label.setFont(new Font("Arial", 26));
			label.setTextAlignment(TextAlignment.CENTER);
			label.layoutXProperty().bind(pane.widthProperty().subtract(label.widthProperty()).divide(2));
			label.layoutYProperty().bind(pane.heightProperty().subtract(label.heightProperty()).divide(2));
			
			int points = comparator.map.getOrDefault((char) ((int) c + 32), 0);
			Label pointsLabel = new Label(String.valueOf(points));
			pointsLabel.setFont(new Font("Arial", 12));
			pointsLabel.setTextAlignment(TextAlignment.RIGHT);
			pointsLabel.setTranslateX(points == 10 ? 24 : 30);

			pane.getChildren().add(label);
			pane.getChildren().add(pointsLabel);
			
			this.getChildren().add(pane);
		}
	}
}
