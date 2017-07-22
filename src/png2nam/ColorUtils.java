package png2nam;

import java.awt.image.IndexColorModel;


public class ColorUtils
{
    // nes palette colors
    static private byte[] reds = new byte[]  {103,    0,  35,  64,  96, 103,  91,  67,     49,   7,   0,   0,   0,   0,   0,   0,    -77,  32,  81,  122, -91, -80, -83,-115,    110,  46,   6,   0,   3,   0,   0,   0,     -1,  86,-114, -52,  -1,  -1,  -1, -34,    -96, 101,  59,  45,  65,  78,   0,   0,     -1, -71, -47, -22,  -1,  -1,  -1,  -4,    -27, -53, -74, -87, -79, -74,   0,   0};
    static private byte[] greens = new byte[]{103,   31,   6,   0,   0,   0,  16,  37,     52,  72,  79,  70,  58,   0,   0,   0,    -77,  90,  56,   39,  32,  34,  55,  86,    112,-118,-110,-118, 123,   0,   0,   0,     -1, -79,-117, 108,  93, 107, 121, -80,    -44, -20, -15, -30, -60,  78,   0,   0,     -1, -33, -49, -61, -68, -66, -56, -41,    -25, -13,  -7, -13, -25, -74,   0,   0};
    private static byte[] blues = new byte[] {103, -114, -98,-114, 103,  28,   0,   0,      0,   0,   0,  34,  97,   0,   0,   0,    -77, -33,  -5,  -18, -62, 107,   2,   0,      0,   0,   0,  71,-101,   0,   0,   0,     -1,  -1,  -1,  -1,  -1,-101, 100,   0,      0,   0,  68, -78,  -1,  78,   0,   0,     -1,  -1,  -1,  -1,  -1, -18, -65,-103,   -124,-122, -97, -32,  -1, -74,   0,   0};

    static public IndexColorModel palNES = new IndexColorModel(
            6, // 6 bits can store up to 64 colors
            64, // here I use all 64
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
}