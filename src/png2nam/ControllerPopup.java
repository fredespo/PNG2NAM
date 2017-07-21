/**
 * Created by Fredrick on 10/21/2016.
 */

package png2nam;

import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.File;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;

public class ControllerPopup {

    
    @FXML private Label popupMessageLabel;
    
    
    @FXML
    public void initialize()
    {
        popupMessageLabel.setText(Main.png2nam.popupMessage);
    }

    //popup ok button
    public void handlePopupOk(ActionEvent actionEvent)
    {
        Main.png2nam.POPUP_STAGE.close();
    }
}
