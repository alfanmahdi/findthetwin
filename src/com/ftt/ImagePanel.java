package com.ftt;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Timer;
import java.util.ArrayDeque;
import java.util.Deque;

public class ImagePanel extends JPanel {

    private Image[] images;
    private Image backgroundImage;  // New variable for the covering image
    private int[][][] imagePositions; // Array untuk menyimpan posisi gambar dalam array 3D [3][2][2]
    private int previousClickedImageIndex = -1;
    private boolean[] imageFrozen;
    private int[][] imageIndices;
    private int[] appearedImage;
    private int[] previousClickedIndices = {-1, -1, -1, -1};
    private int gap = 5;
    private boolean[][] drawBackgroundImage; // Array to track whether to draw background image for each element
    private int consecutiveFalseCount = 0;
    private Timer delayTimer;
    private Deque<Integer> stack;

    public ImagePanel(String[] imagePaths) {
        initializeImages(imagePaths);
        imageFrozen = new boolean[imagePaths.length];
        initializeImagePositions();
        initializeImageIndices();
        this.setBackground(Color.BLACK);
        initializeTimer();
        this.stack = new ArrayDeque<>();
        }
    
    private void initializeTimer() {
    	delayTimer = new Timer(1000, e -> {
    	    drawBackgroundImage[previousClickedIndices[0]][previousClickedIndices[1]] =
    	            !drawBackgroundImage[previousClickedIndices[0]][previousClickedIndices[1]];
    	    drawBackgroundImage[previousClickedIndices[2]][previousClickedIndices[3]] =
    	            !drawBackgroundImage[previousClickedIndices[2]][previousClickedIndices[3]];
    	    repaint();
    	    consecutiveFalseCount = 0;
    	    // Add the logic to freeze and unfreeze images here
    	    freezeUnfreezeImages();
    	});
        delayTimer.setRepeats(false); // Set to false to execute the timer only once
    }
    
    private void freezeUnfreezeImages() {
        int size = stack.size();
        for (int k = 0; k < size; k++) {
            int index = stack.pop();
            imageFrozen[index] = !imageFrozen[index];
        }
        repaint();
    }
    
    private void initializeAppearedImages() {
        appearedImage = new int[images.length];
        for (int i = 0; i < images.length; i++) {
            appearedImage[i] = 0;
        }
        
    }
    
    private void initializeImageIndices() {
        imageIndices = new int[4][3];
        initializeAppearedImages();
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                int index;
                do {
                    index = new Random().nextInt(images.length);
                } while (appearedImage[index] > 1);

                imageIndices[i][j] = index;
                appearedImage[index]++;
            }
            
        }
        
    }

    private void initializeImages(String[] imagePaths) {
        images = new Image[imagePaths.length];
        for (int i = 0; i < imagePaths.length; i++) {
            images[i] = Toolkit.getDefaultToolkit().getImage(imagePaths[i]);
        }

        // Load the covering image
        backgroundImage = Toolkit.getDefaultToolkit().getImage("src/assets/Logo1.png");
        // Initialize drawBackgroundImage array
        drawBackgroundImage = new boolean[4][3];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                drawBackgroundImage[i][j] = true;  // Set to true for background image initially
            }
        }
    }
    
    private void initializeImagePositions() {
        imagePositions = new int[4][3][2];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                imagePositions[i][j][0] = (i + 3) * 130 + 5;
                imagePositions[i][j][1] = j * 210 + 25;
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                int x = imagePositions[i][j][0];
                int y = imagePositions[i][j][1];

                // Draw the appropriate image based on the boolean array
                drawImage(g, imageIndices[i][j], x, y, drawBackgroundImage[i][j]);

            }
        }
    }

    // Inside the ImagePanel class
    private void drawImage(Graphics g, int index, int x, int y, boolean isBackground) {
        int width = 108;
        int height = 192;
        int cornerRadius = 20;

        // Create a rounded rectangle as the outer border
        RoundRectangle2D outerBorder = new RoundRectangle2D.Float(x - gap, y - gap, width + 2 * gap, height + 2 * gap, cornerRadius, cornerRadius);
        RoundRectangle2D cardHolder = new RoundRectangle2D.Float(x, y, width, height, cornerRadius, cornerRadius);

        // Save the current clip
        Shape originalClip = g.getClip();

        // Set the clipping region to the outer border
        g.setClip(outerBorder);

        // Draw the inner (white) rectangle
        g.setColor(Color.WHITE);
        g.fillRoundRect(x - gap, y - gap, width + 2 * gap, height + 2 * gap, cornerRadius, cornerRadius);

        // Set the clipping region to the rounded rectangle
        g.setClip(cardHolder);

        // Draw the appropriate image inside the inner border
        if (isBackground) {
            g.drawImage(backgroundImage, x, y, width, height, this);
        } else {
            g.drawImage(images[index], x, y, width, height, this);
        }

        // Reset the clip to the original region
        g.setClip(originalClip);
    }

    public void handleImageClick(int mouseX, int mouseY) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                int x = imagePositions[i][j][0];
                int y = imagePositions[i][j][1] + 25;
                int width = 108;
                int height = 192;

                if (!imageFrozen[imageIndices[i][j]] &&
                        ((mouseX >= x && mouseX <= x + (width + 2 * gap + 3) && mouseY >= y && mouseY <= y + (height + 2 * gap)) ||
                                (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height))) {
                    handleImageClick(i, j, drawBackgroundImage);
                    break;
                }
                
            }
            
        }
    }

    private void handleImageClick(int i, int j, boolean[][] drawBackgroundImage) {
        int clickedIndex = imageIndices[i][j];

        boolean isNewClick = clickedIndex != previousClickedImageIndex;
        boolean isDifferentPosition = i != previousClickedIndices[0] || j != previousClickedIndices[1];

        if (isNewClick) {
            repaint();
            System.out.println("Image " + clickedIndex + " clicked!");

            previousClickedImageIndex = clickedIndex;
            previousClickedIndices[2] = previousClickedIndices[0];
            previousClickedIndices[3] = previousClickedIndices[1];
            previousClickedIndices[0] = i;
            previousClickedIndices[1] = j;

            // Toggle the boolean variable for the next draw
            drawBackgroundImage[i][j] = !drawBackgroundImage[i][j];

            // If consecutiveFalseCount is 1, restart the timer
            if (consecutiveFalseCount == 1) {
            	for (int k = 0; k < images.length; k++) {
            		if (imageFrozen[k] == false) {
            			imageFrozen[k] = true;
            			stack.push(k);
            		}
            	}
                delayTimer.restart();
                previousClickedImageIndex = -1;
            }
            consecutiveFalseCount++;
        } else {
            if (isDifferentPosition) {
                drawBackgroundImage[i][j] = !drawBackgroundImage[i][j];
                imageFrozen[clickedIndex] = true;
                repaint();
                System.out.println("Image " + clickedIndex + " is now frozen!");
                consecutiveFalseCount = 0; // Reset consecutiveFalseCount if a different position is clicked
            } else {
                System.out.println("Image " + clickedIndex + " already clicked!");
            }
        }
    }
    
}