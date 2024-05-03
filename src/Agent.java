public class Agent extends Entity{
    public static final char AGENT = 'A';
    public Agent() {
        super(AGENT);
        //System.out.println("Agent created");
    }

    public void move(Entity[][] map, int x, int y) {
        //Select a direction to move out of 8 possible directions
        int[] dx = {0, 0, 1, -1, 1, -1, 1, -1};
        int[] dy = {1, -1, 0, 0, 1, -1, -1, 1};
        int direction = (int) (Math.random() * 8);
        int newX = x + dx[direction];
        int newY = y + dy[direction];

        //Check if the new position can be moved onto
        if (newX >= 0 && newX < map.length && newY >= 0 && newY < map[0].length && map[newX][newY] == null) {
            map[newX][newY] = map[x][y];
            map[x][y] = null;
        }
    }
}
