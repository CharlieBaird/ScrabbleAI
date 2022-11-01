package application;

import application.Logic.Player;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Scoreboard extends HBox
{
	private Player player;
	private Label pointsLabel;
	
	public Scoreboard(Player player)
	{
		this.player = player;
		
		this.setMinSize(200, 20);
		
		pointsLabel = new Label();
		pointsLabel.setFont(new Font("Arial", 24.0));
		
		this.getChildren().addAll(pointsLabel);
	}
	
	public void updateScoreLabel()
	{
		pointsLabel.setText(player.name + ": " + String.valueOf(player.getPoints()));
	}
}
