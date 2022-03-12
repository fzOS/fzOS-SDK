package fzos.base;

import fzos.FzOSInternalImplementation;

@FzOSInternalImplementation
public class IOStream {
    public static void printf(String format,Object... args) {
        System.out.printf(format,args);
    }
    public static void println(String str) {
        System.out.println(str);
    }
}
