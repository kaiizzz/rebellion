    
    public class Params {

        
    // Change the value of the following constants to test the program

    public static final int MAP_SIZE = 40;    // map size
    public static final int MAX_JAIL_TERM = 30; // The maximum jail term for agents
    public static final int TICK = 0; // speed of simulation (higher number is slower)
    
    // The vision range of the agents and police
    public static final double VISION = 7; 
    // The government legitimacy value from 0-1
    public static final double GOVERNMET_LEGITIMACY = 0.82; 
    // The initial density of agents in the map
    public static final double INITIAL_AGENT_DENSITY = 70; 
    // The initial density of police in the map
    // IMPORTANT: INITIAL_AGENT_DENSITY + INITIAL_POLICE_DENSITY should be less than 100
    public static final double INITIAL_POLICE_DENSITY = 4; 
    // the proportion that government legitimacy 
    // increases for each nearby jailed agents when extension is enabled
    public static final double EXTENTSION_SCALING = 0.003; 
 
    // whether entities carry out move rule
    public static final boolean MOVEMENT = true;

    }