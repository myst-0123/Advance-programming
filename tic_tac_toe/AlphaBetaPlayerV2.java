import static java.lang.Float.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class AlphaBetaPlayerV2 implements Player {
    float eval;
    int depthLimit;
    int move;

    public AlphaBetaPlayerV2(int depthLimit) {
        // super("AlphaBeta" + depthLimit);
        // this.eval = eval;
        this.depthLimit = depthLimit;
    }

    public int getNextMove(int[] board) {
        int rest = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0)
                rest++;
        }
        System.out.println(rest);
        // if (rest % 2 == 1) {
        // System.out.println("max");
        maxSearch(board, NEGATIVE_INFINITY, POSITIVE_INFINITY, this.depthLimit, true);
        // } else
        // minSearch(board, NEGATIVE_INFINITY, POSITIVE_INFINITY, this.depthLimit,
        // true);
        return move;
    }

    float maxSearch(int[] board, float alpha, float beta, int depthLimit, boolean isMyturn) {
        if (isTerminal(board, depthLimit)) {
            return getEvaluation(board);
        }

        var v = NEGATIVE_INFINITY;
        List<Integer> list = getMoves(board);
        sort(board, list, !isMyturn);
        for (int move : list) {
            int[] newBoard = board.clone();
            // int[] inverseBoard = new int[9];
            if (isMyturn)
                newBoard[move] = 1;
            else
                newBoard[move] = 2;
            // for (int i = 0; i < board.length; i++) {
            // if (newBoard[i] == 1)
            // inverseBoard[i] = 2;
            // else if (newBoard[i] == 2)
            // inverseBoard[i] = 1;
            // else
            // inverseBoard[i] = 0;
            // }

            float v0 = minSearch(newBoard, alpha, beta, depthLimit - 1, !isMyturn);

            if (depthLimit == this.depthLimit && v0 > v) {
                this.move = move;
            }

            if (beta <= v0) {
                return v0;
            }
            v = Math.max(v, v0);
            alpha = Math.max(alpha, v0);
        }
        return v;
    }

    float minSearch(int[] board, float alpha, float beta, int depthLimit, boolean isMyturn) {
        if (isTerminal(board, depthLimit)) {
            return getEvaluation(board);
        }

        var v = POSITIVE_INFINITY;
        List<Integer> list = getMoves(board);
        sort(board, list, !isMyturn);
        for (int move : list) {
            int[] newBoard = board.clone();
            // int[] inverseBoard = new int[9];
            if (isMyturn)
                newBoard[move] = 1;
            else
                newBoard[move] = 2;
            // for (int i = 0; i < board.length; i++) {
            // if (newBoard[i] == 1)
            // inverseBoard[i] = 2;
            // else if (newBoard[i] == 2)
            // inverseBoard[i] = 1;
            // else
            // inverseBoard[i] = 0;
            // }

            var v0 = maxSearch(newBoard, alpha, beta, depthLimit - 1, !isMyturn);
            if (depthLimit == this.depthLimit && v0 < v) {
                this.move = move;
            }

            if (alpha >= v0) {
                return v0;
            }
            v = Math.min(v, v0);
            beta = Math.min(beta, v0);
        }
        return v;
    }

    void sort(int[] board, List<Integer> list, boolean isMyturn) { // 評価関数によりソート
        list.sort(Comparator.comparing(move -> {
            int[] newBoard = board.clone();
            newBoard[(int) move] = isMyturn ? 1 : 2;
            return getEvaluation(newBoard);
        }).reversed());
        // nodeCount += list.size();
    }

    boolean isTerminal(int[] board, int depthLimit) {
        return isGoal(board) || depthLimit <= 0;
    }

    public boolean isGoal(int[] board) {
        if (winner(board) != 0)
            return true;

        for (int i = 0; i < board.length; i++)
            if (board[i] == 0)
                return false;

        return true;
    }

    public List<Integer> getMoves(int[] board) {
        List<Integer> moves = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0)
                moves.add(i);
        }
        return moves;
    }

    public int getEvaluation(int[] board) {
        int[] a = { 0, 0, 0 };
        int[] b = { 0, 0, 0 };
        for (int[] line : Main.lines) {
            int[] count = count(line, board);
            for (int j = 0; j < count.length; j++) {
                if (j < 3)
                    a[j] += count[j];
                else
                    b[j - 3] += count[j];
            }
        }
        return -1025 * b[2] + 511 * a[2] - 63 * b[1] + 31 * a[1] - 15 * b[0] + 7 * a[0];
    }

    public int winner(int[] board) {
        int[] a = { 0, 0, 0 };
        int[] b = { 0, 0, 0 };
        for (int[] line : Main.lines) {
            int[] count = count(line, board);
            for (int j = 0; j < count.length; j++) {
                if (j < 3)
                    a[j] += count[j];
                else
                    b[j - 3] += count[j];
            }
        }
        if (a[2] > 0)
            return 1;
        if (b[2] > 0)
            return 2;
        return 0;
    }

    public int[] count(int[] line, int[] board) { // 同色の石のみからなるラインを算出
        int[] ab = { 0, 0, 0, 0, 0, 0 };
        int[] xyz = { 0, 0, 0 };
        for (int i = 0; i < line.length; i++) {
            switch (board[line[i]]) {
                case 1:
                    xyz[i] = 1;
                    break;
                case 2:
                    xyz[i] = -1;
                    break;
                default:
                    break;
            }
        }
        int sum = xyz[0] + xyz[1] + xyz[2];
        if (sum == 3)
            ab[2] += 1;
        if (sum == -3)
            ab[5] += 1;
        if (xyz[0] * xyz[1] * xyz[2] == 0) {
            if (sum == 2)
                ab[1] += 1;
            if (sum == 1)
                ab[0] += 1;
            if (sum == -2)
                ab[4] += 1;
            if (sum == -1)
                ab[3] += 1;
        }
        return ab;
    }
}
