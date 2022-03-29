package fzos.audio;

import fzos.FzOSInternalImplementation;
import fzos.util.File;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;

@FzOSInternalImplementation
public class AudioManager {
    public static Audio openAudioFromFile(File f) throws Exception {
        Field stringField =  File.class.getDeclaredField("filepath");
        stringField.setAccessible(true);
        String fileName = (String)stringField.get(f);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(new java.io.File(System.getProperty("user.dir")
                +System.getProperty("file.separator")+fileName));
        AudioFormat audioFormat = audioStream.getFormat();
        byte[] data = audioStream.readAllBytes();
        return new Audio((int) audioFormat.getSampleRate(), audioFormat.getChannels(), audioFormat.getSampleSizeInBits(), data);
    }
    public static void playAudio(Audio a) throws Exception {
        final AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,a.sampleRate,a.sampleDepth, a.channels, a.channels*a.sampleDepth/8,a.sampleRate,false);
        AudioInputStream audioStream = new AudioInputStream(new ByteArrayInputStream(a.data),format,a.data.length);
        Clip c = AudioSystem.getClip();
        c.open(audioStream);
        c.start();
        c.drain();
    }
}
