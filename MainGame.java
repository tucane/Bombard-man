//This game orginates from the idea of bomberman. It is more like an adventure mode rather than the usual classic vs player mode.
//The player is a playerable character controlled by arrow keys and has the ability to release up to 8 bombs using the space key.
//The objective for the game is to kill all the enemies, who are controlled by Computer AI. The monster can only be damaged by player's bomb
//and they can only attack by touching the player. The player may level up when killing all the enemies and stepping on the level-up grid.
//This is only the trial version, consisting two levels in total. If you would like to play the full version- you CAN'T

//This class is the main game. It runs the game
import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
public class MainGame extends JFrame implements ActionListener{
	GamePanel panel;
	javax.swing.Timer myTimer;
	public static void main(String[] args) throws IOException{
		Menu myMenu=new Menu();
		//once the user clicks on play, the loop ends
		while (!myMenu.startGame){
            System.out.println("Processing");
		}
		MainGame myGame=new MainGame();
	}
	public MainGame() throws IOException{
		super("huh?");
		setLayout(null);
		setSize(800,600);
		setVisible(true);
		panel=new GamePanel();
		panel.setSize(800,600);
		panel.setLocation(0,0);
		//getContentPane().setBackground(Color.BLACK);
		//mygame.setVisible(true);
		add(panel);
		myTimer=new javax.swing.Timer(80,this);
		//myTimer=new javax.swing.Timer(0,this);
		myTimer.start();
	}
	//this method moves the character
	public void player_Move() throws IOException{
		Player player=panel.getPlayer();
		WorldMap myMap=panel.getMap();
		//if there are commands in the stalk
		if (panel.getKeys().size()>0){
			panel.playerMoveSprite();
			//the current key becomes the last key pressed
			int curKey=panel.getKeys().get(panel.getKeys().size()-1);
			//control the player
			if (curKey==player.RIGHT){
				player.changeDirection(Player.RIGHT);
				if (panel.moveable(player.getX()+25,player.getY())){
					player.moveRight();
				}
			}
			if (curKey==player.LEFT){
				player.changeDirection(Player.LEFT);
				if (panel.moveable(player.getX()-25,player.getY())){
					player.moveLeft();
				}
			}
			if (curKey==player.UP){
				player.changeDirection(Player.UP);
				if (panel.moveable(player.getX(),player.getY()-25)){
					player.moveUp();
				}
			}
			if (curKey==player.DOWN){
				player.changeDirection(Player.DOWN);
				if (panel.moveable(player.getX(),player.getY()+25)){
					player.moveDown();
				}
			}
		}
		//if the player is stepping on the level up grid and the player has defeated all the monsters, level up
		if (myMap.getElement(player.getX()/25,player.getY()/25).getType()==WorldMap.LEVEL_UP&& panel.getMonsterList().size()<=0){
			panel.player_Level_Up();
		}
		//the player attacked by the monsters
		panel.player_Affected_Monster();
		//lose if HP reaches 0
		if (player.get_HP()<=0){
			panel.gameOverBox();
			System.exit(0);
		}
	}
	//this method checks all the bombs on the field, and makes certain changes if needed
	public void actionPerformed(ActionEvent evt){
		panel.requestFocusInWindow();
		repaint();
		//player.countLag();
		try{
			//need this since maps might be loaded in player_move method
			player_Move();
		}
		catch (IOException e){
			System.out.println("All levels cleared!");
		}
	}
}
//this class contains all the graphics in the game
class GamePanel extends JPanel implements KeyListener{
	//the current player
	Player player;
	//the sprite index of the player
	int playerFrame;									//used for sprite
	//the stalk for the commands typed
	ArrayList<Integer> keys=new ArrayList<Integer>();
	//loading all the images
	Image tilePic=new ImageIcon("tile.png").getImage();
	Image bombPic=new ImageIcon("bomb.png").getImage();
	Image boomPic=new ImageIcon("explosion.png").getImage();
	Image hardPic=new ImageIcon("hardblock.png").getImage();
	Image softPic=new ImageIcon("softblock.png").getImage();
	Image treePic=new ImageIcon("tree.png").getImage();
	Image nothingPic=new ImageIcon("nothing.png").getImage();
	Image levelupPic=new ImageIcon("levelup.png").getImage();
	Image hpbarPic=new ImageIcon("HPbarpic.png").getImage();
	Image powerUpPic=new ImageIcon("power.png").getImage();
	Image rangeUpPic=new ImageIcon("range.png").getImage();
	Image bombNumUpPic=new ImageIcon("bombnum.png").getImage();
	Image spotionPic=new ImageIcon("smallpotion.png").getImage();
	Image mpotionPic=new ImageIcon("mediumpotion.png").getImage();
	Image lpotionPic=new ImageIcon("largepotion.png").getImage();
	Image invinciblePic=new ImageIcon("invincible.png").getImage();
	//block type, block image
	HashMap<Integer,Image> blockPics=new HashMap<Integer,Image>();
	//item type, item image
	HashMap<Integer,Image> itemPics=new HashMap<Integer,Image>();
	//monster type, monster image
	HashMap<String, Image> monsterPics=new HashMap<String, Image>();
	//monster type, moster dire, monster frame
	Image[][][] monsterSprites =new Image[8][4][3];
	//the current map(based on the current level)
	WorldMap myMap;
	//the current monster list
	ArrayList<Monster> monsterList=new ArrayList<Monster>(100);
	//the initial positions of the levels
	int[] initx={100,100};
	int[] inity={1000,975};
	//current level
	int level;
	public GamePanel() throws IOException{
		super();
		player=new Player();
		//#2 is the standing sprite
		playerFrame=2;
		this.setVisible(true);
		this.addKeyListener(this);
		this.setFocusable(true);
		this.requestFocusInWindow();
		//initial level is level 1
		level=1;
		//level 1 map first
		myMap=new WorldMap(level);
		//set up all the hashmaps
		blockPics.put(WorldMap.P_BLOCK,hardPic);
		blockPics.put(WorldMap.D_BLOCK,softPic);
		blockPics.put(WorldMap.BOMB,bombPic);
		blockPics.put(WorldMap.TREE,treePic);
		blockPics.put(WorldMap.NOTHING,nothingPic);
		blockPics.put(WorldMap.LEVEL_UP,levelupPic);
		itemPics.put(Item.POWER_INCREASE,powerUpPic);
		itemPics.put(Item.RANGE_INCREASE,rangeUpPic);
		itemPics.put(Item.BOMBNUM_INCREASE,bombNumUpPic);
		itemPics.put(Item.SMALL_POTION,spotionPic);
		itemPics.put(Item.MEDIUM_POTION,mpotionPic);
		itemPics.put(Item.LARGE_POTION,lpotionPic);
		//initial position for level 1
		player.setPos(0+25*4,1050-25*2);
		//set up the monster sprites
		for (int i=0;i<8;i++){
			for (int j=0;j<4;j++){
				for (int k=0;k<3;k++){
					monsterSprites[i][j][k]=new ImageIcon("monster"+i+"/"+j+""+k+".png").getImage();
				}
			}
		}
		//load all the monsters
		loadMonsters(level);

	}
	//this method loads all the monsters from a text file to the arraylist
	public void loadMonsters(int level) throws IOException{
		Scanner inFile = new Scanner(new BufferedReader(new FileReader("monsters-"+level+".txt")));
		String[] temp;
		//the first line is the descrptions
		inFile.nextLine();
		while (inFile.hasNextLine()){
			temp=inFile.nextLine().split(" ");
			Monster tempmon=new Monster(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]),Integer.parseInt(temp[2]),Integer.parseInt(temp[4]),Integer.parseInt(temp[5]),Integer.parseInt(temp[3]),player,myMap);
			monsterList.add(tempmon);
		}
	}
	//this method draws everything
	public void paintComponent(Graphics g){
		//draw the map
		drawMap(g,player.getX(),player.getY());
		//draw items on the map
		drawItems(g);
		//draw bomb explosions
		bomb_Check(g);
		//draw the player
		g.drawImage(player.getSprite()[playerFrame],400+5,300-10,this);
		if (player.getInvincible()){
			g.drawImage(invinciblePic,400-12,300-20,this);
		}
		//draw monsters
		drawMonsters(g);
		//draw hp bar
		drawHPbar(g);
	}
	//this method draws the 2d map
	public void drawMap(Graphics g,int posx, int posy){
		//drawing the entire map is inefficient, so i only draw the grids that are on screen
		for (int i=0;i<800/25;i++){
			for (int j=0;j<600/25;j++){
				int posX=player.getX()/25+i-16;
				int posY=player.getY()/25+j-12;
				if (posX<myMap.getX() &&posX>=0 && posY>=0 &&posY<myMap.getY()){
					g.drawImage(blockPics.get(myMap.getElement(posX,posY).getType()),i*25,j*25,this);
				}
			}
		}
	}
	//this method gets all the key pressed
	public void keyPressed(KeyEvent e){
		//there are 5 pics for each direction
		if (e.getKeyCode()==KeyEvent.VK_RIGHT){
			if (!keys.contains(player.RIGHT)){
				keys.add(player.RIGHT);
			}
		}
		else if (e.getKeyCode()==KeyEvent.VK_LEFT){
			if (!keys.contains(player.LEFT)){

				keys.add(player.LEFT);
			}
		}
		else if (e.getKeyCode()==KeyEvent.VK_UP){
			playerFrame=(playerFrame+1)%5;
			if (!keys.contains(player.UP)){

				keys.add(player.UP);
			}
		}

		else if (e.getKeyCode()==KeyEvent.VK_DOWN){
			if (!keys.contains(player.DOWN)){
				keys.add(player.DOWN);
			}
		}
		else if (e.getKeyCode()==KeyEvent.VK_SPACE){
			//you can't release bomb on the level up button
			if (myMap.getElement(player.getX()/25,player.getY()/25).getType()==WorldMap.LEVEL_UP){
				return;
			}
			//if you can still release more bombs
			if (player.getbombNum()>player.getBomblist().size()){
				//you cannot release a bomb on a spot if there is already a bomb on that spot
				boolean collide=false;
				for (int i=0;i<player.getBomblist().size();i++){
					Bomb temp=player.getBomblist().get(i);
					if (temp.getX()==player.getX() && temp.getY()==player.getY()){
						collide=true;
					}
				}
				//if there is no collision, release the bomb
				if (!collide){
					player.releaseBomb();
					myMap.getElement(player.getX()/25,player.getY()/25).changeType(WorldMap.BOMB);
				}
			}
		}
	}
	public void keyReleased(KeyEvent evt){
		//2 is the standing still sprite
		playerFrame=2;
		//remove the keys that are released from the list
		if (evt.getKeyCode()==KeyEvent.VK_DOWN){
			if (keys.contains(player.DOWN)){

				keys.remove(keys.indexOf(player.DOWN));
			}
		}
		if (evt.getKeyCode()==KeyEvent.VK_UP){
			if (keys.contains(player.UP)){

				keys.remove(keys.indexOf(player.UP));
			}
		}
		if (evt.getKeyCode()==KeyEvent.VK_LEFT){
			if (keys.contains(player.LEFT)){

				keys.remove(keys.indexOf(player.LEFT));
			}
		}
		if (evt.getKeyCode()==KeyEvent.VK_RIGHT){
			if (keys.contains(player.RIGHT)){

				keys.remove(keys.indexOf(player.RIGHT));
			}
		}

	}
	public void keyTyped(KeyEvent evt){

	}
	//this method moves the player sprite
	public void playerMoveSprite(){
		playerFrame=(playerFrame+1)%5;
	}
	//this method is in the gamepanel class because it needs to draw something
	public void bomb_Explodes(Bomb bomb, Graphics g){
		for (int i=0;i<monsterList.size();i++){
			monsterList.get(i).resetDire();
		}
		//posx and posy are the positions used to draw on the screen
		//bomb.getX() and bomb.getY() are used to check collisions
		int posX=bomb.getX()-player.getX()+400;
		int posY=bomb.getY()-player.getY()+300;
		int range=bomb.getRange();
		//needs to check all four directions individually since explosion stops when touching a block
		//check origin
		g.drawImage(boomPic,posX,posY,this);
		//bomb affected by bomb itself
		bomb_Affected(bomb.getX(),bomb.getY());
		//player affected by bomb
		player_Affected(bomb.getX(),bomb.getY(),bomb.getPower());
		//monster affected by bomb
		monster_Affected_Bomb(bomb.getX(),bomb.getY(),bomb.getPower());
		//check up
		for (int i=0;i<range;i++){
			bomb_Affected(bomb.getX(),bomb.getY()-25*(i+1));
			player_Affected(bomb.getX(),bomb.getY()-25*(i+1),bomb.getPower());
			monster_Affected_Bomb(bomb.getX(),bomb.getY()-25*(i+1),bomb.getPower());
			if (myMap.explode_Effect(bomb.getX()/25,bomb.getY()/25-(i+1))){
				g.drawImage(boomPic,posX,posY-25*(i+1),this);
			}
			else{
				break;
			}
		}

		//check down
		for (int i=0;i<range;i++){
			bomb_Affected(bomb.getX(),bomb.getY()+25*(i+1));
			player_Affected(bomb.getX(),bomb.getY()+25*(i+1),bomb.getPower());
			monster_Affected_Bomb(bomb.getX(),bomb.getY()+25*(i+1),bomb.getPower());
			if (myMap.explode_Effect(bomb.getX()/25,bomb.getY()/25+(i+1))){
				g.drawImage(boomPic,posX,posY+25*(i+1),this);
			}
			else{
				break;
			}
		}

		//check right

		for (int i=0;i<range;i++){
			bomb_Affected(bomb.getX()+25*(i+1),bomb.getY());
			player_Affected(bomb.getX()+25*(i+1),bomb.getY(),bomb.getPower());
			monster_Affected_Bomb(bomb.getX()+25*(i+1),bomb.getY(),bomb.getPower());
			if (myMap.explode_Effect(bomb.getX()/25+(i+1),bomb.getY()/25)){
				g.drawImage(boomPic,posX+25*(i+1),posY,this);
			}
			else{
				break;
			}
		}

		//check left
		for (int i=0;i<range;i++){
			bomb_Affected(bomb.getX()-25*(i+1),bomb.getY());
			player_Affected(bomb.getX()-25*(i+1),bomb.getY(),bomb.getPower());
			monster_Affected_Bomb(bomb.getX()-25*(i+1),bomb.getY(),bomb.getPower());
			if (myMap.explode_Effect(bomb.getX()/25-(i+1),bomb.getY()/25)){
				g.drawImage(boomPic,posX-25*(i+1),posY,this);
			}
			else{
				break;
			}
		}
		//once the bomb explodes, the tile that has the bomb becomes nothing(it should become what it was before releasing the bomb)
		//but since you can only release a bomb with a NOTHING tile, so it doesn't matter
		myMap.getElement(bomb.getX()/25,bomb.getY()/25).changeType(WorldMap.NOTHING);
	}
	//bomb affected by bomb
	public void bomb_Affected(int x, int y){
		for (int i=0;i<player.getBomblist().size();i++){
			Bomb temp=player.getBomblist().get(i);
			if (x==temp.getX()&& y==temp.getY()&&!temp.has_Exploded()){
				temp.explodes();
			}
		}
	}
	//player affected by bomb
	public void player_Affected(int x, int y,int damage){
		if (player.getX()==x && player.getY()==y){
			player.attacked(damage);
		}
	}
	//player affected by monster
	public void player_Affected_Monster(){
		for (int i=0;i<monsterList.size();i++){
			Monster temp=monsterList.get(i);
			if (temp.getX()*25==player.getX() && temp.getY()*25==player.getY()){
				player.attacked(temp.getAttack());
			}
		}
	}
	//monster affected by bomb
	public void monster_Affected_Bomb(int x, int y, int damage){
		//go through the entire list to check if a monster is damaged
		for (int i=0;i<monsterList.size();i++){
			Monster temp=monsterList.get(i);
			if (temp.getX()*25==x && temp.getY()*25==y){
				temp.reduce_HP(damage);
			}
			if (!temp.isAlive()){
				monsterList.remove(i);
			}
		}
	}
	// this method checks and draws all the bombs and their explosions
	public void bomb_Check(Graphics g){
		for (int i=0;i<player.getBomblist().size();i++){
			if (player.getBomblist().get(i).has_Exploded()){
				bomb_Explodes(player.getBomblist().get(i),g);
				player.getBomblist().remove(i);
			}
		}
	}
	//this method returns the key commands stalk
	public ArrayList<Integer> getKeys(){
		return keys;
	}
	//this method returns the current player
	public Player getPlayer(){
		return player;
	}
	//this method returns the current map
	public WorldMap getMap(){
		return myMap;
	}
	//this method returns the current monster list
	public ArrayList<Monster> getMonsterList(){
		return monsterList;
	}
	//this method checks if the given position is passble
	public boolean moveable(int posX, int posY){
		int tileType=myMap.getElement(posX/25,posY/25).getType();
		//only able to walk on nothing and level up grid
		if (tileType==WorldMap.NOTHING || tileType==WorldMap.LEVEL_UP){
			return true;
		}
		else{
			return false;
		}
	}
	//this method makes the player to level up
	public void player_Level_Up() throws IOException{
		//if all the levels have been cleared
		if (myMap.getLevel()==2){
			clearAllBox();
			System.exit(0);
		}
		int new_level=myMap.getLevel()+1;
		//load the new map based on the current level
		myMap=new WorldMap(new_level);
		//set the new initial position;
		player.setPos(initx[new_level-1],inity[new_level-1]);
		//monster list
		monsterList=new ArrayList<Monster>(100);
		loadMonsters(new_level);
	}
	//this method draws a hp bar on the left side
	public void drawHPbar(Graphics g){
		g.drawImage(hpbarPic,0,200,this);
		int total=player.get_Max_HP();
		int cur= player.get_HP();
		g.setColor(Color.BLACK);
		g.drawRect(0,180,50,15);
		g.setColor(Color.RED);
		g.fillRect(1,181,(int)((double)cur/total*49),14);
	}
	//this method draws all the items on the field
	public void drawItems(Graphics g){
		ArrayList<Item> temp=myMap.getItemlist();
		for (int i=0;i<temp.size();i++){
			g.drawImage(itemPics.get(temp.get(i).getType()),temp.get(i).getX()*25-player.getX()+400,temp.get(i).getY()*25-player.getY()+300,this);
			if (player.getX()==temp.get(i).getX()*25&& player.getY()==temp.get(i).getY()*25){
				temp.get(i).use(player);
				temp.remove(i);
			}
		}
	}
	//this method draws all the monsters on the field
	public void drawMonsters(Graphics g){
		for (int i=0;i<monsterList.size();i++){
			Monster temp=monsterList.get(i);
			g.drawImage(monsterSprites[temp.getType()][temp.getDire()][temp.getFrame()],temp.getX()*25-player.getX()+400,temp.getY()*25-player.getY()+300,this);
		}
	}
	//this method displays a message box with game over message when player loses
	public void gameOverBox(){
		JOptionPane.showMessageDialog(null, "Game Over", "Message", JOptionPane.INFORMATION_MESSAGE);
	}
	public void clearAllBox(){
		JOptionPane.showMessageDialog(null, "All Level Cleared", "Message", JOptionPane.INFORMATION_MESSAGE);
	}
}