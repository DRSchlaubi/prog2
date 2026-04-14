package spaceinvadersProject.controller;

import javashooter.controller.*;
import javashooter.controller.ObjectController;
import javashooter.gameutils.*;
import spaceinvadersProject.playground.SpaceInvadersLevel;

/**
 * This class controls the space invaders.
 */
public class EnemyController extends ObjectController {
  @Override
  public void updateObject() {
    // System.out.println("updatre"+gameObject.getId());
    if ((gameObject.getX() > this.getPlayground().preferredSizeX() * 0.9) && (gameObject.getVX() > 0)) {
      // System.out.println("toleft!"+gameObject.getX());
      gameObject.setVX(-this.getVX());
    }
    if ((gameObject.getX() < this.getPlayground().preferredSizeX() * 0.1) && (gameObject.getVX() < 0)) {
      // System.out.println("toright!"+gameObject.getX());
      gameObject.setVX(-this.getVX());
    }

    // if it reaches the bottom, delete it and deduct points
    if (gameObject.getY() >= this.getPlayground().preferredSizeY()) {
      this.getPlayground().deleteObject(gameObject.getId());
      // add to points counter
      this.getPlayground().incrementFlag("/points", true, 0, -200);
    }

    applySpeedVector();
  }
}
