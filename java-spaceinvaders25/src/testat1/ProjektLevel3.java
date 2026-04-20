package testat1;

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
}
