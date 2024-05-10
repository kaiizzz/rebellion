import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        // gets coordinates of all empty tiles in entire map (can probably create one function to do this for both Entity.move and SetUpMap)
        ArrayList<List<Integer>> emptyTiles = new ArrayList<List<Integer>>();
        for (int i = 0; i < mapSize; i++){
            for (int j = 0; j < mapSize; j++){
                if (map[i][j] == null) {
                    emptyTiles.add(Arrays.asList(i, j));
                }
            }
        }
        try {
            Entity entity;
            // places entities randomly in unoccupied
            while (!entities.isEmpty()) {
                entity = entities.get(0);
                int random = (int) (Math.random()*emptyTiles.size());
                List<Integer> tile = emptyTiles.get(random);
                map[tile.get(0)][tile.get(1)] = entity;
                emptyTiles.remove(random);
                entities.remove(entity);
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
                } else {
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
    

    // helper function to wrap coordinates that go out of map bounds to other side of map
    public static int wrapCoordinates(int pos){
        int result = pos%Main.MAP_SIZE;
        if(result < 0){
            result += Main.MAP_SIZE;
        }
        return result;
    }

}