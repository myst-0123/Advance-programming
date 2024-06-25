package myplayer;

import java.util.Random;
import ap24.Board;
import ap24.Color;
import ap24.Move;
import ap24.Player;

public class RandomPlayer extends Player {
    Random rand = new Random();

    public RandomPlayer(Color color) {
        super("R", color);
    }

    public Move think(Board board) {
        var moves = board.findLegalMoves(getColor());
        var i = this.rand.nextInt(moves.size());
        return moves.get(i);
    }
}