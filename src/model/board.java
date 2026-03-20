package model;

import java.util.*;

public class board {

    private List<Square> squares;

    public Board() {
        squares = new ArrayList<>();
        generateBoard();
    }

    private void generateBoard() {
        for (int i = 0; i < 50; i++) {
            squares.add(SquareFactory.createSquare(i));
        }
    }

    public Square getSquare(int index) {
        return squares.get(index);
    }

    public int getSize() {
        return squares.size();
    }

    public List<Square> getSquares() {
        return squares;
    }
}