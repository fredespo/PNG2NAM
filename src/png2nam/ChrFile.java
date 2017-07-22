package png2nam;


import java.awt.*;
import java.io.*;
import java.lang.Math;
import java.util.ArrayList;



public class ChrFile
{
    private static File outputCHRfile;
    private static File inputCHRfile;
    private static OutputStream outputCHRstream;
    private static ArrayList<int[]> allHexValues;
    private static int numOfTiles = 0;

    public static void Init()
    {
        outputCHRfile = null;
        inputCHRfile = null;
        outputCHRstream = null;
        allHexValues = new ArrayList<>();
        numOfTiles = 0;
    }

    public static void setInputFileCHR(String absPath)
    {
        inputCHRfile = new File(absPath);
        importCHR();
    }

    //Sets the output CHR file, create a new one if necessary
    public static void setOutputFileCHR(String fileName, String dir)
    {
        outputCHRfile = new File(dir + File.separator + fileName+".chr");
        if(outputCHRfile.exists()) Main.png2nam.CHROverwriteConfirm(fileName);
        else Main.png2nam.setOverwriteConfirmed(true);

        try{
            if(outputCHRfile.exists()) outputCHRfile.delete();
            outputCHRfile.createNewFile();

            outputCHRstream = new FileOutputStream(outputCHRfile, false);
        }catch(IOException e){}
    }

    private static void importCHR()
    {
        if(inputCHRfile==null) return;

        FileInputStream inputStream;
        int[] hex;

        try
        {
            inputStream = new FileInputStream(inputCHRfile);
        }
        catch(FileNotFoundException e)
        {
            return;
        }

        if(inputStream==null) return;

        try{

            while(inputStream.available() > 0)
            {
                hex = new int[16];

                for(int i=0; i<16; ++i)
                {
                    hex[i] = inputStream.read();
                }
                if(!isHexDuplicate(hex))
                {
                    allHexValues.add(hex);
                    ++numOfTiles;
                }
            }
        }
        catch(IOException e){}

        try{
            inputStream.close();
        }catch(IOException e){}
    }


    //add a tile to the CHR
    public static void addTile(Color[][] tileData, Color[] palData)
    {
        int[] hex = getHex(tileData, palData);
        if(isHexDuplicate(hex)) return;

        numOfTiles++;
        allHexValues.add(hex);
    }

    public static void exportCHR()
    {
        for(int[] hex : allHexValues)
        {
            writeHexToFile(hex);
        }
    }

    //check if a tile is duplicate based on its hex value
    private static boolean isHexDuplicate(int[] hex)
    {
        for(int i=0; i<allHexValues.size(); ++i)
        {
            if(areHexValuesEqual(hex, allHexValues.get(i)))
            {
                return true;
            }
        }
        return false;
    }

    //check if two tiles are equal based on hex values (16 per tile)
    private static boolean areHexValuesEqual(int[] hex1, int[] hex2)
    {
        for(int j=0; j<16; ++j)
        {
            if(hex1[j] != hex2[j]) return  false;
        }
        return true;
    }

    private static int[] getHex(Color[][] tileData, Color[] palData)
    {
        int[] values = new int[16];

        for(int y=0; y<8; y++)
        {
            for(int x=0; x<8; x++)
            {
                int colorNum = getColorNum(tileData[x][y], palData);

                if( colorNum == 1 || colorNum == 3) values[y] += Math.pow(2,7-x);

                if( colorNum == 2 || colorNum == 3) values[y+8] += Math.pow(2,7-x);
            }
        }
        return values;
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
    private static void writeHexToFile(int[] valsToAdd)
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
    public static int getTileNum(Color[][] tileData, Color[] palData)
    {
        int[] hex = getHex(tileData, palData);
        for(int i=0; i<allHexValues.size(); i++)
        {
            if(areHexValuesEqual(allHexValues.get(i), hex)) return i;
        }

        return 0;
    }
}