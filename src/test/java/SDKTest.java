import fzos.base.IOStream;
import fzos.gui.Window;
import fzos.gui.WindowManager;
import fzos.util.File;

public class SDKTest {
    public static void main(String[] args) throws Exception {
        IOStream.printf("Hello world! This is FzOS Emu Window!\nIt works!%d%d\n",1,45);
        File f = new File("/banner_color");
        int fileSize = (int) f.getDescriptor().fileSize;
        byte[] b = new byte[fileSize];
        fileSize = (int) f.read(b,f.getDescriptor().fileSize);
        String banner = new String(b);
        IOStream.println(banner);
        WindowManager.enterGraphicalMode();
        Window w = WindowManager.createWindow(Window.WINDOW_MODE_NORMAL,
                512, 662,25,50,
                "Hello GUI Test!");
        WindowManager.setWindowEvent(w,new MainWindowEvent(w));
        w.show();
        w = WindowManager.createWindow(Window.WINDOW_MODE_NO_MINIMIZE|Window.WINDOW_MODE_NO_CLOSE,
                600, 200,80,100,
                "Hello GUI Test Window #2!");
        w.show();
        IOStream.println("End.");
    }
}
