import static ap24.Color.BLACK;
import static ap24.Color.WHITE;
import java.util.function.Function;
import ap24.Color;
import ap24.Player;
import ap24.league.Game;
import ap24.league.League;
import ap24.league.OfficialBoard;

class Competition24 {
  final static long TIME_LIMIT_SECONDS = 60;

  public static void main(String args[]) {
    Function<Color, Player[]> builder = (Color color) -> {
      return new Player[] {
        new g24x00.OurPlayer(color),
        new ap24.league.RandomPlayer(color),
      };
    };

    var league = new League(5, builder, TIME_LIMIT_SECONDS);
    league.run();
  }

  public static void singleGame(String args[]) {
    var player1 = new g24x00.OurPlayer(BLACK);
    var player2 = new g24x00.OurPlayer(WHITE);
    var board = new OfficialBoard();
    var game = new Game(board, player1, player2, TIME_LIMIT_SECONDS);
    game.play();
  }
}
