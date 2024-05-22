import java.util.ArrayList;

/**
 * Tile
 * Author: Lucas Kenna
 * Student Number: 1170784
 * Date: 05/03/2024
 * Description: Tile class that represents a tile in the simulation
 */

public class Tile {
    private Entity activeEntity;
    private ArrayList<Entity> jailedEntities;
    private int x;
    private int y;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.activeEntity = null;
        this.jailedEntities = new ArrayList<>();
    }

    // jails the active entity of tile
    public void jailEntity() {
        ((Agent) activeEntity).update(AgentState.JAILED);
        ((Agent) activeEntity).setJailTerm((int) (Math.random() * Main.MAX_JAIL_TERM));
        WorldMap.addJailedAgent((Agent) activeEntity);
        WorldMap.getRebellingAgents().remove((Agent) activeEntity);
        WorldMap.getActiveAgents().remove((Agent) activeEntity);
        this.jailedEntities.add(activeEntity);
        this.activeEntity = null;

    }

    /* helper functions */
    public void setActiveEntity(Entity entity) {
        this.activeEntity = entity;
    }

    public Entity getActiveEntity() {
        return this.activeEntity;
    }

    public boolean jailOccupied() {
        return this.jailedEntities.size() > 0;
    }

    public void freeJailedAgent(Entity agent) {
        this.jailedEntities.remove(agent);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public ArrayList<Entity> getJailedEntities() {
        return this.jailedEntities;
    }
}
