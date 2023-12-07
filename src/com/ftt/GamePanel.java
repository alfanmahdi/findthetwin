package com.ftt;

import java.awt.CardLayout;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
	private CardLayout cardLayout = new CardLayout();
	
	public GamePanel() {
		 setLayout(cardLayout);
		 add(new MainMenuPanel(), "MainMenu");
		 add(new InGamePanel(), "Image");
	}
	
	public void showPanel(String name) {
		cardLayout.show(this, name);
	}
}
