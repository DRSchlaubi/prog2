package spaceinvadersProject.controller;

import javashooter.controller.*;

/**
 * Controls background stars. When they touch the bottom of the display they reappear on top.
 */
public class FallingStarController extends ObjectController {
  int rad = 3;

  @Override
  public void updateObject() {
    // System.out.println("+"+this.gameObject.getId()+" HO"
    // +this.gameObject+"/"+this.getPlayground()) ;
    if (this.getY() + rad >= this.getPlayground().preferredSizeY()) {
      this.setY(10);
    }
    applySpeedVector();
  }
}
