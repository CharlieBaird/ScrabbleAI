package application;

import application.Logic.Player;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ScoreboardContainer extends HBox
{
	private Scoreboard botScoreboard;
	private Scoreboard playerScoreboard;
	
	public ScoreboardContainer(Player bot, Player player)
	{
		this.setMinWidth(200);
		
		Pane pane = new Pane();
		pane.setMinWidth(20);
		botScoreboard = new Scoreboard(bot);
		playerScoreboard = new Scoreboard(player);
		
		this.getChildren().addAll(pane,botScoreboard, playerScoreboard);
	}
	
	public void setNewPlayers(Player bot, Player player)
	{
		this.getChildren().clear();
		
		Pane pane = new Pane();
		pane.setMinWidth(20);
		botScoreboard = new Scoreboard(bot);
		playerScoreboard = new Scoreboard(player);
		
		this.getChildren().addAll(pane,botScoreboard, playerScoreboard);
	}
	
	public void updateScoreLabels()
	{
		botScoreboard.updateScoreLabel();
		playerScoreboard.updateScoreLabel();
	}
}

class Scoreboard extends VBox
{
	private Player player;
	private Label pointsLabel;
	
	public Scoreboard(Player player)
	{
		this.player = player;
		
		this.setMinSize(100, 100);
		
		Label nameLabel = new Label(player.name);
		nameLabel.setFont(new Font("Arial", 24.0));
		
		pointsLabel = new Label();
		pointsLabel.setFont(new Font("Arial", 24.0));
		
		this.getChildren().addAll(nameLabel, pointsLabel);
	}
	
	public void updateScoreLabel()
	{
		pointsLabel.setText(String.valueOf(player.getPoints()));
	}
}
