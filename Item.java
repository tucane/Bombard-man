//this class contains all the information for an item
public class Item{
	//types of the item
	public static final int POWER_INCREASE=0;
	public static final int RANGE_INCREASE=1;
	public static final int BOMBNUM_INCREASE=2;
	public static final int SMALL_POTION=3;
	public static final int MEDIUM_POTION=4;
	public static final int LARGE_POTION=5;
	int type;
	int x,y;
	public Item(int n,int posX,int posY){
		type=n;
		x=posX;
		y=posY;
	}
	//used by the player
	public void use(Player p){
		if (type==POWER_INCREASE){
			p.addPower();
		}
		else if (type==RANGE_INCREASE){
			p.addRange();
		}
		else if (type==BOMBNUM_INCREASE){
			p.addBomb();
		}
		else if (type==SMALL_POTION){
			p.heal(10);
		}
		else if (type==MEDIUM_POTION){
			p.heal(30);
		}
		else if (type==LARGE_POTION){
			p.heal(50);
		}
	}
	//get x position
	public int getX(){
		return x;
	}
	//get y position
	public int getY(){
		return y;
	}
	//get the type
	public int getType(){
		return type;
	}
}