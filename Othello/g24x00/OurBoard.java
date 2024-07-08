package g24x00;

import static ap24.Color.BLACK;
import static ap24.Color.BLOCK;
import static ap24.Color.NONE;
import static ap24.Color.WHITE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import ap24.Board;
import ap24.Color;
import ap24.Move;

public class OurBoard implements Board, Cloneable {
  Color board[];
  Move move = Move.ofPass(NONE);

  long blackBoard = 0;
  long whiteBoard = 0;
  long blockBoard = 0;

  public OurBoard() {
    this.board = Stream.generate(() -> NONE).limit(LENGTH).toArray(Color[]::new);
    init();
  }

  OurBoard(Color board[], Move move) {
    this.board = Arrays.copyOf(board, board.length);
    this.move = move;
  }

  public OurBoard clone() {
    return new OurBoard(this.board, this.move);
  }

  void init() {
    set(Move.parseIndex("c3"), BLACK);
    set(Move.parseIndex("d4"), BLACK);
    set(Move.parseIndex("d3"), WHITE);
    set(Move.parseIndex("c4"), WHITE);
  }

  public Color get(int k) { return this.board[k]; }
  public Move getMove() { return this.move; }

  public Color getTurn() {
    return this.move.isNone() ? BLACK : this.move.getColor().flipped();
  }

  public void set(int k, Color color) {
    this.board[k] = color;
  }

  public boolean equals(Object otherObj) {
    if (otherObj instanceof OurBoard) {
      var other = (OurBoard) otherObj;
      return Arrays.equals(this.board, other.board);
    }
    return false;
  }

  public String toString() {
    return OurBoardFormatter.format(this);
  }

  public int count(Color color) {
    return countAll().getOrDefault(color, 0L).intValue();
  }

  public boolean isEnd() {
    var lbs = findNoPassLegalIndexes(BLACK);
    var lws = findNoPassLegalIndexes(WHITE);
    return lbs.size() == 0 && lws.size() == 0;
  }

  public Color winner() {
    var v = score();
    if (isEnd() == false || v == 0 ) return NONE;
    return v > 0 ? BLACK : WHITE;
  }

  public void foul(Color color) {
    var winner = color.flipped();
    IntStream.range(0, LENGTH).forEach(k -> this.board[k] = winner);
  }

  public int score() {
    var cs = countAll();
    var bs = cs.getOrDefault(BLACK, 0L);
    var ws = cs.getOrDefault(WHITE, 0L);
    var ns = LENGTH - bs - ws;
    int score = (int) (bs - ws);

    score += Integer.signum(score) * ns;

    return score;
  }

  Map<Color, Long> countAll() {
    return Arrays.stream(this.board).collect(
        Collectors.groupingBy(Function.identity(), Collectors.counting()));
  }

  public List<Move> findLegalMoves(Color color) {
    return findLegalIndexes(color).stream()
        .map(k -> new Move(k, color)).toList();
  }

  List<Integer> findLegalIndexes(Color color) {
    var moves = findNoPassLegalIndexes(color);
    if (moves.size() == 0) moves.add(Move.PASS);
    return moves;
  }

  void makeBitBoard() {
    long mask = 0x800000000L;
    for (int i = 0; i < LENGTH; i++) {
      switch(this.board[i]) {
        case BLACK:
          blackBoard |= mask >> i;
          break;
        case WHITE:
          whiteBoard |= mask >> i;
          break;
        case BLOCK:
          blockBoard |= mask >> i;
          break;
        default:
          break;
      }
    }
  }

