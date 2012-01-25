package haveric.statues;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class StPlayerInteract implements Listener{
	int NONE = 0;
	int XGREATER = 1;
	int XLESS = 2;
	int ZGREATER = 3;
	int ZLESS = 4;
	
	public enum StatueDirection {ROT_LEFT, ROT_RIGHT, ROT_ONEEIGHTY, SHIFT_LEFT, SHIFT_RIGHT};
	
	public static Statues plugin;
	
	ArrayList<Item> items;
	
	public StPlayerInteract(Statues st){
		plugin = st;
		
		items = new ArrayList<Item>();
		items.add(new Item(Material.WOOL,  0, new Color(255,255,255))); // white
		items.add(new Item(Material.WOOL,  1, new Color(235,136,68 ))); // orange
		items.add(new Item(Material.WOOL,  2, new Color(198,93 ,208))); // magenta
		items.add(new Item(Material.WOOL,  3, new Color(125,155,218))); // light blue
		items.add(new Item(Material.WOOL,  4, new Color(214,200,33 ))); // yellow
		items.add(new Item(Material.WOOL,  5, new Color(65 ,205,52 ))); // lime
		items.add(new Item(Material.WOOL,  6, new Color(224,155,173))); // pink
		items.add(new Item(Material.WOOL,  7, new Color(72 ,72 ,72 ))); // gray
		items.add(new Item(Material.WOOL,  8, new Color(173,180,180))); // light gray
		items.add(new Item(Material.WOOL,  9, new Color(43 ,127,162))); // cyan
		items.add(new Item(Material.WOOL, 10, new Color(140,64 ,207))); // purple
		items.add(new Item(Material.WOOL, 11, new Color(42 ,56 ,167))); // blue
		items.add(new Item(Material.WOOL, 12, new Color(93 ,56 ,30 ))); // brown
		items.add(new Item(Material.WOOL, 13, new Color(61 ,85 ,26 ))); // green
		items.add(new Item(Material.WOOL, 14, new Color(179,49 ,44 ))); // red
		items.add(new Item(Material.WOOL, 15, new Color(33 ,29 ,29 ))); // black
		
		items.add(new Item(Material.SANDSTONE, 0, new Color(239,230,185)));
		items.add(new Item(Material.SANDSTONE, 0, new Color(196,160,119)));
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		Economy econ = plugin.getEcon();
		Permission perm = plugin.getPerm();
		Player player = event.getPlayer();
		
		World world = player.getWorld();
		Block block = event.getClickedBlock();
		
        ItemStack holding = player.getItemInHand();
        
		String playerName = PlayerToBuild.getPlayer(player);
		
		boolean currencyEnabled = true;
		if(player.isOp() || (perm != null && perm.has(player, Perms.getBuild()))){
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block.getType() == Material.DIAMOND_BLOCK && holding.getType() == Material.WOOL){
				
				if (econ == null || perm.has(player,Perms.getIgnoreCost())){
					currencyEnabled = false;
				} else if (!econ.has(player.getName(), Config.getCost())){
            		player.sendMessage(ChatColor.RED + "Not enough money to create a statue. Need " + Config.getCost());
            		return;
            	}
				
				int direction = getDirection(player.getLocation(),block);
				//player.sendMessage("Statue attempted with direction: " + direction);
				URL url;
				try {
					url = new URL("http://s3.amazonaws.com/MinecraftSkins/"+playerName+".png");
					URLConnection urlConnection = url.openConnection();
		
					BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());

					if (Config.isChatty()) { 
						player.sendMessage("Downloading pixel data through redstone modem"); 
					}

					int[][][] pixelData = null;
					
					BufferedImage img = ImageIO.read(in);
					
					in.close();
					pixelData = new int[img.getWidth()][img.getHeight()][4];
					int[] rgb;
	
					for(int i = 0; i < img.getHeight(); i++){
						for (int j = 0; j < img.getWidth(); j++){
							rgb = getPixelData(img, j, i);
	
							for(int k = 0; k < rgb.length; k++){
								pixelData[j][i][k] = rgb[k];
	
							}
						}
					}
					
					if (Config.isChatty()) { 
						player.sendMessage("Creating pixel mapping matrix for woolBit color space");
						player.sendMessage("Shearing sheep..."); 
					}

					createStatue(world,direction,block.getX(),block.getY(),block.getZ(),pixelData);

                	if (currencyEnabled){
                    	econ.withdrawPlayer(player.getName(), Config.getCost());
                    }
                	
					if (Config.isChatty()) { 
						player.sendMessage("Boom! Look at that statue of "+playerName+"!"); 
					}
					
				} catch(Exception e){
					player.sendMessage("The skin for "+playerName+" was not found. Please check your cApiTAliZation & speeling.");
				}
				
			}
		}
	}
	private int getDirection(Location location, Block block) {
			int dir = NONE;
		if (location.getBlockX() == block.getX()){
			if (location.getBlockZ() > block.getZ()){
				dir = ZGREATER;
			} else if (location.getBlockZ() < block.getZ()){
				dir = ZLESS;
			} else {
				// standing on block;
			}
		} else if (location.getBlockZ() == block.getZ()){
			if (location.getBlockX() > block.getX()){
				dir = XGREATER;
			} else { // locationX < blockX
				dir = XLESS;
			}
		}
		return dir;
	}

	private void createStatue(World w,int dir,int wx, int wy, int wz,int[][][] pd) {
		
		ArrayList<StatueBlock> statueArray = new ArrayList<StatueBlock>();
		// top/bottom arms
		for (int y = 18; y >= 17; y--){
			// top arm
			for (int x = 45; x <= 46; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+y-23,wy+19,wz+x-44), item));
				statueArray.add(new StatueBlock(w.getBlockAt(wx+y-11,wy+19,wz+x-44), item));
			}
			// bottom arm
			for (int x = 49; x <= 50; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+y-23,wy+8,wz+x-48), item));
				statueArray.add(new StatueBlock(w.getBlockAt(wx+y-11,wy+8,wz+x-48), item));			
			}
		}
		
		// top/bottom head
		for (int y = 7; y > 0; y--){
			// top head
			for (int x = 9; x <= 14; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+y-3,wy+27,wz+x-10), item));
			}
			// bottom head
			for (int x = 17; x <= 22; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+y-3,wy+20,wz+x-18), item));
			}
		}
		// main head
		for (int y = 15; y > 7; y--){
			// head right side
			for (int x = 1; x <=6; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+4,wy-y+35,wz+x-2), item));
			}
			// head front
			for (int x = 8; x <=15; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+x-7-4,wy-y+35,wz-2), item));
			}
			// head left side
			for (int x = 17; x <= 22; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+1-4,wy-y+35,wz+x-18), item));
			}
			// head back
			for (int x = 24; x <= 31; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+x-23-4,wy-y+35,wz+5), item));
			}
		}
		// bottom row of 12 pixels tall
		for (int y = 31; y > 19; y--){
			// legs sides
			for (int x = 1; x <= 2; x ++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx-3,wy-y+31,wz+x), item));
				statueArray.add(new StatueBlock(w.getBlockAt(wx+4,wy-y+31,wz+x), item));
			}
			
			// legs front
			for (int x = 4; x < 8; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+x-3,wy-y+31,wz), item));
				statueArray.add(new StatueBlock(w.getBlockAt(wx-x+4,wy-y+31,wz), item));
			}
			
			// legs insides
			for (int x = 9; x <= 10; x ++){
				//Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				//statueArray.add(new StatueBlock(w.getBlockAt(wx-0,wy-y+31,wz+x-8), item));
				//statueArray.add(new StatueBlock(w.getBlockAt(wx+1,wy-y+31,wz+x-8), item));
			}
			
			// legs back
			for (int x = 12; x <= 15; x ++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+x-11,wy-y+31,wz+3), item));
				statueArray.add(new StatueBlock(w.getBlockAt(wx-x+12,wy-y+31,wz+3), item));
			}
			// body front
			for(int x = 20; x <= 27; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+x-19-4,wy-y+39,wz), item));
			}
			// body left
			for (int x = 17; x <= 18; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+1-5,wy-y+39,wz+x-16), item));
			}
			
			//body right
			for (int x = 29; x <= 30; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+0+4,wy-y+39,wz+x-28), item));
			}
			
			// body back
			for(int x = 32; x <= 39; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+x-31-4,wy-y+39,wz+3), item));
			}
			// arms inside
			for (int x=49; x<=50; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+1-5,wy-y+39,wz+x-48), item));
				statueArray.add(new StatueBlock(w.getBlockAt(wx+0+5,wy-y+39,wz+x-48), item));
			}
			// arms outside
			for (int x=41; x<=42; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+1-8,wy-y+39,wz+x-40), item));
				statueArray.add(new StatueBlock(w.getBlockAt(wx+0+8,wy-y+39,wz+x-40), item));
			}
			// arms front
			for(int x=44; x <= 47; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+x-43-8,wy-y+39,wz), item));
				statueArray.add(new StatueBlock(w.getBlockAt(wx-x+44+8,wy-y+39,wz), item));
			}
			//arms back
			for (int x=52; x<=55; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+x-51-8,wy-y+39,wz+3), item));
				statueArray.add(new StatueBlock(w.getBlockAt(wx-x+52+8,wy-y+39,wz+3), item));
			}
		}
		if (dir == ZGREATER){
			rotate(w, w.getBlockAt(wx,wy,wz),statueArray, StatueDirection.ROT_ONEEIGHTY);
		} else if (dir == XLESS){
			rotate(w, w.getBlockAt(wx,wy,wz),statueArray, StatueDirection.ROT_LEFT);
		} else if (dir == XGREATER){
			rotate(w, w.getBlockAt(wx,wy,wz),statueArray, StatueDirection.ROT_RIGHT);
		}
		
		for (int i = 0; i < statueArray.size(); i ++){
			Block block = statueArray.get(i).getBlock();
			Item item = statueArray.get(i).getItem();
			if (block.getType() == Material.AIR){
				block.setType(item.getMaterial());
				block.setData((byte)item.getData());
			}
		}
	}

	private void rotate(World w, Block start, ArrayList<StatueBlock> array, StatueDirection dir){
		int size = array.size();
		for (int i = 0; i < size; i ++){
			Block block = array.get(i).getBlock();
			int bx = block.getX() - start.getX();
			int bz = block.getZ() - start.getZ();
			
			if (dir == StatueDirection.ROT_RIGHT){
				array.get(i).setBlock(w.getBlockAt(block.getX()-bx-bz, block.getY(), block.getZ()-bz-bx+1));
			} else if (dir == StatueDirection.ROT_LEFT){
				array.get(i).setBlock(w.getBlockAt(block.getX()-bx+bz, block.getY(), block.getZ()-bz-bx));
			} else if (dir == StatueDirection.ROT_ONEEIGHTY){
				array.get(i).setBlock(w.getBlockAt(block.getX()-bx-bx, block.getY(), block.getZ()-bz-bz));
			}
		}
	}

	private static int[] getPixelData(BufferedImage img, int x, int y){

		int argb = img.getRGB(x, y);

		int rgb[] = new int[] {
				(argb >> 16) & 0xff, //red
				(argb >> 8) & 0xff, //green
				(argb) & 0xff, //blue
				(argb >> 24) & 0xff, //alpha
		};
		return rgb;
	}
	
	private Item getItem(int r, int g, int b, int a){
		int red,green,blue;
		double tempDif;
		//double rmean;
		double weightR,weightG,weightB;
		
		double dif = 10000;
		int val = 0;
		for (int i = 0; i < items.size(); i++){
			Item temp = items.get(i);
			//rmean = (temp.getRed() + r)/2;
			red = temp.getRed()-r;
			green = temp.getGreen()-g;
			blue = temp.getBlue()-b;
			
		    weightR = 1; //2 + rmean/256;
		    weightG = 1; //4.0;
		    weightB = 1; //2 + (255-rmean)/256;
		    
			tempDif = Math.sqrt(weightR*red*red + weightG*green*green + weightB*blue*blue);
			if (tempDif < dif){
				dif = tempDif;
				val = i;
			}
		}
		
		return items.get(val);
	}
	/*
	 * RRR GGG BBB
	 *   0   0   0  // black
	 *  39  51 154  // blue
	 *  86  51  28  // brown
	 *  67  67  67  // gray
	 *  56  77  24  // green
	 * 164  45  41  // red
	 * 129  54 196  // purple
	 *  39 117 149  // cyan
	 * 191  76 201  // pink
	 * 104 139 212  // light blue?
	 * 234 128  55  // orange
	 * 217 131 155  // light red???
	 *  59 189  48  // bright green
	 * 158 166 166  // light gray
	 * 194 181  28  // yellow
	 * 222 222 222  // white
	 *
	 *
	 */
}
