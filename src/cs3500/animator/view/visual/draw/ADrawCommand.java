package cs3500.animator.view.visual.draw;

import cs3500.animator.model.shape.IShape;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * Abstract draw command class to implement the setVisibility() method which is used when creating
 * an SVG representation of a shape and its motions. Any other methods that may be added in the
 * future to a DrawCommand object that are not implementation dependent should be added here. All
 * DrawCommands added should extend this class.
 */
public abstract class ADrawCommand implements DrawCommand {

  // x and y offset
  protected final int x;
  protected final int y;
  protected final int speed;

  /**
   * Abstract class constructor to set the x and y offsets.
   *
   * @param x integer representing the x offset
   * @param y integer representing the y offset
   */
  protected ADrawCommand(int x, int y, int speed) {
    this.x = x;
    this.y = y;
    this.speed = speed;
  }

  /**
   * Method to write the SVG formatted String that set the visibility of the shape at the
   * appropriate time. Does not change based on shape.
   *
   * @param name  String representing the name of the shape
   * @param start Integer representing the start time of the shape
   * @return String with SVG set commands to set the visibility of the shape
   */
  protected String setVisibility(String name, int start) {
    return String.format(
        "<set id=\"%s\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"%d\" "
            + "duration=\"%d\" to=\"hidden\" >\n", name, 0, start / speed) + "</set>\n"
        + String.format(
        "<set id=\"%s\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"%d\" "
            + "to=\"visible\" >\n", name, start / speed) + "</set>\n";
  }

  protected Graphics2D setupRotation(Graphics2D g, IShape shape) {

    AffineTransform transform = new AffineTransform();
    transform.rotate(-Math.toRadians(shape.getHeading()),
        (shape.getPosition().getX() - x) + shape.getWidth() / 2,
        (shape.getPosition().getY() - y) + shape.getHeight() / 2);

    g.transform(transform);

    return g;
  }
}
