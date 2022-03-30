import fzos.audio.Audio;
import fzos.audio.AudioManager;
import fzos.base.IOStream;
import fzos.util.File;

public class AudioTest {
    public static void main(String[] args) throws Exception {
        File audioFile = new File("/test.wav");
        Audio audio    = AudioManager.openAudioFromFile(audioFile);
        IOStream.printf("Audio sample:%d Hz %d bit*%d, %d bytes in total.\n",
                        audio.sampleRate,audio.sampleDepth,audio.channels,audio.data.length);
        AudioManager.playAudio(audio);
    }
}
