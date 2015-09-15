//this class contains all the information for a tile
import java.util.*;
public class Tile{
	//type of the tile. All types are listed in worldmap class
	private int type;
	public Tile(int t){
		type=t;
	}
	//return the tile type
	public int getType(){
		return type;
	}
	//change the tile type. Only happens when a bomb destroy this tile
	public void changeType(int newt){
		type=newt;
	}

}