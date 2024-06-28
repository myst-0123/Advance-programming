import java.util.*;
import java.util.Scanner;

public class Main {
    static int[][] lines = {
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
        {0, 4, 8}, {2, 4, 6}
    };

    public static void main(String[] args) {
        int mode = 0;
        Scanner sc = new Scanner(System.in);
        System.out.println("===== 三目並べ =====\n");
        while(true) {
            System.out.println("対戦相手を選択してください\n");
            System.out.println("0 : 自分で入力, 1 : ランダムCPU, 2 : 自分のAIを使用\n");
            String str = sc.next();
            if(str.equals("0")) {
                mode = 0;
            }
            else if(str.equals("1")) {
                mode = 1;
            }
            else if(str.equals("2")) {
                mode = 2;
            }
            Game(mode);

            System.out.println("もう一度対戦しますか (y / n)\n");
            str = sc.next();

            if(str.equals("n")) {
                System.out.println("終了します\n");
                break;
            }
        }
    }

    static void Game(int gameMode) {
        int turnNum = 0;
        int result = 0;
        // 現在の盤面
        int[] board = new int[9];
        int[] inverseBoard = new int[9];
        // ゲーム中 Flag
        boolean inGame = true;
        // My Turn => True, Enemy Turn => False
        boolean isMyTurn = generateFirstMovePlayer();
        Scanner sc = new Scanner(System.in);

        // set Player Class
        // =====================================================
        // =====================================================
        // 
        // ↓↓↓ ここを変更 ↓↓↓
        var player = new HumanPlayer();
        // ↑↑↑ ここを変更 ↑↑↑
        //  
        // =====================================================
        // =====================================================

        var enemyPlayer = new HumanPlayer();

        while(inGame) {
            turnNum++;

            // 自分のターン
            if(isMyTurn) {
                int move = -1;
                while(true) {
                    move = player.getNextMove(board);
                    if(move != -1 && board[move] == 0)
                        break;
                }
                board[move] = 1;
                inverseBoard[move] = 2;
            }
            // 相手のターン
            else
            {
                int move = -1;
                while(true) {
                    if(gameMode == 0){
                        
                        try{
                            System.out.println("手を入力してください。(右上:0・左下:8)");
                            String str = sc.next();
                            move = Integer.parseInt(str);
                        }
                        catch (NumberFormatException ex){ }
                    }
                    else if(gameMode == 1) {
                        Random r = new Random();
                        move = r.nextInt(9);
                    }
                    else if(gameMode == 2) {
                        move = enemyPlayer.getNextMove(inverseBoard);
                    }

                    // 正しい入力ならば無限ループから解除
                    if(move != -1 && board[move] == 0)
                        break;
                }

                // 盤面に入力、相手用に反転盤面も作成
                inverseBoard[move] = 1;
                board[move] = 2;
            }

            //  ターンを変更する
            isMyTurn = !isMyTurn;

            // 現在の盤面の表示
            ShowBoard(board, turnNum);

            // 勝敗判定
            // 0 : まだ決着はついていない
            // 1 : 自分の勝利
            // 2 : 相手の勝利
            result = checkFinish(board);
            // resultが0より大きいまたは、すべてのマスが埋まることで終了
            inGame = !((result > 0) || !isContinue(board));

            try {
                Thread.sleep(1000);
            } 
            catch(InterruptedException e) { }
        }
        if(result == 1)
            System.out.println("===== WIN =====");
        else if(result == 2)
            System.out.println("===== LOSE=====");
        else
            System.out.println("===== DRAW=====");
    }

    static int checkFinish(int[] board) {
        for(int[] line : lines) {
            if(board[line[0]] == board[line[1]] && board[line[0]] == board[line[2]])
                return board[line[0]];
        }
        return 0;
    }

    static boolean isContinue(int[] board) {

        for(int i : board) {
            if(i == 0)
                return true;
        }
        return false;
    }

    static boolean generateFirstMovePlayer() {
        Random r = new Random();

        return r.nextInt(100) % 2 == 0;
    }

    static void ShowBoard(int[] board, int turnNum)
    {
        System.out.println(String.format("=== Turn %2d ===", turnNum));
        
        String s = "";
        for(int i = 0; i < board.length; i++) {
            if(board[i] == 1)
                s += " O ";
            else if(board[i] == 2)
                s += " X ";
            else
                s += " - ";
            if(i % 3 == 2)
                s += "\n";
            else
                s += " | ";
        }
        System.out.println(s);
    }
}