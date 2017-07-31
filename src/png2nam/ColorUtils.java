package png2nam;

import java.awt.*;
import java.awt.image.IndexColorModel;


public class ColorUtils
{
    // nes palette colors
    static private byte[] reds = new byte[]  {102,   0,  33,   69, 107, 114, 101,  69,  35,   0,   0,   0,   0,   0,   0,   0,         -79,   8,  71,  119, -83, -67, -75,-113,  99,  27,   0,   0,   0,   0,   0,   0,          -1,  77,-121, -71, -15,  -1,  -1, -17, -55, 127,  71,  44,  47,   0,   0,   0,            -1, -70, -47, -26,  -3,  -1,  -1,  -1, -17, -48, -71, -82, -81,   0,   0,   0};
    static private byte[] greens = new byte[]{103,  31,  13,    4,   3,   3,  17,  31,  46,  57,  61,  56,  50,   0,   0,   0,         -79,  85,  61,   48,  44,  42,  58,  76,  96, 112, 119, 116, 109,   0,   0,   0,          -1, -83,-107,-122,-128, 122,-121,-104, -85, -66, -56, -56, -60,   0,   0,   0,            -1, -27, -37, -43, -46, -49, -43, -37, -29, -20, -16, -16, -18,   0,   0,   0};
    private static byte[] blues = new byte[] {101, -99, -83, -100, 110,  30,   0,   0,   0,   0,   0,  33, 102,   0,   0,   0,         -81, -22,  -1,   -2, -50, 100,   0,   0,   0,   0,   0,  60,-103,   0,   0,   0,          -1,  -1,  -1,  -1,  -1, -45,  95,  18,   0,   0,  32, 112, -52,   0,   0,   0,            -1,  -1,  -1,  -1,  -1, -11, -59, -93,-111,-113, -90, -57, -18,   0,   0,   0};

    static public IndexColorModel palNES = new IndexColorModel(
            6, // 6 bits can store up to 64 colors
            64,
            reds, greens, blues);


    //returns the color number corresponding to the nes palette        
    public static int getColorNumAsInt(int r, int g, int b)
    {
        int result = 0;
        int adjustedRed, adjustedGreen, adjustedBlue;

        adjustedRed = r<=127 ? r : r-256;
        adjustedGreen = g<=127 ? g : g-256;
        adjustedBlue = b<=127 ? b : b-256;

        for(int i=0; i<64;i++)
        {
            if(adjustedRed==reds[i] && adjustedGreen==greens[i] && adjustedBlue==blues[i]) result = i;
        }

        return result;
    }

    public static Color getColor (int colorNum)
    {
        int adjustedRed = reds[colorNum];
        int adjustedGreen = greens[colorNum];
        int adjustedBlue = blues[colorNum];

        if(adjustedRed < 0) adjustedRed = adjustedRed+256;
        if(adjustedGreen < 0) adjustedGreen = adjustedGreen+256;
        if(adjustedBlue < 0) adjustedBlue = adjustedBlue+256;

        return new Color(adjustedRed, adjustedGreen, adjustedBlue);
    }
}