public class ImagawasCutPrint extends CutPrint {
    protected void draw(Cuttable hanzai) {
        System.out.println("マンガの絵を描く");
    }

    protected void cut(Cuttable hanzai) {
        System.out.println("彫刻刀を利用して器用に彫る");
    }

    protected void print(Cuttable hanzai) {
        System.out.println("インクとして、自分の血を使いプリントする");
    }

    protected Cuttable createCuttable() {
        return new Potato();
    }
}
