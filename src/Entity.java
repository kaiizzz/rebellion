import java.util.ArrayList;

/**
 * Entity
 * Author: Bill Zhu
 * Student Number: 115777
 * Date: 05/03/2024
 * Description: Entity class that represents an entity in the simulation
 * Improved by Lucas Kenna
 */

public class Entity {
    protected char symbol;
    protected int xpos;
    protected int ypos;

    public Entity(char symbol) {
        this.symbol = symbol;
    }

    /**
     * Move the entity to a new position
     * 
     * @param map
     * @param x
     * @param y
     */
    public void move(Tile[][] map) {
        ArrayList<Tile> emptyTiles = WorldMap.getTilesInNeighborhood(this.xpos, this.ypos, ' ');
        if (emptyTiles.size() == 0) {
            return;
        }

        // randomly select one empty tile and move to it
        int random = (int) (Math.random() * emptyTiles.size());
        Tile newTile = emptyTiles.get(random);
        map[this.xpos][this.ypos].removeEntity(this);
        newTile.occupy(this);
        setCoords(newTile.getX(), newTile.getY());
        
        
        


    }

    /* Helper functions */
    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public int getXpos() {
        return this.xpos;
    }

    public int getYpos() {
        return this.ypos;
    }

    public void setCoords(int x, int y) {
        this.xpos = x;
        this.ypos = y;
    }

}
