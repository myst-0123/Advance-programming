package ap24;

import static ap24.Board.SIZE;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Move {
    public final static int PASS = -1;
    final static int TIMEOUT = -10;
    final static int ILLEGAL = -20;
    final static int ERROR = -30;

    int index;
    Color color;

    // 与えられた設定でMoveの作成
    public static Move of(int index, Color color) {
        return new Move(index, color);
    }
    public static Move of(String pos, Color color) {
        return new Move(parseIndex(pos), color);
    }
    public static Move ofPass(Color color) {
        return new Move(PASS, color);
    }
    public static Move ofTimeout(Color color) {
        return new Move(TIMEOUT, color);
    }
    public static Move ofIllegal(Color color) {
        return new Move(ILLEGAL, color);
    }
    public static Move ofError(Color color) {
        return new Move(ERROR, color);
    }
    
    // コンストラクタ
    public Move(int index, Color color) {
        this.index = index;
        this.color = color;        
    }

    // ゲッター
    public int getIndex() { return this.index; }
    public int getRow() { return this.index / SIZE; }
    public int getCol() { return this.index % SIZE; }
    public Color getColor() { return this.color; }
    public int hashCode() { return Objects.hash(this.index, this.color); }

    // 等価判定
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;
        // indexおよびcolorが同じなら等価と判別
        Move other = (Move)obj;
        return this.index == other.index && this.color == other.color;
    }

    // フラグ判定
    // 色なし判定
    public boolean isNone() { return this.color == Color.NONE; }
    
    // 正常状態判定
    public boolean isLegal() { return this.index >= PASS; }
    // パス判定
    public boolean isPass() { return this.index == PASS; }
    
    // ファウル判定(盤面外)
    public boolean isFoul() { return this.index < PASS; }
    // タイムアウト判定(時間切れ)
    public boolean isTimeout() { return this.index == TIMEOUT; }
    // 異常状態判定
    public boolean isIllegal() { return this.index == ILLEGAL; }
    // エラー
    public boolean isError() { return this.index == ERROR; }

    // 反転
    public Move flipped() {
        return new Move(this.index, this.color.flipped());
    }

    // 色の変更
    public Move colored(Color color) {
        return new Move(this.index, color);
    }

    // [col][row]のマスが盤面内にあるか判別
    public static boolean isValid(int col, int row) {
        return 0 <= col && col < SIZE && 0 <= row && row < SIZE;
    }

    // 上下左右斜めのdistマス離れた地点を指す配列を返す
    static int[][] offsets(int dist) {
        return new int[][] {
            { -dist, 0 }, { -dist, dist }, { 0, dist }, { dist, dist },
            { dist, 0 }, { dist, -dist }, { 0, -dist }, { -dist, -dist}
        };
    }

    // kのマスの上下左右斜め1マスの9マス分のindexを持つListを返す
    public static List<Integer> adjacent(int k) {
        var ps = new ArrayList<Integer>();
        int col0 = k % SIZE, row0 = k / SIZE;

        for(var o : offsets(1)) {
            int col = col0 + o[0], row = row0 + o[1];
            // [col][row]のマスが盤面の中ならListに追加
            if(Move.isValid(col, row)) ps.add(index(col, row));
        }
        return ps;
    }

    // kマスからdir方向にある全てのマスを取得しListで返す
    public static List<Integer> line(int k, int dir) {
        var line = new ArrayList<Integer>();
        int col0 = k % SIZE, row0 = k / SIZE;

        for(int dist = 1; dist < SIZE; dist++) {
            var o = offsets(dist)[dir];
            int col = col0 + o[0], row = row0 + o[1];
            // 盤面の外へ出たら終了
            if(Move.isValid(col, row) == false)
                break;
            line.add(index(col, row));
        }
        return line;
    }

    // [col][row]のマスのindexを取得する
    public static int index(int col, int row) {
        return SIZE * row + col;
    }
    // indexを文字列にする
    public String toString() { return toIndexString(this.index); }
    // 文字列からindexを取得する
    public static int parseIndex(String pos) {
        return SIZE * (pos.charAt(1) - '1') + pos.charAt(0) - 'a';
    }
    // indexを文字列に変換する
    // indexがPASSならば"..", TIMEOUTならば"@"とする
    public static String toIndexString(int index) {
        if(index == PASS) return "..";
        if(index == TIMEOUT) return "@";
        // colStringとrowStringを組み合わせて文字列とする
        return toColString(index % SIZE) + toRowString(index / SIZE);
    }
    // Rowを文字列にする
    public static String toColString(int col) {
        return Character.toString('a' + col);
    }
    // colを文字列にする
    public static String toRowString(int row) {
        return Character.toString('1' + row);
    }
    // 整数型のindexリストを文字列型のindexリストに変換する。
    public static List<String> toStringList(List<Integer> moves) {
        return moves.stream().map(k -> toIndexString(k)).toList();
    }
}