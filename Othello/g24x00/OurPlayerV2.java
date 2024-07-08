package g24x00;

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

class MyEval2 {
  // static float[][] M = {
  // { 10, 10, 10, 10, 10, 10 },
  // { 10, -5, 1, 1, -5, 10 },
  // { 10, 1, 1, 1, 1, 10 },
  // { 10, 1, 1, 1, 1, 10 },
  // { 10, -5, 1, 1, -5, 10 },
  // { 10, 10, 10, 10, 10, 10 },
  // };
  static float[][] M = {
      { 20, -5, 10, 10, -5, 20 },
      { -5, -10, 1, 1, -10, -5 },
      { 10, 1, 1, 1, 1, 10 },
      { 10, 1, 1, 1, 1, 10 },
      { -5, -10, 1, 1, -10, -5 },
      { 20, -5, 10, 10, -5, 20 },
  };

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
      w = new int[] { +1, +30, -60, -1, +2 }; // 序盤:石を多く取らないようにする≒合法手の数を重視する
    // w = new int[]{1, 0, 0, 0, 0};
    else if (n_b + n_w <= 24)
      w = new int[] { +3, +30, -60, +2, -1 }; // 中盤:合法手の数を重視しつつ、隅や辺にも注意する 石の数にも気をつける
    // w = new int[]{1, 0, 0, 0, 0};
    else
      w = new int[] { +10, +20, -40, +10, -20 }; // 終盤:隅、辺と石の数に注目する
    // w = new int[]{1, 0, 0, 0, 0};
    return w[0] * M[k / SIZE][k % SIZE] * board.get(k).getValue()
        + w[1] * l_b
        + w[2] * l_w
        + w[3] * n_b
        + w[4] * n_w;
  }

  float score_light(Board board, int k) { // 課題2e 参考:https://bassy84.net/#google_vignette
    var cs = ((OurBoard) board).countAll(); // 各色の石の数
    long n_b = cs.getOrDefault(BLACK, 0L); // BLACKの石の数
    long n_w = cs.getOrDefault(WHITE, 0L); // WHITEの石の数
    // int n_b = ((MyBoard) board).count(BLACK); // BLACKの石の数
    // int n_w = ((MyBoard) board).count(WHITE); // WHITEの石の数
    int[] w = new int[3]; // w_1~w_5
    if (n_b + n_w <= 12)
      w = new int[] { 1, -1, 1 }; // 序盤:石を多く取らないようにする≒合法手の数を重視する
    else if (n_b + n_w <= 24)
      w = new int[] { 3, 0, 0 }; // 中盤:合法手の数を重視しつつ、隅や辺にも注意する 石の数にも気をつける
    else
      w = new int[] { 10, +10, -20 }; // 終盤:隅、辺と石の数に注目する
    return w[0] * M[k / SIZE][k % SIZE] * board.get(k).getValue()
        + w[1] * n_b / SIZE / SIZE
        + w[2] * n_w / SIZE / SIZE;
    // return M[k / SIZE][k % SIZE] * board.get(k).getValue();
  }
}

public class OurPlayerV2 extends ap24.Player {
  static final String MY_NAME = "2401";
  MyEval2 eval;
  int depthLimit;
  Move move;
  OurBoard board;

  public OurPlayerV2(Color color) {
    this(MY_NAME, color, new MyEval2(), 6);
  }

  public OurPlayerV2(String name, Color color, MyEval2 eval, int depthLimit) {
    super(name, color);
    this.eval = eval;
    this.depthLimit = depthLimit;
    this.board = new OurBoard();
  }

  public OurPlayerV2(String name, Color color, int depthLimit) {
    this(name, color, new MyEval2(), depthLimit);
  }

  public void setBoard(Board board) {
    for (var i = 0; i < LENGTH; i++) {
      this.board.set(i, board.get(i));
    }
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
    if (depth >= 2)
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
    if (depth >= 2)
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
    if (c == BLACK) {
      // System.out.print("黒");
      shuffled.sort(Comparator.comparing(move -> {

        var newBoard = board.placed((Move) move);

        // System.out.println(this.eval.value2(newBoard));
        return this.eval.value2(newBoard);
      }).reversed());
    }

    else {
      // System.out.print("白");
      shuffled.sort(Comparator.comparing(move -> {

        var newBoard = board.placed((Move) move);
        // System.out.println(this.eval.value2(newBoard));
        return this.eval.value2(newBoard);
      }));
    }
    // nodeCount += list.size();
    // System.out.println(shuffled);
    // Scanner stdIn = new Scanner(System.in);
    // System.out.print("石の数：");
    // int numStones = stdIn.nextInt();
    if (shuffled.size() > 2) {
      return new ArrayList<Move>() {
        {
          add(shuffled.get(0));
          add(shuffled.get(1));
          add(shuffled.get(2));
        }
      };
      // } else if (shuffled.size() == 3) {
      // return new ArrayList<Move>() {
      // {
      // add(shuffled.get(0));
      // add(shuffled.get(1));
      // }
      // };
    }
    return shuffled;

  }
}
