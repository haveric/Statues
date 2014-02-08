package haveric.statues;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import org.bukkit.Material;

public class ColorConfig {
    private static Statues plugin;

    private static File defaultColors;
    private static File customColors;
    private static final int COLORS_VERSION = 2;
    private static ArrayList<Item> listOfItems;

    public static void init(Statues st) {
        plugin = st;
        defaultColors = new File(plugin.getDataFolder() + File.separator + "defaultColors.txt");
        customColors = new File(plugin.getDataFolder() + File.separator + "customColors.txt");
    }

    public static void setup() {
        if (!defaultColors.exists()) {
            try {
                defaultColors.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!customColors.exists()) {
            try {
                customColors.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Scanner sc = new Scanner(defaultColors);

            if (defaultColors.length() > 0) {
                sc.next();
                int fileVersion = sc.nextInt();
                if (fileVersion < COLORS_VERSION) {
                    defaultColors.delete();
                    defaultColors = new File(plugin.getDataFolder() + File.separator + "defaultColors.txt");
                    writeColors(defaultColors, true);
                }
            } else {
                writeColors(defaultColors, true);
            }
            sc.close();

            Scanner sc2 = new Scanner(defaultColors);
            listOfItems = new ArrayList<Item>();
            sc2.next();
            sc2.nextInt();
            while (sc2.hasNext()) {
                String s = sc2.next();
                if (s.startsWith("#")) {
                    sc2.nextLine();
                } else {
                    Material mat = Material.getMaterial(s);
                    int data = sc2.nextInt();
                    int r = sc2.nextInt();
                    int g = sc2.nextInt();
                    int b = sc2.nextInt();
                    listOfItems.add(new Item(mat, data, new Color(r, g, b)));
                }
            }
            sc2.close();
        } catch (FileNotFoundException e) {
            Statues.log.warning("defaultColors.txt not found.");
            e.printStackTrace();
        }

        try {
            Scanner sc3 = new Scanner(customColors);

            if (customColors.length() > 0) {
                listOfItems = new ArrayList<Item>();
                while (sc3.hasNext()) {
                    String s = sc3.next();
                    if (s.startsWith("#")) {
                        sc3.nextLine();
                    } else {
                        Material mat = Material.getMaterial(s);
                        int data = sc3.nextInt();
                        int r = sc3.nextInt();
                        int g = sc3.nextInt();
                        int b = sc3.nextInt();
                        listOfItems.add(new Item(mat, data, new Color(r, g, b)));
                    }
                }
            }
            sc3.close();
        } catch (FileNotFoundException e) {
            Statues.log.warning("customColors.txt not found.");
            e.printStackTrace();
        }
    }

    private static void writeColors(File f, boolean vers) {
        try {
            FileWriter fstream = new FileWriter(f);
            PrintWriter out = new PrintWriter(fstream);
            if (vers) {
                out.println("Version: " + COLORS_VERSION);
            }
            //          Material Data R G B
            out.println("# When copying to customColors, do not copy the version line");
            out.println("#");
            out.println("# Material Data R G B");
            out.println("# white");
            out.println("WOOL 0 255 255 255"); // white
            out.println("# orange");
            out.println("WOOL 1 235 136 68"); //orange
            out.println("# magenta");
            out.println("WOOL 2 198 93 208"); //magenta
            out.println("# light blue");
            out.println("WOOL 3 125 155 218"); //light blue
            out.println("# yellow");
            out.println("WOOL 4 214 200 33"); //yellow
            out.println("# lime");
            out.println("WOOL 5 65 205 52"); //lime
            out.println("# pink");
            out.println("WOOL 6 224 155 173"); //pink
            out.println("# gray");
            out.println("WOOL 7 72 72 72"); //gray
            out.println("# light gray");
            out.println("WOOL 8 173 180 180"); //light gray
            out.println("# cyan");
            out.println("WOOL 9 43 127 162"); //cyan
            out.println("# purple");
            out.println("WOOL 10 140 64 207"); //purple
            out.println("# blue");
            out.println("WOOL 11 42 56 167"); //blue
            out.println("WOOL 11 42 50  84");
            out.println("# brown");
            out.println("WOOL 12 93 56 30"); //brown
            out.println("# green");
            out.println("WOOL 13 61 85 26"); //green
            out.println("# red");
            out.println("WOOL 14 179 49 44"); //red
            out.println("# black");
            out.println("WOOL 15 33 29 29"); //black
            out.println("WOOL 15  0  0  0");
            out.println("# skin");
            out.println("SANDSTONE 0 239 230 185");
            out.println("SANDSTONE 0 196 160 119");
            out.println("HUGE_MUSHROOM_1 0 235 187 159");
            out.println("HUGE_MUSHROOM_1 0 190 109 76");

            out.close();
            fstream.close();
        } catch (IOException e) {
            Statues.log.warning(String.format("File %s not found.", f.getName()));
        }
    }

    public static ArrayList<Item> getListOfItems() {
        return listOfItems;
    }

}
