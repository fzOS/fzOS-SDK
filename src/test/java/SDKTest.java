import java.io.FileInputStream;
import java.io.IOException;

public class SDKTest {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world! This is FzOS Emu Window!\nIt works!\n");
        FileInputStream fis = new FileInputStream("banner_color");
        String banner = new String(fis.readAllBytes());
        for(int i=0;i<20;i++) System.out.println(banner);
        fis.close();
    }
}
