package haveric.statues;

import java.util.ArrayList;
import java.util.Collections;

public class StatueDB {

    private ArrayList<Statue> statues;
    
    public StatueDB() {
        
    }
    
    public void loadStatues() {
        
    }
    
    public void loadStatue(int id) {
        
    }
    
    public void addStatue(Statue statue) {
        if (statue != null){
            statues.add(statue);
        }
    }
    
    public void removeStatue(int id) {
        Statue s = getStatue(id);
        if (s != null){
            statues.remove(s);
        }
    }
    
    public Statue getStatue(int id){
        for (Statue s : statues){
            if (s.getId() == id){
                return s;
            }
        }
        return null;
    }
    
    public int getFirstFreeId(){
        int id = 1;
        
        ArrayList<Integer> nums = new ArrayList<Integer>();
        for (Statue s : statues){
            int temp = s.getId();
            
            if (temp == id){
                id++;
            } else {
                nums.add(temp);
            }
        }
        Collections.sort(nums);
        for (int num : nums){
            if (num == id){
                id++;
            } else {
                break;
            }
        }
        
        return id;
    }
}
