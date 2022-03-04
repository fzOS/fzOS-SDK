package fzos.base;

import fzos.FzOSInternalImplementation;

@FzOSInternalImplementation
public class IOStream {
    public static void printf(String format,Object... args) {
        System.out.println(format);
    }
}
