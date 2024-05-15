import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    // Change the value of the following constants to test the program
    public static final int VISION = 7; // The vision range of the agents and police
    public static final double GOVERNMET_LEGITIMACY = 0.80; // The government legitimacy value from 0-1
    public static final double INITIAL_AGENT_DENSITY = 70; // The initial density of agents in the map
    public static final double INITIAL_POLICE_DENSITY = 4; // The initial density of police in the map
    // IMPORTANT: INITIAL_AGENT_DENSITY + INITIAL_POLICE_DENSITY should be less than
    // 100
    public static final int MAX_JAIL_TERM = 10; // The maximum jail term for agents

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
    public static final int TICK = 100;

    public static void main(String[] args) {
        Main main = new Main();
        Boolean doSteps = false;
        int maxSteps = 0;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to our model for Rebellion.");
        System.out.println("Do you want this to run for a specific number of steps? (y/n)");
        String response = scanner.next();
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
        // WorldMap worldMap = new WorldMap(INITIAL_POLICE_DENSITY,
        // INITIAL_AGENT_DENSITY);
        WorldMap worldMap = new WorldMap(INITIAL_POLICE_DENSITY, INITIAL_AGENT_DENSITY);
        worldMap.setUpMap(MAP_SIZE);
        System.out.println("Initial map:");
        // worldMap.displayMap();

        // file output initial
        File file = new File("output.txt");
        try {

            FileWriter writer = new FileWriter(file, true);
            writer.write("Government Legitimacy: " + GOVERNMET_LEGITIMACY + "\n");
            writer.write("Initial Agent Density: " + INITIAL_AGENT_DENSITY + "\n");
            writer.write("Initial Police Density: " + INITIAL_POLICE_DENSITY + "\n");
            writer.write("Max Jail Term: " + MAX_JAIL_TERM + "\n");
            writer.write("\n");
            writer.write("Step " + 0 + ":\n");
            writer.write("active agents: " + WorldMap.getActiveAgents().size() + "\n");
            writer.write("jailed agents: " + WorldMap.getJailedAgents().size() + "\n");
            writer.write("police: " + WorldMap.getPolice().size() + "\n");
            writer.write("\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // main loop
        if (doSteps) {
            int steps = 1;
            int turns = 0;
            while (turns < maxSteps) {

                // do nest step
                main.step(worldMap.getMap());
                System.out.println();
                System.out.println("Step " + steps + ":");

                // display map
                System.out.println("active agents: " + WorldMap.getActiveAgents().size());
                System.out.println("jailed agents: " + WorldMap.getJailedAgents().size());
                System.out.println("police: " + WorldMap.getPolice().size());

                // file output
                try {

                    FileWriter writer = new FileWriter(file, true);
                    writer.write("Step " + steps + ":\n");
                    writer.write("active agents: " + WorldMap.getActiveAgents().size() + "\n");
                    writer.write("jailed agents: " + WorldMap.getJailedAgents().size() + "\n");
                    writer.write("police: " + WorldMap.getPolice().size() + "\n");
                    writer.write("\n");
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                worldMap.displayMap();

                // sleep for a while
                try {
                    Thread.sleep(TICK);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                steps++;
                turns++;
            }
        } else {
            int steps = 1;
            while (true) {

                // do nest step
                main.step(worldMap.getMap());
                System.out.println();
                System.out.println("Step " + steps + ":");

                // display map
                System.out.println("active agents: " + WorldMap.getActiveAgents().size());
                System.out.println("jailed agents: " + WorldMap.getJailedAgents().size());
                System.out.println("police: " + WorldMap.getPolice().size());

                // file output
                try {

                    FileWriter writer = new FileWriter(file, true);
                    writer.write("Step " + steps + ":\n");
                    writer.write("active agents: " + WorldMap.getActiveAgents().size() + "\n");
                    writer.write("jailed agents: " + WorldMap.getJailedAgents().size() + "\n");
                    writer.write("police: " + WorldMap.getPolice().size() + "\n");
                    writer.write("\n");
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                worldMap.displayMap();

                // sleep for a while
                try {
                    Thread.sleep(TICK);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                steps++;
            }
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