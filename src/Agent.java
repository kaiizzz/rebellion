/**
 * Agent
 * Author: Bill Zhu
 * Student Number: 115777
 * Date: 05/03/2024
 * Description: Agent class that extends Entity class
 */

public class Agent extends Entity {

    public static final double K = 2.3;
    // Initialize the symbols for the agent
    public static final char AGENT = 'A';
    public static final char REBEL = 'R';
    public static final char JAILED = 'J';

    public static final double REBEL_THRESHOLD = 0.1;

    // Initialize the state of the agent
    private AgentState state = AgentState.NORMAL;

    private double percievedHardship;
    private double greivance;
    private double riskAversion;
    private double estimatedArrestProbability;

    public Agent() {
        super(AGENT);
        // percieved hardspip dowuble from 0-1
        this.percievedHardship = Math.random();
        // System.out.println("Percieved Hardship: " + this.percievedHardship);
        // risk aversion double from 0-1
        this.riskAversion = Math.random();
        // System.out.println("Risk Aversion: " + this.riskAversion);
        // greivance calculation
        this.greivance = this.percievedHardship * (1 - Main.GOVERNMET_LEGITIMACY);

        // System.out.println("Agent created");
    }

    public void move(Entity[][] map, int x, int y) {
        // Select a direction to move out of 8 possible directions
        int[] dx = { 0, 0, 1, -1, 1, -1, 1, -1 };
        int[] dy = { 1, -1, 0, 0, 1, -1, -1, 1 };
        int direction = (int) (Math.random() * 8);
        int newX = x + dx[direction];
        int newY = y + dy[direction];
        //rebel(map, x, y);
        // Check if the new position can be moved onto
        if (newX >= 0 && newX < map.length && newY >= 0 && newY < map[0].length && map[newX][newY] == null) {
            map[newX][newY] = map[x][y];
            map[x][y] = null;
        }

    }

    public void update(AgentState state) {
        this.state = state;
        if (state == AgentState.REBEL) {
            this.setSymbol(REBEL);
        } else if (state == AgentState.JAILED) {
            this.setSymbol(JAILED);
        }
    }

    public Boolean rebel(Entity[][] map, int x, int y) {
        // System.out.println((this.greivance - this.riskAversion *
        //         determineArrestProbability(map, x, y)) > REBEL_THRESHOLD);
       // System.out.println(super.getSymbol());
        if ((this.greivance - this.riskAversion * determineArrestProbability(map, x, y)) > REBEL_THRESHOLD) {
            this.state = AgentState.REBEL;
            //this.symbol = REBEL;
            //System.out.println(super.getSymbol());
            return true;
        } else {
            this.state = AgentState.NORMAL;
            //this.symbol = AGENT;
            //System.out.println(super.getSymbol());
            return false;
        }   
    }

    public int countAgentsInNeighbourhood(Entity[][] map, int xpos, int ypos) {
        int count = 0;
        if (xpos + 1 > Main.MAP_SIZE - 1) {
            if (map[0][ypos] != null && map[0][ypos].getSymbol() == Agent.AGENT) {
                count++;
            }
        } else if (xpos - 1 < 0) {
            if (map[Main.MAP_SIZE - 1][ypos] != null && map[Main.MAP_SIZE - 1][ypos].getSymbol() == Agent.AGENT) {
                count++;
            }
        } else if (ypos + 1 > Main.MAP_SIZE - 1) {
            if (map[xpos][0] != null && map[xpos][0].getSymbol() == Agent.AGENT) {
                count++;
            }
        } else if (ypos - 1 < 0) {
            if (map[xpos][Main.MAP_SIZE - 1] != null && map[xpos][Main.MAP_SIZE - 1].getSymbol() == Agent.AGENT) {
                count++;
            }
        } else {
            int[] dx = { 1, -1, 0, 0, 1, -1, 1, -1 };
            int[] dy = { 0, 0, 1, -1, 1, -1, -1, 1 };

            for (int i = 0; i < dx.length; i++) {
                int nx = xpos + dx[i];
                int ny = ypos + dy[i];
                if (map[nx][ny] != null && map[nx][ny].getSymbol() == Agent.AGENT) {
                    count++;
                }
            }
        }

        return count;
    }

    public double determineArrestProbability(Entity[][] map, int xpos, int ypos) {
        // look at all adgacent tiles to see police
        int policeCount = 0;
        if (xpos + 1 > Main.MAP_SIZE - 1) {
            if (map[0][ypos] != null && map[0][ypos].getSymbol() == Police.POLICE) {
                policeCount++;
            }
        } else if (xpos - 1 < 0) {
            if (map[Main.MAP_SIZE - 1][ypos] != null && map[Main.MAP_SIZE - 1][ypos].getSymbol() == Police.POLICE) {
                policeCount++;
            }
        } else if (ypos + 1 > Main.MAP_SIZE - 1) {
            if (map[xpos][0] != null && map[xpos][0].getSymbol() == Police.POLICE) {
                policeCount++;
            }
        } else if (ypos - 1 < 0) {
            if (map[xpos][Main.MAP_SIZE - 1] != null && map[xpos][Main.MAP_SIZE - 1].getSymbol() == Police.POLICE) {
                policeCount++;
            }
        } else {
            int[] dx = { 1, -1, 0, 0, 1, -1, 1, -1 };
            int[] dy = { 0, 0, 1, -1, 1, -1, -1, 1 };

            for (int i = 0; i < dx.length; i++) {
                int nx = xpos + dx[i];
                int ny = ypos + dy[i];
                if (map[nx][ny] != null && map[nx][ny].getSymbol() == Police.POLICE) {
                    policeCount++;
                }
            }
        }

        return (1 - Math.exp(-K * Math.floor(policeCount / (1 + countAgentsInNeighbourhood(map, xpos, ypos)))));
    }
}
