package fzos.threading;

import fzos.FzOSInternalImplementation;

import java.util.HashMap;
import java.util.Map;

@FzOSInternalImplementation
public class ThreadManager {
    private static final Map<Thread,java.lang.Thread> threads = new HashMap<>();
    public static void registerThread(Thread t) {
        t.threadStatus = Thread.THREAD_READY;
        threads.put(t,new java.lang.Thread(t::run));
    }
    public static void startThread(Thread t) {
        t.threadStatus = Thread.THREAD_RUNNING;
        threads.get(t).start();

    }
    public static void suspendThread(Thread t) {
        t.threadStatus = Thread.THREAD_BLOCKED;
        try {
            threads.get(t).wait();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void destroyThread(Thread t) {
        t.threadStatus = Thread.THREAD_TERMINATED;
        threads.get(t).interrupt();
    }
}