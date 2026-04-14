package javashooter.controller;

/**
 * Controls an object that is deleted after a lifetime specified in the constructor, and/or when it
 * leaves the display.
 */
public class LimitedTimeController extends ObjectController {
  protected int rad = 3;
  protected double g0 = -1;
  protected double duration = 0;

  /**
   * Constructor.
   * 
   * @param g0 int initial game time at creation
   * @param duration int duration in seconds
   */
  public LimitedTimeController(double g0, double duration) {
    this.g0 = g0;
    this.duration = duration;
  }
  
  public boolean checkBorder()  {
    return ((gameObject.getY() >= getPlayground().preferredSizeY()) || 
    		(gameObject.getY() < 0) || 
    		(gameObject.getX() >= getPlayground().preferredSizeX()) ||
    		(gameObject.getX() < 0)) ;            
  }

  @Override
  public void updateObject() {
    double gameTime = this.getPlayground().getGameTime();
    applySpeedVector();

    if   (checkBorder() || ((gameTime - g0) > duration)) {
      // LinkedList<String> d = (LinkedList<String>) javashooter.playground.getFlag("delete");
      // d.add(gameObject.getId());
      getPlayground().deleteObject(this.gameObject.getId());
      //System.out.println("deleted" + this.gameObject.getId()+ " "+ (gameTime-g0)+" --vs--"+duration) ;
    }
  }

}
