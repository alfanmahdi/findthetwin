package com.ftt;

import java.awt.CardLayout;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
    private CardLayout cardLayout = new CardLayout();
    private InGamePanel inGamePanel;
    private InGamePanel newInGamePanel;

    public GamePanel(String[] imagePaths) {
        setLayout(cardLayout);
        add(new MainMenuPanel(), "MainMenu");
        add(new LevelPanel(), "Level");
        add(new PausePanel(), "Pause");
        
        // Create InGamePanel and pass imagePaths
        inGamePanel = new InGamePanel(imagePaths);
        add(inGamePanel, "InGame");
    }

    public void showPanel(String name) {
        cardLayout.show(this, name);
    }
    
}
