import java.util.ArrayList;

public class Entity {
    protected char symbol;
    protected int xpos;
    protected int ypos;

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
    public void move(Tile[][] map, int x, int y) {
        ArrayList<Tile> emptyTiles = new ArrayList<>();
        // find all empty tiles within vision range
        for (int i = x - Main.VISION; i<= x + Main.VISION; i++){
            for (int j = y - Main.VISION; j <= y + Main.VISION; j++){
                if(i == x && j == y){
                    continue;
                }
                int wrappedX = WorldMap.wrapCoordinates(i);
                int wrappedY = WorldMap.wrapCoordinates(j);
                if (map[wrappedX][wrappedY].getActiveEntity() == null) {
                    emptyTiles.add(map[wrappedX][wrappedY]);
                }
            }
        }
        
        if(emptyTiles.size() == 0){
            return;
        }

        // randomly select one empty tile and move to it
        int random = (int) (Math.random()*emptyTiles.size());
        Tile newTile = emptyTiles.get(random);
        newTile.setActiveEntity(this);
        setCoords(newTile.getX(), newTile.getY());

        // if moving entity is in jail, remove from old tile's jail
        if (map[x][y].getJailedEntities().contains(this)){
            map[x][y].freeJailedAgent(this);
            return;
        }
        
        // if moving entity was active, instead remove it from old tile
        map[x][y].setActiveEntity(null);
        if (this instanceof Police){
        }
        
    }

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

    public void setCoords (int x, int y){
        this.xpos = x;
        this.ypos = y;
    }

}
