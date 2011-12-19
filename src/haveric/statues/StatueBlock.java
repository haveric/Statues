package haveric.statues;

import org.bukkit.block.Block;

public class StatueBlock {

	private Block block;
	private Item item;
	
	public StatueBlock(Block b, Item i){
		block = b;
		item = i;
	}
	
	public void setBlock(Block b){
		this.block = b;
	}
	
	public Block getBlock(){
		return block;
	}
	
	public void setItem(Item i){
		item = i;
	}
	
	public Item getItem(){
		return item;
	}
}
