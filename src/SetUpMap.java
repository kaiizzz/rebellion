import java.lang.reflect.Array;
import java.util.ArrayList;

public class SetUpMap {
    private double initialCopDensity;
    private double initialAgentDensity;

    private int numberOfAgents;
    private ArrayList<Entity> agents = new ArrayList<Entity>();

    private int numberOfCops;
    private ArrayList<Entity> cops = new ArrayList<Entity>();

    private Entity[][] map;

    public SetUpMap(double d, double e) {
        this.initialCopDensity = d;
        this.initialAgentDensity = e;
        if (this.initialCopDensity + this.initialAgentDensity > 100) {
            System.out.println("Density exceeds 100%");
            System.exit(0);
        }
        this.map = new Entity[Main.MAP_SIZE][Main.MAP_SIZE];
    }

    public void setUpMap(int mapSize) {
        // create cops
        numberOfCops = (int) Math.ceil(initialCopDensity * 0.01 * mapSize * mapSize);
        System.out.println("Number of cops: " + numberOfCops);
        for (int i = 0; i < numberOfCops; i++) {
            cops.add(new Police());
        }

        // Create agents
        numberOfAgents = (int) Math.ceil(initialAgentDensity * 0.01 * mapSize * mapSize);
        System.out.println("Number of agents: " + numberOfAgents);
        for (int i = 0; i < numberOfAgents; i++) {
            agents.add(new Agent());
        }

        placeEntities(cops, mapSize, map, initialCopDensity);
        placeEntities(agents, mapSize, map, initialAgentDensity);
    }

    private void placeEntities(ArrayList<Entity> entities, int mapSize, Entity[][] map, double density) {
        int entitiesCount = entities.size();
        for (Entity entity : entities) {
            try {
                while (entitiesCount > 0) {
                    for (int i = 0; i < mapSize; i++) {
                        for (int j = 0; j < mapSize; j++) {
                            if (map[i][j] == null && entitiesCount > 0) {
                                // 5-% chance of placing entity
                                if (Math.random() > 0.995) {
                                    map[i][j] = entity;
                                    entitiesCount--;
                                }
                            } else {
                                continue;
                            }
                        }
                        // System.out.println();
                    }
                }
            } catch (Exception e) {
                System.out.println("Error placing entity");
            }
        }
    }

    public void displayMap(Entity[][] map) {
        // display map
        for (int i = 0; i < Main.MAP_SIZE; i++) {
            for (int j = 0; j < Main.MAP_SIZE; j++) {
                if (map[i][j] == null) {
                    System.out.print("[ ]");
                } else {
                    // if (map[i][j].getSymbol() == Agent.AGENT) {
                    // System.out.print("[" + Main.ANSI_GREEN + Agent.AGENT + Main.ANSI_RESET +
                    // "]");
                    // } else if (map[i][j].getSymbol() == Police.POLICE) {
                    // System.out.print("[" + Main.ANSI_BLUE + Police.POLICE + Main.ANSI_RESET +
                    // "]");
                    // } else if (map[i][j].getSymbol() == Agent.REBEL) {
                    // System.out.print("[" + Main.ANSI_RED + Agent.REBEL + Main.ANSI_RESET + "]");
                    // } else if (map[i][j].getSymbol() == Agent.JAILED) {
                    // System.out.print("[" + Main.ANSI_PURPLE + Agent.JAILED + Main.ANSI_RESET +
                    // "]");
                    // }

                    if (map[i][j].getSymbol() == Agent.AGENT || map[i][j].getSymbol() == Agent.REBEL) {
                        Agent agent = (Agent) map[i][j];
                        if (agent.rebel(this.map, i, j)) {
                            System.out.print("[" + Main.ANSI_RED + Agent.REBEL + Main.ANSI_RESET + "]");
                        } else {
                            System.out.print("[" + Main.ANSI_GREEN + Agent.AGENT + Main.ANSI_RESET + "]");
                        }

                    } else if (map[i][j].getSymbol() == Agent.JAILED) {
                        System.out.print("[" + Main.ANSI_RED + Agent.JAILED + Main.ANSI_RESET + "]");
                    } else if (map[i][j].getSymbol() == Police.POLICE) {
                        System.out.print("[" + Main.ANSI_BLUE + Police.POLICE + Main.ANSI_RESET + "]");
                    }
                }
            }
            System.out.println();
        }
    }

    public Entity[][] getMap() {
        return this.map;
    }

    public void set(int i, int j, Entity entity) {
        this.map[i][j] = entity;
    }

    public void set2(int i, int j, char symbol) {
        this.map[i][j].setSymbol(symbol);
    }

}