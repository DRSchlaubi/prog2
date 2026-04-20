package testat1;

import spaceinvadersProject.playground.SpaceInvadersLevel;

public class ProjektLevel2 extends SpaceInvadersLevel {
    public ProjektLevel2() {
        super();
        System.out.println("XXX");
    }

    @Override
    public String getName() {
        return "ProjektLevel2";
    }

    @Override
    protected String getStartupMessage() {
        return "Level2";
    }

    @Override
    protected int calcNrEnemies() {
        return 5;
    }

    @Override
    protected double calcEnemySpeedX() {
        return 480;
    }

    @Override
    protected double calcEnemySpeedY() {
        return 80;
    }
}
