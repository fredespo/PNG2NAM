package png2nam;

import java.awt.image.IndexColorModel;


public class ColorUtils
{
    // nes palette colors
    static private byte[] reds = new byte[]  {124,   0,   0,  68,-108, -88, -88,-120,     80,   0,   0,   0,   0,   0,   0,   0,    -68,   0,   0, 104, -40, -28,  -8, -28,    -84,   0,   0,   0,   0,   0,   0,   0,     -8,  60, 104,-104,  -8,  -8,  -8,  -4,     -8, -72,  88,  88,   0, 120,   0,   0,     -4, -92, -72, -40,  -8,  -8, -16,  -4,     -8, -40, -72, -72,   0, -40,   0,   0};
    static private byte[] greens = new byte[]{124,   0,   0,  40,   0,   0,  16,  20,     48, 120, 104,  88,  64,   0,   0,   0,    -68, 120,  88,  68,   0,   0,  56,  92,    124, -72, -88, -88,-120,   0,   0,   0,     -8, -68,-120, 120, 120,  88, 120, -96,    -72,  -8, -40,  -8,  -24, 120,   0,   0,    -4, -28, -72, -72, -72, -92, -48, -32,    -40,  -8,  -8,  -8,  -4, -40,   0,   0};
    private static byte[] blues = new byte[] {124,  -4, -68, -68,-124,  32,   0,   0,      0,   0,   0,   0,  88,   0,   0,   0,    -68,  -8,  -8,  -4, -52,  88,   0,  16,      0,   0,   0,  68,-120,   0,   0,   0,     -8,  -4,  -4,  -8,  -8,-104,  88,  68,      0,  24,  84,-104, -40, 120,   0,   0,     -4,  -4,  -8,  -8,  -8, -64, -80, -88,    120, 120, -72,  -40, -4, -40,   0,   0};

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