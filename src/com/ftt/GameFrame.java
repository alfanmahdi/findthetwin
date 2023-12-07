package com.ftt;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GameFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public GameFrame(String[] imagePaths) {
        setTitle("Find The Twin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        add(new GamePanel(imagePaths));

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Handle resizing here
                handleResize();
            }
            
        });
        
    }

    private void handleResize() {
        // Check if the frame is in a restored down state (not maximized)
        if ((getExtendedState() & JFrame.MAXIMIZED_BOTH) == 0) {
            // Adjust the size as needed when restored down
            int targetWidth = 600;
            int targetHeight = 400;

            // Ensure that the width and height are within bounds
            targetWidth = Math.max(targetWidth, this.getPreferredSize().width);
            targetHeight = Math.max(targetHeight, this.getPreferredSize().height);

            // Set the new size for the frame
            setSize(targetWidth, targetHeight);

            // Center the frame after resizing
            setLocationRelativeTo(null);
        }
        
    }
    
}
