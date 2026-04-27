package testat2;

import javashooter.gameutils.GameLoop;
import javashooter.playground.Playground;

public class EigenesSpielT2 extends GameLoop {
    @Override
    public Playground nextLevel(Playground currentLevel) {
        if (currentLevel == null) return new ExperimentierLevel();
        return null;
    }

    static void main(String[] args) {
        new EigenesSpielT2().runGame(args);
    }
}
