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

    public static boolean extension = false; 

    public static void main(String[] args) {
        Main main = new Main();
        int maxSteps = 1;
        int runs = 1;

        // scanner to scan for user input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to our model for Rebellion.");
        String response = "";
        System.out.println("Would you like to run the model" 
            +"with the extension enabled?");
        while (!response.equals("y") && !response.equals("n")) {
            System.out.println("Please enter 'y' or 'n'");
            response = scanner.next();
        }

        if (response.equals("y")) {
            extension = true;
        }

        System.out.println("\nPlease enter the number of steps: ");
        // get the number of steps only allow numbers
        while (!scanner.hasNextInt()){
            System.out.println("Please enter an integer greater than 0");
            scanner.nextInt();
        }
        maxSteps = scanner.nextInt();

        System.out.println("\nPlease enter the number of runs: ");
        // get the number of runs only allow numbers
        while (!scanner.hasNextInt()) {
            scanner.nextInt();
        }
        runs = scanner.nextInt();

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
        System.out.println("\nPlease wait, running for " 
            + maxSteps + " steps...\n");
        scanner.close();

        // create output file
        File file = new File("output.csv");
        try {
            FileWriter writer = new FileWriter(file, true);

            // write header
            writer.write("MODEL SETTINGS\n");
            writer.write("Government Legitimacy, Vision, Max Jail Term," + 
                "Agent Density, Cop Density\n");
            writer.write(Params.GOVERNMET_LEGITIMACY+"," + Params.VISION +"," 
            + Params.MAX_JAIL_TERM + "," + Params.INITIAL_AGENT_DENSITY + "," 
            + Params.INITIAL_POLICE_DENSITY + "\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // create stats list


        // main loop
        for (int i=0; i<runs; i++){
                    // create inital map
            WorldMap worldMap = new WorldMap();
            worldMap.setUpMap();
            
            // create stats list
            List<List<Integer>> stats = new ArrayList<>();
            for (int x = 0; x < 4; x++) {
                List<Integer> column = new ArrayList<>();
                stats.add(column);
            }

            // run simulation
            System.out.print("Run " + (i + 1) + " of " + runs + ": ");
            int step = 0;
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
                step += 1;

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
                        Thread.sleep(Params.TICK);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (step % ((maxSteps / 50)+1) == 0) {
                        System.out.print("#");
                    }
                }
                
            }
            System.out.print(" ✔"); // prints completed step progress bar
            System.out.println(); // print new line after progress bar

            try {
                FileWriter writer = new FileWriter(file, true);
    
                // write header
                writer.write("\n Run " + (i + 1) + "\n");
                writer.write("step,quiet agents,jailed" 
                + "agents,active agents\n");
    
                // write stats
                for (int j = 0; j < maxSteps; j++) {
                    for (int k = 0; k < 4; k++) {
                        writer.write(String.valueOf(stats.get(k).get(j)));
                        if (k < 3) {
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

        System.out.println("\nSimulation complete.\nStats written" + 
            " to output.csv.\n");

        // write stats to file
        
    }

    /**
     * Move agents and police
     * NOTE - lists of entities are shuffled randomly before move, agent and cop rule
     * this is to emulate how NetLogo patches return in a random order each time
     * 
     * @param map
     */
    public void step(Tile[][] map) {

        // move rule
        Collections.shuffle(WorldMap.getActiveAgents());
        for (Entity entity : WorldMap.getActiveAgents()) {
            entity.move(map);
        }
        Collections.shuffle(WorldMap.getPolice());
        for (Entity entity : WorldMap.getPolice()) {
            entity.move(map);
        }

        // agent rule
        Collections.shuffle(WorldMap.getActiveAgents());
        for (Entity entity : WorldMap.getActiveAgents()) {
            Agent agent = (Agent) entity;
            agent.determineArrestProbability(map);
            agent.checkRebellion(map);
        }

        // cop rule
        Collections.shuffle(WorldMap.getPolice());
        for (Entity entity : WorldMap.getPolice()) {
            Police police = (Police) entity;
            police.attemptArrest(map);
        }

        // decrement jail term for jailed agents
        for (Entity entity : WorldMap.getJailedAgents()) {
            Agent agent = (Agent) entity;
            agent.decrementJailTerm();
        }
        
        // free all agents who have served their jail sentences
        ArrayList<Agent> toRemove = new ArrayList<>();
        for (Agent agent : WorldMap.getJailedAgents()) {
            if (agent.attemptFree()) {
                toRemove.add(agent);
                WorldMap.getActiveAgents().add(agent);
            }
        }
        WorldMap.getJailedAgents().removeAll(toRemove);
    }
}