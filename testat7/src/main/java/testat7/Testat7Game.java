package testat7;

import javashooter.playground.Playground;

public class Testat7Game extends javashooter.gameutils.GameLoop  {

    @Override
    public Playground nextLevel(Playground currentLevel) {
        if (currentLevel == null) return new Testat7Level();
        return null;
    }

    static void main(String[] args) {
        Testat7Game game = new Testat7Game();
        game.runGame(args);
    }

}
