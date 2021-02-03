package cs3500.animator.model.frame;

import cs3500.animator.model.shape.IShape;
import cs3500.animator.model.shape.Position2D;
import java.awt.Color;
import java.util.Objects;

/**
 * Class representing a frame of an object in an animation with an associated time, position, color,
 * width and height of the object.
 */
public class Frame implements IFrame {

  private final int t;
  private final String name;
  private final Position2D pos;
  private final Color color;
  private final double w;
  private final double h;
  private final double heading;

  /**
   * Construct a new key frame object with the passed in parameters.
   *
   * @param t       positive integer representing the time of the frame in ticks
   * @param name    String representing the name of the shape
   * @param pos     {@link Position2D} representing the position of the object at that time
   * @param color   {@link Color} representing the color of the object at that time
   * @param w       non-negative integer representing the width of the frame in pixels
   * @param h       non-negative integer representing the height of the frame in pixels
   * @param heading the heading of the frame in degrees
   * @throws NullPointerException if name, pos or color are null
   */
  public Frame(int t, String name, Position2D pos, Color color, double w, double h,
      double heading) {
    Objects.requireNonNull(name, "Cannot pass a null name to a frame!");
    Objects.requireNonNull(pos, "Cannot pass a null position to a frame!");
    Objects.requireNonNull(color, "Cannot pass a null color to a frame!");

    this.t = t;
    this.pos = pos;
    this.color = color;
    this.w = w;
    this.h = h;
    this.name = name;
    this.heading = heading;
  }

  /**
   * Convenience constructor that doesn't require a heading, defaulting the heading to 0.
   *
   * @param t     positive integer representing the time of the frame in ticks
   * @param name  String representing the name of the shape
   * @param pos   {@link Position2D} representing the position of the object at that time
   * @param color {@link Color} representing the color of the object at that time
   * @param w     non-negative integer representing the width of the frame in pixels
   * @param h     non-negative integer representing the height of the frame in pixels
   */
  public Frame(int t, String name, Position2D pos, Color color, double w, double h) {
    this(t, name, pos, color, w, h, 0);
  }

  @Override
  public String toString() {
    return String
        .format("%d %d %d %d %d %d %d %d", t, (int) pos.getX(), (int) pos.getY(), (int) w, (int) h,
            color.getRed(),
            color.getGreen(),
            color.getBlue());
  }

  @Override
  public void apply(IShape shape) {
    shape.setColor(color);
    shape.setWidth(w);
    shape.setHeight(h);
    shape.setPosition(pos);
    shape.setHeading(heading);
  }

  @Override
  public double getHeading() {
    return this.heading;
  }

  @Override
  public int getTime() {
    return this.t;
  }

  @Override
  public Position2D getPos() {
    return this.pos;
  }

  @Override
  public Color getColor() {
    return this.color;
  }

  @Override
  public double getWidth() {
    return this.w;
  }

  @Override
  public double getHeight() {
    return this.h;
  }

  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Test equality using the toString() method, two identical frames will always return the same
   * value.
   */
  @Override
  public boolean equals(Object that) {
    if (that instanceof Frame) {
      return that.toString().equals(this.toString());
    } else {
      return false;
    }
  }

  /**
   * Use the Objects.hash() method to hash the toString() method to a value.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.toString());
  }
}
