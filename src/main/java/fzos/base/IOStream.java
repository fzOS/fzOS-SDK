package fzos.base;

import fzos.FzOSInternalImplementation;
import java.io.IOException;


@FzOSInternalImplementation
public class IOStream {
    public static void printf(String format,Object... args) {
        System.out.printf(format,args);
    }
    public static void println(String str) {
        System.out.println(str);
    }
    public static void putchar(char c) {
        System.out.write(c);
    }
    public static void flush() {
        System.out.flush();
    }
    public static char getchar() {
        try {
            return (char)System.in.read();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        };
        return (char) -1;
    }
    public static int getNBytes(byte[] buffer,int count) {
        int cnt = 0;
        try {
            cnt = System.in.read(buffer, 0, count);
        }
        catch(Exception ignored) {};
        return cnt;
    }
}
