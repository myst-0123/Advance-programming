package Report.chino.kaoru;

import static Report.chino.kaoru.Color.*;

import java.util.*;
import java.util.stream.*;

public class State implements Cloneable {
    public final static int SIZE = 3;
    public final static int LENGTH = SIZE * SIZE;

    public Color board[] = new Color[LENGTH];
    public Color color = BLACK;
    Move move;

    public int a1, a2, a3, b1, b2, b3;

    public State() {
        for (var i = 0; i < LENGTH; i++) {
            this.board[i] = NONE;
        }
    }

    public State clone() { // 次の盤面用にコピーStateを返す
        var other = new State();
        other.board = Arrays.copyOf(this.board, this.board.length);
        other.color = this.color;
        other.move = this.move;
        other.count();
        return other;
    }

    public String toString() {
        var buf = new StringBuilder();

        for (int col = 0; col < SIZE; col++) {
            var a = this.board[SIZE * col];
            var b = this.board[SIZE * col + 1];
            var c = this.board[SIZE * col + 2];
            if (col > 0)
                buf.append(String.format("-+-+-\n", a, b, c));
            buf.append(String.format("%s|%s|%s\n", a, b, c));
        }

        buf.append(String.format("o: a3=%d,a2=%d,a1=%d\n", this.a3, this.a2, this.a1));
        buf.append(String.format("x: b3=%d,b2=%d,b1=%d", this.b3, this.b2, this.b1));

        return buf.toString();
    }

    public boolean isGoal() {
        if (winner() != NONE)
            return true;

        for (int i = 0; i < LENGTH; i++)
            if (this.board[i] == NONE)
                return false;

        return true;
    }

    public List<Move> getMoves() {
        return IntStream.range(0, LENGTH).mapToObj(i -> new Move(i, this.color))
                .filter(m -> this.board[m.index] == NONE).toList();
    }

    public State perform(Move move) {
        var next = this.clone();
        next.board[move.index] = move.color;
        next.color = move.color.flipped();
        next.move = move;
        next.count();
        return next;
    }

    public Color winner() {
        if (this.a3 > 0)
            return BLACK;
        if (this.b3 > 0)
            return WHITE;
        return NONE;
    }

    public void count() { // 同色の石のみからなるラインを算出
        this.a1 = this.a2 = this.a3 = 0;
        this.b1 = this.b2 = this.b3 = 0;
        count(0, 1, 2);
        count(3, 4, 5);
        count(6, 7, 8);
        count(0, 3, 6);
        count(1, 4, 7);
        count(2, 5, 8);
        count(0, 4, 8);
        count(2, 4, 6);
    }

    public void count(int p, int q, int r) {
        var x = this.board[p].getSign();
        var y = this.board[q].getSign();
        var z = this.board[r].getSign();
        var sum = x + y + z;
        if (sum == 3)
            this.a3 += 1;
        if (sum == -3)
            this.b3 += 1;
        if (x * y * z == 0) {
            if (sum == 2)
                this.a2 += 1;
            if (sum == 1)
                this.a1 += 1;
            if (sum == -2)
                this.b2 += 1;
            if (sum == -1)
                this.b1 += 1;
        }
    }
}
