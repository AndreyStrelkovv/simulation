import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;

/**
 * A class representing shared characteristics of prey.
 * 
 * @author Andrey and Shehan 
 * @version (1, 19.02.2019)
 */
public abstract class Prey extends Animal
{
    // number of steps a prey can go before it has to eat again if it has eaten a cycad.
    protected static final int CYCAD_FOOD_VALUE = 20;
    // number of steps a prey can go before it has to eat again if it has eaten a grass.
    protected static final int GRASS_FOOD_VALUE = 20;
    // A shared random number generator to control breeding.
    protected static final Random rand = Randomizer.getRandom();
    // a list that will contain a list of all the animals created

    /**
     * Constructor for objects of class Prey
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param sex the String sex of an animal, either male or female.
     */
    public Prey(Field field, Location location, String sex)
    {
        // initialise instance variables
        super(field, location, sex);
        BREED_SEARCH_RADIUS = 3;
    } 
    
    /**
     * Method returns true if there is a male animal in a area 
     * of 10*10 locations aroud female animal
     * of the sema specie.
     * 
     * Check if one of the partners is diseased.
     * If yes then another partner catches a disease
     * at a probability.
     * 
     * @return true if a male is in the locations close to the female
     */
    public boolean partnerClose()
    {      
        if(getSex().equals("female")){
            Field field = getField();
            // get list of locations with not null objects.
            List<Location> filled = field.getFilledLocationsInArea(getLocation(), BREED_SEARCH_RADIUS);
            for (Location place : filled){
                //Check if an object is an animal.
                if(field.getObjectAt(place) instanceof Animal){
                    Animal beast = (Animal) field.getObjectAt(place);
                    //Check if an animal is the same class and different sex.
                    if(beast.getClass() == this.getClass() && beast.getSex() == "male"){
                        //Check if partner is diseased.
                        if(beast.hasDisease() && rand.nextDouble() <= CATCH_DISEASE_PROBABILITY){
                            diseased = true;
                        }
                        if(diseased && rand.nextDouble() <= CATCH_DISEASE_PROBABILITY){
                            beast.catchDisease();
                        }
                        return true;
                    }                
                }
            }           
        }
        return false;
    }
}
