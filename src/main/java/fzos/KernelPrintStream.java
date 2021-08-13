package fzos;


import java.io.OutputStream;
import java.io.PrintStream;

public class KernelPrintStream extends PrintStream {
    private final PrintStream ps;

    public KernelPrintStream(OutputStream out) {
        super(out);
        this.ps = System.out;
    }
    @Override
    public void println(String str) {
        ps.println("FzOS:"+str);
    }
    public void write(int i) {
        ps.write(i);
        ps.flush();
    }
}
