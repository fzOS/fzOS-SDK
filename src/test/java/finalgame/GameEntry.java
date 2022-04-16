package finalgame;

import fzos.gui.Window;
import fzos.gui.WindowManager;
import fzos.threading.Thread;
import fzos.threading.ThreadManager;

public class GameEntry {
    private static int[] backgroundImage, personImage;
    private static int backgroundImageHeight, backgroundImageWidth;
    private static int personImageHeight, personImageWidth;
    public static Thread musicThread,gameLogicThread;
    private static Window w;
    public static int personX=11,personY=0;
    public static void main(String[] args) throws Exception {
        WindowManager.enterGraphicalMode();
        BMPImage backgroundBMP = new BMPImage("/map.bmp");
        BMPImage personBMP     = new BMPImage("/person.bmp");
        backgroundImageWidth = backgroundBMP.width;
        backgroundImageHeight = backgroundBMP.height;
        personImageWidth = personBMP.width;
        personImageHeight = personBMP.height;
        backgroundImage = new int[backgroundImageHeight * backgroundImageWidth];
        personImage     = new int[personImageHeight*personImageWidth];
        if (backgroundBMP.data == null || personBMP.data==null) {
            return;
        }
        w = WindowManager.createWindow(Window.WINDOW_MODE_NORMAL | Window.WINDOW_MODE_NO_MAXIMIZE,
                backgroundImageWidth,
                backgroundImageHeight,
                200, 20, "Final Game!");
        for (int i = 0; i < backgroundImageHeight; i++) {
            for (int j = 0; j < backgroundImageWidth; j++) {
                int startPos = (i * backgroundImageWidth + j) * 4;
                backgroundImage[(backgroundImageHeight - 1 - i) * backgroundImageWidth + j]
                        = (((int) backgroundBMP.data[startPos]) & 0xFF)
                        | ((((int) backgroundBMP.data[startPos + 1]) & 0xFF) << 8)
                        | ((((int) backgroundBMP.data[startPos + 2]) & 0xFF) << 16)
                        | ((((int) backgroundBMP.data[startPos + 3]) & 0xFF) << 24);
            }
        }
        for (int i = 0; i < personImageHeight; i++) {
            for (int j = 0; j < personImageWidth; j++) {
                int startPos = (i * personImageWidth + j) * 4;
                personImage[(personImageHeight - 1 - i) * personImageWidth + j]
                        = (((int) personBMP.data[startPos]) & 0xFF)
                        | ((((int) personBMP.data[startPos + 1]) & 0xFF) << 8)
                        | ((((int) personBMP.data[startPos + 2]) & 0xFF) << 16)
                        | ((((int) personBMP.data[startPos + 3]) & 0xFF) << 24);
            }
        }
        //Fill in background.
        for (int i = 0; i < backgroundImageWidth * backgroundImageHeight; i++) {
            w.imageData[i + WindowManager.WINDOW_CAPTION_HEIGHT * w.windowWidth] = backgroundImage[i];
        }
        w.show();
        setPersonPos(11,0);
        startBGM();
        startLogic();
        WindowManager.setWindowEvent(w,new GameWindowEvent());
    }
    private static void startLogic() {
        gameLogicThread = new GameLogicThread();
        ThreadManager.registerThread(gameLogicThread);
        ThreadManager.startThread(gameLogicThread);
    }
    private static void startBGM() throws Exception {
        musicThread = new MusicThread("/music.wav");
        ThreadManager.registerThread(musicThread);
        ThreadManager.startThread(musicThread);
    }
    public static void setPersonPos(int x,int y) {
        int origImageX = personX*68;
        int origImageY = (11-personY)*68;
        int newImageX  = x*68;
        int newImageY  = (11-y)*68;
        if(origImageX+68>backgroundImageWidth) {
            origImageX = backgroundImageWidth-68;
        }
        if(newImageX+68>backgroundImageWidth) {
            newImageX = backgroundImageWidth-68;
        }
        if(origImageY+68>backgroundImageHeight) {
            origImageY = backgroundImageHeight-68;
        }
        if(newImageY+68>backgroundImageHeight) {
            newImageY = backgroundImageHeight-68;
        }
        origImageY += WindowManager.WINDOW_CAPTION_HEIGHT;
        newImageY  += WindowManager.WINDOW_CAPTION_HEIGHT;
        //fill in orig block.
        for(int i=origImageY;i<origImageY+68;i++) {
            for(int j=origImageX;j<origImageX+68;j++) {
                w.imageData[i*backgroundImageWidth+j] = backgroundImage[(i-WindowManager.WINDOW_CAPTION_HEIGHT)*backgroundImageWidth+j];
            }
        }
        //fill in person data
        for(int i=0;i<personImageHeight;i++) {
            for(int j=0;j<personImageWidth;j++) {
                if((personImage[i*personImageWidth+j]&0xFF000000)<0) {
                    w.imageData[(i+newImageY)*backgroundImageWidth+(j+newImageX)] = personImage[i*personImageWidth+j];
                }
            }
        }
        personX = x;
        personY = y;
        WindowManager.composite();
    }
}
