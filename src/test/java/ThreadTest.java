import fzos.base.IOStream;
import fzos.threading.Thread;
import fzos.threading.ThreadManager;

class ThreadA extends Thread {

    @Override
    public int run() {
        while(true) {
            IOStream.println("A!");
        }
    }

    @Override
    public int onSignalReceived(int signal) {
        return 0;
    }
}
class ThreadB extends Thread {

    @Override
    public int run() {
        while(true) {
            IOStream.println("B!");
        }
    }

    @Override
    public int onSignalReceived(int signal) {
        return 0;
    }
}
public class ThreadTest {
    public static void main(String[] args) {
        Thread a = new ThreadA(),b = new ThreadB();
        ThreadManager.registerThread(a);
        ThreadManager.registerThread(b);
        ThreadManager.startThread(a);
        ThreadManager.startThread(b);
    }
}
