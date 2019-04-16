import java.util.List;

/**
 * Simple model of Cycad.
 * Cycad reproduces.
 * 
 * @author Andrey and Shehan 
 * @version (1, 19.02.2019) 
 */
public class Cycad extends Plant 
{
    /**
     * Constructor for objects of class Cycad.
     * @param field The field currently occupied.
     * @param location The location within the field. 
     */
    public Cycad(Field field, Location location)
    {
        super(field, location);
        MAX_LITTER_SIZE = 1;
        REPRODUCTION_PROBABILITY = 0.2;
    }
    
    /**
     * gets all free locations around the cycad and adds more in the adjacent locations
     * @param a list of all the new cycads created
     */
    public void reproduce(List<Plant> newCycads)
    {
        if(isAlive() && rand.nextDouble() <= REPRODUCTION_PROBABILITY){        
            Field field = getField();
            List<Location> free = field.getFreeAdjacentLocations(getLocation()); 
            int greens = rand.nextInt(MAX_LITTER_SIZE) + 1;
            for(int g = 0; g < greens && free.size() > 0; g++){
                Location loc = free.remove(0);
                Cycad cycad = new Cycad(field, loc);
                newCycads.add(cycad);
            }
        }
    }
}
