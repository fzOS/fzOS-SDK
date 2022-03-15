package fzos.gui;

import emulator.FontData;
import emulator.FzOSAgent;
import fzos.FzOSInternalImplementation;

import javax.imageio.ImageIO;
import javax.swing.*;
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
    static {
        BufferedImage backgroundImage1;
        try {
            backgroundImage1 = ImageIO.read(new File("background.bmp"));
            //Load Buttons.
            BufferedImage button;
            button = ImageIO.read(new File("close_active.bmp"));
            closeActiveRGB = button.getRGB(0,0,24,24,null,0,24);
            button = ImageIO.read(new File("close_inactive.bmp"));
            closeInactiveRGB = button.getRGB(0,0,24,24,null,0,24);
            button = ImageIO.read(new File("maximize_active.bmp"));
            maximizeActiveRGB = button.getRGB(0,0,24,24,null,0,24);
            button = ImageIO.read(new File("maximize_inactive.bmp"));
            maximizeInactiveRGB = button.getRGB(0,0,24,24,null,0,24);
            button = ImageIO.read(new File("minimize_active.bmp"));
            minimizeActiveRGB = button.getRGB(0,0,24,24,null,0,24);
            button = ImageIO.read(new File("minimize_inactive.bmp"));
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
            @Override
            public void mouseClicked(MouseEvent e) {
                int changed = 0;
                int clickX = e.getX(),clickY = e.getY();
                for(Window w:windows) {
                    //Check range.
                    if(clickX>=w.windowPositionX
                     &&clickX<=w.windowPositionX+w.windowWidth
                     &&clickY>=w.windowPositionY
                     &&clickY<=w.windowPositionY+w.windowHeight) {
                        //TODO:Notify window click event.
                        if((w.windowStatus&Window.WINDOW_STATUS_INACTIVE)==Window.WINDOW_STATUS_INACTIVE) {
                            w.windowStatus &= (~Window.WINDOW_STATUS_INACTIVE);
                            repaintWindowCaption(w);
                            changed = 1;
                        }
                    }
                    else {
                        if((w.windowStatus&Window.WINDOW_STATUS_INACTIVE)==0) {
                            w.windowStatus |= Window.WINDOW_STATUS_INACTIVE;
                            repaintWindowCaption(w);
                            changed = 1;
                        }
                    }
                }
                if(changed==1) {
                    composite();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
    public static void ExitGraphicalMode() {
        FzOSAgent.setGraphicalMode(false);
        for(int i = 0; i< emulatorWindowHeight; i++) {
            for(int j = 0; j< emulatorWindowWidth; j++) {
                backgroundImage.setRGB(j,i,0);
            }
        }
    }
    public static void composite() {
        windowImage.setRGB(0,0,
                emulatorWindowWidth,
                emulatorWindowHeight,
                backgroundRGB,0, emulatorWindowWidth);
        for(Window w:windows) {
            if(w.windowStatus==Window.WINDOW_STATUS_HIDDEN) {
                continue;
            }
            windowImage.setRGB(w.windowPositionX,w.windowPositionY,
                    w.windowWidth,w.windowHeight,
                    w.imageData,0,w.windowWidth
            );
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
    }
    public static Window createWindow(int attributes, int width, int height, int posX, int posY, String title) {
        int[] windowData = new int[width*(height+WINDOW_CAPTION_HEIGHT)];
        Window w = new Window(attributes,title,width,height,posX,posY,windowData);
        repaintWindowCaption(w);
        Arrays.fill(w.imageData, WINDOW_CAPTION_HEIGHT*width,height*width-1,0x00FFFFFF);
        windows.add(w);
        return w;
    }
}
