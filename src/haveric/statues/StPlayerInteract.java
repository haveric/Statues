package haveric.statues;

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

	public StPlayerInteract(Statues st){
		plugin = st;	
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

				if (econ == null || (perm != null && perm.has(player,Perms.getIgnoreCost()))){
					currencyEnabled = false;
				} else if (!econ.has(player.getName(), Config.getCost())){
            		player.sendMessage(ChatColor.RED + "Not enough money to create a statue. Need " + Config.getCost());
            		return;
            	}

				int direction = getDirection(player.getLocation(),block);
				//player.sendMessage("Statue attempted with direction: " + direction);

				try {
					URL url = new URL("http://s3.amazonaws.com/MinecraftSkins/"+playerName+".png");
					URLConnection urlConnection = url.openConnection();

					BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());

					if (Config.isChatty()) {
						player.sendMessage("Downloading pixel data through redstone modem");
					}

					// TODO: surely there's a better way to store this
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
				statueArray.add(new StatueBlock(w.getBlockAt(wx+x-51,wy+23,wz+y-16), item));
				statueArray.add(new StatueBlock(w.getBlockAt(wx-x+52,wy+23,wz+y-16), item));
			}
			// bottom arm
			for (int x = 49; x <= 50; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+x-55,wy+12,wz+y-16), item));
				statueArray.add(new StatueBlock(w.getBlockAt(wx-x+56,wy+12,wz+y-16), item));
			}
		}

		// top/bottom head
		for (int y = 7; y > 0; y--){
			// top head
			for (int x = 9; x <= 14; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx-x+12,wy+31,wz+y-2), item));
			}
			// bottom head
			for (int x = 17; x <= 22; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx-x+20,wy+24,wz+y-2), item));
			}
		}
		// main head
		for (int y = 15; y > 7; y--){
			// head right side
			for (int x = 1; x <=6; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+4,wy-y+39,wz-x+5), item));
			}
			// head front
			for (int x = 8; x <=15; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx-x+8+4,wy-y+39,wz-2), item));
			}
			// head left side
			for (int x = 17; x <= 22; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+1-4,wy-y+39,wz+x-18), item));
			}
			// head back
			for (int x = 24; x <= 31; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+x-23-4,wy-y+39,wz+5), item));
			}
		}
		// bottom row of 12 pixels tall
		for (int y = 31; y > 19; y--){
			// legs sides
			for (int x = 1; x <= 2; x ++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx-3,wy-y+31,wz-x+3), item));
				statueArray.add(new StatueBlock(w.getBlockAt(wx+4,wy-y+31,wz-x+3), item));
			}

			// legs front
			for (int x = 4; x < 8; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+x-7,wy-y+31,wz), item));
				statueArray.add(new StatueBlock(w.getBlockAt(wx-x+8,wy-y+31,wz), item));
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
				statueArray.add(new StatueBlock(w.getBlockAt(wx-x+28-4,wy-y+43,wz), item));
			}
			// body left
			for (int x = 17; x <= 18; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+1-5,wy-y+43,wz+x-16), item));
			}

			//body right
			for (int x = 29; x <= 30; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+0+4,wy-y+43,wz+x-28), item));
			}

			// body back
			for(int x = 32; x <= 39; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+x-31-4,wy-y+43,wz+3), item));
			}
			// arms inside
			for (int x=49; x<=50; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+1-5,wy-y+43,wz+x-48), item));
				statueArray.add(new StatueBlock(w.getBlockAt(wx+0+5,wy-y+43,wz+x-48), item));
			}
			// arms outside
			for (int x=41; x<=42; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+1-8,wy-y+43,wz-x+43), item));
				statueArray.add(new StatueBlock(w.getBlockAt(wx+0+8,wy-y+43,wz-x+43), item));
			}
			// arms front
			for(int x=44; x <= 47; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+x-43-8,wy-y+43,wz), item));
				statueArray.add(new StatueBlock(w.getBlockAt(wx-x+44+8,wy-y+43,wz), item));
			}
			//arms back
			for (int x=52; x<=55; x++){
				Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
				statueArray.add(new StatueBlock(w.getBlockAt(wx+x-51+4,wy-y+43,wz+3), item));
				statueArray.add(new StatueBlock(w.getBlockAt(wx-x+52-4,wy-y+43,wz+3), item));
			}
		}
		if (dir == ZGREATER){
			//rotate(w, w.getBlockAt(wx,wy,wz),statueArray, StatueDirection.ROT_ONEEIGHTY);
			rotateAngle(w, w.getBlockAt(wx,wy,wz),statueArray, 180);
		} else if (dir == XLESS){
			//rotate(w, w.getBlockAt(wx,wy,wz),statueArray, StatueDirection.ROT_LEFT);
			rotateAngle(w, w.getBlockAt(wx,wy,wz),statueArray, -90);
		} else if (dir == XGREATER){
			//rotate(w, w.getBlockAt(wx,wy,wz),statueArray, StatueDirection.ROT_RIGHT);
			rotateAngle(w, w.getBlockAt(wx,wy,wz),statueArray, 90);
		}

		for (int i = 0; i < statueArray.size(); i ++){
			Block block = statueArray.get(i).getBlock();
			Item item = statueArray.get(i).getItem();
			//if (block.getType() == Material.AIR){ // TODO: uncomment after testing
				block.setType(item.getMaterial());
				block.setData((byte)item.getData());
			//}
		}
	}

	// TODO: rotate by angle?
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

	
	private void rotateAngle(World w, Block start, ArrayList<StatueBlock> array, int degrees){
		double rads = Math.toRadians(degrees);
		double cosDeg = Math.cos(rads);
		double sinDeg = Math.sin(rads);
		
		int h = start.getX();
		int k = start.getZ();
		double xConst = h - h*cosDeg + k*sinDeg;
		double yConst = k - k*cosDeg - h*sinDeg;
		
		for (int i = 0, size = array.size(); i < size; i++) {
			Block block = array.get(i).getBlock();
			int x = block.getX();
			int z = block.getZ();
			
			int newX = (int) (xConst + x*cosDeg - z*sinDeg - 0.5);
			int newZ = (int) (yConst + z*cosDeg + x*sinDeg + 0.5);

			array.get(i).setBlock(w.getBlockAt(newX, block.getY(), newZ));
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
		ArrayList<Item> items = ColorConfig.getListOfItems();
		int size = items.size();
		for (int i = 0; i < size; i++){
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
}
