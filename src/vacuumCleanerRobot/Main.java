package vacuumCleanerRobot;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Game game = new Game();
        Thread gameThread = new Thread(game);
        gameThread.run();
    }
}