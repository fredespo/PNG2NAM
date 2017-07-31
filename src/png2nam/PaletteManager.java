package png2nam;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.*;

import java.util.ArrayList;

public class PaletteManager
{
    public static Color[][] pals = new Color[4][4];
    private static File outputPalFile;
    private static OutputStream outputPalStream;


    public static void Init()
    {
        pals = new Color[4][4];
        outputPalFile = null;
        outputPalStream = null;
    }

    public static void importPaletteData(String file)
    {
        FileInputStream inputStream = null;
        try {inputStream = new FileInputStream(file);}
        catch(FileNotFoundException e){return;}

        int palNum = 0;
        int colorNum = 0;
        int val;

        try
        {
            while (inputStream.available() > 0)
            {
                val = inputStream.read();

                if(val != 14) pals[palNum][colorNum] = ColorUtils.getColor(val);
                else pals[palNum][colorNum] = null;

                ++colorNum;
                if(colorNum > 3)
                {
                    colorNum = 0;
                    ++palNum;
                }
            }
        }
        catch(IOException e){}
    }

    //Sets the first color of every palette to the most common color in the image
    public static void setMainColor(BufferedImage img)
    {
        ArrayList<Color> allColors = new ArrayList<Color>(0);
        int numOfColors = 0;
        ArrayList<Integer> colorFreq = new ArrayList<Integer>(0);
        Color[] pal = new Color[4];
        Color mainColor = new Color(0,0,0);
        int palLength = 0;
        Color currentColor;

        
        for(int y=0; y<240; y+=16)
        {
            for(int x=0; x<256; x+=16)
            {
                pal = PaletteManager.getPalOf(ImgUtils.getBlockOf(img, x, y, 16, 16));
                palLength = GetPalLength(pal);

                
                if(palLength==4)
                {
                    for(int n=0; n<4; n++)
                    {
                        currentColor = pal[n];
                        
                        
                        for(int g=0; g<=numOfColors; g++)
                        {
                            if(g < allColors.size() && allColors.get(g).equals(currentColor)) 
                            {
                                colorFreq.set(g, colorFreq.get(g) + 1);
                                break;
                            }
                            
                            if(g == numOfColors)
                            {
                                allColors.add(currentColor);
                                colorFreq.add(new Integer(1));
                                numOfColors++;
                                break;
                            }
                        }
                        
                    }
                }
                
            }
        }
        

        if(numOfColors > 0)
        {
            int currentColorIndex = 0;
            for(int i=0; i<numOfColors; i++)
            {
                if(colorFreq.get(i) > colorFreq.get(currentColorIndex)) currentColorIndex = i; 
            }

            mainColor = allColors.get(currentColorIndex);
        }
        

        for(int palNum=0; palNum<4; palNum++)
        {
            pals[palNum][0] = mainColor;
        }
    }


    //Write the data to a .pal file at the given directory
    public static void exportPalData(String fileName, String dir)
    {
        int valToWrite = 0;

        outputPalFile = new File(dir + File.separator + fileName+".pal");

        try{
            outputPalFile.createNewFile();
            outputPalStream = new FileOutputStream(outputPalFile, false);
        }catch(IOException e){}

        for(int palNum=0; palNum<4; palNum++)
        {
            for(int colorNum=0; colorNum<4; colorNum++)
            {
                Color c = pals[palNum][colorNum];

                if (c==null)
                {
                    valToWrite = 14;
                    //System.out.println("NULL\t" + Integer.toString(valToWrite));
                }
                else
                {
                    valToWrite = ColorUtils.getColorNumAsInt(c.getRed(), c.getGreen(), c.getBlue());
                    //System.out.println(Integer.toString(c.getRed()) + "," + Integer.toString(c.getGreen()) + "," + Integer.toString(c.getBlue()) + "\t" + Integer.toString(valToWrite));
                }

                try {
                    outputPalStream.write(valToWrite);
                }catch(IOException e){}
            }
        }

        try{
            outputPalStream.close();
        }catch(IOException e){}
    }



    //returns the colors used by a given 16x16 pixel block
    private static Color[] getPalOf(Color[][] blockData)
    {
        Color[] resultPal = new Color[4];

        //loop through each pixel in 16x16 block
        for(int y=0; y<16; y++)
        {
            for(int x=0; x<16; x++)
            {
                //compare pixel color to palette
                for(int palIndex=0; palIndex<4; palIndex++)
                {
                    //if found an empty spot, add color to palette
                    if(resultPal[palIndex] == null)
                    {
                        resultPal[palIndex] = blockData[x][y];
                        break;
                    }

                    //if color already exists in palette, go to next pixel
                    if(resultPal[palIndex].equals(blockData[x][y]))
                    {
                        break;
                    }
                }
            }
        }

        return resultPal;
    }





