//This class contains all the information for a monster
import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
public class Monster implements ActionListener{
	//monster type
	private int type;
	//moster HP
	private int HP;
	//monste damage to plae
	private int attack;
	//monster direction
	private int dire;
	//monster position
	private int x,y;
	//monster sprite frame
	private int frame;
	javax.swing.Timer myTimer;
	private Player player;
	private WorldMap myMap;
	//if the escape path is found
	private boolean toEscape=false;
	//used to check if the enemy should back off
	private int pathBlocked;
	private ArrayList<Integer> escapePath=new ArrayList<Integer>(20);
	//the positions
	public static final int UP=1;
	public static final int RIGHT=0;
	public static final int DOWN=3;
	public static final int LEFT=2;
	private boolean[] movedireAble={true,true, true, true};
	public void actionPerformed(ActionEvent evt){
		if (!isAlive()){
			myTimer.stop();
		}
		//monster's movement
		move();
	}
	//lag is inversly porprotional to the speed of the monster
	public Monster(int type, int HP, int attack, int x, int y,int lag,Player p,WorldMap map){
		this.HP=HP;
		this.attack=attack;
		this.type=type;
		this.x=x;
		this.y=y;
		player=p;
		myMap=map;
		//resting position is 1
		frame=1;
		myTimer=new javax.swing.Timer(lag,this);
		myTimer.start();
	}
	//return if the monster is still alive
	public boolean isAlive(){
		return HP>0;
	}
	//get the damage of the monster
	public int getAttack(){
		return attack;
	}
	//the monster is attaked
	public void reduce_HP(int damage){
		HP=Math.max(0,HP-damage);
	}
	//return the direction of the monster
	public int getDire(){
		return dire;
	}
	//get the frame. Used for sprites
	public int getFrame(){
		return frame;
	}
	//get positions
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	//get type
	public int getType(){
		return type;
	}
	//movement of the monster
	public void move(){
		//if the monster is close to the player, it starts to chase the player
		if (withinRange()){
			involvePlayer();
		}
		else{
			//randomly move
			randomMove();
		}
	}
	//this method is the random movement AI of the monster
	public void randomMove(){
		Random die=new Random();
		//5 actions
		int temp=die.nextInt(5);
		if (temp==0){
			stay();
		}
		else if (temp==1){
			moveRight();
		}
		else if (temp==2){
			moveUp();
		}
		else if (temp==3){
			moveLeft();
		}
		else if (temp==4){
			moveDown();
		}
	}
	//this method allows the monster to get away from the player
	public void escapePlayer(){
		//follow the path
		if (escapePath.size()>0){
			if (escapePath.get(0)==UP){
				moveUp();
			}
			else if (escapePath.get(0)==DOWN){
				moveDown();
			}
			else if (escapePath.get(0)==LEFT){
				moveLeft();
			}
			else if (escapePath.get(0)==RIGHT){
				moveRight();
			}
			escapePath.remove(0);
			//if successfully escaped
			if (escapePath.size()==0){
				toEscape=false;
			}
		}
		//find path that moves further away
		else{
			for (int i=0;i<4;i++){
				if (moveable(x+1,y)){
					escapePath.add(RIGHT);
				}
				if (moveable(x-1,y)){
					escapePath.add(LEFT);
				}
				if (moveable(x,y+1)){
					escapePath.add(DOWN);
				}
				if (moveable(x,y-1)){
					escapePath.add(UP);
				}
			}
		}
	}
	//this method is called when the monster is in the range of interaction with the player
	public void involvePlayer(){
		//if there are more than two sides are blocked by bombs
		if (countBlocked()>=2){
			Random die=new Random();
			//1/50 chance the monster is gonna run away
			int chance=die.nextInt(50);
			if (chance==0){
				toEscape=true;
			}
		}
		if (toEscape){
			escapePlayer();
		}
		else{
			chasePlayer();
		}
	}
	//this method is the AI for monster finding a path to chase the player
	//AI cannot be too smart or game will be too hard
	public void chasePlayer(){
		if (x>player.getX()/25 && movedireAble[LEFT]){
			moveLeft();
		}
		else if (x<player.getX()/25&& movedireAble[RIGHT]){
			moveRight();
		}
		else if (y<player.getY()/25&& movedireAble[DOWN]){
			moveDown();
		}
		else if (y>player.getY()/25 && movedireAble[UP]){
			moveUp();
		}
	}
	//the movememts of the monsters
	public void stay(){
		frame=1;
	}
	public void moveUp(){
		frame=(frame+1)%3;
		dire=Monster.UP;
		if (moveable(x,y-1)){
			y-=1;
			//once the player moves the moveables change
			resetDire();
		}
		else{
			movedireAble[UP]=false;
		}
	}
	public void moveDown(){
		frame=(frame+1)%3;
		dire=Monster.DOWN;
		if (moveable(x,y+1)){
			y+=1;
			resetDire();
		}
		else{
			movedireAble[DOWN]=false;
		}
	}
	public void moveLeft(){
		frame=(frame+1)%3;
		dire=Monster.LEFT;
		if (moveable(x-1,y)){
			x-=1;
			resetDire();
		}
		else{
			movedireAble[LEFT]=false;
		}
	}
	public void moveRight(){
		frame=(frame+1)%3;
		dire=Monster.RIGHT;
		if (moveable(x+1,y)){
			x+=1;
			resetDire();
		}
		else{
			movedireAble[RIGHT]=false;
		}
	}
	//if the given position is moveable
	public boolean moveable(int x, int y){
		return myMap.getElement(x,y).getType()==WorldMap.NOTHING || myMap.getElement(x,y).getType()==WorldMap.LEVEL_UP;
	}
	//this method checks if this monster should chase the player
	public boolean withinRange(){
		return Math.abs(x-player.getX()/25)<5 && Math.abs(y-player.getY()/25)<5;
		//return false;
	}
	//reset the moveable directions
	public void resetDire(){
		for (int i=0;i<4;i++){
			movedireAble[i]=true;
		}
	}
	//count the number of sides that are blocked by bombs
	public int countBlocked(){
		int count=0;
		if (myMap.getElement(x,y+1).getType()==WorldMap.BOMB){
			count+=1;
		}
		if (myMap.getElement(x,y-1).getType()==WorldMap.BOMB){
			count+=1;
		}
		if (myMap.getElement(x+1,y).getType()==WorldMap.BOMB){
			count+=1;
		}
		if (myMap.getElement(x-1,y).getType()==WorldMap.BOMB){
			count+=1;
		}
		return count;
	}
}