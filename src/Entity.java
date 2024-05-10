import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Entity {
    protected char symbol;

    public Entity(char symbol) {
        // System.out.println("Entity created");
        this.symbol = symbol;
    }

    /**
     * Move the entity to a new position
     * 
     * @param map
     * @param x
     * @param y
     */
    public void move(Entity[][] map, int x, int y) {
        ArrayList<List<Integer>> emptyTiles = new ArrayList<List<Integer>>();
        // find all empty tiles within vision range
        for (int i = x - Main.VISION; i<= x + Main.VISION; i++){
            for (int j = y - Main.VISION; j <= y + Main.VISION; j++){
                if(i == x && j == y){
                    continue;
                }
                int nx = wrapCoordinates(i);
                int ny = wrapCoordinates(j);
                if (map[nx][ny] == null) {
                    emptyTiles.add(Arrays.asList(nx, ny));
                }
            }
        }
        // randomly select one empty tile to move to
        int tile = (int) (Math.random()*emptyTiles.size());
        List<Integer> newTile = emptyTiles.get(tile);
        map[newTile.get(0)][newTile.get(1)] = map[x][y];
        map[x][y] = null;
        
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    // helper function to wrap coordinates that go out of map bounds to other side of map
    int wrapCoordinates(int pos){
        int result = pos%Main.MAP_SIZE;
        if(result < 0){
            result += Main.MAP_SIZE;
        }
        return result;
    }

}
