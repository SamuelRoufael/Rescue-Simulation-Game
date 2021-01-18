package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameOverGUI extends JFrame {
	JButton Exit;
	JButton NewGame;
	JPanel mainPanel;
	JLabel background;
	
	public GameOverGUI() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(700,100, 750, 550);
		setLayout(new BorderLayout());
		createbackground();
		setVisible(true);
		validate();
		createMain();
		
	}
	private void createbackground() {
		ImageIcon image = new ImageIcon("GameOver.jpg");
		Image img = image.getImage();
		Image dimg = img.getScaledInstance(this.getWidth(),this.getHeight(),Image.SCALE_SMOOTH);
		image.setImage(dimg);
		background = new JLabel("",image,JLabel.CENTER);
		add(background,BorderLayout.CENTER);
	}
	private void createMain() {
		mainPanel = new JPanel(new FlowLayout());
		mainPanel.setPreferredSize(new Dimension(100,100));
		add(mainPanel,BorderLayout.EAST);
		createExit();
		NewGame();
		mainPanel.add(Exit);
		mainPanel.add(NewGame);
	}
	private void createExit() {
		Exit = new JButton("Exit Game");
		Exit.setBounds(0,200,100,100);
		Exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
	private void NewGame() {
		NewGame = new JButton("NewGame");
		NewGame.setBounds(0,50,100,100);
		NewGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new View();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				dispose();
			}
			
		});
	}
	public static void main(String[] args) {
		new GameOverGUI();
	}
}
