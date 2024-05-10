import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Todo: make this class a proper singleton
public class WorldMap {
    private double initialCopDensity;
    private double initialAgentDensity;

    private int numberOfAgents;
    private static ArrayList<Entity> agents = new ArrayList<Entity>();

    private int numberOfCops;
    private static ArrayList<Entity> cops = new ArrayList<Entity>();

    private Entity[][] map;

    public WorldMap(double d, double e) {
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
        // gets coordinates of all empty tiles in entire map (can probably create one function to do this for both Entity.move and WorldMap)
        ArrayList<List<Integer>> emptyTiles = new ArrayList<List<Integer>>();
        for (int i = 0; i < mapSize; i++){
            for (int j = 0; j < mapSize; j++){
                if (map[i][j] == null) {
                    emptyTiles.add(Arrays.asList(i, j));
                }
            }
        }
        List<Entity> remEntities = new ArrayList<>(entities);
        try {
            Entity entity;
            // places entities randomly in unoccupied
            while (!remEntities.isEmpty()) {
                entity = remEntities.get(0);
                int random = (int) (Math.random()*emptyTiles.size());
                List<Integer> tile = emptyTiles.get(random);
                map[tile.get(0)][tile.get(1)] = entity;
                emptyTiles.remove(random);
                remEntities.remove(entity);
            }
        } catch (Exception e) {
            System.out.println("Error placing entities");
        }
        
    }

    public void displayMap(Entity[][] map) {
        // display map
        for (int i = 0; i < Main.MAP_SIZE; i++) {
            for (int j = 0; j < Main.MAP_SIZE; j++) {
                if (map[i][j] == null) {
                    System.out.print("[ ]");
                } 
                else {
                    char symbol = map[i][j].getSymbol();
                    String color = Main.ANSI_GREEN;
                    if (symbol == Agent.JAILED) {
                        color = Main.ANSI_PURPLE;
                    } else if (symbol == Police.POLICE) {
                        color = Main.ANSI_BLUE;
                    } else if (symbol == Agent.REBEL){
                        color = Main.ANSI_RED;
                    }
                    System.out.print("[" + color + symbol + Main.ANSI_RESET + "]");
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
    

    // helper function to wrap coordinates that go out of map bounds to other side of map
    public static int wrapCoordinates(int pos){
        int result = pos%Main.MAP_SIZE;
        if(result < 0){
            result += Main.MAP_SIZE;
        }
        return result;
    }

    public static List<Entity> getAgents(){
        return agents;
    }

    public static List<Entity> getPolice(){
        return cops;
    }

}