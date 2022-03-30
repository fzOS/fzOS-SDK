package fzos.audio;

public class Audio {
    public int sampleRate;
    public int channels;
    public int sampleDepth;
    private int dataLength; //INTERNAL USE ONLY, UNUSABLE BY SDK!
    private int offsetToPcm;  //INTERNAL USE ONLY, UNUSABLE BY SDK!
    public byte[] data;
    protected Audio(int sampleRate, int channels, int sampleDepth, byte[] data) {
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.sampleDepth = sampleDepth;
        this.data = data;
    }
}
