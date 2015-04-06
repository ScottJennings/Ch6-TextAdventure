/**
 * Item class -  an item in an adventure game.
 * 
 * @author Scott Jennings
 * @version 2015.04.05
 */
public class Item
{
    // instance variables - replace the example below with your own
    private String description;
    private int weight;

    public Item(String desc, int wgt)
    {
        description = desc;
        weight = wgt;
    }

    /**
     * Get description
     * 
     * @return the item description
     */
    public String getDescription()
    {
        return description; 
    }
    
    /**
     * get weight
     * 
     * @return the item weight
     */
    public int getWeight()
    {
        return weight;
    }
}
