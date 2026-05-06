package testat4;

import javashooter.playground.Playground;
import testat4.playground.Testat4Level;

public class Testat4Game extends javashooter.gameutils.GameLoop  {

    @Override
    public Playground nextLevel(Playground currentLevel) {
        if (currentLevel == null) return new Testat4Level();
        return null;
    }

    static void main(String[] args) {
        Testat4Game game = new Testat4Game();
        game.runGame(args);
    }

}
