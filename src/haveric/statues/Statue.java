package haveric.statues;

import java.util.ArrayList;
import java.util.Date;

import org.bukkit.Location;
import org.bukkit.block.Block;

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
    
    public Statue() {
        
    }
    
    public int getId(){
        return id;
    }
}
