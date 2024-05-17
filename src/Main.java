import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

    // Change the value of the following constants to test the program
    public static final int VISION = 7; // The vision range of the agents and police
    public static final double GOVERNMET_LEGITIMACY = 0.82; // The government legitimacy value from 0-1
    public static final double INITIAL_AGENT_DENSITY = 70; // The initial density of agents in the map
    public static final double INITIAL_POLICE_DENSITY = 4; // The initial density of police in the map
    // IMPORTANT: INITIAL_AGENT_DENSITY + INITIAL_POLICE_DENSITY should be less than
    // 100
    public static final int MAX_JAIL_TERM = 30; // The maximum jail term for agents

    // color constants
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    // map size
    public static final int MAP_SIZE = 40;

    // speed of simulation (higher number is slower)
    public static final int TICK = 0;

    public static void main(String[] args) {
        Main main = new Main();
        Boolean doSteps = false;
        int maxSteps = 1;

        // scanner to scan for user input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to our model for Rebellion.");
        String response = "";
        while (!response.equals("y") && !response.equals("n")) {
            System.out.println("Please enter 'y' or 'n'");
            response = scanner.next();
        }
        if (response.equals("y")) {
            System.out.println("Please enter the number of steps: ");
            maxSteps = scanner.nextInt();
            doSteps = true;
            System.out.println("Running for " + maxSteps + " steps...");
        } else {
            System.out.println("Running... press ctrl+c to stop.");
            doSteps = false;
        }
        scanner.close();

        // insert and display initial map
        WorldMap worldMap = new WorldMap(INITIAL_POLICE_DENSITY, INITIAL_AGENT_DENSITY);
        worldMap.setUpMap(MAP_SIZE);
        System.out.println("Initial map:");
        worldMap.displayMap();
        
        // create output file
        File file = new File("output.csv");

        // create stats list
        List<List<Integer>> stats = new ArrayList<>(); 
        for(int i=0; i<4; i++){
            List<Integer> column = new ArrayList<>(); 
            stats.add(column);
        }

        // main loop
        int step = 1;
        while (step <= maxSteps) {

            // update stats
            int rebelCount = WorldMap.getRebellingAgents().size();
            int quietCount = WorldMap.getActiveAgents().size() - rebelCount;
            int jailedCount = WorldMap.getJailedAgents().size();
            stats.get(0).add(step);
            stats.get(1).add(quietCount);
            stats.get(2).add(jailedCount);
            stats.get(3).add(rebelCount);

            // run step
            main.step(worldMap.getMap());
            
            // display stats for debugging
            System.out.println();
            System.out.println("Step " + step + ":");
            System.out.println("quiet agents: " + quietCount);
            System.out.println("rebeling agents: " + rebelCount);
            System.out.println("jailed agents: " + jailedCount);

            // display map for debugging
            worldMap.displayMap();

            // pause for TICK milliseconds for animation
            try {
                Thread.sleep(TICK);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (doSteps){
                step += 1;
            }
        }
        
        // write stats to file
        try {
            FileWriter writer = new FileWriter(file, true);
            
            // write header
            writer.write("step,quiet agents,jailed agents,active agents\n");

            // write stats
            for(int i=0; i<maxSteps; i++){
                for(int j=0; j<4; j++){
                    writer.write(String.valueOf(stats.get(j).get(i)));
                    if (j<3){ 
                        writer.write(",");
                    }
                }
                writer.write("\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Move agents and police
     * 
     * @param map
     */
    public void step(Tile[][] map) {

        // free all agents who have served their jail sentences
        ArrayList<Agent> toRemove = new ArrayList<>();
        for (Agent agent : WorldMap.getJailedAgents()) {
            if (agent.attemptFree()) {
                toRemove.add(agent);
                WorldMap.getActiveAgents().add(agent);
            }
        }
        WorldMap.getJailedAgents().removeAll(toRemove);

        // move rule
        for (Entity entity : WorldMap.getActiveAgents()) {
            entity.move(map, entity.getXpos(), entity.getYpos());
        }
        for (Entity entity : WorldMap.getPolice()) {
            entity.move(map, entity.getXpos(), entity.getYpos());
        }

        // agent rule
        int count = 0;
        Collections.shuffle(WorldMap.getActiveAgents());
        for (Entity entity : WorldMap.getActiveAgents()) {
            Agent agent = (Agent) entity;
            agent.determineArrestProbability(map, agent.getXpos(), agent.getYpos());
            agent.checkRebellion(map, agent.getXpos(), agent.getYpos());
            if (agent.getSymbol() == Agent.REBEL) {
                count += 1;
            }
        }
        System.out.println("rebelling agents: " + count);

        // cop rule
        for (Entity entity : WorldMap.getPolice()) {
            Police police = (Police) entity;
            police.attemptArrest(map);
        }

        // decrement jail term for jailed agents
        for (Entity entity : WorldMap.getJailedAgents()) {
            Agent agent = (Agent) entity;
            agent.decrementJailTerm();
        }

    }
}