  long makeLegalBoard(Color color) {
    long playerBoard;
    long opponentBoard;
    long horizontalWatchBoard; // 左右端の番人
    long verticalWatchBoard; // 上下端の番人
    long allSideWatchBoard; //4辺の番人
    long blankBoard = ~(blackBoard | whiteBoard);
    long legalBoard;

    switch(color) {
      case BLACK:
        playerBoard = blackBoard;
        opponentBoard = whiteBoard;
        break;
      case WHITE:
        playerBoard = whiteBoard;
        opponentBoard = blackBoard;  
        break;
      default:
        playerBoard = 0;
        opponentBoard = 0;
    }

    horizontalWatchBoard = opponentBoard & 0x79e79e79eL;
    verticalWatchBoard = opponentBoard & 0x03fffffc0L;
    allSideWatchBoard = opponentBoard & 0x01e79e780L;

    // 左
    long temp = horizontalWatchBoard & (playerBoard << 1);
    for (int i = 0; i < 3; i++) {
      temp |= horizontalWatchBoard & (temp << 1);
    }
    legalBoard = blankBoard & (temp << 1);

    // 右
    temp = horizontalWatchBoard & (playerBoard >> 1);
    for (int i = 0; i < 3; i++) {
      temp |= horizontalWatchBoard & (temp >> 1);
    }
    legalBoard |= blankBoard & (temp >> 1);

    // 上
    temp = verticalWatchBoard & (playerBoard << 6);
    for (int i = 0; i < 3; i++) {
      temp |= verticalWatchBoard & (temp << 6);
    }
    legalBoard |= blankBoard & (temp << 6);

    // 下
    temp = verticalWatchBoard & (playerBoard >> 6);
    for (int i = 0; i < 3; i++) {
      temp |= verticalWatchBoard & (temp >> 6);
    }
    legalBoard |= blankBoard & (temp >> 6);

    // 右斜め上
    temp = allSideWatchBoard & (playerBoard << 5);
    for (int i = 0; i < 3; i++) {
      temp |= allSideWatchBoard & (temp << 5);
    }
    legalBoard |= blankBoard & (temp << 5);

    // 左斜め上
    temp = allSideWatchBoard & (playerBoard << 7);
    for (int i = 0; i < 3; i++) {
      temp |= allSideWatchBoard & (temp << 7);
    }
    legalBoard |= blankBoard & (temp << 7);

    // 右斜め下
    temp = allSideWatchBoard & (playerBoard >> 7);
    for (int i = 0; i < 3; i++) {
      temp |= allSideWatchBoard & (temp >> 7);
    }
    legalBoard |= blankBoard & (temp >> 7);
    
    // 左斜め下
    temp = allSideWatchBoard & (playerBoard >> 5);
    for (int i = 0; i < 3; i++) {
      temp |= allSideWatchBoard & (temp >> 5);
    }
    legalBoard |= blankBoard & (temp >> 5);

    return legalBoard & ~(blockBoard);
  }

  List<Integer> findNoPassLegalIndexes(Color color) {
    makeBitBoard();
    long legalBoard = makeLegalBoard(color);
    var moves = new ArrayList<Integer>();
    for (int k = LENGTH-1; k >= 0; k--) {
      if (((legalBoard >> k) & 1L) == 1) {
        moves.add(LENGTH-1-k);
      }
    }
    return moves;
  }

  List<List<Integer>> lines(int k) {
    var lines = new ArrayList<List<Integer>>();
    for (int dir = 0; dir < 8; dir++) {
      var line = Move.line(k, dir);
      lines.add(line);
    }
    return lines;
  }

  List<Move> outflanked(List<Integer> line, Color color) {
    if (line.size() <= 1) return new ArrayList<Move>();
    var flippables = new ArrayList<Move>();
    for (int k: line) {
      var c = get(k);
      if (c == NONE || c == BLOCK) break;
      if (c == color) return flippables;
      flippables.add(new Move(k, color));
    }
    return new ArrayList<Move>();
  }

  public OurBoard placed(Move move) {
    var b = clone();
    b.move = move;

    if (move.isPass() | move.isNone())
      return b;

    var k = move.getIndex();
    var color = move.getColor();
    var lines = b.lines(k);
    for (var line: lines) {
      for (var p: outflanked(line, color)) {
        b.board[p.getIndex()] = color;
      }
    }
    b.set(k, color);

    return b;
  }

  public OurBoard flipped() {
    var b = clone();
    IntStream.range(0, LENGTH).forEach(k -> b.board[k] = b.board[k].flipped());
    b.move = this.move.flipped();
    return b;
  }
}