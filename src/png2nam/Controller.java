/**
 * Created by Fredrick on 10/21/2016.
 */
package png2nam;


import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.fxml.FXML;

import java.io.File;

public class Controller {

    @FXML private Label inputImageName;
    @FXML private Label outputDirectoryName;
    @FXML private TextField exportName;
    @FXML private Label exportStatus;
    @FXML private CheckBox outputSameAsInput;
    @FXML private Button chooseOutputDir;
    @FXML private Button exportButton;
    @FXML private Button chrChoose;
    @FXML private Button palChoose;
    @FXML private Label chrFileDir;
    @FXML private Label palFileDir;

    private static boolean createCHR = true;

    public static boolean isCreatePal()
    {
        return createPal;
    }

    private static boolean createPal = true;

    public static boolean isCreateCHR()
    {
        return createCHR;
    }

    //Choose input image
    public void handleChooseImage()

    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(inputImageName.getText()).getParentFile());

        fileChooser.setTitle("Open Input Image (256 x 240)");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png")
            );
        File file = fileChooser.showOpenDialog(Main.png2nam.PRIMARY_STAGE);
        if(file!=null)
        {
            if(Main.png2nam.checkInputImage(file.getAbsolutePath()))
            {
                inputImageName.setText(file.getAbsolutePath());
                if (outputSameAsInput.isSelected())
                    outputDirectoryName.setText(new File(inputImageName.getText()).getParent());
                String fileName = new File(inputImageName.getText()).getName();
                try
                {
                    exportName.setText(fileName.substring(0, fileName.length() - 4));
                } catch (StringIndexOutOfBoundsException e) {}
                exportButton.setDisable(false);
                exportStatus.setText("...");
            }
            else exportStatus.setText(Main.png2nam.exportStatus);
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
        Main.png2nam.StartConversion(inputImageName.getText(), exportName.getText(), outputDirectoryName.getText(), chrFileDir.getText(), palFileDir.getText());
        exportStatus.setText(Main.png2nam.exportStatus);
    }

    public void chrAddToOption()
    {
        chrChoose.setDisable(false);
        chrFileDir.setOpacity(1.0);
        createCHR = false;
    }

    public void chrCreateOption()
    {
        chrChoose.setDisable(true);
        chrFileDir.setOpacity(0.5);
        createCHR = true;
    }

    public void palCreateOption()
    {
        palChoose.setDisable(true);
        palFileDir.setOpacity(0.5);
        createPal = true;
    }

    public void palAddToOption()
    {
        palChoose.setDisable(false);
        palFileDir.setOpacity(1.0);
        createPal = false;
    }

    public void chooseChr()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(inputImageName.getText()).getParentFile());
        fileChooser.setTitle("Select CHR");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CHR", "*.chr")
        );

        File file = fileChooser.showOpenDialog(Main.png2nam.PRIMARY_STAGE);

        if(file != null)
        {
            chrFileDir.setText(file.getAbsolutePath());
        }
    }

    public void choosePal()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(inputImageName.getText()).getParentFile());
        fileChooser.setTitle("Select Palette");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PAL", "*.pal")
        );

        File file = fileChooser.showOpenDialog(Main.png2nam.PRIMARY_STAGE);

        if(file != null)
        {
            palFileDir.setText(file.getAbsolutePath());
        }
    }
}
