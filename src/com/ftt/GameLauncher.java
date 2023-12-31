package com.ftt;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameLauncher {
    
    public static String[] imagePaths = new String[] {
            "src/assets/foto0.jpg", "src/assets/foto1.jpg", "src/assets/foto2.jpg",
            "src/assets/foto3.jpg", "src/assets/foto4.jpg", "src/assets/foto5.jpg"
    };

    public static void main(String[] args) {
//        imagePaths = new String[] {
//            "src/assets/foto0.jpg", "src/assets/foto1.jpg", "src/assets/foto2.jpg",
//            "src/assets/foto3.jpg", "src/assets/foto4.jpg", "src/assets/foto5.jpg"
//        };
        
        GameFrame GameFrame = new GameFrame(imagePaths);
        GameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        GameFrame.setVisible(true);
        GameFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
            
        });
        
    }
    
}
