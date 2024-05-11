import java.util.ArrayList;
import java.util.Collections;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final int MAP_SIZE = 40;
    public static final int TICK = 100;
    public static final int VISION = 7;
    public static final double GOVERNMET_LEGITIMACY = 0.82;
    public static final double INITIAL_AGENT_DENSITY = 70;
    public static final double INITIAL_POLICE_DENSITY = 4;
    public static final int MAX_JAIL_TERM = 30;
    
    public static void main(String[] args) {
        Main main = new Main();

        // insert and display initial map
        WorldMap worldMap = new WorldMap(INITIAL_POLICE_DENSITY, INITIAL_AGENT_DENSITY);
        worldMap.setUpMap(MAP_SIZE);
        System.out.println("Initial map:");
        worldMap.displayMap();

        // main loop
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

    /**
     * Move agents and police
     * 
     * @param map
     */
    public void step(Tile[][] map) {

        // free all agents who have served their jail sentences
        ArrayList<Agent> toRemove = new ArrayList<>();
        for (Agent agent : WorldMap.getJailedAgents()){
            if (agent.attemptFree()){
                toRemove.add(agent);
                WorldMap.getActiveAgents().add(agent);
            }
        }
        WorldMap.getJailedAgents().removeAll(toRemove);

        // move rule
        for (Entity entity : WorldMap.getActiveAgents()){
            entity.move(map, entity.getXpos(), entity.getYpos());
        }
        for (Entity entity : WorldMap.getPolice()){
            entity.move(map, entity.getXpos(), entity.getYpos());
        } 

        // agent rule
        int count = 0;
        Collections.shuffle(WorldMap.getActiveAgents());
        for (Entity entity : WorldMap.getActiveAgents()){
            Agent agent = (Agent) entity;
            agent.determineArrestProbability(map, agent.getXpos(), agent.getYpos());
            agent.checkRebellion(map, agent.getXpos(), agent.getYpos());
            if(agent.getSymbol() == Agent.REBEL){
                count +=1;
            }
        }
        System.out.println("rebelling agents: " + count);

        // cop rule
        for (Entity entity : WorldMap.getPolice()){
            Police police = (Police) entity;
            police.attemptArrest(map);
        } 

        // decrement jail term for jailed agents
        for (Entity entity : WorldMap.getJailedAgents()){
            Agent agent = (Agent) entity;
            agent.decrementJailTerm();
        }

    }
}