package myplayer;

import static ap24.Color.BLACK;
import static ap24.Color.WHITE;

public class CalcWinRate {
    public static void main(String[] args) {
        int winNumBlack = 0;
        int winNumWhite = 0;
        for (int i = 0; i < 100; i++) {
            var player1 =  new myplayer.MyPlayer(BLACK);
            var player2 =  new myplayer.RandomPlayer(WHITE);
            var board = new MyBoard();
            var game = new MyGame(board, player1, player2);
            game.play();
            if(game.getWinner(game.board) == player1) winNumBlack++;
        }

        for (int i = 0; i < 100; i++) {
            var player1 =  new myplayer.MyPlayer(WHITE);
            var player2 =  new myplayer.RandomPlayer(BLACK);
            var board = new MyBoard();
            var game = new MyGame(board, player2, player1);
            game.play();
            if(game.getWinner(game.board) == player1) winNumWhite++;
        }
        
        System.out.println("\nwin rate:" + (float)(winNumBlack + winNumWhite) / 2. + "%");
        System.out.println("win rate(black):" + (float)winNumBlack + "%");
        System.out.println("win rate(white):" + (float)winNumWhite + "%");
    }
}
