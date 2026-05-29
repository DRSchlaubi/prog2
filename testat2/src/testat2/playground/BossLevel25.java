package testat2.playground;

import javashooter.gameobjects.GameObject;
import spaceinvadersProject.playground.SpaceInvadersLevel;

import java.awt.*;

public class BossLevel25 extends SpaceInvadersLevel {
    int hitCnt = 0;

    @Override
    protected int calcNrEnemies() {
        return 1;
    }

    @Override
    protected double calcEnemySpeedX() {
        return 4.5;
    }

    @Override
    protected double calcEnemyShotProb() {
        return 0.01;
    }

    @Override
    protected String getStartupMessage() {
        return "BossLevel!!";
    }

    @Override
    protected void actionIfEnemyIsHit(GameObject e, GameObject shot) {
        if (hitCnt++ == 20) super.actionIfEnemyIsHit(e, shot);
        deleteObject(shot.getId());
    }

    @Override
    protected GameObject createEnemyShot(GameObject enemy) {
        var gO = super.createEnemyShot(enemy);
        if (gO == null) return null;

        var ego = getObject("ego");

        var S = 400;
        var dx = ego.getX() - enemy.getX();
        var dy = ego.getY() - enemy.getY();
        var n = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        var vx = dx * S / n;
        var vy = dy * S / n;

        gO.setVX(vx);
        gO.setVY(vy);

        return gO;
    }

    @Override
    public void redrawLevel(Graphics2D g2) {
        super.redrawLevel(g2);
        drawText(g2, 20, "Hit-count: " + hitCnt + "/ 25", 120, 20, Color.YELLOW);
    }
}
