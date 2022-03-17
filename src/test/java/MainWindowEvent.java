import fzos.gui.Window;
import fzos.gui.WindowEvent;
import fzos.gui.WindowManager;
import fzos.threading.Thread;
import fzos.threading.ThreadManager;
public class MainWindowEvent extends WindowEvent {
    Window w;
    Thread audioThread;
    public MainWindowEvent(Window w) {
        this.w = w;
        audioThread = new BackgroundMusicThread();
        ThreadManager.registerThread(audioThread);
    }
    @Override
    public void onResize(int newWidth, int newHeight) {

    }

    @Override
    public void onClick(int clickX, int clickY) {
        if(audioThread.threadStatus!=Thread.THREAD_RUNNING) {
            ThreadManager.startThread(audioThread);
        }
        w.imageData[(clickY+WindowManager.WINDOW_CAPTION_HEIGHT)* w.windowWidth+clickX] = 0x0000000;
        WindowManager.composite();
    }

    @Override
    public void onMove(int origX, int origY, int newX, int newY) {

    }

    @Override
    public void onMinimize() {

    }

    @Override
    public void onClose() {
        ThreadManager.destroyThread(audioThread);
    }

    @Override
    public void onActivate() {

    }

    @Override
    public void onInactivate() {

    }
}
