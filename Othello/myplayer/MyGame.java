package myplayer;

import static ap24.Color.BLACK;
import static ap24.Color.WHITE;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ap24.Board;
import ap24.Color;
import ap24.Move;
import ap24.Player;

public class MyGame {
  public static void main(String args[]) {
    var player1 =  new myplayer.MyPlayer(BLACK);
    var player2 =  new myplayer.RandomPlayer(WHITE);
    var board = new MyBoard();
    var game = new MyGame(board, player1, player2);
    game.play();
  }

  static final float TIME_LIMIT_SECONDS = 60;

  Board board;
  Player black; // 先手
  Player white; // 後手
  Map<Color, Player> players; // プレイヤー2人をまとめたもの
  List<Move> moves = new ArrayList<Move>();
  Map<Color, Float> times = new HashMap<>(Map.of(BLACK, 0f, WHITE, 0f));

  public MyGame(Board board, Player black, Player white) {
    this.board = board.clone();
    this.black = black;
    this.white = white;
    this.players = Map.of(BLACK, black, WHITE, white);
  }

  public void play() {
    this.players.values().forEach(p -> p.setBoard(this.board.clone()));

    while (this.board.isEnd() == false) {
      var turn = this.board.getTurn();
      var player = this.players.get(turn);

      Error error = null;
      long t0 = System.currentTimeMillis(); // 時間計測開始
      Move move;

      // play
      try {
        move = player.think(board.clone()).colored(turn); // 着手
      } catch (Error e) {
        error = e;
        move = Move.ofError(turn);
      }

      // record time
      long t1 = System.currentTimeMillis();// 時間計測終了
      final var t = (float) Math.max(t1 - t0, 1) / 1000.f;
      this.times.compute(turn, (k, v) -> v + t);

      // check
      move = check(turn, move, error);
      moves.add(move);

      // update board
      if (move.isLegal()) {
        board = board.placed(move);
      } else {
        board.foul(turn);
        break;
      }

      System.out.println(board);
    }

    printResult(board, moves);
  }

  Move check(Color turn, Move move, Error error) {
    if (move.isError()) {
      System.err.printf("error: %s %s", turn, error);
      System.err.println(board);
      return move;
    }

    if (this.times.get(turn) > TIME_LIMIT_SECONDS) {
      System.err.printf("timeout: %s %.2f", turn, this.times.get(turn));
      System.err.println(board);
      return Move.ofTimeout(turn);
    }

    var legals = board.findLegalMoves(turn);
    if (move == null || legals.contains(move) == false) {
      System.err.printf("illegal move: %s %s", turn, move);
      System.err.println(board);
      return Move.ofIllegal(turn);
    }

    return move;
  }

  public Player getWinner(Board board) {
    return this.players.get(board.winner());
  }

  public void printResult(Board board, List<Move> moves) {
    var result = String.format("%5s%-9s", "", "draw");
    var score = Math.abs(board.score());
    if (score > 0)
      result = String.format("%-4s won by %-2d", getWinner(board), score);

    var s = toString() + " -> " + result + "\t| " + toString(moves);
    System.out.println(s);
  }

  public String toString() {
    return String.format("%4s vs %4s", this.black, this.white);
  }

  public static String toString(List<Move> moves) {
    return moves.stream().map(x -> x.toString()).collect(Collectors.joining());
  }
}
