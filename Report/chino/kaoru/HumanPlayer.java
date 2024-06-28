package Report.chino.kaoru;

import static Report.chino.kaoru.Color.*;

import java.util.Scanner;

public class HumanPlayer extends Player {
    Scanner scanner = new Scanner(System.in);

    public HumanPlayer() {
        super("Human");
    }

    protected Move search(State state) {
        var moves = state.getMoves();

        int index;
        Outer: while (true) {
            System.out.print("マス座標(0~8)：");
            index = scanner.nextInt();
            for (int i = 0; i < moves.size(); i++) {
                if (index == moves.get(i).index) {
                    index = i;
                    break Outer;
                }

            }
        }
        return moves.get(index);
    }
}
