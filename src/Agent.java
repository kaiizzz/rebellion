/**
 * Agent
 * Author: Bill Zhu
 * Student Number: 115777
 * Date: 05/03/2024
 * Description: Agent class that extends Entity class
 */

public class Agent extends Entity {

    // constant k
    public static final double K = 2.3;

    // Initialize the symbols for the agent
    public static final char AGENT = 'A';
    public static final char REBEL = 'R';
    public static final char JAILED = 'J';

    // constant for the threshold of rebellion
    public static final double REBEL_THRESHOLD = 0.1;

    // Initialize the state of the agent
    private AgentState state = AgentState.NORMAL;

    // Initialize the variables for the agent
    private double percievedHardship;
    private double greivance;
    private double riskAversion;

    public Agent() {
        super(AGENT);

        // percieved hardspip dowuble from 0-1
        this.percievedHardship = Math.random();

        // risk aversion double from 0-1
        this.riskAversion = Math.random();

        // greivance calculation
        this.greivance = this.percievedHardship * (1 - Main.GOVERNMET_LEGITIMACY);

    }

    public void update(AgentState state) {
        this.state = state;
        if (state == AgentState.REBEL) {
            this.setSymbol(REBEL);
        } else if (state == AgentState.JAILED) {
            this.setSymbol(JAILED);
        }
    }

    /**
     * Determine if the agent will rebel
     * 
     * @param map
     * @param x
     * @param y
     * @return
     */
    public Boolean rebel(Entity[][] map, int x, int y) {
        // System.out.println((this.greivance - this.riskAversion *
        // determineArrestProbability(map, x, y)) > REBEL_THRESHOLD); // uncomment for
        // debugging

        if ((this.greivance - this.riskAversion * determineArrestProbability(map, x, y)) > REBEL_THRESHOLD) {
            this.state = AgentState.REBEL;
            // this.symbol = REBEL;
            // System.out.println(super.getSymbol());
            return true;
        } else {
            this.state = AgentState.NORMAL;
            // this.symbol = AGENT;
            // System.out.println(super.getSymbol());
            return false;
        }
    }

    /**
     * Count the number of agents of type in the neighbourhood
     * 
     * @param map
     * @param xpos
     * @param ypos
     * @param agentType
     * @return count
     */
    public int countAgentsInNeighbourhood(Entity[][] map, int xpos, int ypos, char agentType) {
        int count = 0;
        for (int i=0; i<Main.VISION; i++){
            // checks each tile in vision, wrapping around x and y is map boundary is reached
            for (int[] dir : Main.DIRECTION_TUPLES) {
                int nx = wrapCoordinates(xpos, dir[0]*i);
                int ny = wrapCoordinates(ypos, dir[1]*i);
                if (map[nx][ny] != null && map[nx][ny].getSymbol() == agentType) {
                    count++;
                }
            }
        }  

        return count;
    }

    /**
     * Determine the probability of arrest
     * 
     * @param map
     * @param xpos
     * @param ypos
     * @return probability
     */
    public double determineArrestProbability(Entity[][] map, int xpos, int ypos) {
        // look at all adgacent tiles to see police
        int policeCount = countAgentsInNeighbourhood(map, xpos, ypos, Police.POLICE);
        return (1 - Math.exp(-K * Math.floor(policeCount / (1 + countAgentsInNeighbourhood(map, xpos, ypos, Agent.REBEL)))));
    }


    int wrapCoordinates(int pos, int offset){
        int result = (pos + offset)%Main.MAP_SIZE;
        if(result < 0){
            result += Main.MAP_SIZE;
        }
        return result;
    }

}
