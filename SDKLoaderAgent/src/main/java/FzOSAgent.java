import java.lang.instrument.Instrumentation;
import fzos.*;
public class FzOSAgent {
    public static void premain(String arg, Instrumentation inst) {
        System.out.println("Loading agent...");
        KernelPrintStream printStream = new KernelPrintStream(System.out);
        System.setOut(printStream);
    }
}
