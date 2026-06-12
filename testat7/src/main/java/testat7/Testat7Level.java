package testat7;

import javashooter.controller.LimitedTimeController;
import javashooter.gameobjects.GameObject;
import javashooter.gameobjects.RectObject;
import javashooter.rendering.TextArtist;
import spaceinvadersProject.playground.SpaceInvadersLevel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Testat7Level extends SpaceInvadersLevel {
    private final List<String> hitEnemies = new ArrayList<>();

    @Override
    protected void actionIfEnemyIsHit(GameObject e, GameObject shot) {
        e.addArtist(new TextArtist(e, "X", 20, Color.RED));
        hitEnemies.add(e.getId());
        deleteObject(shot.getId());
    }

    @Override
    protected void actionIfEgoCollidesWithCollect(GameObject collect, GameObject ego) {
        super.actionIfEgoCollidesWithCollect(collect, ego);
        var iterator = hitEnemies.listIterator();
        while (iterator.hasNext()) {
            var enemyID = iterator.next();
            deleteObject(enemyID);
            iterator.remove();
            var enemy = getObject(enemyID);
            if (enemy == null) continue;
            var eX = enemy.getX();
            var eY = enemy.getY();


            for (int i = 0; i < 20; i++) {
                var rV = ThreadLocalRandom.current().nextDouble(0, 40);
                var vX = (ThreadLocalRandom.current().nextBoolean() ? 1 : -1) * rV;
                var vY = (ThreadLocalRandom.current().nextBoolean() ? 1 : -1) * Math.sqrt(1600 - (rV * rV));

                var rectObject = new RectObject(enemyID.replace("n", "b") + "explosion" + i, this, eX, eY, vX, vY, 2,2,  Color.RED);
                rectObject.setController(new LimitedTimeController(getGameTime(), 5));
                addObject(rectObject);
            }
        }

    }
}
