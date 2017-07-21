package png2nam;

import java.awt.*;
import java.awt.image.BufferedImage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.*;
import java.io.File;
import java.io.*;

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