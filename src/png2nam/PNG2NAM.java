package png2nam;

import java.awt.*;
import java.awt.image.BufferedImage;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PNG2NAM 
{
    public Stage PRIMARY_STAGE;
    public Stage POPUP_STAGE;
    public Stage OVERWRITE_CONFIRM_STAGE;
    public String popupMessage = "";
    public String exportStatus="";
    public String overwriteMessage="";
    private BufferedImage inputImg;
    private boolean overwriteConfirmed = false;

    public void setOverwriteConfirmed(boolean overwriteConfirmed)
    {
        this.overwriteConfirmed = overwriteConfirmed;
    }

    
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        PRIMARY_STAGE = primaryStage;
        primaryStage.setTitle("PNG2NAM");
        primaryStage.setScene(new Scene(root,1000,600));
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public void showPopup(String title, String txt) throws Exception
    {
        //Parent popup = null;
        popupMessage = txt;
        
        Parent popup = FXMLLoader.load(getClass().getResource("popup.fxml"));

        POPUP_STAGE = new Stage();
        POPUP_STAGE.initOwner(PRIMARY_STAGE);
        POPUP_STAGE.initModality(Modality.APPLICATION_MODAL);
        POPUP_STAGE.setTitle(title);
        POPUP_STAGE.setScene(new Scene(popup,500,250));
        POPUP_STAGE.setAlwaysOnTop(true);
        POPUP_STAGE.requestFocus();
        POPUP_STAGE.setResizable(false);
        POPUP_STAGE.showAndWait();
    }

    public void showOverwriteConfirm(String title, String txt) throws Exception
    {
        overwriteMessage=txt;
        Parent popup = FXMLLoader.load(getClass().getResource("overwriteConfirm.fxml"));

        OVERWRITE_CONFIRM_STAGE = new Stage();
        OVERWRITE_CONFIRM_STAGE.initOwner(PRIMARY_STAGE);
        OVERWRITE_CONFIRM_STAGE.initModality(Modality.APPLICATION_MODAL);
        OVERWRITE_CONFIRM_STAGE.setTitle(title);
        OVERWRITE_CONFIRM_STAGE.setScene(new Scene(popup,500,250));
        OVERWRITE_CONFIRM_STAGE.setAlwaysOnTop(true);
        OVERWRITE_CONFIRM_STAGE.requestFocus();
        OVERWRITE_CONFIRM_STAGE.setResizable(false);
        OVERWRITE_CONFIRM_STAGE.showAndWait();
    }

    public boolean checkInputImage(String inputImagePath)
    {
        if(inputImagePath=="")
        {
            try{
                exportStatus="Error: Select input image";
                showPopup("PNG2NAM ERROR","You must select an input image!");
            }catch(Exception e){System.out.println(e);}
            return false;
        }

        inputImg = ImgUtils.getImageData(inputImagePath);

        String inputDir = inputImagePath.replace(".png","");
        //ImgUtils.saveImage(inputImg,  inputDir + "_indexedImage.png");


        if(inputImg.getWidth() != 256 || inputImg.getHeight() != 240)
        {
            try{
                exportStatus="Error: Input image must be 256 by 240";
                showPopup("PNG2NAM ERROR","You must select a 256px by 240px image!");
            }catch(Exception e){System.out.println(e);}
            return false;
        }

        return true;
    }

    public void CHROverwriteConfirm(String filename)
    {
        try {showOverwriteConfirm("PNG2NAM WARNING","CHR file " + filename + ".chr already exists! Overwrite?");}
        catch(Exception e){}
    }

    public void StartConversion(String inputImagePath, String outputName, String outputDir, String inputCHR, String inputPal)
    {
        PaletteManager.Init();
        ChrFile.Init();
        Nametable.Init();

        if(!checkInputImage(inputImagePath)) return;

        if(outputDir.length()==0)
        {
            try{
                exportStatus="Error: Select output directory";
                showPopup("PNG2NAM ERROR","You must select an output directory!");
            }catch(Exception e){System.out.println(e);}
            return;
        }

        if(outputName.length()==0)
        {
            try{
                exportStatus="Error: Choose an export name";
                showPopup("PNG2NAM ERROR","You must type an export name!");
            }catch(Exception e){System.out.println(e);}
            return;
        }

        if(Controller.isCreatePal()) PaletteManager.setMainColor(inputImg);
        else PaletteManager.importPaletteData(inputPal);

        Color[][] metaTile = new Color[16][16];
        Color[][] tile = new Color[8][8];
        if(!Controller.isCreateCHR()) ChrFile.setInputFileCHR(inputCHR);

        overwriteConfirmed = false;
        ChrFile.setOutputFileCHR(outputName, outputDir);
        if(!overwriteConfirmed)
        {
            exportStatus="Canceled";
            return;
        }

        Nametable.setNAM(outputName, outputDir);
        int attr;
        Color[] pal = new Color[4];


        for(int metaTileY=0; metaTileY<240; metaTileY+=16)
        {
            for(int metaTileX=0; metaTileX<256; metaTileX+=16)
            {
                metaTile = ImgUtils.getBlockOf(inputImg, metaTileX, metaTileY, 16, 16);
                attr = PaletteManager.getAttr(metaTile);

                Nametable.addAttr(attr);

                for(int tileY=0; tileY<16; tileY+=8)
                {
                    for(int tileX=0; tileX<16; tileX+=8)
                    {
                        tile = ImgUtils.getBlockOf(metaTile, tileX, tileY, 8, 8);

                        ChrFile.addTile(tile, PaletteManager.pals[attr]);
                        Nametable.addTile(tile, metaTileX+tileX, metaTileY+tileY, PaletteManager.pals[attr]);
                    }
                }
            }
        }

        ChrFile.exportCHR();
        ChrFile.padCHR();
        Nametable.finalizeNam();
        PaletteManager.exportPalData(outputName, outputDir);

        exportStatus="Conversion complete!";
    }

   
}