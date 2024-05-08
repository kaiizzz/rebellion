public class Entity {
    protected char symbol;

    public Entity(char symbol) {
        // System.out.println("Entity created");
        this.symbol = symbol;
    }

    public void move() {
        System.out.println("Moving");
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
}
