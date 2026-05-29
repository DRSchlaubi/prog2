package testat2;

import javashooter.gameutils.GameLoop;
import javashooter.playground.Playground;
import testat2.playground.BossLevel25;

public class Testat2 extends GameLoop {
    @Override
    public Playground nextLevel(Playground currentLevel) {
        return (currentLevel == null) ? new BossLevel25() : null;
    }

    static void main(String[] args) {
        new Testat2().runGame(args);
    }
}
