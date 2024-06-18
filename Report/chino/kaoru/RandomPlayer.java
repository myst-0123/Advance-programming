package Report.chino.kaoru;

public class RandomPlayer extends Player {
    public RandomPlayer() {
        super("Random");
    }

    protected Move search(State state) {
        var moves = state.getMoves();
        int index = new java.util.Random().nextInt(moves.size());
        return moves.get(index);
    }
}
