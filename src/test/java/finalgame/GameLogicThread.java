package finalgame;
import fzos.base.IOStream;
import fzos.threading.Thread;
public class GameLogicThread extends Thread {
    //0:正常，1：障碍，2：陷阱
    public static byte[] mapData;
    public GameLogicThread() {
         mapData = new byte[] {
                 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0,
                 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0,
                 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0,
                 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0,
                 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0,
                 1, 0, 0, 0, 1, 1, 1, 2, 0, 0, 1, 0,
                 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0,
                 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1,
                 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1,
                 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 0, 0,
                 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0,
                 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0
         };
    }
    @Override
    public int run() {
        while(true) {
            int targetX = GameEntry.personX, targetY = GameEntry.personY;
            char c = IOStream.getchar();
            switch (c) {
                case 'w':{
                    if(GameEntry.personY<11) {
                        targetY++;
                    }
                    break;
                }
                case 'a':{
                    if(GameEntry.personX>0) {
                        targetX--;
                    }
                    break;
                }
                case 's':{
                    if(GameEntry.personY>0) {
                        targetY--;
                    }
                    break;
                }
                case 'd':{
                    if(GameEntry.personX<11) {
                        targetX++;
                    }
                    break;
                }
                default: {
                    //Do nothing.
                    continue;
                }
            }
            if(mapData[(11-targetY)*12+targetX]==0) {
                GameEntry.setPersonPos(targetX,targetY);
                continue;
            }
            if(mapData[(11-targetY)*12+targetX]==2) {
                GameEntry.setPersonPos(11,0);
            }
        }
    }

    @Override
    public int onSignalReceived(int signal) {
        return 0;
    }
}
