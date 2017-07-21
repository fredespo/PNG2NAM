package png2nam;

import java.awt.*;
import java.awt.image.BufferedImage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class PNG2NAM 
{
    public Stage PRIMARY_STAGE;
    public Stage POPUP_STAGE;
    public String popupMessage = "";
    public String exportStatus="";
    private BufferedImage inputImg;

    public PNG2NAM(){};
    
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        PRIMARY_STAGE = primaryStage;
        primaryStage.setTitle("PNG2NAM");
        primaryStage.setScene(new Scene(root,800,470));
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public void showPopup(String title, String txt) throws Exception
    {
        //Parent popup = null;
        popupMessage = txt;
        
        Parent popup = FXMLLoader.load(getClass().getResource("popup.fxml"));

        POPUP_STAGE = new Stage();
        POPUP_STAGE.setTitle(title);
        POPUP_STAGE.setScene(new Scene(popup,450,175));
        PRIMARY_STAGE.hide();
        POPUP_STAGE.requestFocus();
        POPUP_STAGE.setResizable(false);
        POPUP_STAGE.showAndWait();
        PRIMARY_STAGE.show();
    }

    public boolean checkImage(String inputImagePath)
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

        if(inputImg.getWidth() != 256 || inputImg.getHeight() != 240)
        {
            try{
                exportStatus="Error: Input image must be 256px by 240px";
                showPopup("PNG2NAM ERROR","You must select a 256px by 240px image!");
            }catch(Exception e){System.out.println(e);}
            return false;
        }
        return true;
    }

    public void StartConversion(String inputImagePath, String outputName, String outputDir)
    {
        PaletteManager.Init();
        ChrFile.Init();
        Nametable.Init();

        if(!checkImage(inputImagePath)) return;

        PaletteManager.setMainColor(inputImg);

        Color[][] metaTile;
        Color[][] tile;
        ChrFile.setCHR(outputName, outputDir);
        Nametable.setNAM(outputName, outputDir);
        int attr;


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
                        Nametable.addTile(tile, metaTileX+tileX, metaTileY+tileY);
                    }
                }
            }
        }

        ChrFile.padCHR();
        Nametable.finalizeNam();
        PaletteManager.exportPalData(outputName, outputDir);

        exportStatus="Conversion complete!";
    }

   
}