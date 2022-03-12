package fzos.audio;

import fzos.FzOSInternalImplementation;
import fzos.util.File;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;

@FzOSInternalImplementation
public class AudioManager {
    /*
    * typedef struct {
    U8 id[4];
    U32 size;
    U16 audio_format;
    U16 num_channels;
    U32 sample_rate;
    U32 byte_rate;
    U16 block_align;
    U16 bits_per_sample;
}__attribute__((packed)) WavFormatChunk;
    */
    public static Audio openAudioFromFile(File f) throws Exception {
        Field stringField =  File.class.getDeclaredField("filepath");
        stringField.setAccessible(true);
        String fileName = (String)stringField.get(f);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(new java.io.File(fileName));
        AudioFormat audioFormat = audioStream.getFormat();
        byte[] data = audioStream.readAllBytes();
        return new Audio((int) audioFormat.getSampleRate(), audioFormat.getChannels(), audioFormat.getSampleSizeInBits(), data);
    }
    public static void playAudio(Audio a) throws Exception {
        final AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,a.sampleRate,a.sampleDepth, a.channels, 2,2,false);
        AudioInputStream audioStream = new AudioInputStream(new ByteArrayInputStream(a.data),format,a.data.length);
        System.out.printf("%d %d %d %d\n",a.data.length,a.sampleRate,a.sampleDepth,a.channels);
        System.out.println(audioStream.available());
        Clip c = AudioSystem.getClip();
        c.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) System.out.println("Done");
        });
        c.open(audioStream);
        c.start();
    }
}
