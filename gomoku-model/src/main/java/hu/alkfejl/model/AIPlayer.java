package hu.alkfejl.model;

import java.util.Random;

public class AIPlayer extends Player {
    public AIPlayer(String name, boolean isActive, PlayerSign sign) {
        super(name, isActive, sign);
    }

    public Pair<Integer, Integer> move(int boardHeight, int boardWidth){
        Random rand = new Random();
        return new Pair<>(rand.nextInt(boardHeight), rand.nextInt(boardWidth));
    }
}
