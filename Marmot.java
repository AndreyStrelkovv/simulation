import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A simple model of a marmot.
 * Marmot age, move, breed, eat plants, catch disease and die.
 * 
 * @author Andrey and Shehan
 * @version (1, 19.02.2019)
 */
public class Marmot extends Prey
{
    // Characteristics shared by all marmot (class variables).

    // The age at which a marmot can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a marmot can live.
    private static final int MAX_AGE = 120;
    // The likelihood of a marmot breeding.
    private static final double BREEDING_PROBABILITY = 0.15;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    
    // Individual characteristics (instance fields).
    
    // The marmot's age.
    private int age;
    // The marmot's food level, which is increased by eating plant.
    private int foodLevel;

    /**
     * Create a new marmot. A marmot may be created with age
     * zero (a new born) or with a random age and initial foodLevel = 30.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param sex The String sex, either male or female.
     */
    public Marmot(boolean randomAge, Field field, Location location, String sex)
    {
        super(field, location, sex);
   
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = 30;
        }
        else{
             age = 0;
             foodLevel = rand.nextInt(CYCAD_FOOD_VALUE);        
        }
    }
    
    /**
     * This is what the marmot does most of the time - it runs 
     * around. Sometimes it will breed, catch disease or die of old age.
     * @param newMarmots A list to return newly born marmots.
     */
    public void act(List<Animal> newMarmots)
    {
        setDisease();
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newMarmots);            
            // Try to move into a free location.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
    
    /**
     * No difference for act at fog for the prey.
     * Calls act instead.
     * @param newMarmots A list to return newly born marmots.
     */
    public void actAtFog(List<Animal> newMarmots)
    {
        act(newMarmots);
    }
    
    /**
     * During night animal doesn't hunt and doesn't breed.
     */
    public void sleep()
    {
        setDisease();
        incrementAge();
        incrementHunger();
        if(!isAlive()){
            setDead();
        }
    }    
    
    /**
     * Increase the age.
     * This could result in the prey's death.
     */
    private void incrementAge()
    {
        if (diseased) {
            age = age + 2 ;
        }
        else {
            age++;
        }
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * This makes the marmot more hungry. This could result in the marmot's death.
     */
    private void incrementHunger(){
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for plant adjacent to the current location.
     * Only the first plant is eaten
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()){
            Location where = it.next();
            Object plant = field.getObjectAt(where);
            
            if(plant instanceof Cycad){
                Cycad cycad = (Cycad) plant;
                if(cycad.isAlive()){
                    foodLevel = CYCAD_FOOD_VALUE;
                    cycad.setDead();
                    return where;
                }                            
            }
            if(plant instanceof Grass){
                Grass grass = (Grass) plant;
                if(grass.isAlive()){
                    foodLevel = GRASS_FOOD_VALUE;
                    grass.setDead();
                   return where;
                }                
            }
        }
        
        return null;
    }
    
    /**
     * Check whether or not this marmot is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newMarmots A list to return newly born marmot.
     */
    private void giveBirth(List<Animal> newMarmots)
    {
        // New marmot are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            String sex = new String();
            Location loc = free.remove(0);
            Marmot young = new Marmot(false, field, loc, giveSex());
            newMarmots.add(young);
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY && partnerClose()) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A rabbit can breed if it has reached the breeding age.
     * @return true if the marmot can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE; 
    }
}
