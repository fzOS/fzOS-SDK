package finalgame;

import fzos.util.File;

public class BMPImage {
    /*
    typedef struct {
        CHAR8         CharB;
        CHAR8         CharM;
        UINT32        Size;
        UINT16        Reserved[2];
        UINT32        ImageOffset;
        UINT32        HeaderSize;
        UINT32        PixelWidth;
        UINT32        PixelHeight;
        UINT16        Planes;          ///< Must be 1
        UINT16        BitPerPixel;     ///< 1, 4, 8, or 24
        UINT32        CompressionType;
        UINT32        ImageSize;       ///< Compressed image size in bytes
        UINT32        XPixelsPerMeter;
        UINT32        YPixelsPerMeter;
        UINT32        NumberOfColors;
        UINT32        ImportantColors;
} BMP_IMAGE_HEADER;
    */
    int width;
    int height;
    int colorDepth;
    byte[] data;
    public BMPImage(File f) throws Exception {
        byte[] header = new byte[54];//sizeof BMP_IMAGE_HEADER
        f.read(header,54);
        int offset = (((int)header[10])&0xFF)
                   | ((((int)header[11])&0xFF)<<8)
                   | ((((int)header[12])&0xFF)<<16)
                   | ((((int)header[13])&0xFF)<<24);
        width = (((int)header[18])&0xFF)
              | ((((int)header[19])&0xFF)<<8)
              | ((((int)header[20])&0xFF)<<16)
              | ((((int)header[21])&0xFF)<<24);
        height = (((int)header[22])&0xFF)
               | ((((int)header[23])&0xFF)<<8)
               | ((((int)header[24])&0xFF)<<16)
               | ((((int)header[25])&0xFF)<<24);
        colorDepth = (((int)header[28])&0xFF) | ((((int)header[29])&0xFF)<<8);
        if(f.seek(offset)==offset) {
            data = new byte[width*height*(colorDepth/8)];
            f.read(data, (long) width *height*(colorDepth/8));
        }
    }
    public BMPImage(String filename) throws Exception {
        this(new File(filename));

    }
}
