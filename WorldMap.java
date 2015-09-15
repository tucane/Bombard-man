//this class contains all the information for the current map
import java.io.*;
import java.util.*;
public class WorldMap{
	//types of the tiles
	public final static int P_BLOCK=0;				//permanent block
	public final static int D_BLOCK=1;				//destructable block
	public final static int TREE=2; 					//trees
	public final static int NOTHING=3;				//nothing here
	public final static int BOMB=4;					//if this tile has a bomb on it
	public final static int LEVEL_UP=5;				//the tile that allows you to get to the next level
	//the map
	private Tile[][] myMap;
	//current level
	private int cur_level;
	private ArrayList<Item> itemlist=new ArrayList<Item>(50);
	public WorldMap(int level) throws IOException{
		try{
			Scanner inFile = new Scanner(new BufferedReader(new FileReader("level/level"+level+".txt")));
			//level1x,level1y,level2x,level2y...
			int[] sizex={101,102};
			int[] sizey={43,41};
			myMap=new Tile[sizex[level-1]][sizey[level-1]];
			cur_level=level;
			String temp;
			int count=0;							//used to keep tract of the index
			while (inFile.hasNextLine()){
				temp=inFile.nextLine();
				for (int i=0;i<temp.length();i++){
					myMap[i][count]=new Tile(Character.getNumericValue(temp.charAt(i)));
					//System.out.println(myMap[i][count].getType());
				}
				count+=1;
			}
		}
		catch (IOException e){
			System.out.println("meep");
		}
	}
	//the method for bomb exploding, return if the explosion is blocked
	public boolean explode_Effect(int x, int y){
		//bomb
		if (myMap[x][y].getType()==TREE){
			return false;
		}
		else if (myMap[x][y].getType()==D_BLOCK){
			//tile gets destroyed, but the explosion stops
			myMap[x][y].changeType(NOTHING);
			if (toGenerate()){
				itemlist.add(generate_Item(x,y));
			}
			return false;
		}
		else if (myMap[x][y].getType()==P_BLOCK){
			return false;
		}
		else if (myMap[x][y].getType()==NOTHING){
			return true;
		}
		else if (myMap[x][y].getType()==BOMB){
			return true;
		}
		else if (myMap[x][y].getType()==LEVEL_UP){
			return true;
		}
	return true;
	}
	public int getX(){
		return myMap.length;
	}
	public int getY(){
		return myMap[0].length;
	}
	public Tile getElement(int x,int y){
		return myMap[x][y];
	}
	public int getLevel(){
		return cur_level;
	}
	//if to generate an item
	public boolean toGenerate(){
		//no more than 100 items on the map
		if (itemlist.size()>=50){
			return false;
		}
		Random die=new Random();
		// 1/8 chance to generate an item when destroying a block
		int chance=die.nextInt(8);
		return chance==5;
	}
	public Item generate_Item(int x, int y){
		Random die=new Random();
		//0,1,2 are power up items(power, range and bombnum increases, 3,4,5 are small, medium and large
		//potions respecfully
		int[] itemlist={0,0,0,0,0,1,1,1,1,1,2,2,2,2,2,3,3,3,3,3,3,3,4,4,4,5};
		int index= die.nextInt(itemlist.length);
		return new Item(itemlist[index],x,y);
	}
	//return the item list
	public ArrayList<Item> getItemlist(){
		return itemlist;
	}
}