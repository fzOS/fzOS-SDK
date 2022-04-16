package fzos.gui;

import fzos.FzOSInternalImplementation;

@FzOSInternalImplementation
public class Window {
    public static final int WINDOW_MODE_NORMAL      = 0x00000000;
    public static final int WINDOW_MODE_NO_MAXIMIZE = 0x00000001;
    public static final int WINDOW_MODE_NO_MINIMIZE = 0x00000002;
    public static final int WINDOW_MODE_NO_CLOSE    = 0x00000004;
    public static final int WINDOW_MODE_BORDERLESS  = 0x00000008;

    public static final int WINDOW_STATUS_NORMAL    = 0x00000000;
    public static final int WINDOW_STATUS_HIDDEN    = 0x00000001;
    public static final int WINDOW_STATUS_INACTIVE  = 0x00000002;


    public int windowMode;
    public int windowStatus;
    public String windowTitle;
    public int windowWidth,windowHeight,windowPositionX,windowPositionY;
    public final int[] imageData;
    public WindowEvent event;
    protected Window(int windowMode, String windowTitle, int windowWidth, int windowHeight, int windowPositionX, int windowPositionY, int[] imageData) {
        this.windowMode = windowMode;
        this.windowTitle = windowTitle;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.windowPositionX = windowPositionX;
        this.windowPositionY = windowPositionY;
        this.imageData = imageData;
        this.windowStatus = WINDOW_STATUS_HIDDEN;
    }
    public void show() {
        windowStatus = WINDOW_STATUS_NORMAL;
        WindowManager.composite();
    }
    public void hide() {
        windowStatus = WINDOW_STATUS_HIDDEN;
        WindowManager.composite();
    }
}
