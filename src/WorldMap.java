import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * WorldMap
 * Author: Lucas Kenna. Original By Bill Zhu
 * Student Number: 1170784
 * Date: 05/03/2024
 * Description: WorldMap class that represents the map in the simulation
 */

public class WorldMap {

    private static ArrayList<Entity> quietAgents; 
    private static ArrayList<Agent> jailedAgents;
    private static ArrayList<Agent> rebellingAgents;

    private static ArrayList<Entity> cops;

    private static Tile[][] map;



    @SuppressWarnings("unused")
    public WorldMap() {

        // check if density exceeds 100%
        if (Params.INITIAL_POLICE_DENSITY + Params.INITIAL_AGENT_DENSITY > 100) {
            System.out.println("Density exceeds 100%");
            System.exit(0);
        }

        // create map
        WorldMap.map = new Tile[Params.MAP_SIZE][Params.MAP_SIZE];
        for (int i = 0; i < Params.MAP_SIZE; i++) {
            for (int j = 0; j < Params.MAP_SIZE; j++) {
                map[i][j] = new Tile(i, j);
            }
        }
    }

    /**
     * Set up the map with the initial density of cops and agents
     * 
     */
    public void setUpMap() {
        quietAgents = new ArrayList<>();
        jailedAgents = new ArrayList<>();
        rebellingAgents = new ArrayList<>();
        cops = new ArrayList<>();

        // create cops
        int numberOfCops = (int) (Params.INITIAL_POLICE_DENSITY * 0.01 * Params.MAP_SIZE * Params.MAP_SIZE);
        // System.out.println("Number of cops: " + numberOfCops); // for debugging
        for (int i = 0; i < numberOfCops; i++) {
            cops.add(new Police());
        }

        // Create agents
        int numberOfAgents = (int) (Params.INITIAL_AGENT_DENSITY * 0.01 * Params.MAP_SIZE * Params.MAP_SIZE);
        // System.out.println("Number of agents: " + numberOfAgents); // for debugging
        for (int i = 0; i < numberOfAgents; i++) {
            quietAgents.add(new Agent());

        }
        placeEntities(cops, Params.INITIAL_POLICE_DENSITY);
        placeEntities(quietAgents, Params.INITIAL_AGENT_DENSITY);
    }

    /**
     * randomly distribute a list of entities across the map
     * 
     * @param entities
     * @param density
     */
    private void placeEntities(ArrayList<Entity> entities, double density) {
        // gets coordinates of all empty tiles in entire map (can probably create one
        // function to do this for both Entity.move and WorldMap)
        ArrayList<List<Integer>> emptyTiles = new ArrayList<List<Integer>>();
        for (int i = 0; i < Params.MAP_SIZE; i++) {
            for (int j = 0; j < Params.MAP_SIZE; j++) {
                if (map[i][j].getActiveEntity() == null) {
                    emptyTiles.add(Arrays.asList(i, j));
                }
            }
        }

        // copy entities to avoid concurrent modification
        List<Entity> remEntities = new ArrayList<>(entities);
        try {
            Entity entity;
            // places entities randomly into unoccupied tiles
            while (!remEntities.isEmpty()) {
                entity = remEntities.get(0);
                int random = (int) (Math.random() * emptyTiles.size());
                List<Integer> tile = emptyTiles.get(random);
                map[tile.get(0)][tile.get(1)].occupy(entity);
                entity.setCoords(tile.get(0), tile.get(1));
                emptyTiles.remove(random);
                remEntities.remove(entity);
            }
        } catch (Exception e) {
            System.out.println("Error placing entities");
        }

    }

    /**
     * Display the map
     * 
     */
    public void displayMap() {
        // display map
        for (int i = 0; i < Params.MAP_SIZE; i++) {
            for (int j = 0; j < Params.MAP_SIZE; j++) {
                Tile tile = map[i][j];
                if (!(tile.getActiveEntity() == null)) {
                    char symbol = tile.getActiveEntity().getSymbol();
                    String color = Main.ANSI_GREEN;
                    // colors police blue, rebels red and jailed agents purple
                    if (symbol == Agent.JAILED) {
                        color = Main.ANSI_PURPLE;
                    } else if (symbol == Police.POLICE) {
                        color = Main.ANSI_BLUE;
                    } else if (symbol == Agent.REBEL) {
                        color = Main.ANSI_RED;
                    }
                    // highlight the borders of tiles that are occupied and also have jailed agents purple
                    if (tile.getJailedEntities().size()>0) {
                        System.out.print(
                                Main.ANSI_PURPLE + "[" + color + symbol + Main.ANSI_PURPLE + "]" + Main.ANSI_RESET);
                    } else {
                        System.out.print(color + "[" + symbol + "]" + Main.ANSI_RESET);
                    }
                } else if (tile.getJailedEntities().size()>0) {
                    System.out.print(Main.ANSI_PURPLE + "[" + "J" + "]" + Main.ANSI_RESET);
                }

                else {
                    System.out.print("[ ]");
                }
            }
            System.out.println();
        }
    }

    /**
     * Get the tiles in the neighborhood of a given tile that contain a certain type
     * of entity or are empty or contain jailed agents
     * 
     * @param x
     * @param y
     * @param type
     * @return
     */
    static ArrayList<Tile> getTilesInNeighborhood(int x, int y, char type) {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = (int) -Params.VISION -1; i <= (int) Params.VISION +1; i++) {
            for (int j = (int)-Params.VISION -1; j <= (int) Params.VISION +1; j++) {
                if ((i * i) + (j * j) <= Params.VISION * Params.VISION) {
                    int nx = WorldMap.wrapCoordinates(x + i);
                    int ny = WorldMap.wrapCoordinates(y + j);
                    Tile tile = map[nx][ny];
                    if (tile.getActiveEntity() == null) {
                        if (type == ' ') {
                            tiles.add(map[nx][ny]);
                            continue;
                        }

                    } else if (tile.getActiveEntity().getSymbol() == type) {
                        tiles.add(tile);
                        continue;
                    }
                    if (type == 'J' && tile.getJailedEntities().size() > 0) {
                        tiles.add(tile);
                    }

                }
            }
        }
        return tiles;
    }

    public Tile[][] getMap() {
        return map;
    }

    // helper function to wrap coordinates that go out of map bounds to other side
    // of map
    public static int wrapCoordinates(int pos) {
        int result = pos % Params.MAP_SIZE;
        if (result < 0) {
            result += Params.MAP_SIZE;
        }
        return result;
    }

    /* helper functions */
    public static void addJailedAgent(Agent agent) {
        jailedAgents.add(agent);
        quietAgents.remove(agent);
    }

    public static List<Entity> getActiveAgents() {
        return quietAgents;
    }

    public static List<Entity> getPolice() {
        return cops;
    }

    public static ArrayList<Agent> getJailedAgents() {
        return jailedAgents;
    }

    public static ArrayList<Agent> getRebellingAgents() {
        return rebellingAgents;
    }

}