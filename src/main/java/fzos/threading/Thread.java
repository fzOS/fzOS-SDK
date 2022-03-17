package fzos.threading;

public abstract class Thread {
    public final int threadId;
    public static int currentThreadId = 0;
    public static final int THREAD_READY=0;
    public static final int THREAD_RUNNING=1;
    public static final int THREAD_BLOCKED=2;
    public static final int THREAD_TERMINATED=4;
    public int threadStatus;
    protected Thread() {
        this.threadId = currentThreadId++;
    }
    public abstract int run();
    public abstract int onSignalReceived(int signal);
}
