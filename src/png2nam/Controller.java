/**
 * Created by Fredrick on 10/21/2016.
 */
package png2nam;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.File;
import javafx.scene.control.TextField;

public class Controller {

    @FXML private Label inputImageName;
    @FXML private Label outputDirectoryName;
    @FXML private TextField exportName;
    @FXML private Label exportStatus;
    @FXML private CheckBox outputSameAsInput;
    @FXML private Button chooseOutputDir;

    //Choose input image
    public void handleChooseImage(){
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open Input Image (256 x 240)");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png")
            );
        File file = fileChooser.showOpenDialog(Main.png2nam.PRIMARY_STAGE);
        if(file!=null)
        {
            inputImageName.setText(file.getAbsolutePath());
            if(outputSameAsInput.isSelected()) outputDirectoryName.setText(new File(inputImageName.getText()).getParent());
            String fileName = new File(inputImageName.getText()).getName();
            try
            {
                exportName.setText(fileName.substring(0,fileName.length()-4));
            }
            catch(StringIndexOutOfBoundsException e){}
        }
    }

    public void handleSetOutputDirectory()
    {
        if(outputSameAsInput.isSelected())
        {
            File inputFile = new File(inputImageName.getText());
            if(inputFile != null) outputDirectoryName.setText(inputFile.getParent());
            chooseOutputDir.setDisable(true);
        }
        else chooseOutputDir.setDisable(false);
    }

    //Choose output directory 
    public void handleChooseOutputDir()
    {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setInitialDirectory(new File(inputImageName.getText()).getParentFile());
        dirChooser.setTitle("Select Output Directory");
        File outputDir = dirChooser.showDialog(Main.png2nam.PRIMARY_STAGE);
        if(outputDir!=null) outputDirectoryName.setText(outputDir.getAbsolutePath());
    }

    //export files
    public void handleExport()
    {
        Main.png2nam.StartConversion(inputImageName.getText(), exportName.getText(), outputDirectoryName.getText());
        exportStatus.setText(Main.png2nam.exportStatus);
    }
}
