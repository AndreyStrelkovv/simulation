import java.util.Random;

/**
 * Condition class that changes day time and weather.
 *
* @author Andrey and Shehan
 * @version (1, 19.02.2019)
 */
public class Condition
{  
   //Day - true , night - false
   private boolean day;
   //length of day is 5 one step simulations
   private int dayLength = 5;
   // length of full day is 8 one step simulations
   private int dayAndNight = 8;
   //Type of weather
   private String type;
   //likelihood of a rain falling on this step(STEP, not day).
   private static final double RAIN_PROBABILITY = 0.1;
   //likelihood of a dry weatheron this step(STEP, not day).
   private static final double DRY_PROBABILITY = 0.2;
   //likelihood of a fog on this step(STEP, not day).
   private static final double FOG_PROBABILITY = 0.3;
   //Random generator for choosing weather
   private Random rand;
   //Counting number of full days past since the start of the simulation
   private int dayNumber;
   //How many steps have past since the start of the simulation
   private int currentTime;
   
   /**
    * Constructor of condition class.
    * No parameters needed. 
    * Enforces day and warm weather for a good start.
    */
   public Condition()
   {
       type = "warm";      
       day = true;
       dayNumber = 0;
       currentTime = 0;
   }
   
   /**
    * Depending on the probability,
    * chooses weather.
    */
   public void changeWeather()
   {
       Random rand = Randomizer.getRandom();
       if (rand.nextDouble() <= RAIN_PROBABILITY){
           type = "rain";
        }
       else if(rand.nextDouble() <= DRY_PROBABILITY){
           type = "dry";
       }
       else if(rand.nextDouble() <= FOG_PROBABILITY){
           type = "fog";
        }
       else{
           type = "warm";
       }
   }
   
   /**
    * @Return the weather type.
    */
   public String getWeather()
   {
       return type;
   }
   
   /**
    * @Return true if it rains.
    */
   public boolean rainWeather()
   {
       return type.equals("rain");
   }
   
   /**
    * @Return true if the weather is dry.
    */
   public boolean dryWeather()
   {
       return type.equals("dry");
   }
   
   /**
    * @Return true if the weather is fog.
    */
   public boolean fogWeather()
   {
       return type.equals("fog");
   }
      
   /**
    * Increment number of steps past 
    * and number of days if needed.
    */
   public void incrementTime()
   {
       currentTime++;
       if(currentTime % dayAndNight < dayLength){
           day = true;
       }
       else{
           day = false;
       }
       dayNumber = currentTime / 8;
   }
   
   /**
    * @Return true if it is day, false if night.
    */
   public boolean day()
   {
      return day; 
   }   
   
   /**
    * @Return number of days
    */
   public int numberOfDays()
   {
       return dayNumber;
   }
}
