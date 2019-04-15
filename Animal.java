import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author Andrey and Shehan
 * @version (1, 19.02.2019)
 */
public abstract class Animal
{
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    // The animal's sex, either male or female.
    protected String sex;
    //The likelihood of an animal being infected and containing the disease.
    protected static double DISEASE_PROBABILITY = 0.0001;
    //The likelihood of an animal to catch a disease from anoter animal.
    protected static double CATCH_DISEASE_PROBABILITY = 0.05;
    // A shared random number generator to control breeding.
    protected static final Random rand = Randomizer.getRandom();
    // True if animal catches a disease.
    protected boolean diseased;
    
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param sex the String sex of an animal, either male or female. 
     */
    public Animal(Field field, Location location, String sex)
    {
        diseased = false;
        alive = true;
        this.sex = sex;
        this.field = field;
        setLocation(location);
    }
    
    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Animal> newAnimals);
    
    /**
     * Another act method but during fog.
     * Acts at certain restrictions
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void actAtFog(List<Animal> newAnimals);
    
    /**
     * Animals act at night.
     */
    abstract public void sleep();
    
    /**
     * Return the sex of an animal.
     */
    public String getSex(){
        return sex;
    }

    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
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
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the animal at the new location in the given field.
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
    
    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    protected Field getField()
    {
        return field;
    } 
    
    /**
     * Return true if anumal has diseasse,
     * false otherwise
     */
    public boolean hasDisease()
    {
        return diseased;
    }
    
    /**
     * Changes state of an animal from
     * not diseased to diseased.
     */
    public void catchDisease()
    {
        diseased = true;
    }
    
    /**
     * Gives random sex for the animal.
     * Only implement heterosexual here.
     * Sorry for political incorrectness.
     */
    public static String giveSex() 
    {
        String sex = new String();
        if (rand.nextDouble()<0.5){
             sex = "male";
        }
        else{
             sex = "female";
        }
        return sex;
    }

    /**
     * For all animals, it checks to see whether an animal is currently infected or not 
     * If this is the case and the random number generated is below the disease probability, it infects that animal
     */
    public void setDisease()
    {
        // If animal is not infected and random probability gives true - infect it.
        if (hasDisease() == false && rand.nextDouble() <= DISEASE_PROBABILITY) {
            //The line below checks to see if animals get infected
            //System.out.println("He's got HIV");
            
            diseased = true;            
        }
    }
}
