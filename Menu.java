//This class is the menu, it allows the user to play the game, read instructions. NO HIGH SCORE because there is no scoring system in the game
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.io.*;
public class Menu extends JFrame implements ActionListener{
	public boolean startGame=false;
	JButton[] options=new JButton[3];
	JFrame instruction_Back;
	JButton iback;
	JFrame high_Score_Back;
	JButton hback;
	JButton instruction;
	ImageIcon instruc_back=new ImageIcon("instruc_back.png");
	String[] word={"Play Game","Instruction","High Score"};
	public Menu(){
		super("Menu");
		instruction_Back=new JFrame();
		high_Score_Back=new JFrame();
		setLayout(null);
		instruction_Back.setLayout(null);
		high_Score_Back.setLayout(null);
		iback=new JButton("Menu");
		iback.setSize(100,30);
		hback=new JButton("Menu");
		hback.setSize(100,30);
		for (int i=0; i<3;i++){
			options[i]=new JButton(word[i]);
			options[i].setSize(150,30);
		}
		instruction=new JButton();
		instruction.setIcon(instruc_back);
		instruction.setSize(300,400);
		instruction_Back.add(instruction);
		display_Menu();
		//display_Instruction();
		instruction_Back.setSize(500,500);
		instruction_Back.add(iback);
		high_Score_Back.setSize(500,500);
		high_Score_Back.add(hback);
		setSize(500,500);
		setVisible(true);
	}
	public void display_Instruction(){
		instruction.setLocation(0,0);
		//instruction_Back.add(instruction);
		iback.addActionListener(this);
		iback.setLocation(350,400);
		setVisible(false);
		instruction_Back.setVisible(true);
		//Graphics g = getGraphics()
	}
	public void display_High_Score(){
		hback.addActionListener(this);
		hback.setLocation(350,400);
		setVisible(false);
		high_Score_Back.setVisible(true);
		//find highscore.txt
	}
	public void display_Menu(){
		instruction_Back.setVisible(false);
		high_Score_Back.setVisible(false);
		setVisible(true);
		for (int i=0;i<3;i++){
			options[i].addActionListener(this);
			options[i].setLocation(150,100+70*i);
			add(options[i]);
		}
	}
	public void actionPerformed(ActionEvent e){
		Object source = e.getSource();
		//play game
		if (source==options[0]){
			setVisible(false);
			startGame=true;
		}
		//instruction
		else if (source==options[1]){
			display_Instruction();
		}
		//high score
		else if (source==options[2]){
			display_High_Score();
		}
		else if (source==iback || source==hback){
			display_Menu();
		}
		repaint();
	}

}