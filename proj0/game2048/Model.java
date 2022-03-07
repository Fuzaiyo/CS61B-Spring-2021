package game2048;

import java.util.Formatter;
import java.util.Iterator;
import java.util.Observable;


/**
 * The state of a game of 2048.
 *
 * @author TODO: Wang junfu
 */
public class Model extends Observable {
    /**
     * Current contents of the board.
     */
    private Board board;
    /**
     * Current score.
     */
    private int score;
    /**
     * Maximum score so far.  Updated when game ends.
     */
    private int maxScore;
    /**
     * True iff game is ended.
     */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /**
     * Largest piece value.
     */
    public static final int MAX_PIECE = 2048;

    /**
     * A new 2048 game on a board of size SIZE with no pieces
     * and score 0.
     */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /**
     * A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes.
     */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /**
     * Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     * 0 <= COL < size(). Returns null if there is no tile there.
     * Used for testing. Should be deprecated and removed.
     */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /**
     * Return the number of squares on one side of the board.
     * Used for testing. Should be deprecated and removed.
     */
    public int size() {
        return board.size();
    }

    /**
     * Return true iff the game is over (there are no moves, or
     * there is a tile with value 2048 on the board).
     */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /**
     * Return the current score.
     */
    public int score() {
        return score;
    }

    /**
     * Return the current maximum game score (updated at end of game).
     */
    public int maxScore() {
        return maxScore;
    }

    /**
     * Clear the board to empty and reset the score.
     */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /**
     * Add TILE to the board. There must be no Tile currently at the
     * same position.
     */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /**
     * Tilt the board toward SIDE. Return true iff this changes the board.
     * <p>
     * 1. If two Tile objects are adjacent in the direction of motion and have
     * the same value, they are merged into one Tile of twice the original
     * value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     * tilt. So each move, every tile will only ever be part of at most one
     * merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     * value, then the leading two tiles in the direction of motion merge,
     * and the trailing tile does not.
     */
    public boolean tilt(Side side) {
        boolean changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the changed local variable to true.

        //1.变换坐标系,让我们只考虑向上的情况
        board.setViewingPerspective(side);

        //2.当键盘按下"up"时，实际上是对每一列进行操作，因此可以抽象出一个辅助方法专门处理一列上的情况
        for (int col = 0; col < board.size(); col++) {
            //当辅助函数确实进行了棋盘的改动，返回一个bool值来设置changed变量
            if (mergeAtOneclu(col))
                changed = true;
        }

        //3.将坐标系变换回来
        board.setViewingPerspective(Side.NORTH);

        checkGameOver();

        if (changed) {
            setChanged();
        }
        return changed;
    }

    /**
     * 只专注于一行上的棋盘合并情况
     *
     * @param col
     * @return
     */
    public boolean mergeAtOneclu(int col) {
        boolean ifchange = false;

        //1.对于一个格子，检查它是不是空棋子，是的话————它后面的非空棋子就要往前移动 这样做的目的是为了让非空棋子紧凑到一起以方便Merge
        for (int i = board.size() - 1; i > 0; i--) {
            boolean check = compress(i, col);
            if (check)
                ifchange = true;
        }

        //2. 现在非空的棋子都紧凑的贴在一块了，可以根据value值来合并
        if (board.tile(col, board.size() - 1) != null) {

            for (int i = board.size() - 1; i > 0; i--) {
                if (board.tile(col, i - 1) != null && board.tile(col, i).value() == board.tile(col, i - 1).value()) {
                    board.move(col, i, board.tile(col, i - 1));
                    score = score + board.tile(col, i).value();
                    ifchange = true;
                    //每合并一个 就空出来了，要对那个位置以后的棋子进行压缩
                    for (int j = i - 1; j > 0; j--) {
                        compress(j, col);
                    }
                }
            }
        }
        return ifchange;
    }

    public boolean compress(int nullindex, int col) {   //只是单个棋子往前移
        if (board.tile(col, nullindex) == null) {  //找到一个空棋子
            //继续往后找一个非空棋子
            for (int notnullindex = nullindex - 1; notnullindex >= 0; notnullindex--) {
                if (board.tile(col, notnullindex) != null) { //找到了一个非空棋子
                    //把非空棋子移到空棋子位置
                    board.move(col, nullindex, board.tile(col, notnullindex));
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Checks if the game is over and sets the gameOver variable
     * appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /**
     * Determine whether game is over.
     */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /**
     * Returns true if at least one space on the Board is empty.
     * Empty spaces are stored as null.
     */
    public static boolean emptySpaceExists(Board b) {
        //遍历棋盘上的所有位置看看有没有空瓷砖  1.有就说明有空瓷砖存在返回True  2.没有空瓷砖返回false
        int size = b.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Tile tile = b.tile(i, j);
                if (tile == null) {
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        int size = b.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Tile tile = b.tile(i, j);
                if (tile != null && tile.value() == MAX_PIECE) {
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        boolean isEmpty = emptySpaceExists(b);
        //成功情况1：至少有一个空格
        if (isEmpty)
            return true;

        //成功情况2： 注意！ 到这已经棋盘全满了，棋盘没有空格了
        for (int i = 0; i < b.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                Tile tile = b.tile(i, j);
                //对于每一个位置，依次检查它的上下左右是否有相邻元素
                //1.检查上方向(存在往上的方向的话)  以下同理
                if (i != 0) {
                    Tile tile1 = b.tile(i - 1, j);
                    if (tile1.value() == tile.value())
                        return true;
                }

                //2.检查下方向
                if (i != b.size() - 1) {
                    Tile tile1 = b.tile(i + 1, j);
                    if (tile1.value() == tile.value())
                        return true;
                }

                //3.检查左方向
                if (j != 0) {
                    Tile tile1 = b.tile(i, j - 1);
                    if (tile1.value() == tile.value())
                        return true;
                }

                //4.检查右方向
                if (j != b.size() - 1) {
                    Tile tile1 = b.tile(i, j + 1);
                    if (tile1.value() == tile.value())
                        return true;
                }

            }
        }
        return false;
    }


    @Override
    /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
