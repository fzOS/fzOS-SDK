package fzos.threading;

public abstract class Thread {
    public int threadId;
    public static final int THREAD_RUNNING=0;
    public static final int THREAD_READY=1;
    public static final int THREAD_BLOCKED=2;
    public static final int THREAD_TERMINATED=4;
    public int threadStatus;
    public abstract int run();
    public abstract int onSignalReceived(int signal);
}
