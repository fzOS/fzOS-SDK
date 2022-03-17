import fzos.audio.Audio;
import fzos.audio.AudioManager;
import fzos.threading.Thread;
import fzos.util.File;

public class BackgroundMusicThread extends Thread {
    public static final int SIGNAL_TOGGLE_PLAYING = 0x666;
    private Audio a;
    private boolean running = false;
    public BackgroundMusicThread() {
        try {
            File f = new File("test.wav");
            a = AudioManager.openAudioFromFile(f);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public int run() {
        try {
            if(a!=null) {
                running = true;
                AudioManager.playAudio(a);
                running = false;
            }
            return 0;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public int onSignalReceived(int signal) {
        try {
            if(signal==SIGNAL_TOGGLE_PLAYING) {
                if(!running) {
                    running = true;
                    AudioManager.playAudio(a);
                    running = false;
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }
}
