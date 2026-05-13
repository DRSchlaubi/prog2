package testat5;

import javashooter.gameutils.GameLoop;
import javashooter.playground.Playground;
import testat5.playground.Testat5Level;

public class Testat5Game extends GameLoop {
    @Override
    public Playground nextLevel(Playground currentLevel) {
        return currentLevel == null ? new Testat5Level() : null;
    }

    static void main(String[] args) {
        new Testat5Game().runGame(args);
    }
}
