import fzos.base.IOStream;
import fzos.util.File;

import java.io.IOException;

public class SDKTest {
    public static void main(String[] args) throws IOException {
        IOStream.printf("Hello world! This is FzOS Emu Window!\nIt works!\n");
        File f = new File("banner_color");
        int fileSize = (int) f.getDescriptor().fileSize;
        byte[] b = new byte[fileSize];
        fileSize = (int) f.read(b,f.getDescriptor().fileSize);
        String banner = new String(b);
        IOStream.printf(banner);
    }
}
