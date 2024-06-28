package Report.chino.kaoru;

public class Eval {
    public float value(State state) { // 同色の石のみからなるラインを評価値に利用
        return -1025 * state.b3 + 511 * state.a3 - 63 * state.b2 + 31 * state.a2 - 15 * state.b1 + 7 * state.a1;
    }
}
