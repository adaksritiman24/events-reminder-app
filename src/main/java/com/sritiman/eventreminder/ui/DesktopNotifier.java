package com.sritiman.eventreminder.ui;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;

@Slf4j
public class DesktopNotifier {
    private static TrayIcon trayIcon;

    static {
        try {
            if (SystemTray.isSupported()) {

                SystemTray tray = SystemTray.getSystemTray();

                Image image = new BufferedImage(
                        16,
                        16,
                        BufferedImage.TYPE_INT_ARGB
                );

                trayIcon = new TrayIcon(image, "Reminder App");
                trayIcon.setImageAutoSize(true);

                tray.add(trayIcon);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void showNotification(String title, String message) {

        if (trayIcon != null) {
            trayIcon.displayMessage(
                    title,
                    message,
                    TrayIcon.MessageType.INFO
            );
        }
    }
}
