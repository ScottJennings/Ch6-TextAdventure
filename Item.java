
/**
 * Item class -  an item in an adventure game.
 * 
 * @author Scott Jennings
 * @version 04/06/2015
 */
public class Item
{
    private String description;
    private int weight;

    public Item(String desc, int wgt)
    {
        description = desc;
        weight = wgt;
    }

    /**
     * get description
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
