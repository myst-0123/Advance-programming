package g24x00;

import static ap24.Color.BLACK;
import static ap24.Color.WHITE;
import ap24.league.*;

public class CalcWinRateOurPlayer {
    public static void main(String[] args) {
        int winNumBlack = 0;
        int winNumWhite = 0;
        for (int i = 0; i < 100; i++) {
            var player1 =  new g24x00.OurPlayer(BLACK);
            var player2 =  new RandomPlayer(WHITE);
            var board = new OurBoard();
            var game = new Game(board, player1, player2, 60);
            game.play();
            if(game.getWinner(game.getBoard()) == player1) winNumBlack++;
            System.out.println("\nwin rate:" + (float)(winNumBlack + winNumWhite) / (float)(i + 1) * 100. + "%(" + (i + 1) + ")");
        }

        for (int i = 0; i < 100; i++) {
            var player1 =  new OurPlayer(WHITE);
            var player2 =  new RandomPlayer(BLACK);
            var board = new OurBoard();
            var game = new Game(board, player2, player1, 60);
            game.play();
            if(game.getWinner(game.getBoard()) == player1) winNumWhite++;
            System.out.println("\nwin rate:" + (float)(winNumBlack + winNumWhite) / (101. + (float)i) * 100. + "%(" + (i + 101) + ")");
        }
        
        System.out.println("\nwin rate:" + (float)(winNumBlack + winNumWhite) / 2. + "%");
        System.out.println("win rate(black):" + (float)winNumBlack + "%");
        System.out.println("win rate(white):" + (float)winNumWhite + "%");
    }
}
