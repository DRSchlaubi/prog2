package testat4.playground;

import javashooter.collider.RectCollider;
import javashooter.controller.KinematicsController;
import javashooter.gameobjects.GameObject;
import javashooter.gameobjects.RectObject;
import spaceinvadersProject.playground.SpaceInvadersLevel;
import testat4.w8.rendering.FlashArtist;

import java.awt.*;

public class Testat4Level extends SpaceInvadersLevel {

    @Override
    protected void setupInitialState() {
        super.setupInitialState();

        var flyEnemy1 = new RectObject("fly_enemy1", this, 300, 300, 30, 20, 30, 30, Color.BLUE);
        flyEnemy1.setController(new KinematicsController());

        var flyEnemy2 = new RectObject("fly_enemy2", this, 200, 200, 0.2, 0.2, 30, 30, Color.GREEN);
        flyEnemy2.setController(new KinematicsController());

        var flyEnemy3 = new RectObject("fly_enemy3", this, 300, 400, 30, 20, 30, 30, Color.BLUE);
        flyEnemy3.setController(new KinematicsController());

        flyEnemy1.addCollider(new RectCollider("jochen", flyEnemy1, 30, 30));
        flyEnemy2.addCollider(new RectCollider("joche2", flyEnemy2, 30, 30));
        flyEnemy3.addCollider(new RectCollider("joche3", flyEnemy3, 30, 30));

        addObject(flyEnemy1);
        addObject(flyEnemy2);
        addObject(flyEnemy3);

        flyEnemy3.addArtist(new FlashArtist(flyEnemy3, 30, 30));
    }

    @Override
    protected void actionIfEgoCollidesWithEnemy(GameObject enemy, GameObject ego) {
        if (enemy.getName().startsWith("fly_enemy")) IO.println("AUA");
        else super.actionIfEgoCollidesWithEnemy(enemy, ego);
    }

    @Override
    protected void actionIfEnemyIsHit(GameObject e, GameObject shot) {
        if (!e.getName().startsWith("fly_enemy")) super.actionIfEnemyIsHit(e, shot);
    }
}
