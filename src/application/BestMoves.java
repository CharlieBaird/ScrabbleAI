package application;

import java.util.ArrayList;

import application.Logic.Play;
import application.Logic.Player;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class BestMoves extends VBox
{
	ScrabbleBoard board;
	Player player;
	VBox movesBox;
	
	MoveView selected;
	
	public BestMoves(ScrabbleView view, ScrabbleBoard board, Player player, Hand hand)
	{
		this.board = board;
		this.player = player;
		this.movesBox = new VBox();
		
		Button giveBestMovesButton = new Button("Show Best Moves");
		giveBestMovesButton.setOnAction((event) -> {
			reset();
			if (!view.isPlayersTurn) return;
			ArrayList<Play> plays = player.getPlays();
			
			if (plays == null || plays.isEmpty())
				return;
			
			for (int i=0; i<plays.size(); i++)
			{
				if (i == 10) break;
				
				Play play = plays.get(i);
				
				movesBox.getChildren().add(new MoveView(board, play, hand, this));
			}
		});
		
		this.setSpacing(20);
		movesBox.setSpacing(20);
		
		this.getChildren().add(giveBestMovesButton);
		this.getChildren().add(movesBox);
	}
	
	public void reset()
	{
		// Remove all except button
		movesBox.getChildren().clear();
		selected = null;
	}
}

class MoveView extends BorderPane
{
	private Play play;
	private ScrabbleBoard board;
	private Hand hand;
	private BestMoves bestMoves;
	
	public MoveView(ScrabbleBoard board, Play play, Hand usedHand, BestMoves bestMoves)
	{
		this.board = board;
		this.play = play;
		this.hand = usedHand;
		this.bestMoves = bestMoves;
		
		this.setMinWidth(200);
		this.setMaxWidth(200);
		this.setMinHeight(40);
		this.setMaxHeight(40);
		
		HBox wordLabelWrap = new HBox();
		wordLabelWrap.setAlignment(Pos.CENTER);
		wordLabelWrap.setMinHeight(40);
		Label wordLabel = new Label(play.getWord());
		wordLabel.setFont(new Font("Arial", 14));
		wordLabelWrap.getChildren().add(wordLabel);
		this.setLeft(wordLabelWrap);
		
		HBox pointsLabelWrap = new HBox();
		pointsLabelWrap.setAlignment(Pos.CENTER);
		pointsLabelWrap.setMinHeight(40);
		Label pointsLabel = new Label(String.valueOf(play.getPoints()));
		pointsLabel.setFont(new Font("Arial", 14));
		pointsLabelWrap.getChildren().add(pointsLabel);
		this.setRight(pointsLabelWrap);
		
		BorderPane.setMargin(wordLabelWrap, new Insets(-20,0,0,0));
		BorderPane.setMargin(pointsLabelWrap, new Insets(-20,0,0,0));
		
		this.setStyle(
				"-fx-border-width: 20px;" +
                "-fx-border-radius: 5px;" + 
                "-fx-border-color: #DDDDDD;");
		
		this.setCursor(Cursor.HAND);
		
		// Add listeners
		this.addEventFilter(
            MouseEvent.MOUSE_CLICKED,
            new EventHandler<MouseEvent>() {
                public void handle(final MouseEvent mouseEvent)
                {
                	if (bestMoves.selected == null || bestMoves.selected.play.equals(play))
                		toggleSelected();
                }
            });
		this.addEventFilter(
            MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
                public void handle(final MouseEvent mouseEvent)
                {
                	selectedStyle();
                	if (bestMoves.selected == null)
                	{
                		board.previewPlay(play, hand);
                	}
                }
            });
		this.addEventFilter(
            MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
                public void handle(final MouseEvent mouseEvent)
                {
                	if (bestMoves.selected == null)
                	{
                		resetStyle();
                		board.resetCurrentMove();
                	}
                	else if (bestMoves.selected.play.equals(play))
                	{
                		return;
                	}
                	else
                	{
                		resetStyle();
                		if (bestMoves.selected.play.equals(play))
                		{
                			board.resetCurrentMove();
                		}
                	}
                		
                }
            });
	}
	
	private void toggleSelected()
	{
		if (bestMoves.selected == null || !bestMoves.selected.play.equals(play))
		{
			bestMoves.selected = this;
		}
		else
		{
			bestMoves.selected = null;
		}
	}
	
	private void resetStyle()
	{
		this.setStyle( 
				"-fx-border-width: 20px;" +
                "-fx-border-radius: 5px;" + 
                "-fx-border-color: #DDDDDD;");
	}
	
	private void selectedStyle()
	{
		this.setStyle( 
                "-fx-border-width: 20px;" +
                "-fx-border-radius: 5px;" + 
                "-fx-border-color: #C0C0C0;");
	}
}















