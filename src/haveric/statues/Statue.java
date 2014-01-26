package haveric.statues;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Statue {

    private int id;

    private ArrayList<StatueBlock> blocks;
    private Location location;

    private String player;

    private boolean filled = false;
    private boolean lights = false;

    private boolean update = false;
    private int updateTime = -1;
    private Date lastUpdate;

    enum Direction { NONE, NORTH, EAST, SOUTH, WEST, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST }

    public Statue() {

    }

    public int getId(){
        return id;
    }


    public int[][][] loadTexture(Player builder, String statueName) {
        int[][][] pixelData = null;
        try {
            URL url = new URL("http://s3.amazonaws.com/MinecraftSkins/" + statueName + ".png");
            URLConnection urlConnection = url.openConnection();

            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());

            if (Config.isChatty()) {
                builder.sendMessage("Downloading pixel data through redstone modem");
            }

            // TODO: surely there's a better way to store this


            BufferedImage img = ImageIO.read(in);

            in.close();
            pixelData = new int[img.getWidth()][img.getHeight()][4];
            int[] rgb;

            for (int i = 0; i < img.getHeight(); i++) {
                for (int j = 0; j < img.getWidth(); j++) {
                    rgb = getPixelData(img, j, i);

                    for (int k = 0; k < rgb.length; k++) {
                        pixelData[j][i][k] = rgb[k];
                    }
                }
            }
        } catch (Exception e) {
            builder.sendMessage("The skin for " + statueName + " was not found. Please check your cApiTAliZation & speeling.");
        }
        return pixelData;
    }

    public void createStatue(World w, Direction direction, int wx, int wy, int wz, int[][][] pd) {

        ArrayList<StatueBlock> statueArray = new ArrayList<StatueBlock>();

        // top/bottom arms
        for (int y = 18; y >= 17; y--) {
            // top arm
            for (int x = 45; x <= 46; x++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx+x-51, wy+23, wz-y+19), item));
                statueArray.add(new StatueBlock(w.getBlockAt(wx-x+52, wy+23, wz-y+19), item));
            }
            // bottom arm
            for (int x = 49; x <= 50; x++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx+x-55, wy+12, wz+y-16), item));
                statueArray.add(new StatueBlock(w.getBlockAt(wx-x+56, wy+12, wz+y-16), item));
            }
        }

        // top/bottom head
        for (int y = 7; y >= 0; y--) {
            // top head
            if (y < 7 && y > 0) {
                for (int x = 9; x <= 14; x++) {
                    Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                    statueArray.add(new StatueBlock(w.getBlockAt(wx-x+12, wy+31, wz-y+5), item));
                }
                // bottom head
                for (int x = 17; x <= 22; x++) {
                    Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                    statueArray.add(new StatueBlock(w.getBlockAt(wx-x+20, wy+24, wz+y-2), item));
                }
            }
            // top hat
            for (int x = 40; x <= 47; x++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx-x+44, wy+32, wz-y+5), item));
            }

            // bottom hat
            for (int x = 48; x <= 55; x++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx-x+52, wy+23, wz+y-2), item));
            }
        }
        // main head
        for (int y = 15; y > 7; y--) {
            // head right side
            for (int x = 1; x <=6; x++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx+4, wy-y+39, wz-x+5), item));
            }
            // head front
            for (int x = 8; x <=15; x++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx-x+12, wy-y+39, wz-2), item));
            }
            // head left side
            for (int x = 17; x <= 22; x++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx-3, wy-y+39, wz+x-18), item));
            }
            // head back
            for (int x = 24; x <= 31; x++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx+x-27, wy-y+39, wz+5), item));
            }

            // hat right side
            for (int x = 32; x <= 39; x++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx+5, wy-y+39, wz-x+37), item));
            }

            // hat front
            for (int x = 40; x <= 47; x++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx-x+44, wy-y+39, wz-3), item));
            }

            // hat left side
            for (int x = 48; x <= 55; x++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx-4, wy-y+39, wz+x-50), item));
            }

            // hat back
            for (int x = 56; x <= 63; x++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx+x-59, wy-y+39, wz+6), item));
            }
        }
        // bottom row of 12 pixels tall
        for (int y = 31; y > 19; y--) {
            // legs sides
            for (int x = 1; x <= 2; x ++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx-3, wy-y+31, wz-x+3), item));
                statueArray.add(new StatueBlock(w.getBlockAt(wx+4, wy-y+31, wz-x+3), item));
            }

            // legs front
            for (int x = 4; x < 8; x++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx+x-7, wy-y+31, wz), item));
                statueArray.add(new StatueBlock(w.getBlockAt(wx-x+8, wy-y+31, wz), item));
            }

            // legs insides
            for (int x = 9; x <= 10; x ++) {
                //Item item = getItem(pd[x][y][0],pd[x][y][1],pd[x][y][2],pd[x][y][3]);
                //statueArray.add(new StatueBlock(w.getBlockAt(wx-0,wy-y+31,wz+x-8), item));
                //statueArray.add(new StatueBlock(w.getBlockAt(wx+1,wy-y+31,wz+x-8), item));
            }

            // legs back
            for (int x = 12; x <= 15; x ++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx+x-11, wy-y+31, wz+3), item));
                statueArray.add(new StatueBlock(w.getBlockAt(wx-x+12, wy-y+31, wz+3), item));
            }
            // body front
            for (int x = 20; x <= 27; x++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx-x+24, wy-y+43, wz), item));
            }
            // body left
            for (int x = 17; x <= 18; x++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx-3, wy-y+43, wz+x-16), item));
            }

            //body right
            for (int x = 29; x <= 30; x++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx+4, wy-y+43, wz+x-28), item));
            }

            // body back
            for (int x = 32; x <= 39; x++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx+x-35, wy-y+43, wz+3), item));
            }
            // arms inside
            for (int x=49; x<=50; x++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx-4, wy-y+43 ,wz+x-48), item));
                statueArray.add(new StatueBlock(w.getBlockAt(wx+5, wy-y+43, wz+x-48), item));
            }
            // arms outside
            for (int x=41; x<=42; x++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx-7, wy-y+43, wz-x+43), item));
                statueArray.add(new StatueBlock(w.getBlockAt(wx+8, wy-y+43, wz-x+43), item));
            }
            // arms front
            for (int x=44; x <= 47; x++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx+x-51, wy-y+43, wz), item));
                statueArray.add(new StatueBlock(w.getBlockAt(wx-x+52, wy-y+43, wz), item));
            }
            //arms back
            for (int x=52; x<=55; x++) {
                Item item = getItem(pd[x][y][0], pd[x][y][1], pd[x][y][2], pd[x][y][3]);
                statueArray.add(new StatueBlock(w.getBlockAt(wx+x-47, wy-y+43, wz+3), item));
                statueArray.add(new StatueBlock(w.getBlockAt(wx-x+48, wy-y+43, wz+3), item));
            }
        }
        if (direction == Direction.SOUTH) {
            rotateAngle(w, w.getBlockAt(wx, wy, wz), statueArray, 180);
        } else if (direction == Direction.WEST) {
            rotateAngle(w, w.getBlockAt(wx, wy, wz), statueArray, -90);
        } else if (direction == Direction.EAST) {
            rotateAngle(w, w.getBlockAt(wx, wy, wz), statueArray, 90);
        } else if (direction == Direction.SOUTHEAST) {
            rotateAngle(w, w.getBlockAt(wx, wy, wz), statueArray, 135);
        } else if (direction == Direction.SOUTHWEST) {
            rotateAngle(w, w.getBlockAt(wx, wy, wz), statueArray, -135);
        } else if (direction == Direction.NORTHEAST) {
            rotateAngle(w, w.getBlockAt(wx, wy, wz), statueArray, 45);
        } else if (direction == Direction.NORTHWEST) {
            rotateAngle(w, w.getBlockAt(wx, wy, wz), statueArray, -45);
        }

        for (int i = 0; i < statueArray.size(); i++) {
            // save the old state so we can restore if we need to.
            StatueBlock sBlock = statueArray.get(i);
            sBlock.setPrevBlock(sBlock.getBlock().getState());

            Block block = sBlock.getBlock();
            Item item = sBlock.getItem();
            if (block.getType() == Material.AIR) {
                block.setType(item.getMaterial());
                block.setData((byte) item.getData());
            }
        }
    }

    private void rotateAngle(World w, Block start, ArrayList<StatueBlock> array, int degrees) {
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

    private static int[] getPixelData(BufferedImage img, int x, int y) {

        int argb = img.getRGB(x, y);

        int[] rgb = new int[] {
                (argb >> 16) & 0xff, //red
                (argb >> 8) & 0xff, //green
                (argb) & 0xff, //blue
                (argb >> 24) & 0xff, //alpha
        };
        return rgb;
    }


    private Item getItem(int r, int g, int b, int a) {
        if (a == 0) {
            return new Item(Material.AIR, 0, new Color(0, 0, 0));
        }

        int red, green, blue;
        double tempDif;
        //double rmean;
        double weightR, weightG, weightB;

        double dif = 10000;
        int val = 0;
        ArrayList<Item> items = ColorConfig.getListOfItems();
        int size = items.size();
        for (int i = 0; i < size; i++) {
            Item temp = items.get(i);
            //rmean = (temp.getRed() + r)/2;
            red = temp.getRed() - r;
            green = temp.getGreen() - g;
            blue = temp.getBlue() - b;

            weightR = 1; //2 + rmean/256;
            weightG = 1; //4.0;
            weightB = 1; //2 + (255-rmean)/256;

            tempDif = Math.sqrt(weightR*red*red + weightG*green*green + weightB*blue*blue);
            if (tempDif < dif) {
                dif = tempDif;
                val = i;
            }
        }
        return items.get(val);
    }

    public static Direction getDirection(Player player) {
        double rot = (player.getLocation().getYaw() - 90) % 360;
        if (rot < 0) {
            rot += 360;
        }

        Direction dir = Direction.NONE;

        if (0 <= rot && rot < 22.5) {
            dir = Direction.EAST;
        } else if (22.5 <= rot && rot < 67.5) {
            dir = Direction.SOUTHEAST;
        } else if (67.5 <= rot && rot < 112.5) {
            dir = Direction.SOUTH;
        } else if (112.5 <= rot && rot < 157.5) {
            dir = Direction.SOUTHWEST;
        } else if (157.5 <= rot && rot < 202.5) {
            dir = Direction.WEST;
        } else if (202.5 <= rot && rot < 247.5) {
            dir = Direction.NORTHWEST;
        } else if (247.5 <= rot && rot < 292.5) {
            dir = Direction.NORTH;
        } else if (292.5 <= rot && rot < 337.5) {
            dir = Direction.NORTHEAST;
        } else if (337.5 <= rot && rot < 360) {
            dir = Direction.EAST;
        }
        return dir;
    }
}
