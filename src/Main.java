import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Main
 * Author: Bill Zhu, Lucas Kenna and Lin Xie
 * Student Number: 115777, 1170784, 1231766
 * Date: 05/03/2024
 * Description: Main class that runs the simulation
 */

public class Main {

    // Change the value of the following constants to test the program
    public static final int VISION = 7; // The vision range of the agents and police
    public static final double GOVERNMET_LEGITIMACY = 0.82; // The government legitimacy value from 0-1
    public static final double INITIAL_AGENT_DENSITY = 70; // The initial density of agents in the map
    public static final double INITIAL_POLICE_DENSITY = 4; // The initial density of police in the map
    public static final double EXTENTSION_SCALING = 0.003; // the proportion government legitimacy increases
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

    public static boolean extension = false;

    public static void main(String[] args) {
        Main main = new Main();
        int maxSteps = 1;

        // scanner to scan for user input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to our model for Rebellion.");
        String response = "";
        System.out.println("Would you like to run the model with the extension enabled?");
        while (!response.equals("y") && !response.equals("n")) {
            System.out.println("Please enter 'y' or 'n'");
            response = scanner.next();
        }

        if (response.equals("y")) {
            extension = true;
        }

        System.out.println("\nPlease enter the number of steps: ");
        maxSteps = scanner.nextInt();

        System.out.println("\nWould you like to display the map in the command line? (y/n)");
        response = "";
        boolean displayMap = false;
        while (!response.equals("y") && !response.equals("n")) {
            System.out.println("Please enter 'y' or 'n'");
            response = scanner.next();
        }
        if (response.equals("y")) {
            displayMap = true;
        }
        System.out.println("\nPlease wait, running for " + maxSteps + " steps...\n");
        System.out.print("Progress: ");
        scanner.close();

        // insert and display initial map
        WorldMap worldMap = new WorldMap(INITIAL_POLICE_DENSITY, INITIAL_AGENT_DENSITY);
        worldMap.setUpMap(MAP_SIZE);

        // diplay the map if the user wants to
        if (displayMap) {
            System.out.println("Initial map:");
            worldMap.displayMap();
        }

        // create output file
        File file = new File("output.csv");

        // create stats list
        List<List<Integer>> stats = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
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
            if (displayMap) {
                System.out.println();
                System.out.println("Step " + step + ":");
                System.out.println("quiet agents: " + quietCount);
                System.out.println("rebeling agents: " + rebelCount);
                System.out.println("jailed agents: " + jailedCount);

                // display map
                worldMap.displayMap();
            }

            // pause for TICK milliseconds for animation
            if (displayMap) {
                try {
                    Thread.sleep(TICK);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            step += 1;

            // every 1 percent of the way, print a #
            if (!displayMap) {
                if (step % (maxSteps / 35) == 0) {
                    System.out.print("#");
                }
            }
        }

        System.out.println("\n\nSimulation complete.\nStats written to output.csv.\n");

        // write stats to file
        try {
            FileWriter writer = new FileWriter(file, true);

            // write header
            writer.write("step,quiet agents,jailed agents,active agents\n");

            // write stats
            for (int i = 0; i < maxSteps; i++) {
                for (int j = 0; j < 4; j++) {
                    writer.write(String.valueOf(stats.get(j).get(i)));
                    if (j < 3) {
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