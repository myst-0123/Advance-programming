package human;

import ap24.Board;
import ap24.Color;
import ap24.Move;
import ap24.Player;

public class HumanPlayer extends Player {
    Scanner sc = new Scanner(System.in);

    // コンストラクタ
    public HumanPlayer(Color color) {
        super("H", color);
    }

    public Move think(Board board) {
        int num = -1;
        var moves = board.findLegalMoves(getColor());

        System.out.println(String.format("手を選択してください (0～%d)\n", moves.size()));

        // 入力をintに変換
        String str = sc.next();
        try{
            num = Integer.parseInt(str);
        }
        // 数字以外が入力されたら再帰し、もう一度入力を行う。
        catch (NumberFormatException ex){
            System.out.println("数字を入力してください。\n");
            return think(board);
        }
        // movesの要素数内なら終了
        if(num >= 0 && num < moves.size()) {
            return moves.get(num);
        }
        // 要素数外なら再帰する
        return think(board);
    }
}
