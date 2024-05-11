import java.util.ArrayList;

public class Police extends Entity {
    public static final char POLICE = 'P';

    public Police() {
        super(POLICE);
        // System.out.println("Police created");
    }

    public void attemptArrest(Tile map[][]) {

        // find tiles in vision range that contain rebels
        ArrayList<Tile> rebelTiles = new ArrayList<>();
        for (int i = xpos - Main.VISION; i<= xpos + Main.VISION; i++){
            for (int j = ypos - Main.VISION; j <= ypos + Main.VISION; j++){
                if(i == xpos && j == ypos){
                    continue;
                }
                int nx = WorldMap.wrapCoordinates(i);
                int ny = WorldMap.wrapCoordinates(j);
                Entity potentialEntity = map[nx][ny].getActiveEntity();
                if (potentialEntity != null && potentialEntity.getSymbol() == Agent.REBEL) {
                    rebelTiles.add(map[nx][ny]);
                }
            }
        }  
        
        if (rebelTiles.size() == 0) {
            return;
        }
        
        // select a random rebel tile
        int random = (int) (Math.random() * rebelTiles.size());
        Tile chosenTile = rebelTiles.get(random);

        // jail rebel, move onto tile
        chosenTile.jailEntity();
        chosenTile.setActiveEntity(this);
        map[xpos][ypos].setActiveEntity(null);
        setCoords(chosenTile.getX(), chosenTile.getY());
         
    }

}
