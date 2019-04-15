import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A simple model of a zebra.
 * Zebra age, move, breed, eat plants, catch disease and die.
 * 
 * @author Andrey and Shehan
 * @version (1, 19.02.2019)
 */
public class Zebra extends Prey
{
    // Characteristics shared by all zebra (class variables).

    // The age at which a zebra can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a zebra can live.
    private static final int MAX_AGE = 300;
    // The likelihood of a zebra breeding.
    private static final double BREEDING_PROBABILITY = 0.21;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    
    // Individual characteristics (instance fields).
    
    // The zebra's age.
    private int age;
    // The zebra's food level, which is increased by eating plant.
    private int foodLevel;

    /**
     * Create a new zebra. A zebra may be created with age
     * zero (a new born) or with a random age and initial foodLevel = 30.
     * 
     * @param randomAge If true, the zebra will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param sex The String sex, either male or female.
     */
    public Zebra(boolean randomAge, Field field, Location location, String sex)
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
     * This is what the zebra does most of the time - it runs 
     * around. Sometimes it will breed, catch disease or die of old age.
     * @param newZebras A list to return newly born zebras.
     */
    public void act(List<Animal> newZebras)
    {
        setDisease();
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newZebras);            
            // Try to move into a Lfree location.
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
     * @param newZebras A list to return newly born zebras.
     */
    public void actAtFog(List<Animal> newZebra)
    {
        act(newZebra);
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
     * This could result in the zebra's death.
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
     * This makes the zebra more hungry. This could result in the zebra's death.
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
     * Check whether or not this zebra is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newZebras A list to return newly born zebra.
     */
    private void giveBirth(List<Animal> newZebras)
    {
        // New zebra are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Zebra young = new Zebra(false, field, loc, giveSex());
            newZebras.add(young);
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
     * @return true if the rabbit can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE; 
    }
}
