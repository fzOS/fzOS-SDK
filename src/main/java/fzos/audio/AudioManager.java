package fzos.audio;

import fzos.FzOSInternalImplementation;
import fzos.util.File;

@FzOSInternalImplementation
public class AudioManager {
    Audio openAudioFromFile(File f) {
        return new Audio(0,0,0,null);
    }
}
