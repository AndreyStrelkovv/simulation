import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;

/**
 * A class representing shared characteristics of predator.
 * 
 * @author Andrey and Shehan 
 * @version (1, 19.02.2019)
 */
public abstract class Predator extends Animal
{
    // The food value of a single marmot. In effect, this is the
    // number of steps a predator can go before it has to eat again if it has eaten a marmot.
    protected static final int MARMOT_FOOD_VALUE = 20;
    // The food value of a single zebra. In effect, this is the
    // number of steps a predator can go before it has to eat again if it has eaten a zebra.
    protected static final int ZEBRA_FOOD_VALUE = 25;
    // The food value of a single buffalo. In effect, this is the
    // number of steps a predator can go before it has to eat again if it has eaten a buffalo.
    protected static final int BUFFALO_FOOD_VALUE = 30;
    //Area in which female predator looks for male predetor to breed

    // A shared random number generator to control breeding.
    protected static final Random rand = Randomizer.getRandom();

    /**
     * Constructor for objects of class Predetor
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param sex the String sex of an animal, either male or female.
     */
    public Predator(Field field, Location location, String sex)
    {
        // initialise instance variables
        super(field, location, sex); 
        BREED_SEARCH_RADIUS = 7;
    }

    /**
     * Method returns true if there is a male animal in a area 
     * of 10*10 locations aroud female animal
     * of the same species.
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
                        if(beast.hasDisease() && rand.nextDouble()<= CATCH_DISEASE_PROBABILITY){
                            diseased = true;
                        }
                        if(diseased && rand.nextDouble()<= CATCH_DISEASE_PROBABILITY){
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
