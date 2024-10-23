package org.example.battleship.server;

import java.util.Random;

public class PlayerField {
    private final boolean[][] shots = new boolean[10][10];
    private final boolean[][] ships = new boolean[10][10];
    private final String playerName;

    PlayerField(String playerName) {
        this.playerName = playerName;
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            ships[x][y] = true;
        }
    }

    boolean shoot(int x, int y) throws Exception {
        if (shots[x][y]) {
            throw new Exception("Уже стрелял");
        }
        shots[x][y] = true;
        return ships[x][y];
    }

    String getPlayerName() {
        return playerName;
    }
}
