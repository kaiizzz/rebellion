import java.util.ArrayList;

/**
 * Police
 * Author: Lucas Kenna
 * Student Number: 1170784
 * Date: 05/03/2024
 * Description: Police class that extends Entity class
 */

public class Police extends Entity {
    public static final char POLICE = 'P';

    public Police() {
        super(POLICE);
    }

    public void attemptArrest(Tile map[][]) {

        // get tiles in vision range that contain rebels
        ArrayList<Tile> rebelTiles = WorldMap.getTilesInNeighborhood(this.xpos, this.ypos, 'R');

        if (rebelTiles.size() == 0) {
            return;
        }

        // select a random rebel tile, move to it and jail rebel
        int random = (int) (Math.random() * rebelTiles.size());
        Tile chosenTile = rebelTiles.get(random);
        ((Agent) chosenTile.getActiveEntity()).update(AgentState.JAILED);
        map[this.xpos][this.ypos].removeEntity(this);
        chosenTile.occupy(this);
        setCoords(chosenTile.getX(), chosenTile.getY());
        

    }

}
