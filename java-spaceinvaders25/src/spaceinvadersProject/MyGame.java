package spaceinvadersProject;

import javashooter.gameutils.GameLoop;
import javashooter.playground.Playground;
import spaceinvadersProject.playground.Level1;

public class MyGame extends GameLoop {
    static void main(String[] args) {
        new MyGame().runGame(args);
    }

    @Override
    public Playground nextLevel(Playground currentLevel) {
        if (currentLevel == null) {
            return new Level1();
        }
        System.exit(0);
        // If only Java had a Nothing type
        throw new RuntimeException("System.exit did not end VM!");
    }
}
