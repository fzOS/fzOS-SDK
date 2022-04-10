package finalgame;

import fzos.audio.Audio;
import fzos.audio.AudioManager;
import fzos.threading.Thread;
import fzos.util.File;

public class MusicThread extends Thread {
    private final Audio backgroundAudio;
    private boolean running = true;
    public MusicThread(String name) throws Exception {
        backgroundAudio = AudioManager.openAudioFromFile(new File(name));
    }

    @Override
    public int run() {
        while(running) {
            try {
                AudioManager.playAudio(backgroundAudio);
            }
            catch (Exception ignored) {};
        }
        return 0;
    }

    @Override
    public int onSignalReceived(int signal) {
        if(signal==THREAD_SIGNAL_TERMINATE) {
            running = false;
            AudioManager.stopAudio();
        }
        return 0;
    }
}
