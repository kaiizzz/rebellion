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
    public static final double GOVERNMET_LEGITIMACY = 0.25;
    public static final double INITIAL_AGENT_DENSITY = 20;
    public static final double INITIAL_POLICE_DENSITY = 15;
    
    public static final int[][] DIRECTION_TUPLES = { {1,0}, {-1,0}, {0,1}, {0,-1}, {1,1}, {-1,-1}, {1,-1}, {-1,1} };

    private Entity[][] map = new Entity[MAP_SIZE][MAP_SIZE];

    public static void main(String[] args) {
        Main main = new Main();

        // insert and display initial map
        SetUpMap setUpMap = new SetUpMap(INITIAL_POLICE_DENSITY, INITIAL_AGENT_DENSITY);
        setUpMap.setUpMap(MAP_SIZE);
        System.out.println("Initial map:");
        setUpMap.displayMap(setUpMap.getMap());

        // main loop
        int steps = 1;
        while (true) {

            // do nest step
            main.step(setUpMap.getMap());
            System.out.println();
            System.out.println("Step " + steps + ":");

            // display map
            setUpMap.displayMap(setUpMap.getMap());

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
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                if (map[i][j] != null
                        && (map[i][j].getSymbol() == Agent.AGENT || map[i][j].getSymbol() == Agent.REBEL)) {
                    Agent agent = (Agent) map[i][j];
                    agent.move(map, i, j);
                }
                if (map[i][j] != null && map[i][j].getSymbol() == Police.POLICE) {
                    Police police = (Police) map[i][j];
                    police.move(map, i, j);
                }
            }
        }
    }
}