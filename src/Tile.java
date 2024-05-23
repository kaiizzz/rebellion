import java.util.ArrayList;

/**
 * Tile
 * Author: Lucas Kenna
 * Student Number: 1170784
 * Date: 05/03/2024
 * Description: Tile class that represents a tile in the simulation
 */

public class Tile {
    private ArrayList<Entity> entities;
    private int x;
    private int y;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.entities = new ArrayList<>();
    }

    public void occupy(Entity entity){
        entities.add(entity);
    }

    public Entity getActiveEntity() {
        for (Entity entity : entities){
            if (entity.getSymbol() != 'J'){
                return entity;
            }
        }
        return null;
    }

    public void removeEntity(Entity entity){
        entities.remove(entity);
    }

    public ArrayList<Entity> getJailedEntities() {
        ArrayList<Entity> jailed = new ArrayList<>();
        for (Entity entity : entities){
            if (entity.getSymbol() == 'J'){
                jailed.add(entity);
            }
        }
        return jailed;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
