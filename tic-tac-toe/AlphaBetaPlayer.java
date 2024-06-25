
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AlphaBetaPlayer implements Player {
    Scanner scanner = new Scanner(System.in);
    int move = 0; //選択すべき手
    int depthLimit = 4; //深さ制限

    public AlphaBetaPlayer() {
        // super("Human");
    }

    // protected Move search(State state) {
    //     var moves = state.getMoves();

    //     int index;
    //     Outer: while (true) {
    //         System.out.print("マス座標(0~8)：");
    //         index = scanner.nextInt();
    //         for (int i = 0; i < moves.size(); i++) {
    //             if (index == moves.get(i).index) {
    //                 index = i;
    //                 break Outer;
    //             }

    //         }
    //     }
    //     return moves.get(index);
    // }
    public int getNextMove(int[] board) { //次の最善手を返す(0~8)
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        maxSearch(board, alpha, beta, depthLimit);
        return move;
    }

    int maxSearch(int[] board, int alpha, int beta, int depthLimit){ //max探索
        int rest = 0; //まだ石が置かれていないマスの数を格納する変数
        List<Integer> nextmoves = new ArrayList<>(); //まだ石が置かれていないマスの番号を格納するリスト
        for (int i = 0; i < board.length; i++) {
            if(board[i] == 0){
                rest++;
                nextmoves.add(i);
            }
        }
        if(rest == 0 || depthLimit <= 0) return getEvaluation(board); //置けるマスが無いか深さ制限なら評価値返却

        for (Integer nextmove : nextmoves) { //各設置可能なマスに対して
            int[] newBoard = new int[9];
            int[] inverseBoard = new int[9];
            for (int i = 0; i < board.length; i++) {
                //そのマスに石を置いたときの盤面を作成
                newBoard[i] = board[i];
                if(i == nextmove) newBoard[i] = 1;
                //1と2が反転した盤面を作成
                if(newBoard[i] == 1) inverseBoard[i] = 2;
                else if(newBoard[i] == 2) inverseBoard[i] = 1;
                else inverseBoard[i] = 0;
            }

            int v = minSearch(inverseBoard, alpha, beta, depthLimit - 1); //min探索
            if(v > alpha){ //評価値がalphaより大きければ
                alpha = v; //alpha更新
                if(depthLimit == this.depthLimit) this.move = nextmove; //getNextMoveから直接呼び出されたメソッドなら、選択すべき手を更新
            }
            if(alpha >= beta) break; //カット
        }
        
        return alpha;
    }

    int minSearch(int[] board, int alpha, int beta, int depthLimit){ //min探索
        int rest = 0; //まだ石が置かれていないマスの数を格納する変数
        List<Integer> nextmoves = new ArrayList<>(); //まだ石が置かれていないマスの番号を格納するリスト
        for (int i = 0; i < board.length; i++) {
            if(board[i] == 0){
                rest++;
                nextmoves.add(i);
            }
        }
        if(rest == 0 || depthLimit <= 0) return getEvaluation(board); //置けるマスが無いか深さ制限なら評価値返却

        for (Integer nextmove : nextmoves) { //各設置可能なマスに対して
            int[] newBoard = new int[9];
            int[] inverseBoard = new int[9];
            for (int i = 0; i < board.length; i++) {
                //そのマスに石を置いたときの盤面を作成
                newBoard[i] = board[i];
                if(i == nextmove) newBoard[i] = 1;
                //1と2が反転した盤面を作成
                if(newBoard[i] == 1) inverseBoard[i] = 2;
                else if(newBoard[i] == 2) inverseBoard[i] = 1;
                else inverseBoard[i] = 0;
            }
            
            int v = maxSearch(inverseBoard, alpha, beta, depthLimit - 1); //max探索
            beta = Math.min(v, beta); //評価値がbetaより小さければbeta更新
            if(alpha >= beta) break; //カット
        }
        
        return beta;
    }

    int getEvaluation(int[] board){ //評価値を返すメソッド
        int[] a = {0, 0, 0}; //a1,a2,a3
        int[] b = {0, 0, 0}; //b1,b2,b3
        for (int[] line : Main.lines) { //各ラインに対して
            int[] count = count(line, board);
            for (int j = 0; j < count.length; j++) { //countメソッドの結果を加算
                if(j < 3) a[j] += count[j];
                else b[j - 3] += count[j];
            }
        }
        
        return -1023 * b[2] + 511 * a[2] -63 * b[1] + 31 * a[1] - 15 * b[0] + 7 * a[0]; //評価値を返却
    }

    public int[] count(int[] line, int[] board) { //あるラインにおけるa1~b3をカウントするメソッド
        int[] count = {0, 0, 0, 0, 0, 0}; //a1,a2,a3,b1,b2,b3
        int myStone = 0; //自分の石の数
        int enemyStone = 0; //相手の石の数
        //自分の石の数、相手の石の数をカウント
        for (int i : line) {
            switch (board[i]) {
                case 1: myStone++; break;
                case 2: enemyStone++; break;
                default : break;
            }
        }
        //石の数から、a1~b3のうち加算すべきものをインクリメント
        if(enemyStone == 0 && myStone > 0) count[myStone - 1]++;
        if(myStone == 0 && enemyStone > 0) count[enemyStone + 2]++;

        return count;
    }
}
