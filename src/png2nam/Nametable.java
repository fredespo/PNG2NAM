package png2nam;

import java.awt.*;

import java.io.*;

public class Nametable
{
    private static int[][] attributes = new int[16][15];
    private static int attrX, attrY;
    private static int numOfTiles = 0;

    private static File outputNAMfile;
    private static OutputStream outputNAMstream;
    private static int[][] allTiles = new int[32][30];


    public static void Init()
    {
        attributes = new int[16][15];
        attrX = 0;
        attrY = 0;
        numOfTiles = 0;
        outputNAMfile = null;
        outputNAMstream = null;
        allTiles = new int[32][30];
    } 

    //Set the output Nametable file, create a new one if necessary 
    public static void setNAM(String fileName, String dir)
    {
        outputNAMfile = new File(dir + File.separator + fileName+".nam");

        try{
            outputNAMfile.createNewFile();
            outputNAMstream = new FileOutputStream(outputNAMfile, false);
        }catch(IOException e){}
    }


    //Add an attribute that will be written to the nametable, automatically advances write position from top to bottom 
    public static void addAttr(int attr)
    {
        attributes[attrX][attrY] = attr;

        if(++attrX >= 16)
        {
            attrX=0;
            if(++attrY >= 15) attrY=0;
        }
    }


    //Add a tile to the Nametable file, automatically advances the write position from top to bottom 
    public static void addTile(Color[][] tileData, int x, int y)
    {
       int tileNumber = ChrFile.getTileNum(tileData);

        allTiles[x/8][y/8] = tileNumber;
        numOfTiles++;
    }


    //export all the data to the Nametable file and pad with empty tiles if necessary
    public static void finalizeNam()
    {
        for(int tileY=0; tileY<30; tileY++)
        {
            for(int tileX=0; tileX<32; tileX++)
            {
                try
                {
                    outputNAMstream.write(allTiles[tileX][tileY]);
                }catch(IOException e){}
            }
        }


        while(numOfTiles++ < 960)
        {
            try {
                outputNAMstream.write(0);
            }catch(IOException e){}
        }



        int valToWrite;

        for(int y=0; y<15; y+=2)
        {
            for(int x=0; x<16; x+=2) {

                if (y < 14)
                {
                    valToWrite = 16 * 4 * attributes[x + 1][y + 1] + 16 * attributes[x][y + 1] + 4 * attributes[x + 1][y] + attributes[x][y];
                }
                else
                {
                    valToWrite = 4 * attributes[x + 1][y] + attributes[x][y];
                }

                try {
                    outputNAMstream.write(valToWrite);
                } catch (IOException e) {
                }
            }
        }

        try{
            outputNAMstream.close();
        }catch(IOException e){}

        try
        {
            java.awt.Desktop.getDesktop().open(outputNAMfile);
        }catch(IOException e){System.out.println(e);}
    }
}