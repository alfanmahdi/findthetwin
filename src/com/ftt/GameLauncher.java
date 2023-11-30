package com.ftt;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameLauncher {

    public static void main(String[] args) {
        String[] imagePaths = {"src/assets/foto0.jpg", "src/assets/foto1.jpg", "src/assets/foto2.jpg",
        		"src/assets/foto3.jpg", "src/assets/foto4.jpg", "src/assets/foto5.jpg"};
        InGameFrame inGameFrame = new InGameFrame(imagePaths);

        // Maximize the frame
        inGameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        inGameFrame.setVisible(true);

        inGameFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }
}
