import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.instrument.Instrumentation;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;

class FzOSEmulatorWindow extends JFrame {
    public static final char COLOR_SWITCHING_CHAR = '%';
    final BufferedImage image;
    final JLabel label = new JLabel();
    final int width,height;
    final OutputStream os;
    public FzOSEmulatorWindow(int width,int height,String title) {
        this.width = width;
        this.height = height;
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
        };
    }
    public OutputStream getOutputStream() {
        return os;
    }
}
public class FzOSAgent {
    public static final PrintStream originalPrintStream=System.out;
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
        FzOSEmulatorWindow window = new FzOSEmulatorWindow(width,height,title);
        window.setVisible(true);
        System.setOut(new PrintStream(window.getOutputStream()));
    }
}
