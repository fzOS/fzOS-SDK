import fzos.base.IOStream;
import fzos.util.File;
import fzos.util.FileDescriptor;

public class FileTest {
    public static void main(String[] args) throws Exception{
        File f = new File("/banner_color");
        FileDescriptor fd = f.getDescriptor();
        IOStream.printf("size:%d\n",fd.fileSize);
        byte[] b = new byte[2];
        f.read(b,2);
        IOStream.printf("data:%d%d\n",fd.fileSize);
        IOStream.printf("offset:%d\n",fd.offset);
    }
}
