package cs3500.animator.model.shape;

import java.awt.Color;

/**
 * Represents an implementation of a rectangle from the Shape class, initializing the shape type to
 * be "rectangle". Extends the abstract shape class.
 */
public class Rectangle extends Shape {

  /**
   * Default constructor creates a rectangle object that is black with its position set to (0, 0),
   * its dimensions set to 0x0 and its name set to the given name.
   *
   * @param name String representing the name of the shape
   */
  public Rectangle(String name) {
    this(Color.BLACK, new Position2D(0, 0), 0, 0, name, 0.0, 0);
    this.type = "rectangle";
  }

  /**
   * Construct a Rectangle object with the given name and layer.
   *
   * @param name String representing the name of the shape
   * @param layer integer representing the layer of the shape
   */
  public Rectangle(String name, int layer) {
    this(Color.BLACK, new Position2D(0, 0), 0, 0, name, 0.0, layer);
    this.type = "rectangle";
  }

  /**
   * Constructs a Rectangle with the given properties and shape type "rectangle".
   *
   * @param color    the color of the shape
   * @param position the initial position of the shape
   * @param width    the initial width of the shape
   * @param height   the initial height of the shape
   * @param name     the unique name of the shape
   */
  public Rectangle(Color color, Position2D position, int width, int height, String name) {
    super(color, position, width, height, name, 0);
    this.type = "rectangle";
  }

  /*
  public Rectangle(Color color, Position2D position, int width, int height, String name,
      double heading) {
    super(color, position, width, height, heading, name, 0);
    this.type = "rectangle";
  }
   */

  /**
   * Construct a Rectangle object with the given properties including heading and layer.
   *
   * @param color the color of the shape
   * @param position the initial position of the shape
   * @param width    the initial width of the shape
   * @param height   the initial height of the shape
   * @param name     the unique name of the shape
   * @param heading  the angle of the shape
   * @param layer    the layer for the shape to be drawn on
   */
  public Rectangle(Color color, Position2D position, int width, int height, String name,
      double heading, int layer) {
    super(color, position, width, height, heading, name, layer);
    this.type = "rectangle";
  }

  /**
   * Copy constructor creates a rectangle object with the same properties and associated motions as
   * the given shape.
   *
   * @param shape {@link IShape} to be copied
   */
  public Rectangle(IShape shape) {
    super(shape);
    this.type = "rectangle";
  }

  @Override
  public IShape copy() {
    return new Rectangle(this);
  }
}
