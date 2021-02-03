package cs3500.animator.model.shape;

import java.awt.Color;

/**
 * Represents an implementation of an ellipse from the Shape class, initializing the shape type to
 * be "ellipse". Extends the abstract shape class.
 */
public class Ellipse extends Shape {

  /**
   * Default constructor creates an ellipse object that is black with its position set to (0, 0),
   * its dimensions set to 0x0 and its name set to the given name.
   *
   * @param name String representing the name of the shape
   */
  public Ellipse(String name) {
    this(Color.BLACK, new Position2D(0, 0), 0, 0, name, 0.0, 0);
    this.type = "ellipse";
  }

  /**
   * Default constructor creates an ellipse object that is black with its position set to (0, 0),
   * its dimensions set to 0x0 and its name set to the given name.
   *
   * @param name String representing the name of the shape
   */
  public Ellipse(String name, int layer) {
    this(Color.BLACK, new Position2D(0, 0), 0, 0, name,
        0.0, layer);
  }

  /**
   * Constructor for the Ellipse class. Creates a shape with the type "ellipse", passing the rest of
   * the parameters to the super constructor.
   *
   * @param color    the color of the ellipse
   * @param position the position of the ellipse
   * @param width    the width of the ellipse
   * @param height   the height of the ellipse
   * @param name     the unique name of the shape
   */
  public Ellipse(Color color, Position2D position, double width, double height, String name) {
    super(color, position, width, height, name, 0);
    this.type = "ellipse";
  }

  /**
   * Construct an Ellipse object with the passed in values including heading and layer.
   *
   * @param color    the color of the ellipse
   * @param position the position of the ellipse
   * @param width    the width of the ellipse
   * @param height   the height of the ellipse
   * @param name     the unique name of the shape
   * @param heading  the angle that the shape is pointing
   * @param layer    the layer for the shape to be drawn on
   */
  public Ellipse(Color color, Position2D position, double width, double height, String name,
      double heading, int layer) {
    super(color, position, width, height, heading, name, layer);
    this.type = "ellipse";
  }

  /**
   * Copy constructor for the ellipse class. Creates an ellipse identical to this one.
   *
   * @param shape the shape to create a copy of.
   */
  public Ellipse(IShape shape) {
    super(shape);
    this.type = "ellipse";
  }

  @Override
  public IShape copy() {
    return new Ellipse(this);
  }
}
