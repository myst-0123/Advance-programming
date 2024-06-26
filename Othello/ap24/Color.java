package ap24;

import java.util.Map;

public enum Color {
    BLACK(1),
    WHITE(-1),
    NONE(0),
    BLOCK(3);
    
    // 盤面の状態を表示する記号の組み合わせを示すシンボル
    static Map<Color, String> SYMBOLS = 
        Map.of(BLACK, "o", WHITE, "X", BLOCK, "#");
    private int value;

    // コンストラクタ
    private Color(int value) {
        this.value = value;
    }

    public int getValue() { return this.value; }

    // 色の反転
    public Color flipped() {
        switch(this) {
            case BLACK: return WHITE;
            case WHITE: return BLACK;
            default: return this;
        }
    }
    public String toString() { return SYMBOLS.get(this); }

    // 記号シンボルから色への変換
    public Color parse(String str) {
        return Map.of("o", BLACK, "x", WHITE).getOrDefault(str, NONE);
    }
}