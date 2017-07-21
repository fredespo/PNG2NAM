package png2nam;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.*;

import javax.imageio.ImageIO;

public class ImgUtils
{

    //returns a subsection of an image, returned as a 2-D Color array
    public static Color[][] getBlockOf(BufferedImage img, int blockX, int blockY, int blockWidth, int blockHeight)
    {
        Color[][] buffer = new Color[blockWidth][blockHeight];

        for(int y=0; y < blockHeight; y++)
        {
            for(int x=0; x < blockWidth; x++)
            {
                buffer[x][y] = new Color(img.getRGB(blockX+x, blockY+y));
            }
        }

        return buffer;
    }


    //returns a subsection of a 2-D Color array
    public static Color[][] getBlockOf(Color[][] colorArray, int blockX, int blockY, int blockWidth, int blockHeight)
    {
        Color[][] buffer = new Color[blockWidth][blockHeight];

        for(int y=0; y < blockHeight; y++)
        {
            for(int x=0;x<blockWidth; x++)
            {
                buffer[x][y]= colorArray[blockX + x][blockY + y];
            }
        }

        return buffer;
    }



    //returns an ImageBuffer containing the pixel information of an image at the given absolute path
    public static BufferedImage getImageData(String filePath)
    {
        BufferedImage imageData = null;
        int imgWidth, imgHeight;

        //open file
        File fileName = new File(filePath);

        //read image data
        try
        {
            imageData = ImageIO.read(fileName);
        }catch(IOException e){}

        imgWidth = imageData.getWidth();
        imgHeight = imageData.getHeight();


        //Limit color palette
        BufferedImage newImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_BYTE_INDEXED, ColorUtils.palNES);

        for(int y=0; y<imgHeight; y++)
        {
            for(int x=0; x<imgWidth; x++)
            {
                Color c = new Color(imageData.getRGB(x, y));
                newImage.setRGB(x,y, c.getRGB());
            }
        }

        return newImage;
    }
}