// IMyAidlInterface.aidl
package net.yrom.screenrecorder;

// Declare any non-default types here with import statements
import net.yrom.screenrecorder.model.DanmakuBean;

interface IScreenRecorderAidlInterface {
    void startScreenRecord();
    void stopScreenRecord();
    boolean isStartedScreenRecord();

    void stopScreenCapture();
    void startScreenCapture();
    boolean isStartedScreenCapture();
}
