package myplayer;

import static ap24.Board.LENGTH;
import static ap24.Board.SIZE;
import static ap24.Color.BLACK;
import static ap24.Color.WHITE;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import ap24.Board;
import ap24.Color;
import ap24.Move;

class MyEval {
    static float[][] M = {
            { 10, 10, 10, 10, 10, 10 },
            { 10, -5, 1, 1, -5, 10 },
            { 10, 1, 1, 1, 1, 10 },
            { 10, 1, 1, 1, 1, 10 },
            { 10, -5, 1, 1, -5, 10 },
            { 10, 10, 10, 10, 10, 10 },
    };

    // boardのスコアを取得する
    public float value(Board board) {
        if (board.isEnd())
            return 1000000 * board.score();

        return (float) IntStream.range(0, LENGTH)
                .mapToDouble(k -> score(board, k))
                .reduce(Double::sum).orElse(0);
    }

    // 重み付きでスコアを返す
    float score(Board board, int k) { // 課題2e 参考:https://bassy84.net/#google_vignette
        int l_b = ((MyBoard) board).findLegalIndexes(BLACK).size(); // BLACKの合法手
        int l_w = ((MyBoard) board).findLegalIndexes(WHITE).size(); // WHITEの合法手
        int n_b = ((MyBoard) board).count(BLACK); // BLACKの石の数
        int n_w = ((MyBoard) board).count(WHITE); // WHITEの石の数

        int[] w = new int[5]; // w_1~w_5
        if (n_b + n_w <= 12)
            w = new int[] { 1, +30, -60, +2, -1 }; // 序盤:石を多く取らないようにする≒合法手の数を重視する 94.5%
            //w = new int[]{0, 1, -2, 0, 0}; //89%
            //w = new int[]{1, 0, 0, 0, 0}; //91%
        else if (n_b + n_w <= 24)
            w = new int[] { 3, +30, -60, +5, -5 }; // 中盤:合法手の数を重視しつつ、隅や辺にも注意する 石の数にも気をつける
            //w = new int[]{0, 1, -2, 0, 0};
            //w = new int[]{1, 0, 0, 0, 0};
        else
            w = new int[] { 10, +20, -40, +10, -20 }; // 終盤:隅、辺と石の数に注目する
            //w = new int[]{0, 1, -2, 0, 0};
            //w = new int[]{1, 0, 0, 0, 0};
        return w[0] * M[k / SIZE][k % SIZE] * board.get(k).getValue() + w[1] * l_b + w[2] * l_w + w[3] * n_b + w[4] * n_w;
    }
}

public class MyPlayer extends ap24.Player {
    static final String MY_NAME = "MY24";
    MyEval eval;
    int depthLimit;
    Move move;
    MyBoard board;

    // コンストラクタ
    public MyPlayer(Color color) {
        this(MY_NAME, color, new MyEval(), 2);
    }

    public MyPlayer(String name, Color color, MyEval eval, int depthLimit) {
        super(name, color);
        this.eval = eval;
        this.depthLimit = depthLimit;
        this.board = new MyBoard();
    }

    public MyPlayer(String name, Color color, int depthLimit) {
        this(name, color, new MyEval(), depthLimit);
    }

    // 現在の盤面をセット
    public void setBoard(Board board) {
        for (var i = 0; i < LENGTH; i++) {
            this.board.set(i, board.get(i));
        }
    }

    boolean isBlack() {
        return getColor() == BLACK;
    }

    // 決定した行動を返す
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

    // maxサーチ
    float maxSearch(Board board, float alpha, float beta, int depth) {
        if (isTerminal(board, depth))
            return this.eval.value(board);

        var moves = board.findLegalMoves(BLACK);
        moves = order(moves, board);

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

    // Minサーチ
    float minSearch(Board board, float alpha, float beta, int depth) {
        if (isTerminal(board, depth))
            return this.eval.value(board);

        var moves = board.findLegalMoves(WHITE);
        moves = order(moves, board);

        for (var move : moves) {
            var newBoard = board.placed(move);
            float v = maxSearch(newBoard, alpha, beta, depth + 1);
            beta = Math.min(beta, v);
            if (alpha >= beta)
                break;
        }

        return beta;
    }

    // 探索の終了条件
    boolean isTerminal(Board board, int depth) {
        return board.isEnd() || depth > this.depthLimit;
    }

    // Listのシャッフル
    List<Move> order(List<Move> moves, Board board) {
        var shuffled = new ArrayList<Move>(moves);
        Collections.shuffle(shuffled);

        // shuffled.sort(Comparator.comparing(move -> {
        //     var newBoard = board.placed((Move) move);
        //     return this.eval.value(newBoard);
        // }).reversed());
        // nodeCount += list.size();

        return shuffled;
    }
}
