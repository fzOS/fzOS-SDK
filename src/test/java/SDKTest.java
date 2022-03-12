
import fzos.audio.Audio;
import fzos.audio.AudioManager;
import fzos.base.IOStream;
import fzos.util.File;

public class SDKTest {
    public static void main(String[] args) throws Exception {
        IOStream.printf("Hello world! This is FzOS Emu Window!\nIt works!%d%d\n",1,45);
        File f = new File("banner_color");
        int fileSize = (int) f.getDescriptor().fileSize;
        byte[] b = new byte[fileSize];
        fileSize = (int) f.read(b,f.getDescriptor().fileSize);
        String banner = new String(b);
        IOStream.println(banner);
        char ch;
        while(true) {
            ch = IOStream.getchar();
            if(ch==(char)-1) {
                break;
            }
            IOStream.putchar(ch);
            IOStream.flush();
            if(ch=='p') {
                f = new File("test.wav");
                Audio a = AudioManager.openAudioFromFile(f);
                AudioManager.playAudio(a);
            }
        }
        IOStream.println("End.");
    }
}
