package ap24;

import java.util.List;

public interface Board {
    int SIZE = 6;
    int LENGTH = SIZE * SIZE;

    Color get(int k);
    Move getMove();
    Color getTrun();
    int count(Color color);
    boolean isEnd();
    Color winner();
    void foul(Color color);
    int score();
    List<Move> findLegalMoves(Color color);
    Board placed(Move move);
    Board flipped();
    Board clone();
}