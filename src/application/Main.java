package application;
	
import application.Logic.Board;
import application.Logic.Player;
import application.Logic.TileBag;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,1280,800);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			Board board = new Board();
			ScrabbleView view = new ScrabbleView(board);
			
			root.getChildren().add(view);
			
			board.setDefaultWords();
			view.update();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}