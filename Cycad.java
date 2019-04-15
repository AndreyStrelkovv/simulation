import java.util.List;
import java.util.Random;

/**
 * Simple model of Cycad.
 * Cycad reproduces.
 * 
 * @author Andrey and Shehan 
 * @version (1, 19.02.2019) 
 */
public class Cycad extends Plant 
{
    // instance variables - replace the example below with your own
    private static final int MAX_LITTER_SIZE = 1;
    // Get random object fo further use.
    private static final Random rand = Randomizer.getRandom();
    //Likelihood of a plant reproducing.
    private static final double REPRODUCTION_PROBABILITY = 0.2;

    /**
     * Constructor for objects of class Cycad.
     * @param field The field currently occupied.
     * @param location The location within the field. 
     */
    public Cycad(Field field, Location location)
    {
        super(field, location);
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
