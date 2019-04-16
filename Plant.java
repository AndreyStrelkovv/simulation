import java.util.List;
import java.util.Random;

/**
 * Class representing shared characeristics of plants.
 * 
 * @author Andrey and Shehan 
 * @version (1, 19.02.2019)
 */
public abstract class Plant
{
    // Whether alive or not.
    private boolean alive;
    // Plants fied.
    private Field field;
    // Plants location in the field.
    private Location location;
    // Get random object fo further use.
    protected static final Random rand = Randomizer.getRandom();
    // instance variables - replace the example below with your own
    protected int MAX_LITTER_SIZE;
    //Likelihood of a plant reproducing.
    protected double REPRODUCTION_PROBABILITY;

    /**
     * Create a new plant at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Plant(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
    }
    
    /**
     * Make this plant reproduce 
     * @param newPlants A list to receive newly born plants.
     */
    abstract public void reproduce(List<Plant> newPlants);

    /**
     * @return true if alive
     * false othervise
     */
    public boolean isAlive()
    {
        return alive;
    }
    
    /**
     * @return plant's field
     */
    protected Field getField()
    {
        return field;
    }
    
    /**
     * Set the plant and clear the location.
     * Araise it from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }
    
    /**
     * @return the current location of the plant.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the plant at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
}
