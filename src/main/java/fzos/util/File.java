package fzos.util;

import fzos.FzOSInternalImplementation;

import java.io.FileInputStream;
import java.io.IOException;

@FzOSInternalImplementation
public class File {
    private final FileInputStream fos;
    static class FileDescriptor {
        long fileSize;
        long offset;
    }

    private final FileDescriptor descriptor;
    public File(String filepath) throws IOException {
        java.io.File internFile = new java.io.File(filepath);
        fos = new FileInputStream(internFile);
        descriptor = new FileDescriptor();
        descriptor.fileSize = fos.available();
        descriptor.offset = 0;
    }
    public long read(byte[] buffer,long size) {
        if(descriptor.offset+size>descriptor.fileSize) {
            size = descriptor.fileSize-descriptor.offset;
        }
        if(size<=0) {
            return 0;
        }
        try {
            int ignored = fos.read(buffer,0, (int) size);
            if(ignored==0) {
                return -1;
            }
        }
        catch (IOException ex) {
            return -1;
        }
        return size;
    }
    public long seek(long offset) throws IOException {
        fos.getChannel().position(0);
        return offset;
    }
    public FileDescriptor getDescriptor() {
        return descriptor;
    }
}
