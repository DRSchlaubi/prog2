package spaceinvadersProject.controller;
import javashooter.controller.*;

public class SimpleShotController extends ObjectController {
  int rad = 3;

  @Override
  public void updateObject() {
    if (gameObject.getY() < 0) {
      // LinkedList<String> deleteList = (LinkedList<String>) javashooter.playground.getFlag("delete");
      // deleteList.add(gameObject.getId());
      getPlayground().deleteObject(this.gameObject.getId());
    } else {
      applySpeedVector();
    }
  }
}
