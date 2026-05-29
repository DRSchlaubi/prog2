package testat5;

import javashooter.playground.Playground;
import testat5.playground.Testat5Level;

public class Testat5Game extends javashooter.gameutils.GameLoop  {

    @Override
    public Playground nextLevel(Playground currentLevel) {
        if (currentLevel == null) return new Testat5Level();
        return null;
    }

    static void main(String[] args) {
        Testat5Game game = new Testat5Game();
        game.runGame(args);
    }

}
