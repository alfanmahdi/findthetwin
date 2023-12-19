package com.ftt;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayDeque;
import java.util.Deque;

public class InGamePanel extends JPanel {

	private Image[] images;
    private Image backgroundImage;
    public boolean[][] drawCoverImage;
    private int[][][] imagePositions;
    public int[][] imageIndices;
    private int[] previousClickedIndices = new int[4];
    public int previousClickedImageIndex = -1;
    public boolean[] imageFrozen;
    public int[] appearedImage;
    private Timer delayTimer;
    public int imageAmount;
    public int row;
    public int column;
    public int width;
    public int height;
    private int gap = 5;
    private int consecutiveFalseCount = 0;
    private Deque<Integer> stack;
    Color backgroundColor = Color.decode("#23253F");
    public Timer gameTimer;
    private int remainingTime = 45; // Initial time in seconds
    private JLabel timerLabel;
    private int score = 0;
    private JLabel scoreLabel;
    private int frozenImageCounter = 0;

    public InGamePanel(String[] imagePaths) {
    	setPreviousClickedIndices();
    	setImageAmount(8);
    	setRow(4);
    	setColumn(4);
    	setWidth(108);
    	setHeight(192);
        initializeImages(imagePaths);
        initializeImageFrozen();
        initializeImagePositions();
        initializeImageIndices();
        initializeTimer();
        this.setBackground(backgroundColor);
        this.stack = new ArrayDeque<>();
        initializeTimer();
        initializeGameTimer();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleImageClick(e.getX(), e.getY());
            }
            
        });
        JPanel button = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        addButton(this, "Pause", gbc, new Dimension(100, 50));
        // Create and add the timer label
        timerLabel = new JLabel();
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setHorizontalAlignment(SwingConstants.LEFT);
        timerLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(timerLabel);
        scoreLabel = new JLabel();
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setHorizontalAlignment(SwingConstants.LEFT);
        scoreLabel.setVerticalAlignment(SwingConstants.TOP);
        add(scoreLabel);
        updateScoreDisplay();
        updateTimerDisplay();
        gameTimer.stop();
    }
    
    private void addButton(JPanel panel, String text, GridBagConstraints gbc, Dimension preferredSize) {
		JButton button = new JButton(text);
		button.setPreferredSize(preferredSize);
		
		button.setFont(new Font("Arial", Font.BOLD, 18));
		button.setForeground(Color.WHITE);
		button.setBackground(backgroundColor);
		
		gbc.insets = new Insets(0, 0, 0, 0);
		
		button.addActionListener(new ButtonHandler());
		
		button.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				button.setBackground(Color.WHITE);
				button.setForeground(backgroundColor);
			}
			
			public void mouseExited(MouseEvent e) {
				button.setBackground(backgroundColor);
				button.setForeground(Color.WHITE);
			}
		});
		
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setFocusPainted(false);
		panel.add(button, gbc);
	}
    
    private class ButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Pause")) {
				gameTimer.stop();
				switchToPanel("Pause");
			}
			
		}
		
	}
    
    private void initializeGameTimer() {
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remainingTime--;

                if (remainingTime <= 0) {
                    gameTimer.stop();
                    handleGameLose();
                }

                // Update the timer display in your frame (you need to define a method to update UI)
                updateTimerDisplay();
            }
        });

        gameTimer.start();
    }
    
    private void updateTimerDisplay() {
        // Set the text of the timer label and position it to the left and centerY
        timerLabel.setText("Time: " + remainingTime + " seconds");
        int labelWidth = timerLabel.getPreferredSize().width;
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int labelX = centerX - labelWidth - 10; // Adjust the position as needed
        int labelY = centerY - timerLabel.getPreferredSize().height / 2;
        timerLabel.setBounds(labelX, labelY, labelWidth, timerLabel.getPreferredSize().height);
    }
    
    private void initializeImages(String[] imagePaths) {
        images = new Image[imageAmount];
        for (int i = 0; i < imageAmount; i++) {
            images[i] = Toolkit.getDefaultToolkit().getImage(imagePaths[i]);
        }

        backgroundImage = Toolkit.getDefaultToolkit().getImage("src/assets/Logo1.png");
        
        drawCoverImage = new boolean[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                drawCoverImage[i][j] = true;
            }
            
        }
        
    }
    
    private void initializeImagePositions() {
        imagePositions = new int[row][column][2];        
    }
    
    private void initializeImageIndices() {
        imageIndices = new int[row][column];
        initializeAppearedImages(); 
    }
    
    private void initializeImageFrozen() {
        imageFrozen = new boolean[imageAmount];
    }
    
    private void initializeTimer() {
    	delayTimer = new Timer(500, e -> {
    	    drawCoverImage[previousClickedIndices[0]][previousClickedIndices[1]] =
    	            !drawCoverImage[previousClickedIndices[0]][previousClickedIndices[1]];
    	    drawCoverImage[previousClickedIndices[2]][previousClickedIndices[3]] =
    	            !drawCoverImage[previousClickedIndices[2]][previousClickedIndices[3]];
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
        appearedImage = new int[imageAmount];
    }
    
    public void setPreviousClickedIndices() {
    	for(int i = 0; i < 4; i++) {
    		previousClickedIndices[i] = -1;
    	}
    	
    }
    
    public void setImagePositions(int row, int collumn) {
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	int centerX = screenSize.width/2;
    	int centerY = screenSize.height/2;
    	
    	for (int i = 0; i < row; i++) {
    		for (int j = 0; j < column; j++) {
    			imagePositions[i][j][0] = centerX + (i - row/2) * (width + 20);
                imagePositions[i][j][1] = centerY + (j - column/2) * (height + 20);
    		}
    	}
    }
    
    public void setImageIndices(int row, int column) {
    	for (int i = 0; i < imageAmount; i++) {
            appearedImage[i] = 0;
        }
    	
    	for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                int index;
                do {
                    index = new Random().nextInt(imageAmount);
                } while (appearedImage[index] > 1);

                imageIndices[i][j] = index;
                appearedImage[index]++;
            }
            
        }
    	
    }
    
    public void setImageAmount(int imageAmount) {
    	this.imageAmount = imageAmount;
    }
    
    public void setRow(int row) {
    	this.row = row;
    }
    
    public void setColumn(int column) {
    	this.column = column;
    }
    
    public void setWidth(int width) {
    	this.width = width;
    }
    
    public void setHeight(int height) {
    	this.height = height;
    }
    
    public void setRemainingTime(int remainingTimer) {
    	this.remainingTime = remainingTimer;
    }
    
    public void setScore(int score) {
    	this.score = score;
    }
    
    public void setFrozenImageCounter(int frozenImageCounter) {
    	this.frozenImageCounter = frozenImageCounter;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                int x = imagePositions[i][j][0];
                int y = imagePositions[i][j][1];

                // Draw the appropriate image based on the boolean array
                drawImage(g, imageIndices[i][j], x, y, drawCoverImage[i][j]);
            }
            
        }
        
    }

    private void drawImage(Graphics g, int index, int x, int y, boolean isBackground) {
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
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                int x = imagePositions[i][j][0];
                int y = imagePositions[i][j][1] + 25;
                
                if (!imageFrozen[imageIndices[i][j]] &&
                        ((mouseX >= x && mouseX <= x + (width + 2 * gap + 3) && mouseY >= y && mouseY <= y + (height + 2 * gap)) ||
                                (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height))) {
                    handleImageClick(i, j, drawCoverImage);
                    break;
                }
                
            }
            
        }
        
    }

    private void handleImageClick(int i, int j, boolean[][] drawCoverImage) {
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
            drawCoverImage[i][j] = !drawCoverImage[i][j];

            // If consecutiveFalseCount is 1, restart the timer
            if (consecutiveFalseCount == 1) {
            	for (int k = 0; k < imageAmount; k++) {
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
                drawCoverImage[i][j] = !drawCoverImage[i][j];
                imageFrozen[clickedIndex] = true;
                repaint();
                System.out.println("Image " + clickedIndex + " is now frozen!");
                frozenImageCounter++;
                // Increment the score by 20
                score += 20;
                updateScoreDisplay(); // Add a method to update the score display
                consecutiveFalseCount = 0; // Reset consecutiveFalseCount if a different position is clicked
             // Check if all images are frozen (You Win!)
                if (frozenImageCounter == imageAmount) {
                    handleGameWin(); // Add a method to handle winning
                }
            } else {
                System.out.println("Image " + clickedIndex + " already clicked!");
            }
            
        }
        
    }
    
    private void updateScoreDisplay() {
        // Assuming you have a JLabel named scoreLabel
        scoreLabel.setText("Score: " + score);
    }
    
    private void switchToPanel(String panelName) {
		GamePanel parent = (GamePanel) getParent();
		parent.showPanel(panelName);
	}
    
    private void handleGameWin() {
        gameTimer.stop();
        int option = JOptionPane.showOptionDialog(
                this,
                "You Win!",
                "Congratulations",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"Play Again", "Main Menu"},
                "OK");

        if (option == 0) {
        	switchToPanel("Level");
        	setRemainingTime(45);
        	setScore(0);
        	setFrozenImageCounter(0);
        	for (int i = 0; i < imageAmount; i++) {
                imageFrozen[i] = !imageFrozen[i];
            }
            
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    drawCoverImage[i][j] = true;
                }
                
            }
        }
        else if (option == 1) {
            switchToPanel("MainMenu");
            setRemainingTime(45);
            setScore(0);
            setFrozenImageCounter(0);
            for (int i = 0; i < imageAmount; i++) {
                imageFrozen[i] = !imageFrozen[i];
            }
            
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    drawCoverImage[i][j] = true;
                }
                
            }
            
        }
        
    }
    
    private void handleGameLose() {
    	int option = JOptionPane.showOptionDialog(
                this,
                "You Lose!",
                "Loser!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"Play Again", "Main Menu"},
                "OK");

    	if (option == 0) {
    		switchToPanel("Level");
            setRemainingTime(45);
            setScore(0);
            setFrozenImageCounter(0);
            for (int i = 0; i < imageAmount; i++) {
                imageFrozen[i] = !imageFrozen[i];
            }
            
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    drawCoverImage[i][j] = true;
                }
                
            }
    	}
    	else if (option == 1) {
        	switchToPanel("MainMenu");
            setRemainingTime(45);
            setScore(0);
            setFrozenImageCounter(0);
            for (int i = 0; i < imageAmount; i++) {
                imageFrozen[i] = !imageFrozen[i];
            }
            
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    drawCoverImage[i][j] = true;
                }
                
            }
            
        }
        
    }
    
}
