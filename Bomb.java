//This class is a bomb class. It contains all the information about a bomb
import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
public class Bomb implements ActionListener{
	private int x,y;						//position
	private int p,r;						//power, range
	private long startTime;					//bomb released time
	javax.swing.Timer myTimer;
	private boolean toExplode;
	public Bomb(int bx, int by,Player c){
		x=bx;
		y=by;
		p=c.getPower();
		r=c.getRange();
		toExplode=false;
		myTimer=new javax.swing.Timer(0,this);
		myTimer.start();
		startTime=System.currentTimeMillis();//get start time
	}
	public void actionPerformed(ActionEvent evt){
		//if the bomb has been on field for 3 seconds
		if (System.currentTimeMillis()-startTime>=3000){
			toExplode=true;
			myTimer.stop();
		}
	}
	//since explosion method is not in this class, getter methods are needed
	public int getPower(){
		return p;
	}
	public int getRange(){
		return r;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	//if the bomb should explode
	public void explodes(){
		toExplode=true;
	}
	//get the current status
	public boolean has_Exploded(){
		return toExplode;
	}
}