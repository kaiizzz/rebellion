public class Entity {
    private char symbol;
    public Entity(char symbol) {
        //System.out.println("Entity created");
        this.symbol = symbol;
    }
    
    public void move() {
        System.out.println("Moving");
    }

    public char getSymbol() {
        return symbol;
    }
}
