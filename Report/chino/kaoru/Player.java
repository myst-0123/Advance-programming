package Report.chino.kaoru;

public class Player {
    String name;
    public Color color;

    public Player(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public Move think(State state) {
        var move = search(state);
        move.color = this.color;
        return move;
    }

    protected Move search(State state) {
        return null;
    }
}
