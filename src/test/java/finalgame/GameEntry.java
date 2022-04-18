package finalgame;

import fzos.gui.Window;
import fzos.gui.WindowManager;
import fzos.threading.Thread;
import fzos.threading.ThreadManager;

public class GameEntry {
    public static Thread musicThread,gameLogicThread;
    private static Window w;
    private static int personImageHeight, personImageWidth;
    public static int personX=11,personY=0;
    public static void main(String[] args) throws Exception {
        int backgroundImageHeight, backgroundImageWidth;
        WindowManager.enterGraphicalMode();
        BMPImage backgroundBMP = new BMPImage("/map.bmp");
        BMPImage personBMP     = new BMPImage("/person.bmp");
        backgroundImageWidth = backgroundBMP.width;
        backgroundImageHeight = backgroundBMP.height;
        personImageWidth = personBMP.width;
        personImageHeight = personBMP.height;
        if (backgroundBMP.data == null || personBMP.data==null) {
            return;
        }
        w = WindowManager.createWindow(Window.WINDOW_MODE_NORMAL | Window.WINDOW_MODE_NO_MAXIMIZE,
                backgroundImageWidth,
                backgroundImageHeight,
                200, 20, "Final Game!");
        w.setContent(backgroundBMP.data);
        w.setSprite(personBMP.data,personImageWidth,personImageHeight);
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
        personX = x;
        personY = y;
        w.setSpritePos(x*personImageWidth,(11-y)*personImageHeight);
        WindowManager.composite();
    }
    /*
    public static void setPersonPos(int x,int y) {
        int origImageX = personX*68;
        int origImageY = (11-personY)*68;
        int newImageX  = x*68;
        int newImageY  = (11-y)*68;

        WindowManager.composite();
    }

     */
}
