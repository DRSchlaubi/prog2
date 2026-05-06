package testat1;

import javashooter.gameobjects.GameObject;
import spaceinvadersProject.playground.SpaceInvadersLevel;

public class ProjektLevel3 extends SpaceInvadersLevel {
    public ProjektLevel3() {
        super();
        System.out.println("XXX");
    }

    @Override
    public String getName() {
        return "ProjektLevel3";
    }

    @Override
    protected String getStartupMessage() {
        return "Level3";
    }

    @Override
    protected int calcNrEnemies() {
        return 10;
    }

    @Override
    protected double calcEnemySpeedX() {
        return 800;
    }

    @Override
    protected double calcEnemySpeedY() {
        return 140;
    }

    @Override
    protected void actionIfEgoCollidesWithEnemy(GameObject enemy, GameObject ego) {
        if (enemy.getName().startsWith("fly_")) {
            System.out.println("dsa");
        } else {
            System.out.println("dsa");
        }
    }
}
