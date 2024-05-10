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
    public static final int TICK = 1000;
    public static final int VISION = 4;
    public static final double GOVERNMET_LEGITIMACY = 0.8;
    public static final double INITIAL_AGENT_DENSITY = 10;
    public static final double INITIAL_POLICE_DENSITY = 5;
    
    public static void main(String[] args) {
        Main main = new Main();

        // insert and display initial map
        WorldMap WorldMap = new WorldMap(INITIAL_POLICE_DENSITY, INITIAL_AGENT_DENSITY);
        WorldMap.setUpMap(MAP_SIZE);
        System.out.println("Initial map:");
        // WorldMap.displayMap(WorldMap.getMap());

        // main loop
        int steps = 1;
        
        while (true) {

            // do nest step
            main.step(WorldMap.getMap());
            System.out.println();
            System.out.println("Step " + steps + ":");

            // display map
            WorldMap.displayMap(WorldMap.getMap());

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
    public void step(Entity[][] map) {
        // move rule
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                if (map[i][j] != null){
                    map[i][j].move(map, i, j);
                }
            }
        }
        // agent rule
        // ugly cast to Agent as agents in WorldMap is List<Entities>
        // perhaps change placeEntities function later so agents can be List<Agent> 
        for (Entity entity : WorldMap.getAgents()){
            Agent agent = (Agent) entity;
            agent.determineArrestProbability(map, agent.getXpos(), agent.getYpos());
        }
        // 
        for (Entity entity : WorldMap.getAgents()){
            Agent agent = (Agent) entity;
            agent.attemptRebellion(map, agent.getXpos(), agent.getYpos());
        }    
    }
}