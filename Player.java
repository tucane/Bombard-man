//This class is the character class. It contains all the information of the layer
import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
public class Player implements ActionListener{
	private int max_HP;							//max HP of the player, may change due to items
	private int HP;								//current HP of the player, may change due to attack and potions
	private int bombNum;						//the maximum of bombs that the character can have on the field
	private int power;							//how much damage the bombs are going to do
	private int range;							//how far the bombs are gonna explode
	private int x;								//player x position
	private int y;								//player y position
	private int lag;							//not using lag, delay nor count
	private int delay;
	private int count;
	// 0 right, 1 up, 2 left , 3 down
	private int direction;
	public static final int RIGHT=0;
	public static final int UP=1;
	public static final int LEFT=2;
	public static final int DOWN=3;
	public final int max_BombNum=8;				//at most 8 bombs on the field
	public final int max_Power=80;				//at most 80 damages
	public final int max_Range=10;				//at most 10 grids range
	private ArrayList<Bomb> bomblist=new ArrayList<Bomb>(8);			//all the bombs in this list
	private boolean invincible;											//if the player is invincible
	private long invincible_Start_Time;									//when does it start. Ends 5 seconds after
	private Image[][] pics=new Image[4][5];								//all the images of the player
	public void actionPerformed(ActionEvent evt){
		if (!invincible){
			invincible_Start_Time=System.currentTimeMillis();
		}
		//once the player becomes invincible, count to 5 seconds
		if (System.currentTimeMillis()-invincible_Start_Time>=5000){
			invincible=false;
		}
	}

	public Player(){
		max_HP=100;
		HP=100;
		power=30;
		range=1;
		bombNum=3;
		invincible=false;
		//initial position
		x=0;
		y=0;
		//initial direction
		direction=0;
		delay=3;
		count=0;
		//set up all the pictures
		for (int i=0;i<4;i++){
			for (int j=0;j<5;j++){
				pics[i][j]=new ImageIcon("character/"+i+""+j+".png").getImage();

			}
		}
		javax.swing.Timer myTimer=new javax.swing.Timer(0,this);
		myTimer.start();

	}
	//getting method
	public int get_Max_HP(){
		return max_HP;
	}
	public int get_HP(){
		return HP;
	}
	public int getbombNum(){
		return bombNum;
	}
	public int getPower(){
		return power;
	}
	public int getRange(){
		return range;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public boolean getInvincible(){
		return invincible;
	}
	public ArrayList<Bomb> getBomblist(){
		return bomblist;
	}
	public Image[] getSprite(){
		return pics[direction];
	}
	//HP stuff methods
	public void heal(int amount){
		HP=Math.min(max_HP,HP+amount);
	}
	//the method when the player is attacked
	public void attacked(int amount){
		if (!invincible){
			HP=Math.max(0,HP-amount);
			invincible=true;
		}
	}
	//return if the player is still alive
	public boolean isAlive(){
		return HP>0;
	}
	//bombNum increases
	public void addBomb(){
		bombNum=Math.min(bombNum+1,max_BombNum);
	}
	//power increases
	public void addPower(){
		power=Math.min(power+10,max_Power);
	}
	//range increases
	public void addRange(){
		range=Math.min(range+1,max_Range);
	}
	//using an item
	public void power_Up(Item item){
		item.use(this);
	}
	public void changeDirection(int dir){
		direction=dir;
	}
	//move in different directions
	public void moveRight(){
		x+=25;
		/*
		for (int i=0;i<25;i++){
			x+=1;
		}
		*/
	}
	public void moveLeft(){
		/*
		for (int i=0;i<25;i++){
			x-=1;
		}
		*/
		x-=25;
	}
	public void moveUp(){
		/*
		for (int i=0;i<25;i++){
			y-=1;
		}
		*/
		y-=25;
	}
	public void moveDown(){
		/*
		for (int i=0;i<25;i++){
			y+=1;
		}
		*/
		y+=25;
	}
	//become invincible after being hit
	public void become_Invincible(){
		invincible=true;
	}
	//not using this one
	public void countLag(){
		if (count<delay){
			count+=1;
		}
	}
	//not using this one
	public void resetLag(){
		count=0;
	}
	public void releaseBomb(){
		//if you can release more bombs
		bomblist.add(new Bomb(x,y,this));
	}
	public void setPos(int nx, int ny){
		x=nx;
		y=ny;
	}
	//not using this one
	public boolean listen(){
		return count==delay;
	}

}