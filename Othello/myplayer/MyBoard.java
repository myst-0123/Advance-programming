package myplayer;

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

public class MyBoard implements Board, Cloneable {
    Color board[];
    Move move = Move.ofPass(NONE); //パスを意味するMove

    public MyBoard(){ //開始時のインスタント生成用コンストラクタ
        this.board = Stream.generate(() -> NONE).limit(LENGTH).toArray(Color[]::new);
        init();
    }

    MyBoard(Color board[], Move move){ //引数で初期化するコンストラクタ
        this.board = Arrays.copyOf(board, board.length);
        this.move = move;
    }

    public MyBoard clone(){ //インスタンスのクローン
        return new MyBoard(this.board, this.move);
    }

    void init(){ //盤面の初期化
        set(Move.parseIndex("c3"), BLACK);
        set(Move.parseIndex("d4"), BLACK);
        set(Move.parseIndex("d3"), WHITE);
        set(Move.parseIndex("c4"), WHITE);
    }

    public Color get(int k){return this.board[k];} //マスkに置かれた石の色を返却
    public Move getMove(){return this.move;} //moveのゲッタ

    public Color getTurn(){ //手番の色を返却
        return this.move.isNone() ? BLACK : this.move.getColor().flipped(); //初手は黒、それ以降は反転した色を返却
    }

    public void set(int k, Color color){ //マスkにcolor色の石を置く
        this.board[k] = color;
    }

    public boolean equals(Object otherObj){ //otherObjが表す盤面が現在の盤面と同一ならtrue
        if(otherObj instanceof MyBoard){
            var other = (MyBoard)otherObj;
            return Arrays.equals(this.board, other.board);
        }
        return false;
    }

    public String toString(){ //出力
        return MyBoardFormatter.format(this);
    }

    public int count(Color color){ //color色の石の数を返却？
        return countAll().getOrDefault(color, 0L).intValue();
    }

    public boolean isEnd(){ //ゲームが終了するか否かを返却
        var lbs = findNoPassLegalIndexes(BLACK); //BLACKが取ることができるマスの番号のリスト
        var lws = findNoPassLegalIndexes(WHITE); //WHITEが取ることができるマスの番号のリスト
        return lbs.size() == 0 && lws.size() == 0; //両者に取れる石があるかどうか
    }

    public Color winner() { //勝者を返却(BLACKorWHITE)
        var v = score();
        if(isEnd() == false || v == 0) return NONE; //まだ決着が着いていないならNONE
        return v > 0 ? BLACK : WHITE;
    }

    public void foul(Color color){
        var winner = color.flipped();
        IntStream.range(0, LENGTH).forEach(k -> this.board[k] = winner);
    }

    public int score() {
        var cs = countAll();
        var bs = cs.getOrDefault(BLACK, 0L);
        var ws = cs.getOrDefault(WHITE, 0L);
        var ns = LENGTH - bs - ws;
        int score = (int)(bs - ws);

        if(bs == 0 || ws == 0)
            score += Integer.signum(score) * ns;

        return score;
    }

    Map<Color, Long> countAll(){
        return Arrays.stream(this.board).collect(
            Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public List<Move> findLegalMoves(Color color) { //color側のプレイヤーの可能なMoveのリストを返却
        return findNoPassLegalIndexes(color).stream()
            .map(k -> new Move(k, color)).toList(); //番号のリストをMoveのリストに変換
    }

    List<Integer> findLegalIndexes(Color color){ //color側のプレイヤーが取ることができるマスの番号のリストを返却(パスを考慮)
        var moves = findNoPassLegalIndexes(color);
        if(moves.size() == 0) moves.add(Move.PASS); //可能なMoveが無ければパス
        return moves;
    }

    List<Integer> findNoPassLegalIndexes(Color color){ //color側のプレイヤーが取ることができるマスの番号のリストを返却(パスを考慮しない)
        var moves = new ArrayList<Integer>();
        for(int k = 0; k < LENGTH; k++){
            var c = this.board[k];
            if(c != NONE) continue; //石が置かれていなければ次のマスを参照 -> 置かれている石の色はなんでもいい？
            for(var line : lines(k)){
                var outflanking = outflanked(line, color); //ある方向の可能なMoveのリスト
                if(outflanking.size() > 0) moves.add(k); //可能なMoveがあればadd
            }
        }
        return moves;
    }

    List<List<Integer>> lines(int k){ //マスkから8方向(上下左右+斜め)のマスの番号のリスト
        var lines = new ArrayList<List<Integer>>();
        for (int dir = 0; dir < 8; dir++) {
            var line = Move.line(k, dir);
            lines.add(line);
        }
        return lines;
    }

    List<Move> outflanked(List<Integer> line, Color color){ //color色の石からのlineにおいて、可能なMove(ひっくり返せる石)のリストを返却
        if(line.size() <= 1) return new ArrayList<Move>(); //lineに石が1個以下なら可能なMove無し
        var flippables = new ArrayList<Move>();
        for (int k : line) {
            var c = get(k);
            if(c == NONE || c == BLOCK) break; //石の無いマスや壁に到達したらbreak
            if(c == color) return flippables; //color色の石が現れたらreturn
            flippables.add(new Move(k, color)); //color色でない石なら(ひっくり返せるかもしれないので)add
        }
        return new ArrayList<Move>();
    }

    public MyBoard placed(Move move) {
        var b = clone();
        b.move = move;

        if(move.isPass() | move.isNone())
            return b;

        var k = move.getIndex();
        var color = move.getColor();
        var lines = b.lines(k);
        for(var line: lines){
            for(var p: outflanked(line, color)){
                b.board[p.getIndex()] = color;
            }
        }
        b.set(k, color);

        return b;
    }

    public MyBoard flipped() {
        var b = clone();
        IntStream.range(0, LENGTH).forEach(k -> b.board[k] = b.board[k].flipped());
        b.move = this.move.flipped();
        return b;
    }
}
