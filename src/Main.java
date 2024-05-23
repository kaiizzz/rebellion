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
            +" with the extension enabled?");
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
            + maxSteps + " steps and " + runs + " runs...\n");
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
        List<List<Integer>> stats = new ArrayList<>();
        for (int x = 0; x < 3*runs; x++) {
            List<Integer> column = new ArrayList<>();
            stats.add(column);
        }
        for (int i=0; i<runs; i++){
                    // create inital map
            WorldMap worldMap = new WorldMap();
            worldMap.setUpMap();
            
            // create stats list


            // run simulation
            System.out.print("Run " + (i + 1) + " of " + runs + ": ");
            int step = 0;
            while (step <= maxSteps) {

                // update stats
                int rebelCount = WorldMap.getRebellingAgents().size();
                int quietCount = WorldMap.getActiveAgents().size() - rebelCount;
                int jailedCount = WorldMap.getJailedAgents().size();
                stats.get(0+i*3).add(quietCount);
                stats.get(1+i*3).add(jailedCount);
                stats.get(2+i*3).add(rebelCount);

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

        }

        try {
            FileWriter writer = new FileWriter(file, true);
            ArrayList<ArrayList<Double>> averages = new ArrayList<>();
            ArrayList<ArrayList<Double>> variances = new ArrayList<>();
        
            for(int i=0; i<3; i++){
                averages.add(new ArrayList<Double>());
                for(int j=0; j<runs; j++){
                    int sum = 0;
                    for (int k=0; k<maxSteps; k++){
                        sum += stats.get(i + 3*j).get(k);
                    }
                    double average = (sum/(double)maxSteps);
                    averages.get(i).add(average);
                }
            }

            for(int i=0; i<3; i++){
                variances.add(new ArrayList<Double>());
                for(int j=0; j<runs; j++){
                    double variance = 0;
                    for (int k=0; k<maxSteps; k++){
                        variance += Math.pow(stats.get(i + 3*j).get(k) - averages.get(i).get(j), 2);
                    }
                    variance /= (double) maxSteps;
                    variances.get(i).add(variance);
                }
            }

            // write header
            for (int i=0; i<runs; i++){
                writer.write("Run " + (i + 1) + ",,,,");
            }
            writer.write(",quiet mean of means, quiet variance,, jailed mean of means, jailed mean variance,, acitve mean of means, active mean variance");
            writer.write("\nMean:,");
            for (int i=0; i<runs; i++){
                for(int j=0; j<3; j++){ 
                    writer.write(averages.get(j).get(i) + ",");
                }
                writer.write(",");
            }
            writer.write("\nVariance:,");
            for (int i=0; i<runs; i++){
                for(int j=0; j<3; j++){ 
                    writer.write(variances.get(j).get(i) + ",");
                }
                writer.write(",");
            }

            for(int i=0; i<3; i++){ 
                double averageTotal = 0;
                double varianceTotal = 0;
                for(int j=0; j<runs; j++){
                    averageTotal += averages.get(i).get(j);
                    varianceTotal += variances.get(i).get(j);
                }
                double averageAverage = averageTotal/((double) runs);
                double averageVariance = varianceTotal/((double) runs);
                if(i==0){
                    writer.write(averageAverage + ", ");
                    writer.write(averageVariance + ",,");
                }
                if(i==1){
                    writer.write(averageAverage + ", ");
                    writer.write(averageVariance + ",,");
                }  
                if(i==2){
                    writer.write(averageAverage + ",");
                    writer.write(averageVariance + "\n");
                }      
                
            }
        

            writer.write("\n");
            for (int i=0; i<runs; i++){
                writer.write("step,quiet agents,jailed" 
                + "agents,active agents,");
            }
            writer.write("\n");
            for (int i = 0; i < maxSteps; i++) {
                for (int j=0; j<runs; j++){
                    writer.write(i + ",");
                    for (int k = 0; k < 3; k++) {
                        writer.write(String.valueOf(stats.get(k + 3*j).get(i)) + ",");
                    }
                }
                writer.write(",\n");
            } 
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
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


        ArrayList<Entity> allEntities = new ArrayList<>();
        allEntities.addAll(WorldMap.getActiveAgents());
        allEntities.addAll(WorldMap.getPolice());
        allEntities.addAll(WorldMap.getJailedAgents());
        Collections.shuffle(allEntities);

        for (Entity entity : allEntities){
            if (entity.getSymbol() != 'J'){
                entity.move(map);
            }
            if (entity.getSymbol() == 'A' || entity.getSymbol() == 'R'){
                Agent agent = (Agent) entity;
                agent.determineArrestProbability(map);
                agent.checkRebellion(map);
            }
            if (entity.getSymbol() == 'P'){
                Police police = (Police) entity;
                police.attemptArrest(map);
            }
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