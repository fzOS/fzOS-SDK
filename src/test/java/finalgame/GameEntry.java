package finalgame;

import fzos.gui.Window;
import fzos.gui.WindowManager;

public class GameEntry {
    private static int[] backgroundImage, personImage;
    private static int backgroundImageHeight, backgroundImageWidth;
    private static int personImageHeight, personImageWidth;
    private static Window w;
    public static void main(String[] args) throws Exception {
        WindowManager.enterGraphicalMode();
        BMPImage backgroundBMP = new BMPImage("map.bmp");
        backgroundImageWidth   = backgroundBMP.width;
        backgroundImageHeight  = backgroundBMP.height;
        backgroundImage        = new int[backgroundImageHeight*backgroundImageWidth];
        if(backgroundBMP.data!=null) {
            w = WindowManager.createWindow(Window.WINDOW_MODE_NORMAL,
                    backgroundImageWidth,
                    backgroundImageHeight,
                    200,20,"Final Game!");
            for(int i=0;i<backgroundImageHeight;i++) {
                for(int j=0;j<backgroundImageWidth;j++) {
                    int startPos = (i*backgroundImageWidth+j)*4;
                    backgroundImage[(backgroundImageHeight-1-i)*w.windowWidth+j]
                            = (((int)backgroundBMP.data[startPos])&0xFF)
                            | ((((int)backgroundBMP.data[startPos+1])&0xFF)<<8)
                            | ((((int)backgroundBMP.data[startPos+2])&0xFF)<<16)
                            | ((((int)backgroundBMP.data[startPos+3])&0xFF)<<24);
                }

            }
            //Fill in background.
            for(int i=0;i<backgroundImageWidth*backgroundImageHeight;i++) {
                w.imageData[i+WindowManager.WINDOW_CAPTION_HEIGHT*w.windowWidth]=backgroundImage[i];
            }
            w.show();
        }
    }
}
