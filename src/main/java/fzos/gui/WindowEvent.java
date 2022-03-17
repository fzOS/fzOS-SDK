package fzos.gui;

public abstract class WindowEvent {
    public abstract void onResize (int newWidth,int newHeight);
    public abstract void onClick (int clickX,int clickY);
    public abstract void onMove (int origX,int origY,int newX,int newY);
    public abstract void onMinimize ();
    public abstract void onClose();
    public abstract void onActivate();
    public abstract void onInactivate();
}
