package fzos.common;

import fzos.FzOSInternalImplementation;

@FzOSInternalImplementation
public class Semaphore {
    private volatile int val;
    synchronized int acquire() {
        while (val <= 0) {
            Thread.onSpinWait();
        }
        val -= 1;
        return val;
    }
    synchronized int release() {
        val += 1;
        return val;
    }
}
