package stan.free.space;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import stan.free.space.ui.panes.main.MainPane;

public class Main
    extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setScene(new Scene(new MainPane(), 640, 640, Color.TRANSPARENT));
        primaryStage.show();
    }
}