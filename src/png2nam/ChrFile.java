package png2nam;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.lang.Math;
import java.util.ArrayList;



public class ChrFile
{
    private static File outputCHRfile;
    private static OutputStream outputCHRstream;
    private static ArrayList<Color[][]> tiles = new ArrayList<Color[][]>(0);
    private static int numOfTiles = 0;


    private ChrFile(){}


    public static void Init()
    {
        outputCHRfile = null;
        outputCHRstream = null;
        tiles = new ArrayList<Color[][]>(0);
        numOfTiles = 0;
    }


    //Sets the output CHR file, create a new one if necessary
    public static void setCHR(String fileName, String dir)
    {
        outputCHRfile = new File(dir + File.separator + fileName+".chr");

        try{
            outputCHRfile.createNewFile();
            outputCHRstream = new FileOutputStream(outputCHRfile, false);
        }catch(IOException e){}
    }


    //add a tile to the CHR, automatically advances the write position from top to bottom  
    public static void addTile(Color[][] tileData, Color[] palData)
    {
        if (isTileDuplicate(tileData)) return;

        tiles.add(tileData);
        numOfTiles++;

        addToCHR(tileData, palData);
    }

    
    //determine if a tile is already present in the CHR
    private static boolean isTileDuplicate(Color[][] tileToBeAdded)
    {
        for(int i=0; i<numOfTiles; i++)
        {
            if(areTilesEqual(tiles.get(i), tileToBeAdded)) return true;
        }

        return false;
    }


    //Are two given 8x8 blocks equal? (contain the same color pixels in the same order)
    private static boolean areTilesEqual(Color[][] tile1, Color[][] tile2)
    {
        for(int k=0; k<8; k++)
        {
            if(!Arrays.equals(tile1[k], tile2[k]))
            {
                return false;
            }
        }
        return true;
    }


    //add a tile to the hex data (each row in tile gets a hex value representing the pixels)
    private static void addToCHR(Color[][] tileData, Color[] pal)
    {
        int[] values = new int[16];

        for(int y=0; y<8; y++)
        {
            for(int x=0; x<8; x++)
            {
                int colorNum = getColorNum(tileData[x][y], pal);

                if( colorNum == 1 || colorNum == 3) values[y] += Math.pow(2,7-x);

                if( colorNum == 2 || colorNum == 3) values[y+8] += Math.pow(2,7-x);
            }
        }
        addToHex(values);
    }




    //determine which color in the given pal a pixel is using (0-3)
    private static int getColorNum(Color c, Color[] pal)
    {
        for(int index=0; index<4; index++)
        {
            if(pal[index] == null) return 0;

            if(pal[index].equals(c))
            {
                return index;
            }
        }

        return 0;
    }


    //add a given array of 16 values to the hex data
    private static void addToHex(int[] valsToAdd)
    {
        for(int valIndex=0; valIndex<16; valIndex++)
        {
            try
            {
                outputCHRstream.write(valsToAdd[valIndex]);

            }catch(IOException e){}
        }
    }


    //pad the CHR with 'blank' tiles to the next KB mark 
    public static void padCHR()
    {
         while(numOfTiles%64 != 0) {
            for (int i = 0; i < 16; i++) {
                try {
                    outputCHRstream.write(0);
                } catch (IOException e) {
                }
            }
            numOfTiles++;
        }

        try{
            outputCHRstream.close();
        }catch(IOException e){}
    }

    //return the tile number corresponding to an 8x8 pixel block
    public static int getTileNum(Color[][] tileData)
    {
        for(int i=0; i<numOfTiles; i++)
        {
            if(areTilesEqual(tiles.get(i), tileData)) return i;
        }

        return 0;
    }
}