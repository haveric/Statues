package haveric.statues;

import java.awt.Color;

import org.bukkit.Material;

public class Item {

    private Material material;
    private int data;
    private Color color;
    public Item(Material m, int d, Color c) {
        this.material = m;
        this.data = d;
        this.color = c;
    }

    public Material getMaterial() {
        return material;
    }

    public int getData() {
        return data;
    }

    public int getRed() {
        return color.getRed();
    }

    public int getGreen() {
        return color.getGreen();
    }

    public int getBlue() {
        return color.getBlue();
    }
}
