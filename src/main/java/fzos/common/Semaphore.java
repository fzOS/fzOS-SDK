package fzos.common;

import fzos.FzOSInternalImplementation;

@FzOSInternalImplementation
public class Semaphore {
    private volatile int val;
    synchronized int acquire() {
        while (val <= 0) {
            try {
                this.wait();
            }
            catch (Exception ignored){}
        }
        val -= 1;
        return val;
    }
    synchronized int release() {
        val += 1;
        try {
            this.notify();
        }
        catch (Exception ignored){}
        return val;
    }
}
