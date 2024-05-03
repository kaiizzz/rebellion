public class SetUpMap {
    private float initialCopDensity;
    private float initialAgentDensity;
    private int numberOfAgents;
    private int numberOfCops;

    public SetUpMap(float initialCopDensity, float initialAgentDensity, int numberOfAgents, int numberOfCops) {
        this.initialCopDensity = initialCopDensity;
        this.initialAgentDensity = initialAgentDensity;
        this.numberOfAgents = numberOfAgents;
        this.numberOfCops = numberOfCops;
    }

    public Entity[][] setUpMap(int mapSize) {
        Entity[][] map = new Entity[mapSize][mapSize];
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                // make map
            }
        }
        return map;
    }
}