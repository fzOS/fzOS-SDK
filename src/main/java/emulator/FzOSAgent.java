package emulator;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.instrument.Instrumentation;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
class FzOSEmulatorWindow extends JFrame {
    public static final char COLOR_SWITCHING_CHAR = '%';
    private final BufferedImage image;
    private final JLabel label = new JLabel();
    private final OutputStream os;
    public BufferedImage getImage() {
        return image;
    }
    public JLabel getLabel() {
        return label;
    }
    private final KeyboardInputStream is;
    private static abstract class KeyboardInputStream extends InputStream {
        public static final int KEYBOARD_BUFFER_SIZE=512;
        protected final char[] keyboardRingBuffer = new char[KEYBOARD_BUFFER_SIZE];
        protected volatile int front=0,rear=0;
        public abstract void put(char in);
    }
    public FzOSEmulatorWindow(final int width, final int height, String title) {
        setTitle(title);
        setResizable(false);
        image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        label.setSize(width,height);
        label.setIcon(new ImageIcon(image));
        label.setVisible(true);
        this.getContentPane().add(label);
        label.setLocation(0,0);
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        os = new OutputStream() {
            final int maxCharX=width/8;
            int color=0xffffff;
            final int maxCharY=height/16;
            int currentCharX=0,currentCharY=0;
            //为了改颜色特地设置的变量。
            int remainingColorCharacter = 0;
            void newLine() {
                currentCharY++;
                currentCharX=0;
                if(currentCharY>=maxCharY) {
                    int[] framebuffer = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
                    System.arraycopy(framebuffer,16*width,framebuffer,0,(height-16)*width);
                    Arrays.fill(framebuffer,(height-16)*width,height*width-1,0);
                    image.setRGB(0,0,width,height,framebuffer,0,width);
                    currentCharY--;
                }
                label.repaint();
            }
            @Override
            public void write(int i) {
                if(i>FontData.FONT_COUNT) {
                    i=0;
                }
                if(remainingColorCharacter>0) {
                    if(remainingColorCharacter==7) {
                        //跳过#。
                        remainingColorCharacter--;
                        return;
                    }
                    i =Character.digit(i,16);
                    color |= (i<<((remainingColorCharacter-1)*4));
                    remainingColorCharacter--;
                    return;
                }
                if(i==COLOR_SWITCHING_CHAR) {
                    remainingColorCharacter = 7;
                    color = 0;
                    return;
                }
                if(i=='\b') {
                    if(currentCharX>=1) {
                        currentCharX--;
                    }
                    image.setRGB(currentCharX*8,currentCharY*16,8,16,FontData.fontData[' '],0,8);
                    return;
                }
                if(i=='\n') {
                    newLine();
                    return;
                }
                if(color!=0xffffff) {
                    int[] fontWithoutColor = FontData.fontData[i];
                    int[] fontWithColor = new int[8*16];
                    for(int k=0;k<8*16;k++) {
                        fontWithColor[k]= fontWithoutColor[k]&color;
                    }
                    image.setRGB(currentCharX*8,currentCharY*16,8,16,fontWithColor,0,8);
                }
                else {
                    image.setRGB(currentCharX*8,currentCharY*16,8,16,FontData.fontData[i],0,8);
                }
                currentCharX++;
                if(currentCharX>=maxCharX) {
                    newLine();
                }
            }
            @Override
            public void flush() {
                label.repaint();
            }
        };
        KeyListener listener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                is.put(e.getKeyChar());
            }
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {}
        };
        addKeyListener(listener);
        setFocusable(true);
        is = new KeyboardInputStream() {
            @Override
            public int available() {
                return (front+KEYBOARD_BUFFER_SIZE-rear)/KEYBOARD_BUFFER_SIZE;
            }

            @Override
            public void put(char in) {
                synchronized(keyboardRingBuffer) {
                    keyboardRingBuffer[front] = in;
                    front = (front + 1) % KEYBOARD_BUFFER_SIZE;
                    try {
                        keyboardRingBuffer.notify();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public int read() {
                synchronized(keyboardRingBuffer) {
                    while(front==rear) {
                        try {
                            keyboardRingBuffer.wait();
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                    char c = keyboardRingBuffer[rear];
                    rear = (rear+1)%KEYBOARD_BUFFER_SIZE;
                    return c;
                }
            }
        };
        System.setIn(is);
    }
    public OutputStream getOutputStream() {
        return os;
    }
    public InputStream getInputStream() {
        return is;
    }
}
public class FzOSAgent {
    private static final PrintStream originalPrintStream=System.out;
    private static final InputStream originalInputStream=System.in;
    private static FzOSEmulatorWindow window;
    public static void premain(String arg, Instrumentation inst) {
        System.out.println("Loading agent...");
        int width=800,height=600;
        String title = "FzOS emulation window";
        Matcher resolutionMatcher = Pattern.compile("resolution=((\\d)+)\\*((\\d)+)")
                                    .matcher(arg);
        if(resolutionMatcher.find()) {
            width = Integer.parseInt(resolutionMatcher.group(1));
            height = Integer.parseInt(resolutionMatcher.group(3));
        }
        Matcher titleMatcher = Pattern.compile("title=([^&]+)")
                               .matcher(arg);
        if(titleMatcher.find()) {
            title = titleMatcher.group(1);
        }
        FontData.prepareFonts();
        window = new FzOSEmulatorWindow(width,height,title);
        window.setVisible(true);
        setGraphicalMode(false);
    }
    public static void setGraphicalMode(boolean b) {
        if(b) {
            System.setOut(originalPrintStream);
            //System.setIn(originalInputStream);
        }
        else {
            System.setOut(new PrintStream(window.getOutputStream()));
            //System.setIn(window.getInputStream());
        }
    }
    public static BufferedImage getImage() {
        return window.getImage();
    }

    public static JLabel getLabel() {
        return window.getLabel();
    }
}
