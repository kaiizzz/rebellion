/**
 * Agent
 * Author: Bill Zhu
 * Student Number: 115777
 * Date: 05/03/2024
 * Description: Agent class that extends Entity class
 * Edited and improved by Lucas Kenna
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
    private double baseGrievance;
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

        if (Main.extension){
            int jailedCount = 0;
            for (Tile tile : WorldMap.getTilesInNeighborhood(xpos, ypos, 'J')){
                for (Entity entity : tile.getJailedEntities())
                // in case of agents who have not been able to move out of jail due to lack of
                    if(((Agent) entity).jailTerm > 0) {
                        jailedCount += 1;
                    }
            };
            //System.out.println(jailedCount);
            grievance = percievedHardship * (1 - (Main.GOVERNMET_LEGITIMACY*(1+(Main.EXTENTSION_SCALING*jailedCount))));
        }
   
        if ((grievance - (this.riskAversion * this.arrestProbability)) > REBEL_THRESHOLD) {
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
     * Determine the probability of arrest
     * 
     * @param map
     * @param xpos
     * @param ypos
     * @return probability
     */
    public void determineArrestProbability(Tile[][] map, int xpos, int ypos) {
        // look at all adgacent tiles to see police
        int policeCount = WorldMap.getTilesInNeighborhood(xpos, ypos, 'P').size();
        int agentCount = (1 + WorldMap.getTilesInNeighborhood(xpos, ypos, 'R').size());
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
