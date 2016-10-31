package seedu.address.ui;

import java.awt.GraphicsEnvironment;

import com.melloware.jintellitype.JIntellitype;

import javafx.application.Platform;

public class HotkeyListener {

    private static int identifierCounter;
    private static int refCount;
    private final int identifier;
    private Runnable action;

    public HotkeyListener(int modifier, int keycode) {
        if (GraphicsEnvironment.isHeadless() || !JIntellitype.isJIntellitypeSupported()) {
            this.identifier = 0;
            return;
        }
        synchronized (HotkeyListener.class) {
            this.identifier = ++identifierCounter;
            JIntellitype.getInstance().addHotKeyListener(identifier -> {
                if (identifier != this.identifier) {
                    return;
                }
                Platform.runLater(this::onHotkey);
            });
            JIntellitype.getInstance().registerHotKey(this.identifier, modifier, keycode);
            refCount++;
        }
    }

    public synchronized void setAction(Runnable action) {
        this.action = action;
    }

    public synchronized void destroy() {
        if (this.identifier <= 0) {
            return;
        }
        JIntellitype.getInstance().unregisterHotKey(this.identifier);
        synchronized (HotkeyListener.class) {
            if (--refCount <= 0) {
                JIntellitype.getInstance().cleanUp();
            }
        }
    }

    private synchronized void onHotkey() {
        if (this.action != null) {
            this.action.run();
        }
    }
}
