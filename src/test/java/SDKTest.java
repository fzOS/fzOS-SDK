import fzos.base.IOStream;
import fzos.util.File;

import java.io.IOException;

public class SDKTest {
    public static void main(String[] args) throws IOException {
        IOStream.printf("Hello world! This is FzOS Emu Window!\nIt works!\n");
        File f = new File("banner_color");
        byte[] b = new byte[(int) f.getDescriptor().fileSize];
        f.read(b,f.getDescriptor().fileSize);
        String banner = new String(b);
        IOStream.printf(banner);
    }
}
