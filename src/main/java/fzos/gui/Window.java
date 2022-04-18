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
    public int[] sprite;
    public int[] shadowSprite;
    private int spriteX,spriteY=WindowManager.WINDOW_CAPTION_HEIGHT,spriteWidth,spriteHeight;
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
    public void setContent(byte[] bytes) {
        for (int i = 0; i < this.windowHeight; i++) {
            for (int j = 0; j < this.windowWidth; j++) {
                int startPos = (i * this.windowWidth + j) * 4;
                this.imageData[(this.windowHeight+WindowManager.WINDOW_CAPTION_HEIGHT - 1 - i) * this.windowWidth + j]
                        = (((int) bytes[startPos]) & 0xFF)
                        | ((((int) bytes[startPos + 1]) & 0xFF) << 8)
                        | ((((int) bytes[startPos + 2]) & 0xFF) << 16)
                        | ((((int) bytes[startPos + 3]) & 0xFF) << 24);
            }
        }
    }
    public void setSprite(byte[] sprite,int width,int height) {
        this.sprite = new int[width*height];
        this.shadowSprite = new int[width*height];
        this.spriteWidth = width;
        this.spriteHeight = height;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int startPos = (i * width + j) * 4;
                this.sprite[(height - 1 - i) * width + j]
                        = (((int) sprite[startPos]) & 0xFF)
                        | ((((int) sprite[startPos + 1]) & 0xFF) << 8)
                        | ((((int) sprite[startPos + 2]) & 0xFF) << 16)
                        | ((((int) sprite[startPos + 3]) & 0xFF) << 24);
            }
        }
        for(int i=0;i<spriteHeight;i++) {
            System.arraycopy(imageData, (i + WindowManager.WINDOW_CAPTION_HEIGHT) * windowWidth, shadowSprite, i * spriteWidth, spriteWidth);
        }

    }
    public void setSpritePos(int x,int y) {
        if(x+spriteWidth>windowWidth) {
            x = windowWidth-spriteWidth;
        }
        if(y+spriteHeight>windowHeight) {
            y = windowHeight-spriteHeight;
        }
        y+= WindowManager.WINDOW_CAPTION_HEIGHT;
        //Shadow copy.
        for(int i=0;i<spriteHeight;i++) {
            for(int j=0;j<spriteWidth;j++) {
                imageData[(i+spriteY)*windowWidth+spriteX+j] = shadowSprite[i*spriteWidth+j];
            }
        }
        //fill in new Shadow.
        for(int i=0;i<spriteHeight;i++) {
            for(int j=0;j<spriteWidth;j++) {
                shadowSprite[i*spriteWidth+j] = imageData[(i+y)*windowWidth+x+j];
            }
        }
        //fill in Sprite.
        for(int i=0;i<spriteHeight;i++) {
            for(int j=0;j<spriteWidth;j++) {
                if((sprite[i*spriteWidth+j]&0xFF000000)<0) {
                    imageData[(i+y)*windowWidth+(j+x)] = sprite[i*spriteWidth+j];
                }
            }
        }
        spriteX = x;
        spriteY = y;
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
