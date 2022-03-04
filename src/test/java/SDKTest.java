import fzos.base.IOStream;

import java.io.FileInputStream;
import java.io.IOException;

public class SDKTest {
    public static void main(String[] args) throws IOException {
        IOStream.printf("Hello world! This is FzOS Emu Window!\nIt works!\n");
        FileInputStream fis = new FileInputStream("banner_color");
        String banner = new String(fis.readAllBytes());
        IOStream.printf(banner);
        fis.close();
    }
}
