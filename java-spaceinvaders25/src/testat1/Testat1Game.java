package testat1;

import javashooter.gameutils.GameLoop;
import javashooter.playground.Playground;

public class Testat1Game extends GameLoop {
    public Testat1Game() {
        System.out.println("Hallo");
    }

    static void main(String[] args) {
        Testat1Game testat1Game = new Testat1Game();
        new ProjektLevel1();
        new ProjektLevel2();
        new ProjektLevel3();

        testat1Game.runGame(args);
    }

    @Override
    public Playground nextLevel(Playground currentLevel) {
        var name = currentLevel == null ? null : currentLevel.getName();
        return switch (name) {
            case null -> new ProjektLevel1();
            case "ProjektLevel1" -> new ProjektLevel2();
            case "ProjektLevel2" -> new ProjektLevel3();
            default -> null;
        };
    }
}
