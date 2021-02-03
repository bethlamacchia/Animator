package cs3500.animator.model.shape;

import java.util.Objects;

/**
 * This class represents a 2D position, which has an x and a y coordinate.
 */
public final class Position2D {

  private double x;
  private double y;

  /**
   * Initialize this object to the specified position.
   *
   * @param x the x-coordinate of the position
   * @param y the y-coordinate of the position
   */
  public Position2D(double x, double y) {
    this.setX(x);
    this.setY(y);
  }

  /**
   * Copy constructor.
   *
   * @param v the 2Dposition
   */
  public Position2D(Position2D v) {
    this.setX(v.x);
    this.setY(v.y);
  }

  /**
   * get the x coordinate of this position.
   *
   * @return the x coordinate of this position.
   */
  public double getX() {
    return x;
  }

  /**
   * Set the x coordinate of this object.
   *
   * @param x the x coordinate of this position.
   */
  public void setX(double x) {
    this.x = x;
  }

  /**
   * get the y coordinate of this position.
   *
   * @return the y coordinate of this position.
   */
  public double getY() {
    return y;
  }

  /**
   * Set the y coordinate of this object.
   *
   * @param y the y coordinate of this position.
   */
  public void setY(double y) {
    this.y = y;
  }

  @Override
  public boolean equals(Object a) {
    if (this == a) {
      return true;
    }
    if (!(a instanceof Position2D)) {
      return false;
    }

    Position2D that = (Position2D) a;

    return ((Math.abs(this.x - that.x) < 0.01)
        && (Math.abs(this.y - that.y) < 0.01));
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.x, this.y);
  }
}