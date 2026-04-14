/**
 * This package contains the class {@link javashooter.gameobjects.GameObject} and some subclasses defined for
 * convenience and ease of use. These classes describe dynamic game objects appearing in a game
 * level described by a {@link javashooter.playground.Playground} subclass. {@link javashooter.gameobjects.GameObject} is
 * not usually subclassed except for simplicity to define objects with new properties. Instead, it
 * aggregates instances of {@link javashooter.rendering.Artist}, {@link controller.ObjectController} and
 * {@link javashooter.collider.Collider} which you can subclass separately to modularly change and adapt certain
 * aspects of a game object. See more in the documentation of {@link javashooter.gameobjects.GameObject}.
 */
package spaceinvadersProject.gameobjects;
