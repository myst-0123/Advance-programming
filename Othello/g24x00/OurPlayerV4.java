package g24x00;

import static ap24.Board.LENGTH;
import static ap24.Board.SIZE;
import static ap24.Color.BLACK;
import static ap24.Color.WHITE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;
import ap24.Board;
import ap24.Color;
import ap24.Move;

class MyEval4 {
    // = {
    // { 10, 10, 10, 10, 10, 10 },
    // { 10, -5, 1, 1, -5, 10 },
    // { 10, 1, 1, 1, 1, 10 },
    // { 10, 1, 1, 1, 1, 10 },
    // { 10, -5, 1, 1, -5, 10 },
    // { 10, 10, 10, 10, 10, 10 },
    // };
    float[][] Mori = {
            { 20, -5, 10, 10, -5, 20 },
            { -5, -10, 1, 1, -10, -5 },
            { 10, 1, 1, 1, 1, 10 },
            { 10, 1, 1, 1, 1, 10 },
            { -5, -10, 1, 1, -10, -5 },
            { 20, -5, 10, 10, -5, 20 },
    };
    float[][] M = new float[Mori.length][Mori[0].length];

    public void Minit() {
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M.length; j++) {
                M[i][j] = Mori[i][j];
            }
        }
    }

    public void setM(int i, float val) {
        M[i / 6][i % 6] = val;
    }

    public float value(Board board) {
        if (board.isEnd())
            return 1000000 * board.score();

        return (float) IntStream.range(0, LENGTH)
                .mapToDouble(k -> score(board, k))
                .reduce(Double::sum).orElse(0);
    }

    public float value2(Board board) {
        if (board.isEnd())
            return 1000000 * board.score();

        return (float) IntStream.range(0, LENGTH)
                .mapToDouble(k -> score_light(board, k))
                .reduce(Double::sum).orElse(0);
    }

    // 重み付きでスコアを返す
    float score(Board board, int k) { // 課題2e 参考:https://bassy84.net/#google_vignette
        int l_b = ((OurBoard) board).findLegalIndexes(BLACK).size(); // BLACKの合法手
        int l_w = ((OurBoard) board).findLegalIndexes(WHITE).size(); // WHITEの合法手
        int n_b = ((OurBoard) board).count(BLACK); // BLACKの石の数
        int n_w = ((OurBoard) board).count(WHITE); // WHITEの石の数

        int[] w = new int[5]; // w_1~w_5
        if (n_b + n_w <= 12)
            // w = new int[] { 1, +3, -9, -1, 1 }; // 序盤:石を多く取らないようにする≒合法手の数を重視する
            w = new int[] { 1, 30, -60, -1, 2 };
        else if (n_b + n_w <= 24)
            // w = new int[] { 3, +5, -15, 1, -1 }; // 中盤:合法手の数を重視しつつ、隅や辺にも注意する 石の数にも気をつける
            w = new int[] { 3, 30, -60, 2, -1 };
        else
            // w = new int[] { 10, +10, -20, +10, -20 }; // 終盤:隅、辺と石の数に注目する
            w = new int[] { 10, 20, -40, 10, -20 };
        return w[0] * M[k / SIZE][k % SIZE] * board.get(k).getValue()
                + w[1] * l_b / SIZE / SIZE
                + w[2] * l_w / SIZE / SIZE
                + w[3] * n_b / SIZE / SIZE
                + w[4] * n_w / SIZE / SIZE;
    }

    float score_light(Board board, int k) { // 課題2e 参考:https://bassy84.net/#google_vignette
        var cs = ((OurBoard) board).countAll(); // 各色の石の数
        long n_b = cs.getOrDefault(BLACK, 0L); // BLACKの石の数
        long n_w = cs.getOrDefault(WHITE, 0L); // WHITEの石の数
        // int n_b = ((MyBoard) board).count(BLACK); // BLACKの石の数
        // int n_w = ((MyBoard) board).count(WHITE); // WHITEの石の数
        int[] w = new int[3]; // w_1~w_5
        if (n_b + n_w <= 12)
            w = new int[] { 1, -20, 10 }; // 序盤:石を多く取らないようにする≒合法手の数を重視する
        else if (n_b + n_w <= 24)
            w = new int[] { 3, 1, -2 }; // 中盤:合法手の数を重視しつつ、隅や辺にも注意する 石の数にも気をつける
        else
            w = new int[] { 10, +10, -20 }; // 終盤:隅、辺と石の数に注目する
        return w[0] * M[k / SIZE][k % SIZE] * board.get(k).getValue()
                + w[1] * n_b / SIZE / SIZE
                + w[2] * n_w / SIZE / SIZE;
        // return M[k / SIZE][k % SIZE] * board.get(k).getValue();
    }
}

public class OurPlayerV4 extends ap24.Player {
    static final String MY_NAME = "2404";
    MyEval4 eval;
    int depthLimit;
    Move move;
    OurBoard board;

    public OurPlayerV4(Color color) {
        this(MY_NAME, color, new MyEval4(), 8);
    }

    public OurPlayerV4(String name, Color color, MyEval4 eval, int depthLimit) {
        super(name, color);
        this.eval = eval;
        this.depthLimit = depthLimit;
        this.board = new OurBoard();
    }

