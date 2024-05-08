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
        // Select a direction to move out of 8 possible directions
        int[] dx = { 0, 0, 1, -1, 1, -1, 1, -1 };
        int[] dy = { 1, -1, 0, 0, 1, -1, -1, 1 };
        int direction = (int) (Math.random() * 8);
        int newX = x + dx[direction];
        int newY = y + dy[direction];

        // Check if the new position can be moved onto
        if (newX >= 0 && newX < map.length && newY >= 0 && newY < map[0].length && map[newX][newY] == null) {
            // check is there is another entity in that space
            map[newX][newY] = map[x][y];
            map[x][y] = null;
        } else {
            // If the new position is out of bounds, move to the opposite side of the map
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
            if (map[newX][newY] == null) {
                map[newX][newY] = map[x][y];
                map[x][y] = null;
            }
        }
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
}
