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
    public void move(Tile[][] map, int x, int y) {
        ArrayList<Tile> emptyTiles = WorldMap.getTilesInNeighborhood(x, y, ' ');
        if (emptyTiles.size() == 0) {
            return;
        }

        // randomly select one empty tile and move to it
        int random = (int) (Math.random() * emptyTiles.size());
        Tile newTile = emptyTiles.get(random);
        newTile.setActiveEntity(this);
        setCoords(newTile.getX(), newTile.getY());

        // if moving entity is in jail, remove from old tile's jail
        if (map[x][y].getJailedEntities().contains(this)) {
            map[x][y].freeJailedAgent(this);
            return;
        }

        // if moving entity was active, instead remove it from old tile
        map[x][y].setActiveEntity(null);
        if (this instanceof Police) {
        }

    }

    /* Helper functions */
    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public int getXpos() {
        return xpos;
    }

    public int getYpos() {
        return ypos;
    }

    public void setCoords(int x, int y) {
        this.xpos = x;
        this.ypos = y;
    }

}