    public OurPlayerV4(String name, Color color, int depthLimit) {
        this(name, color, new MyEval4(), depthLimit);
    }

    public void setBoard(Board board) {
        this.eval.Minit();
        for (var i = 0; i < LENGTH; i++) {
            this.board.set(i, board.get(i));
            // System.out.print(board.get(i).getValue());
            if (board.get(i).getValue() == 3) {
                this.eval.setM(i, 0);
                if (i == 0) {
                    this.eval.setM(1, 20);
                    this.eval.setM(6, 20);
                } else if (i == 2 || i == 3) {
                    this.eval.setM(i - 1, 20);
                    this.eval.setM(i + 1, 20);
                } else if (i < 5) {
                    this.eval.setM(i - 1, 20);
                    this.eval.setM(i + 1, 20);
                } else if (i == 5) {
                    this.eval.setM(4, 20);
                    this.eval.setM(11, 20);
                } else if (i == 12 || i == 18) {
                    this.eval.setM(i - 6, 20);
                    this.eval.setM(i + 6, 20);
                } else if (i < 30) {
                    this.eval.setM(i - 6, 20);
                    this.eval.setM(i + 6, 20);
                } else {
                    this.eval.setM(24, 20);
                    this.eval.setM(31, 20);
                }
            }

        }
        // System.out.println(Arrays.deepToString(this.eval.M));
    }

    boolean isBlack() {
        return getColor() == BLACK;
    }

    public Move think(Board board) {
        this.board = this.board.placed(board.getMove());

        if (this.board.findNoPassLegalIndexes(getColor()).size() == 0) {
            this.move = Move.ofPass(getColor());
        } else {
            var newBoard = isBlack() ? this.board.clone() : this.board.flipped();
            this.move = null;

            maxSearch(newBoard, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, 0);

            this.move = this.move.colored(getColor());
        }

        this.board = this.board.placed(this.move);
        return this.move;
    }

    float maxSearch(Board board, float alpha, float beta, int depth) {
        if (isTerminal(board, depth))
            return this.eval.value(board);

        var moves = board.findLegalMoves(BLACK);

        moves = order(moves, board, BLACK);

        if (depth == 0)
            this.move = moves.get(0);

        for (var move : moves) {
            var newBoard = board.placed(move);
            float v = minSearch(newBoard, alpha, beta, depth + 1);

            if (v > alpha) {
                alpha = v;
                if (depth == 0)
                    this.move = move;
            }

            if (alpha >= beta)
                break;
        }

        return alpha;
    }

    float minSearch(Board board, float alpha, float beta, int depth) {
        if (isTerminal(board, depth))
            return this.eval.value(board);

        var moves = board.findLegalMoves(WHITE);
        // if (depth >= 2)
        moves = order(moves, board, WHITE);

        for (var move : moves) {
            var newBoard = board.placed(move);
            float v = maxSearch(newBoard, alpha, beta, depth + 1);
            beta = Math.min(beta, v);
            if (alpha >= beta)
                break;
        }

        return beta;
    }

    boolean isTerminal(Board board, int depth) {
        return board.isEnd() || depth > this.depthLimit;
    }

    List<Move> order(List<Move> moves, Board board, Color c) {
        var shuffled = new ArrayList<Move>(moves);
        // for (Move m : moves) {
        // System.out.println(m.getIndex());
        // }
        // System.out.println(c);
        // // Collections.shuffle(shuffled);
        // System.out.println(shuffled);
        Map<Move, Float> moveEvaluationMap = new HashMap<>();
        for (Move move : shuffled) {
            var newBoard = board.placed(move);
            moveEvaluationMap.put(move, this.eval.value2(newBoard));
        }

        if (c == BLACK) {
            shuffled.sort(Comparator.comparing(moveEvaluationMap::get).reversed());
        } else {
            shuffled.sort(Comparator.comparing(moveEvaluationMap::get));
        }

        // if (c == BLACK) {
        // // System.out.print("黒");
        // shuffled.sort(Comparator.comparing(move -> {

        // var newBoard = board.placed((Move) move);

        // // System.out.println(this.eval.value2(newBoard));
        // return this.eval.value2(newBoard);
        // }).reversed());
        // }

        // else {
        // // System.out.print("白");
        // shuffled.sort(Comparator.comparing(move -> {

        // var newBoard = board.placed((Move) move);
        // // System.out.println(this.eval.value2(newBoard));
        // return this.eval.value2(newBoard);
        // }));
        // }
        // nodeCount += list.size();
        // System.out.println(shuffled);
        // Scanner stdIn = new Scanner(System.in);
        // System.out.print("石の数：");
        // int numStones = stdIn.nextInt();
        if (shuffled.size() > 4) {
            return new ArrayList<Move>() {
                {
                    add(shuffled.get(0));
                    add(shuffled.get(1));
                    add(shuffled.get(2));
                    add(shuffled.get(3));
                }
            };
            // } else if (shuffled.size() == 3) {
            // // return new ArrayList<Move>() {
            // // {
            // // add(shuffled.get(0));
            // // add(shuffled.get(1));
            // // }
            // // };
        }
        return shuffled;
    }

}
