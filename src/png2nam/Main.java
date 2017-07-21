package png2nam;


import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application
{
    public static PNG2NAM png2nam = new PNG2NAM();

     @Override
      public void start(Stage primaryStage) throws Exception
      {
          png2nam.start(primaryStage);
      }

     public static void main(String args[]) 
    {
        launch(args);
    }
}