package Report.chino.kaoru;

public class Move {
    public int index;
    Color color;
    public float val;

    public Move(int index, Color color) {
        this.index = index;
        this.color = color;
    }

    public Move flipped() {
        return new Move(this.index, this.color.flipped());
    }
}
