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
    public static final double GOVERNMET_LEGITIMACY = 0.5;

    // public static final int NUMBER_OF_AGENTS = 0;
    // public static final int NUMBER_OF_POLICE = 0;
    public static final double INITIAL_AGENT_DENSITY = 20;
    public static final double INITIAL_POLICE_DENSITY = 20;
    // private SetUpMap setUpMap = new SetUpMap(0.5, 0.5, 10, 10);
    private Entity[][] map = new Entity[MAP_SIZE][MAP_SIZE];

    public static void main(String[] args) {
        Main main = new Main();
        // System.out.println("Hello, World!");

        // insert
        SetUpMap setUpMap = new SetUpMap(INITIAL_AGENT_DENSITY, INITIAL_POLICE_DENSITY);
        setUpMap.setUpMap(MAP_SIZE);
        setUpMap.displayMap();
        while (true) {
            main.step(setUpMap.getMap());
            System.out.println();
            setUpMap.displayMap();
            try {
                Thread.sleep(TICK);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public void step(Entity[][] map) {
        // move agents
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                if (map[i][j] != null
                        && (map[i][j].getSymbol() == Agent.AGENT || map[i][j].getSymbol() == Agent.REBEL)) {
                    Agent agent = (Agent) map[i][j];
                    agent.move(map, i, j);
                }
            }
        }
    }
}