package myplayer;

import static ap24.Board.SIZE;
import static ap24.Color.BLACK;
import static ap24.Color.NONE;
import static ap24.Color.WHITE;
import java.util.List;
import java.util.Map;
import ap24.Move;

public class MyBoardFormatter {
  public static String format(MyBoard board) {
    var turn = board.getTurn();
    var move = board.getMove();
    var blacks = board.findNoPassLegalIndexes(BLACK);
    var whites = board.findNoPassLegalIndexes(WHITE);
    var legals = Map.of(BLACK, blacks, WHITE, whites);

    var buf = new StringBuilder("  ");
    for (int k = 0; k < SIZE; k++) buf.append(Move.toColString(k)); // 列番号abcdef
    buf.append("\n");

    for (int k = 0; k < SIZE * SIZE; k++) {
      int col = k % SIZE; // 上から何番目
      int row = k / SIZE; // 左から何番目

      if (col == 0) buf.append((row + 1) + "|"); // 行番号，盤面の左端

      if (board.get(k) == NONE) {
        boolean legal = false;
        var b = blacks.contains(k);
        var w = whites.contains(k);
        if (turn == BLACK && b) legal = true;
        if (turn == WHITE && w) legal = true;
        buf.append(legal ? '.' : ' '); // 着手可能位置
      } else {
        var s = board.get(k).toString();
        if (move != null && k == move.getIndex()) s = s.toUpperCase();
        buf.append(s);
      }

      if (col == SIZE - 1) {
        buf.append("| ");// 盤面の右端
        if (row == 0 && move != null) {
          buf.append(move); // 今回の手
        } else if (row == 1) {
          buf.append(turn + ": " + toString(legals.get(turn))); // 手番(X,O), 着手可能位置のリスト
        }
        buf.append("\n");
      }
    }

    buf.setLength(buf.length() - 1);// 列番号abcdef?
    return buf.toString();
  }

  static List<String> toString(List<Integer> moves) {
    return moves.stream().map(k -> Move.toIndexString(k)).toList();
  }
}
