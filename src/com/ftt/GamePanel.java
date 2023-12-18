package com.ftt;

import java.awt.CardLayout;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
    private CardLayout cardLayout = new CardLayout();
    private InGamePanel inGamePanel;

    public GamePanel(String[] imagePaths) {
        setLayout(cardLayout);
        add(new MainMenuPanel(), "MainMenu");
        
        // Create InGamePanel and pass imagePaths
        inGamePanel = new InGamePanel(imagePaths);
        add(inGamePanel, "InGame");
        add(new LevelPanel(inGamePanel), "Level");
    }

    public void showPanel(String name) {
        cardLayout.show(this, name);
    }
    
}
