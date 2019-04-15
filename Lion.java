import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a lion.
 * Lions age, move, eat prey, catch disease and die.
 * 
 * @author Andrey and Shehan
 * @version (1, 19.02.2019)
 */
public class Lion extends Predator
{
    // Characteristics shared by all foxes (class variables).
    
    // The age at which a lion can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age to which a lion can live.
    private static final int MAX_AGE = 400;
    // The likelihood of a lion breeding.
    private static final double BREEDING_PROBABILITY = 0.06;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 1;
    
    // Individual characteristics (instance fields).
    // The lion's age.
    private int age;
    // The lion's food level, which is increased by eating prey.
    private int foodLevel;

    /**
     * Create a fox. A lion can be created as a new born (age zero
     * and not hungry) or with a random age and initial foodLevel = 30.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param sex The String sex, either male or female.
     */ 
    public Lion(boolean randomAge, Field field, Location location, String sex)
    {
        super(field, location, sex); 
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = 30;
        }
        else {
            age = 0;
            foodLevel = MARMOT_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the lion does most of the time: it hunts for
     * prey. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param newLions A list to return newly born lions.
     */
    public void act(List<Animal> newLions)
    {
        setDisease();
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newLions);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
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
     * This is what the lion does during fog: 
     * In the process, it might breed, die of hunger,
     * or die of old age.
     * @param newLions A list to return newly born lions.
     */
    public void actAtFog(List<Animal> newLions)
    {
        setDisease();
        incrementAge();
        incrementHunger(); 
        if(isAlive()){
            giveBirth(newLions);
            //Doesn'n hunt so move to random adjacent location.
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            // See if it was possible to move.
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
     * Increase the age. This could result in the lion's death.
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
     * This makes the lion more hungry. This could result in the lion's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for prey adjacent to the current location.
     * Only the first live prey is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Marmot && foodLevel < MARMOT_FOOD_VALUE) {
                Marmot marmot = (Marmot) animal;
                if(marmot.isAlive()) { 
                    marmot.setDead();
                    foodLevel = MARMOT_FOOD_VALUE;
                    return where; 
                }
            }
            else if(animal instanceof Zebra && foodLevel < ZEBRA_FOOD_VALUE) {
                Zebra zebra = (Zebra)animal;
                if(zebra.isAlive()) { 
                    zebra.setDead();
                    foodLevel = ZEBRA_FOOD_VALUE;
                    return where;
                }
            }
            else if(animal instanceof Buffalo) {
                Buffalo buffalo = (Buffalo)animal;
                if(buffalo.isAlive()) { 
                    buffalo.setDead();
                    foodLevel = BUFFALO_FOOD_VALUE;
                    return where;
                }
            }            
        }
        return null;
    }
    
    /**
     * Check whether or not this lion is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newLions A list to return newly born lions.
     */
    private void giveBirth(List<Animal> newLions)
    {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Lion young = new Lion(false, field, loc, giveSex());
            newLions.add(young);
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
     * A fox can breed if it has reached the breeding age.
     * 
     * @return true if the age of the animal is greater than or equal to the breeding age for that animal
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
