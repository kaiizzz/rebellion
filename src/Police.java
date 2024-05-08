import java.lang.reflect.Array;
import java.util.ArrayList;

public class Police extends Entity {
    public static final char POLICE = 'P';

    public Police() {
        super(POLICE);
        // System.out.println("Police created");
    }

    public void makeArrest(Entity map[][], int xpos, int ypos) {
        // Select a direction to move out of 8 possible directions
        int[] dx = { 0, 0, 1, -1, 1, -1, 1, -1 };
        int[] dy = { 1, -1, 0, 0, 1, -1, -1, 1 };
        int direction = (int) (Math.random() * 8);
        int newX = xpos + dx[direction];
        int newY = ypos + dy[direction];

        ArrayList<Integer[]> rebels = new ArrayList<Integer[]>();

        // check for rebels in the vicinity
        if (newX >= 0 && newX < map.length && newY >= 0 && newY < map[0].length && map[newX][newY] != null) {
            if (map[newX][newY].getSymbol() == Agent.REBEL) {
                rebels.add(new Integer[] { newX, newY });
            }
        } else {
            // check for rebels on the other side of the map
            if (newX < 0) {
                newX = Main.MAP_SIZE - 1;
            }
            if (newX >= Main.MAP_SIZE) {
                newX = 0;
            }
            if (newY < 0) {
                newY = Main.MAP_SIZE - 1;
            }
            if (newY >= Main.MAP_SIZE) {
                newY = 0;
            }
            if (map[newX][newY] != null && map[newX][newY].getSymbol() == Agent.REBEL) {
                rebels.add(new Integer[] { newX, newY });
            }
        }

        // select a random rebel from the list
        Integer[] rebel = null;
        if (rebels.size() > 0) {
            int randomRebel = (int) (Math.random() * rebels.size());
            rebel = rebels.get(randomRebel);
        }

        // move onto the rebel and set the rebel to jailed if the rebel is not null
        if (rebel != null) {
            Agent agent = (Agent) map[rebel[0]][rebel[1]];
            agent.update(AgentState.JAILED);
            // map[newX][newY] = " " + map[rebel[0]][rebel[1]] + map[newX][newY];
            map[newX][newY] = null;
        }
    }

}
