package javashooter.controller;

public class KinematicsController extends ObjectController {

  @Override
  public void updateObject() {
    this.applySpeedVector();
  }

}
