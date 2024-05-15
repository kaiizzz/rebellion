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
    private double grievance;
    private double riskAversion;
    private double arrestProbability;
    private int jailTerm;

    public Agent() {
        super(AGENT);

        // percieved hardspip dowuble from 0-1
        this.percievedHardship = Math.random();

        // risk aversion double from 0-1
        this.riskAversion = Math.random();

        // greivance calculation
        this.grievance = this.percievedHardship * (1 - Main.GOVERNMET_LEGITIMACY);

        //this.arrestProbability = 1;

    }

    public void update(AgentState state) {
        this.state = state;
        if (state == AgentState.REBEL) {
            this.setSymbol(REBEL);
        } else if (state == AgentState.NORMAL){
            this.setSymbol(AGENT);
        } else if (state == AgentState.JAILED) {
            this.setSymbol(JAILED);
        }
    }

    public  String toString(){
        
        return ("percived hardship: " + percievedHardship + " " + "risk aversion: " + riskAversion);
        
    }

    /**
     * Determine if the agent will rebel
     * 
     * @param map
     * @param x
     * @param y
     * @return
     */
    public void checkRebellion(Tile[][] map, int x, int y) {
        //System.out.println((this.grievance - this.riskAversion * arrestProbability) ); // uncomment for
        // debugging

        if ((this.grievance - (this.riskAversion * this.arrestProbability)) > REBEL_THRESHOLD) {
            if (state == AgentState.NORMAL){
                WorldMap.getRebellingAgents().add(this);  
            }
            update(AgentState.REBEL);
            
            

        } else {
            if (state == AgentState.REBEL){
                WorldMap.getRebellingAgents().remove(this);
            }
            update(AgentState.NORMAL);
            
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
    public int countAgentsInNeighbourhood(Tile[][] map, int xpos, int ypos, char agentType) {
        int count = 0;
        // checks each tile in vision, wrapping around x and y is map boundary is reached
        //System.out.println("position: (" + xpos + ", " + ypos + ")");
        for (int i = xpos - Main.VISION; i<= xpos + Main.VISION; i++){
            for (int j = ypos - Main.VISION; j <= ypos + Main.VISION; j++){
                int nx = WorldMap.wrapCoordinates(i);
                int ny = WorldMap.wrapCoordinates(j);
                if (map[nx][ny].getActiveEntity() != null && map[nx][ny].getActiveEntity().getSymbol() == agentType) {
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
    public void determineArrestProbability(Tile[][] map, int xpos, int ypos) {
        // look at all adgacent tiles to see police
        int policeCount = countAgentsInNeighbourhood(map, xpos, ypos, Police.POLICE);
        int agentCount = 1 + (1 + countAgentsInNeighbourhood(map, xpos, ypos, Agent.REBEL));
        this.arrestProbability = (1 - Math.exp(-K * Math.floor(policeCount / agentCount)));
    }

    public void setJailTerm(int term) {
        this.jailTerm = term;
    }

    public void decrementJailTerm() {
        this.jailTerm -= 1;
    }

    public boolean attemptFree() {
        if (this.jailTerm > 0){
            return false;
        }
        update(AgentState.NORMAL);
        return true;
    }

}
