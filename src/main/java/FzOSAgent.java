import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.instrument.Instrumentation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fzos.*;

import javax.swing.*;

class FzOSEmulatorWindow extends JFrame {
    final BufferedImage image;
    final JLabel label = new JLabel();
    final OutputStream os = new OutputStream() {
        @Override
        public void write(int i) {

        }
    };
    public FzOSEmulatorWindow(int width,int height,String title) {
        setSize(width,height);
        setTitle(title);
        setResizable(false);
        image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        label.setSize(width,height);
        label.setIcon(new ImageIcon(image));
        label.setVisible(true);
        this.getContentPane().add(label);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
public class FzOSAgent {
    public static void premain(String arg, Instrumentation inst) {
        System.out.println("Loading agent...");
        int width=800,height=600;
        String title = "FzOS emulation window";
        Matcher resolutionMatcher = Pattern.compile("resolution=((\\d)+)\\*((\\d)+)")
                                    .matcher(arg);
        System.out.println(resolutionMatcher.groupCount());
        if(resolutionMatcher.find()) {
            width = Integer.parseInt(resolutionMatcher.group(1));
            height = Integer.parseInt(resolutionMatcher.group(3));
        }
        Matcher titleMatcher = Pattern.compile("title=([^&]+)")
                               .matcher(arg);

        if(titleMatcher.find()) {
            title = titleMatcher.group(1);
        }
        FzOSEmulatorWindow window = new FzOSEmulatorWindow(width,height,title);
        window.setVisible(true);
        KernelPrintStream printStream = new KernelPrintStream(System.out);
        System.setOut(printStream);
    }
}
