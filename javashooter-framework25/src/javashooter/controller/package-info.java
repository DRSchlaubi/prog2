/**
 * The javashooter.controller package contains Object controllers that govern an objects behavior, without being
 * involved in its collision or drawing properties. Object controllers react to user actions/inputs
 * and govern for example the movement of {@link javashooter.gameobjects.GameObject} instances. They can access
 * their parent objects' properties and, by indirection, the properties and methods of the
 * associated level. <br>
 * Controllers should be designed in such a way that they implement behavior that is
 * level-independent. All level-dependent behavior should be implemented in
 * {@link javashooter.playground.Playground#applyGameLogic}.
 */
package javashooter.controller;
