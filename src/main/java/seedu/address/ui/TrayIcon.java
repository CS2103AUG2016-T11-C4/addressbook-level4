package seedu.address.ui;

import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.SystemTray;
import java.util.Optional;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 * A tray icon.
 */
public class TrayIcon {

    public enum MessageType { ERROR, INFO, NONE, WARNING }

    private final String title;
    private final Image image;
    private Runnable trayIconAction;
    private Optional<java.awt.TrayIcon> trayIcon = Optional.empty();

    public TrayIcon(Image image, String title) {
        this.image = image;
        this.title = title;
        if (!GraphicsEnvironment.isHeadless()) {
            javax.swing.SwingUtilities.invokeLater(this::addIconToTrayAWT);
        }
    }

    public synchronized void setTrayIconAction(Runnable trayIconAction) {
        this.trayIconAction = trayIconAction;
    }

    public void displayMessage(final String caption, final String text, final MessageType messageType) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            synchronized (this) {
                if (!this.trayIcon.isPresent()) {
                    return;
                }
                this.trayIcon.get().displayMessage(caption, text, messageTypeToAWTMessageType(messageType));
            }
        });
    }

    private java.awt.TrayIcon.MessageType messageTypeToAWTMessageType(MessageType messageType) {
        switch (messageType) {
        case ERROR:
            return java.awt.TrayIcon.MessageType.ERROR;
        case INFO:
            return java.awt.TrayIcon.MessageType.INFO;
        case WARNING:
            return java.awt.TrayIcon.MessageType.WARNING;
        default:
            return java.awt.TrayIcon.MessageType.NONE;
        }
    }

    /**
     * Destroys the tray icon.
     */
    public synchronized void destroy() {
        if (trayIcon.isPresent()) {
            javax.swing.SwingUtilities.invokeLater(this::removeIconFromTrayAWT);
        }

        while (trayIcon.isPresent()) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
    }

    /**
     * Runs on the AWT event thread.
     */
    private void addIconToTrayAWT() {
        // ensure awt toolkit is initialized
        java.awt.Toolkit.getDefaultToolkit();

        // app requires system tray for support, just exit if there is no support.
        if (!SystemTray.isSupported()) {
            return;
        }

        final java.awt.SystemTray tray = SystemTray.getSystemTray();
        final java.awt.Image image = SwingFXUtils.fromFXImage(this.image, null);
        final java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image, this.title, null);
        trayIcon.addActionListener(event -> Platform.runLater(this::onTrayIconActionFX));

        // Add the icon to the system tray
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            return;
        }

        synchronized (this) {
            this.trayIcon = Optional.of(trayIcon);
        }
    }

    /**
     * Runs on the AWT event thread
     */
    private synchronized void removeIconFromTrayAWT() {
        final java.awt.SystemTray tray = SystemTray.getSystemTray();
        if (this.trayIcon.isPresent()) {
            final java.awt.TrayIcon trayIcon = this.trayIcon.get();
            this.trayIcon = Optional.empty();
            tray.remove(trayIcon);
        }
        notifyAll();
    }

    private synchronized void onTrayIconActionFX() {
        if (this.trayIconAction != null) {
            this.trayIconAction.run();
        }
    }

}
