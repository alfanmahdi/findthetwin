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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MainMenuPanel extends JPanel {
	Color backgroundColor = Color.decode("#23253F");
	public MainMenuPanel() {
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        Dimension buttonSize = new Dimension(150, 50);

        JPanel buttons = new JPanel(new GridBagLayout());
        
        addButton(buttons, "Play", gbc, buttonSize);
        addButton(buttons, "Setting", gbc, buttonSize);
        addButton(buttons, "Exit", gbc, buttonSize);

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
		button.setFocusPainted(false);
		
		panel.add(button, gbc);
	}
	
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		setBackground(backgroundColor);
		
		Dimension d = this.getSize();
		
		// Customize main menu appearance
		Font fnt0 = new Font("arial", Font.BOLD, 96);
		g.setFont(fnt0);
		g.setColor(Color.WHITE);
//		g.drawString("FIND THE TWIN", getWidth() / 2 - 350, getHeight() / 2 - 300);
		centeredString("FIND THE TWIN", d.width, d.height, g);
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
			if (e.getActionCommand().equals("Play")) {
				switchToPanel("InGame");
			} else if (e.getActionCommand().equals("Setting")) {
				//
			} else if (e.getActionCommand().equals("Exit")) {
				JOptionPane option = new JOptionPane();
				int choose = JOptionPane.showConfirmDialog(null, "Confirm exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
				if (choose == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
				
			}
			
		}
		
	}
	
	private void switchToPanel(String panelName) {
		GamePanel parent = (GamePanel) getParent();
		parent.showPanel(panelName);
	}
	
}
