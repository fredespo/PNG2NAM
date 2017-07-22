/**
 * Created by Fredrick on 10/21/2016.
 */

package png2nam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ControllerOverwrite
{

    
    @FXML private Label overwriteMessageLabel;
    
    
    @FXML
    public void initialize()
    {
        overwriteMessageLabel.setText(Main.png2nam.overwriteMessage);
    }

    //overwrite cancel button
    public void handleOverwriteCancel()
    {
        Main.png2nam.OVERWRITE_CONFIRM_STAGE.close();
        Main.png2nam.setOverwriteConfirmed(false);
    }

    //overwrite confirm button
    public void handleOverwriteConfirm()
    {
        Main.png2nam.OVERWRITE_CONFIRM_STAGE.close();
        Main.png2nam.setOverwriteConfirmed(true);
    }
}
