package com.encdec;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Gui extends JFrame {
	
	JFrame main = new JFrame("Crypteur [By Marcus]");
	
	JPanel j1 = new JPanel (new FlowLayout());
	JCheckBox crypt = new JCheckBox("Crypt");
	JCheckBox decrypt = new JCheckBox("Decrypt");
	
	JLabel passLabel = new JLabel("Mot de passe : ");
	JTextField passField = new JTextField(30);
	
	JPanel j2 = new JPanel();
	JButton startButton = new JButton ("Lancer");
	
	public String choice = null;
	public String password = null;
	
	public Gui() {
		j1.add(crypt);
		j1.add(decrypt);
		crypt.addItemListener(new CheckBoxListener());
		decrypt.addItemListener(new CheckBoxListener());
		
		j1.add(passLabel);
		j1.add(passField);
		
		j2.add(startButton);
		startButton.addActionListener(new ButtonListener());
		
		main.add(j1);
		main.add(j2);
		main.setLayout(new FlowLayout());
		main.setSize(700, 200);
		main.setVisible(true);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private class CheckBoxListener implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
			if(crypt.isSelected())
				choice = "c";
			else if (decrypt.isSelected())
				choice = "d";
        }
    }
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (passField.getText() != null && passField.getText() != "" && choice != null) {
				Banane.init(choice, passField.getText());
			}
		}
    }
}