    //Are two given palettes equal? (contain the same colors)
    //also true if pal2 contains pal1
    private static boolean PalsEqual(Color[] pal1, Color[] pal2)
    {
        boolean result = true;

        //compare each color in palette 1 to each color in palette 2
        for(int index_1=0; index_1<4; index_1++)
        {
            if(pal1[index_1] == null) break;

            for(int index_2=0; index_2<4; index_2++)
            {
                //if color match found, set result to true and move to next color in palette 1
                if(pal2[index_2] == pal1[index_1])
                {
                    result = true;
                    break;
                }

                //if no color match found and we are at the end of palette 2, then the palettes are not equal
                else if(index_2==3 || pal2[index_2]==null)
                {
                    result = false;
                    break;
                }
            }
            //if palettes are not equal, stop searching
            if(result == false) break;
        }

        return result;
    }




    //Add the colors of one palette into another palette with empty space and return the result
    private static Color[] MergePals(Color[] pal1, Color[] pal2)
    {
        int mergedIndex=0;
        boolean colorFound = false;

        //store the result of merge here
        Color[] mergedPal = new Color[4];

        //loop through each color in palette 1
        for(int index=0; index<4; index++)
        {

            //see if color already exists in mereged pal
            colorFound = false;
            for(int m=0; m<4; m++)
            {
                if(mergedPal[m]!=null && pal1[index]!=null && pal1[index].equals(mergedPal[m]))
                {
                    colorFound = true;
                    break;
                }
            }

            //if color doesn't already exits in mereged pal, add it
            if(!colorFound) {
                if (pal1[index] == null) break;
                mergedPal[mergedIndex] = pal1[index];
                mergedIndex++;
            }
        }

        //loop through each color in palette 2
        for(int index=0; index<4; index++)
        {

            //see if color already exists in mereged pal
            colorFound = false;
            for(int m=0; m<4; m++)
            {
                if(mergedPal[m]!=null && pal2[index]!=null && pal2[index].equals(mergedPal[m]))
                {
                    colorFound = true;
                    break;
                }
            }

            //if color doesn't already exits in mereged pal, add it
            if(!colorFound) {
                if (pal2[index] == null) break;
                mergedPal[mergedIndex] = pal2[index];
                mergedIndex++;
            }
        }

        return mergedPal;
    }






    //return the true length of a palette (not counting null spaces)
    private static int GetPalLength(Color[] array)
    {
        for(int index=0; index < 4; index++)
        {
            if(array[index]==null) return index;
        }

        return 4;
    }






    //add a given palette to the collection of 4 palettes
    //optimize where possible
    //return the palette position the new palette was added to (0-3)
    public static int getAttr(Color[][] imgData)
    {
        Color[] attrPal = new Color[4];
        attrPal = getPalOf(imgData);

        for(int palNum=0; palNum < 4; palNum++)
        {
            //if palette is already added, return
            if(PalsEqual(attrPal, pals[palNum])) return palNum;

                //if palette has not already been added,
                //merge the given palette into an 'existing' one if possible (which could mean putting the given pal in an empty spot)
            else if (GetPalLength(attrPal) - getNumOfMatchesBewteenPals(attrPal,pals[palNum]) <= 4 - GetPalLength(pals[palNum]))
            {
                pals[palNum] = MergePals(pals[palNum], attrPal);
                return palNum;
            }
        }
        return 0;
    }


    //returns the number of common colors between two sets of 4 colors
    private static int getNumOfMatchesBewteenPals(Color[] pal1ToMatch, Color[] pal2ToMatch)
    {
        int length_pal1 = GetPalLength(pal1ToMatch);
        int length_pal2 = GetPalLength(pal2ToMatch);
        int matches = 0;

        for(int q=0; q<length_pal1; q++)
        {
            for(int w=0; w<length_pal2; w++)
            {
                if(pal2ToMatch[w].equals(pal1ToMatch[q]))
                {
                    matches++;
                    break;
                }
            }
        }

        return matches;
    }

    private PaletteManager()
    {
    }
}