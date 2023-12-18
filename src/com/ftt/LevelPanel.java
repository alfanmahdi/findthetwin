package com.ftt;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class LevelPanel extends JPanel {
	Color backgroundColor = Color.decode("#23253F");
	private InGamePanel inGamePanel;
	
	public LevelPanel(InGamePanel inGamePanel) {
		this.inGamePanel = inGamePanel;
		
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.NORTH;
		
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		Dimension buttonSize = new Dimension(300, 50);
		
		JPanel buttons = new JPanel(new GridBagLayout());
		
		addButton(buttons, "Easy", gbc, buttonSize);
		addButton(buttons, "Medium", gbc, buttonSize);
		addButton(buttons, "Hard", gbc, buttonSize);
		addButton(buttons, "Back to Main Menu", gbc, buttonSize);
		
		gbc.weighty = 1;
		add(buttons, gbc);
	}
	
	private void addButton(JPanel panel, String text, GridBagConstraints gbc, Dimension preferredSize) {
		JButton button = new JButton(text);
		button.setPreferredSize(preferredSize);
		
		panel.setBackground(backgroundColor);
		
		button.setFont(new Font("Arial", Font.BOLD, 24));
		button.setForeground(Color.WHITE);
		button.setBackground(backgroundColor);
		button.setBorder(new LineBorder(Color.WHITE));
		
		// Add margin to the bottom of the button
		gbc.insets = new Insets(0, 0, 20, 0);
		
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
		button.setFocusable(false);
		
		panel.add(button, gbc);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		setBackground(backgroundColor);
		
		Dimension d = this.getSize();
		
		// Customize main menu appearance
		Font fnt0 = new Font("Arial", Font.BOLD, 62);
		g.setFont(fnt0);
		g.setColor(Color.WHITE);
		
		centeredString("Select Difficulty", d.width, d.height, g);
	}
	
	public void centeredString(String s, int width, int height, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		int x = (width - fm.stringWidth(s)) / 2;
		int y = (fm.getAscent() + height - (fm.getAscent() + fm.getDescent())) / 5;
		g.drawString(s, x, y);
	}
	
	private class ButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getActionCommand().equals("Easy")) {
				inGamePanel.setImageAmount(2);
                inGamePanel.setRow(2);
                inGamePanel.setColumn(2);
                inGamePanel.setImageIndices(2, 2);
                inGamePanel.setWidth(108);
                inGamePanel.setHeight(192);
                inGamePanel.setImagePositions(2, 2);
                inGamePanel.gameTimer.start();
				switchToPanel("InGame");
			} else if (e.getActionCommand().equals("Medium")) {
				inGamePanel.setImageAmount(4);
                inGamePanel.setRow(4);
                inGamePanel.setColumn(2);
                inGamePanel.setImageIndices(4, 2);
                inGamePanel.setWidth(108);
                inGamePanel.setHeight(192);
                inGamePanel.setImagePositions(4, 2);
                inGamePanel.gameTimer.start();
				switchToPanel("InGame");
			} else if (e.getActionCommand().equals("Hard")) {
				inGamePanel.setImageAmount(8);
                inGamePanel.setRow(4);
                inGamePanel.setColumn(4);
                inGamePanel.setImageIndices(4, 4);
                inGamePanel.setWidth(72);
                inGamePanel.setHeight(128);
                inGamePanel.setImagePositions(4, 4);
                inGamePanel.gameTimer.start();
				switchToPanel("InGame");
			}  else if (e.getActionCommand().equals("Back to Main Menu")) {
				switchToPanel("MainMenu");
			}
		}
	}
	
	private void switchToPanel(String panelName) {
		GamePanel parent = (GamePanel) getParent();
		parent.showPanel(panelName);
	}
}
