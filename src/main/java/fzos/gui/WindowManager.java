package fzos.gui;

import emulator.FontData;
import emulator.FzOSAgent;
import fzos.FzOSInternalImplementation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@FzOSInternalImplementation
public class WindowManager {
    public static final int DEFAULT_BACKGROUND_COLOR = 0xb0beda;
    public static final int WINDOW_CAPTION_COLOR_ACTIVE = 0x417b98;
    public static final int WINDOW_CAPTION_COLOR_INACTIVE = 0x585858;
    public static final int WINDOW_CAPTION_HEIGHT = 32;
    private static final BufferedImage backgroundImage;
    private static final BufferedImage windowImage;
    private static final JLabel windowLabel;
    private static int emulatorWindowWidth, emulatorWindowHeight;
    private static final List<Window> windows = new ArrayList<>();
    private static int[] backgroundRGB;
    private static int[] minimizeActiveRGB,minimizeInactiveRGB,
                         maximizeActiveRGB,maximizeInactiveRGB,
                         closeActiveRGB,closeInactiveRGB;
    private static Window activatedWindow;
    static {
        BufferedImage backgroundImage1;
        try {
            backgroundImage1 = ImageIO.read(new File(System.getProperty("user.dir")
                    +System.getProperty("file.separator")
                    +"background.bmp"));
            //Load Buttons.
            BufferedImage button;
            button = ImageIO.read(new File(System.getProperty("user.dir")
                    +System.getProperty("file.separator")
                    +"close_active.bmp"));
            closeActiveRGB = button.getRGB(0,0,24,24,null,0,24);
            button = ImageIO.read(new File(System.getProperty("user.dir")
                    +System.getProperty("file.separator")
                    +"close_inactive.bmp"));
            closeInactiveRGB = button.getRGB(0,0,24,24,null,0,24);
            button = ImageIO.read(new File(System.getProperty("user.dir")
                    +System.getProperty("file.separator")
                    +"maximize_active.bmp"));
            maximizeActiveRGB = button.getRGB(0,0,24,24,null,0,24);
            button = ImageIO.read(new File(System.getProperty("user.dir")
                    +System.getProperty("file.separator")
                    +"maximize_inactive.bmp"));
            maximizeInactiveRGB = button.getRGB(0,0,24,24,null,0,24);
            button = ImageIO.read(new File(System.getProperty("user.dir")
                    +System.getProperty("file.separator")
                    +"minimize_active.bmp"));
            minimizeActiveRGB = button.getRGB(0,0,24,24,null,0,24);
            button = ImageIO.read(new File(System.getProperty("user.dir")
                    +System.getProperty("file.separator")
                    +"minimize_inactive.bmp"));
            minimizeInactiveRGB = button.getRGB(0,0,24,24,null,0,24);
        } catch (IOException e) {
            e.printStackTrace();
            backgroundImage1 = null;
        }
        backgroundImage = backgroundImage1;
        windowImage = FzOSAgent.getImage();
        windowLabel = FzOSAgent.getLabel();
    }
    public static void enterGraphicalMode() {
        FzOSAgent.setGraphicalMode(true);
        emulatorWindowWidth = windowImage.getWidth();
        emulatorWindowHeight = windowImage.getHeight();
        for(int i = 0; i< emulatorWindowHeight; i++) {
            for(int j = 0; j< emulatorWindowWidth; j++) {
                windowImage.setRGB(j,i,DEFAULT_BACKGROUND_COLOR);
            }
        }
        if(backgroundImage!=null) {
            int marginX = (emulatorWindowWidth -backgroundImage.getWidth())/2;
            int marginY = (emulatorWindowHeight -backgroundImage.getHeight())/2;
            int[] backgroundRGB = backgroundImage.getRGB(0,0,
                                        backgroundImage.getWidth(),
                                        backgroundImage.getHeight(),
                                        null,0,backgroundImage.getWidth());
            windowImage.setRGB(marginX,marginY,
                               backgroundImage.getWidth(),
                               backgroundImage.getHeight(),
                               backgroundRGB,0,backgroundImage.getWidth());
            WindowManager.backgroundRGB = windowImage.getRGB(0,0,
                    emulatorWindowWidth,
                    emulatorWindowHeight,
                    null,0, emulatorWindowWidth);
            windowLabel.repaint();
        }
        windowLabel.addMouseListener(new MouseListener() {
            private int startX, startY;
            @Override
            public void mouseClicked(MouseEvent e) {
                int clickX = e.getX(),clickY = e.getY();
                Window w = getWindowAtPosition(clickX,clickY);
                activateWindow(w);
                if(w!=null) {
                    clickX -= w.windowPositionX;
                    clickY -= w.windowPositionY;
                    if(clickY<WINDOW_CAPTION_HEIGHT) {
                        String button1="",button2="",button3="";
                        if((w.windowMode&Window.WINDOW_MODE_NO_CLOSE)!=Window.WINDOW_MODE_NO_CLOSE) {
                            button3 = "Close!";
                            if((w.windowMode&Window.WINDOW_MODE_NO_MAXIMIZE)!=Window.WINDOW_MODE_NO_MAXIMIZE) {
                                button2 = "Maximize!";
                            }
                            if((w.windowMode&Window.WINDOW_MODE_NO_MINIMIZE)!=Window.WINDOW_MODE_NO_MINIMIZE) {
                                if(!button2.equals("")) {
                                    button1 = "Minimize!";
                                }
                                else {
                                    button2 = "Minimize!";
                                }
                            }
                        }
                        else {
                            if((w.windowMode&Window.WINDOW_MODE_NO_MAXIMIZE)!=Window.WINDOW_MODE_NO_MAXIMIZE) {
                                button3 = "Maximize!";
                            }
                            if((w.windowMode&Window.WINDOW_MODE_NO_MINIMIZE)!=Window.WINDOW_MODE_NO_MINIMIZE) {
                                if(!button3.equals("")) {
                                    button2 = "Minimize!";
                                }
                                else {
                                    button3 = "Minimize!";
                                }
                            }
                        }

                        //In Window Title......
                        //24--8--24--8--24--10
                        int buttonAreaPosition = clickX-(w.windowWidth-98);
                        if((buttonAreaPosition>=0)&&(buttonAreaPosition<=24)) {
                            System.out.println(button1);
                        }
                        else if((buttonAreaPosition>=32)&&(buttonAreaPosition<=56)) {
                            System.out.println(button2);
                        }
                        else if((buttonAreaPosition>=64)&&(buttonAreaPosition<=88)) {
                            System.out.println(button3);
                            if(button3.equals("Close!")) {
                                //TODO:Send close event to window.
                                if(w.event!=null) {
                                    w.event.onClose();
                                }
                                destroyWindow(w);
                                composite();
                            }
                        }
                    }
                    else {
                        if(w.event!=null) {
                            w.event.onClick(clickX,clickY-WINDOW_CAPTION_HEIGHT);
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getX()- startX !=0&&e.getY()- startY !=0) {
                    Window w = getWindowAtPosition(startX, startY);
                    if(w != null) {
                        if(startY-w.windowPositionY<=WINDOW_CAPTION_HEIGHT) {
                            //Activate it.
                            activateWindow(w);
                            w.windowPositionX += (e.getX()- startX);
                            w.windowPositionY += (e.getY()- startY);
                            composite();
                        }
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
    public static void exitGraphicalMode() {
        FzOSAgent.setGraphicalMode(false);
        for(int i = 0; i< emulatorWindowHeight; i++) {
            for(int j = 0; j< emulatorWindowWidth; j++) {
                backgroundImage.setRGB(j,i,0);
            }
        }
    }
    public static void activateWindow(Window w) {
        boolean needsComposite = false;
        if(w != activatedWindow) {
            if(w!=null) {
                w.windowStatus &= ~(Window.WINDOW_STATUS_INACTIVE);
                repaintWindowCaption(w);
                windows.remove(w);
                windows.add(w);
                needsComposite = true;
            }
            if(activatedWindow!=null) {
                activatedWindow.windowStatus |= Window.WINDOW_STATUS_INACTIVE;
                repaintWindowCaption(activatedWindow);
                needsComposite = true;
            }
        }
        activatedWindow = w;
        if(needsComposite) {
            composite();
        }
    }
    public static void composite() {
        windowImage.setRGB(0,0,
                emulatorWindowWidth,
                emulatorWindowHeight,
                backgroundRGB,0, emulatorWindowWidth);
        int compositeAreaLeft,compositeAreaRight,compositeAreaTop,compositeAreaBottom;
        for(Window w:windows) {
            if(w.windowStatus==Window.WINDOW_STATUS_HIDDEN) {
                continue;
            }
            compositeAreaLeft = (w.windowPositionX<0)?(-w.windowPositionX):0;
            compositeAreaRight = ((w.windowPositionX+w.windowWidth)>emulatorWindowWidth)?
                                    (emulatorWindowWidth-w.windowPositionX):
                                    w.windowWidth;
            compositeAreaTop = (w.windowPositionY<0)?(-w.windowPositionY):0;
            compositeAreaBottom = ((w.windowPositionY+w.windowHeight+WINDOW_CAPTION_HEIGHT)>emulatorWindowHeight)?
                    (emulatorWindowHeight-w.windowPositionY):
                    w.windowHeight+WINDOW_CAPTION_HEIGHT;
            for(int i=compositeAreaTop;i<compositeAreaBottom;i++) {
                windowImage.setRGB(Math.max(w.windowPositionX, 0),w.windowPositionY+i,
                        compositeAreaRight-compositeAreaLeft,1,
                        w.imageData,w.windowWidth*i+compositeAreaLeft,compositeAreaRight-compositeAreaLeft
                );
            }
        }
        windowLabel.repaint();
    }
    public static void repaintWindowCaption(Window w) {
        Arrays.fill(w.imageData, 0, WINDOW_CAPTION_HEIGHT* w.windowWidth -1,
                ((w.windowStatus&Window.WINDOW_STATUS_INACTIVE)==Window.WINDOW_STATUS_INACTIVE)?
                WINDOW_CAPTION_COLOR_INACTIVE:WINDOW_CAPTION_COLOR_ACTIVE);
        int[] closeRGB = ((w.windowStatus&Window.WINDOW_STATUS_INACTIVE)==Window.WINDOW_STATUS_INACTIVE)?closeInactiveRGB:closeActiveRGB;
        int[] minimizeRGB = ((w.windowStatus&Window.WINDOW_STATUS_INACTIVE)==Window.WINDOW_STATUS_INACTIVE)?minimizeInactiveRGB:minimizeActiveRGB;
        int[] maximizeRGB = ((w.windowStatus&Window.WINDOW_STATUS_INACTIVE)==Window.WINDOW_STATUS_INACTIVE)?maximizeInactiveRGB:maximizeActiveRGB;
        int drawPositionLeft = w.windowWidth - 10;
        if((w.windowMode&Window.WINDOW_MODE_NO_CLOSE)!=Window.WINDOW_MODE_NO_CLOSE) {
            drawPositionLeft -= 24;
            for(int j=0;j<24;j++) {
                System.arraycopy(closeRGB,j*24,w.imageData,(4+j)*w.windowWidth+drawPositionLeft,24);
            }
            drawPositionLeft -= 8;
        }
        if((w.windowMode&Window.WINDOW_MODE_NO_MAXIMIZE)!=Window.WINDOW_MODE_NO_MAXIMIZE) {
            drawPositionLeft -= 24;
            for(int j=0;j<24;j++) {
                System.arraycopy(maximizeRGB,j*24,w.imageData,(4+j)*w.windowWidth+drawPositionLeft,24);
            }
            drawPositionLeft -= 8;
        }
        if((w.windowMode&Window.WINDOW_MODE_NO_MINIMIZE)!=Window.WINDOW_MODE_NO_MINIMIZE) {
            drawPositionLeft -= 24;
            for(int j=0;j<24;j++) {
                System.arraycopy(minimizeRGB,j*24,w.imageData,(4+j)*w.windowWidth+drawPositionLeft,24);
            }
        }
        drawPositionLeft = w.windowWidth - (w.windowWidth-drawPositionLeft)*2;
        if(w.windowTitle!=null && !(w.windowTitle.length()==0)) {
            int charCount = w.windowTitle.length();
            if((charCount*12+10)>drawPositionLeft) {
                charCount = (drawPositionLeft-10)/12;
            }
            drawPositionLeft = (w.windowWidth - (charCount*12))/2;
            for(int i=0;i<charCount;i++) {
                int[] charRGB = FontData.upscaleTo12x24Px(w.windowTitle.charAt(i),
                        ((w.windowStatus&Window.WINDOW_STATUS_INACTIVE)==Window.WINDOW_STATUS_INACTIVE)?
                                WINDOW_CAPTION_COLOR_INACTIVE:
                                WINDOW_CAPTION_COLOR_ACTIVE);
                for(int j=0;j<24;j++) {
                    System.arraycopy(charRGB,j*12,w.imageData,(5+j)*w.windowWidth+drawPositionLeft,12);
                }
                drawPositionLeft += 12;
            }
        }
    }
    public static Window createWindow(int attributes, int width, int height, int posX, int posY, String title) {
        int[] windowData = new int[width*(height+WINDOW_CAPTION_HEIGHT)];
        Window w = new Window(attributes,title,width,height,posX,posY,windowData);
        repaintWindowCaption(w);
        Arrays.fill(w.imageData, WINDOW_CAPTION_HEIGHT*width,(height+WINDOW_CAPTION_HEIGHT)*width-1,0x00FFFFFF);
        for(Window ws:windows) {
            if((ws.windowStatus&Window.WINDOW_STATUS_INACTIVE)!=Window.WINDOW_STATUS_INACTIVE) {
                ws.windowStatus |= Window.WINDOW_STATUS_INACTIVE;
                repaintWindowCaption(ws);
            }
        }
        activatedWindow = w;
        windows.add(w);
        return w;
    }
    public static Window getWindowAtPosition(int posX, int posY) {
        for(int i=windows.size()-1;i>=0;i--) {
            Window w = windows.get(i);
            //Check range.
            if(posX>=w.windowPositionX
                    &&posX<=w.windowPositionX+w.windowWidth
                    &&posY>=w.windowPositionY
                    &&posY<=w.windowPositionY+w.windowHeight+WINDOW_CAPTION_HEIGHT) {
                return w;
            }
        }
        return null;
    }
    public static void destroyWindow(Window w) {
        windows.remove(w);
        if(windows.size()>0) {
            w = windows.get(windows.size()-1);
            w.windowStatus &=(~Window.WINDOW_STATUS_INACTIVE);
        }
        repaintWindowCaption(w);
    }
    public static void setWindowEvent(Window w,WindowEvent we) {
        w.event = we;
    }
}
