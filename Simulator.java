import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing lion, hyena, buffalo, marmot and zebra.
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 250;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 200;
    // The probability that a lion will be created in any given grid position.
    private static final double LION_CREATION_PROBABILITY = 0.01;
    // The probability that a marmot will be created in any given grid position.
    private static final double MARMOT_CREATION_PROBABILITY = 0.04;    
    // The probability that a hyena will be created in any given grid position.
    private static final double HYENA_CREATION_PROBABILITY = 0.02;
    // The probability that a buffalo will be created in any given grid position.
    private static final double BUFALLO_CREATION_PROBABILITY = 0.03;
    // The probability that a zebra will be created in any given grid position.
    private static final double ZEBRA_CREATION_PROBABILITY = 0.05;
    // The probability that a cycad will be created in any given grid position.
    private static final double CYCAD_CREATION_PROBABILITY = 0.13;
    // The probability that a grass will be created in any given grid position.
    private static final double GRASS_CREATION_PROBABILITY = 0.21;
    //The probability of a plant appearing on a free spot in the field.
    private static final double CYCAD_APPEARENCE_PROBABILITY = 0.025;
    //The probability of a plant appearing on a free spot in the field.
    private static final double GRASS_APPEARENCE_PROBABILITY = 0.05;
    
    // Condition in the enviroment(e.g. fog, rain, day ... ). 
    private Condition condition; 
    // List of animals in the field.
    private List<Animal> animals;
    //List of plants in the field.
    private List<Plant> plants;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        animals = new ArrayList<>();
        plants = new ArrayList<>();
        field = new Field(depth, width);
        condition = new Condition();

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Marmot.class, Color.YELLOW);
        view.setColor(Lion.class, Color.BLUE);
        view.setColor(Hyena.class, Color.RED);
        view.setColor(Buffalo.class, Color.CYAN);
        view.setColor(Zebra.class, Color.BLACK);
        
        view.setColor(Cycad.class, Color.GRAY);
        view.setColor(Grass.class, Color.GREEN);
        
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
            // delay(60);   // uncomment this to run more slowly
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each animal
     * and each plant. 
     * Change conditins and run different methods in case of differend conditions.
     */
    public void simulateOneStep()
    {
        step++;
        
        //Change weather with every step;
        condition.changeWeather();
        //Increment time of the day with every step.
        condition.incrementTime();

        // Provide space for newborn animals.
        List<Animal> newAnimals = new ArrayList<>();        
        // Let all animals act depending on the enviroment condition.
        if(!condition.day()){
            for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
                Animal animal = it.next();
                animal.sleep();
                if(! animal.isAlive()) {
                    it.remove();
                }
            }
        }
        else if(condition.fogWeather()){
            for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
                Animal animal = it.next();
                animal.actAtFog(newAnimals);
                if(! animal.isAlive()) {
                    it.remove();
                }
            }
        }
        else{
            for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
                Animal animal = it.next();
                animal.act(newAnimals);
                if(! animal.isAlive()) {
                    it.remove();
                }
            }
        }
        //infectAnimals();       
        // Add the newly born animals to the main lists.
        animals.addAll(newAnimals);
        
        // Provide space for new plants.
        List<Plant> newPlants = new ArrayList<>(); 
        //let all plants to reproduce. If weather is dry, then don't reproduce.
        if (!condition.dryWeather()){
            for(Iterator<Plant> it = plants.iterator(); it.hasNext();){
                Plant plant = it.next();
                plant.reproduce(newPlants);
                if(!plant.isAlive()){
                    it.remove();
                }            
            }
            List<Location> empty = field.getEmptyLocations();
            Random rand = Randomizer.getRandom();
            for (Location it: empty){
                if(rand.nextDouble() <= CYCAD_APPEARENCE_PROBABILITY) {           
                    Cycad cycad = new Cycad(field, it);
                    plants.add(cycad);
                }   
                else if(rand.nextDouble() <= GRASS_APPEARENCE_PROBABILITY) {          
                    Grass grass = new Grass(field, it);
                    plants.add(grass);
                } 
            }
        }
        else{
            for(Iterator<Plant> it = plants.iterator(); it.hasNext();){
                Plant plant = it.next();
                if(!plant.isAlive()){
                    it.remove();
                }            
            }
        }
        
        // Add the newly born plants to the main lists.
        plants.addAll(newPlants);                

        view.showStatus(step, field, condition);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        animals.clear();
        plants.clear();
        populate();
        
        // Show the starting state in the view.
        view.showStatus(step, field, condition);
    }
    
    /**
     * Randomly populate the field with all types of animals and plants.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= LION_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    String sex = new String();
                    Lion lion = new Lion(true, field, location, Animal.giveSex());
                    animals.add(lion);
                }
                else if(rand.nextDouble() <= HYENA_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Hyena hyena = new Hyena(true, field, location, Animal.giveSex());
                    animals.add(hyena);
                }
                else if(rand.nextDouble() <= BUFALLO_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Buffalo buffalo = new Buffalo(true, field, location, Animal.giveSex());
                    animals.add(buffalo);
                }
                else if(rand.nextDouble() <= MARMOT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Marmot marmot = new Marmot(true, field, location, Animal.giveSex());
                    animals.add(marmot);
                }                                
                else if(rand.nextDouble() <= ZEBRA_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    String sex = new String();
                    Zebra zebra = new Zebra(true, field, location, Animal.giveSex());
                    animals.add(zebra);
                }
                else if(rand.nextDouble() <= CYCAD_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);           
                    Cycad cycad = new Cycad(field, location);
                    plants.add(cycad);
                }   
                else if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);           
                    Grass grass = new Grass(field, location);
                    plants.add(grass);
                }         
                // else leave the location empty.
            }
        }
    }
    
    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
}
