package testat1;

import javashooter.gameobjects.GameObject;
import spaceinvadersProject.playground.SpaceInvadersLevel;

public class ProjektLevel1 extends SpaceInvadersLevel {
    public ProjektLevel1() {
        super();
        System.out.println("XXX");
    }

    @Override
    public String getName() {
        return "ProjektLevel1";
    }

    @Override
    protected String getStartupMessage() {
        return "Level1";
    }

    @Override
    protected int calcNrEnemies() {
        return 1;
    }

    @Override
    protected double calcEnemySpeedX() {
        return 160;
    }

    @Override
    protected double calcEnemySpeedY() {
        return 80;
    }

    @Override
    protected void actionIfEnemyIsHit(GameObject e, GameObject shot) {
        super.actionIfEnemyIsHit(e, shot);

        System.out.println("AUA!");
    }
}
