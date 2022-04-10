package finalgame;

import fzos.gui.WindowEvent;
import fzos.threading.ThreadManager;

public class GameWindowEvent extends WindowEvent {
    @Override
    public void onResize(int newWidth, int newHeight) {

    }

    @Override
    public void onClick(int clickX, int clickY) {

    }

    @Override
    public void onMove(int origX, int origY, int newX, int newY) {

    }

    @Override
    public void onMinimize() {

    }

    @Override
    public void onClose() {
        ThreadManager.destroyThread(GameEntry.gameLogicThread);
        ThreadManager.destroyThread(GameEntry.musicThread);
    }

    @Override
    public void onActivate() {

    }

    @Override
    public void onInactivate() {

    }
}
