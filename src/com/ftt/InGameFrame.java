package com.ftt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InGameFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private ImagePanel imagePanel;

    public InGameFrame(String[] imagePaths) {
        super("In Game");

        imagePanel = new ImagePanel(imagePaths);
        setLayout(new BorderLayout());
        add(imagePanel, BorderLayout.CENTER);

        addMouseListener(new ImageClickListener());
        
        imagePanel = new ImagePanel(imagePaths);
        getContentPane().add(imagePanel, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Handle resizing here
                handleResize();
            }
        });
    }
    
    private void handleResize() {
        // Get the current size of the frame
        Dimension newSize = getSize();

        // Check if the frame is in a restored down state (not maximized)
        if ((getExtendedState() & JFrame.MAXIMIZED_BOTH) == 0) {
            // Adjust the size as needed when restored down
            int targetWidth = 400;
            int targetHeight = 600;

            // Ensure that the width and height are within bounds
            targetWidth = Math.max(targetWidth, imagePanel.getPreferredSize().width);
            targetHeight = Math.max(targetHeight, imagePanel.getPreferredSize().height);

            // Set the new size for the frame
            setSize(targetWidth, targetHeight);
        }
    }

    private class ImageClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            imagePanel.handleImageClick(e.getX(), e.getY());
        }
        
    }

}
