package cs3500.animator.model.motion;

import cs3500.animator.model.shape.IShape;


/**
 * Represents a motion that changes the angle of rotation of the shape from the starting heading to
 * ending heading. Can change the position incrementally over time by calculating the change per
 * tick.
 */
public class Rotation extends AMotion {

  private final double heading1;
  private final double heading2;

  private final double d;


  /**
   * The constructor for the abstract motion class.
   *
   * @param start the start time of this motion
   * @param end   the end time of this motion
   * @param id    the shape id
   * @throws IllegalArgumentException if start or end times are negative or if start is not greater
   *                                  than end time
   */
  public Rotation(int start, int end, String id, double heading1, double heading2) {
    super(start, end, id);
    this.heading1 = heading1;
    this.heading2 = heading2;

    if (start == end) {
      this.d = (this.heading1 - this.heading2);
    } else {
      this.d = (this.heading1 - this.heading2) / (start - end);
    }
  }

  @Override
  public void apply(IShape shape, int ticks) {
    if (ticks >= super.start && ticks <= super.end) {
      int timeDif = (ticks - super.start);
      double newHeading = this.heading1 + (d * timeDif);
      shape.setHeading(newHeading);
    }
  }

  @Override
  public boolean overlaps(Motion m) {
    if (m instanceof Rotation) {
      return checkOverlap(m) && (heading1 != heading2);
    }
    // other is either equal start time or not a MoveMotion, don't need to worry about overlap
    return false;
  }

  @Override
  public boolean isCompatibleWith(IShape shape) {
    if (shape == null) {
      throw new IllegalArgumentException("Cannot apply a motion to a null shape");
    } else if (!(shape.getName().equals(this.getId()))) {
      throw new IllegalArgumentException("Motions can only operate on one shape");
    }
    if (heading1 == shape.getHeading() || this.start < shape.getStartTime()) {
      return true;
    } else {
      throw new IllegalArgumentException(String.format(
          "Start heading of motion does not equal heading of the shape %s at t = %d",
          shape.getName(), getStart()));
    }
  }

  @Override
  public String toSVG(String shapeType, int speed) {
    return "";
  }

  /**
   * Convert the rotation to SVG output.
   *
   * @param shapeType the type of shape
   * @param speed     the speed of the animation
   * @param x         the x position of the shape
   * @param y         the y position of the shape
   * @return a string representing the SVG text of the rotation.
   */
  public String toSVG(String shapeType, int speed, double x, double y) {
    // somehow need to know starting and ending coordinates
    String from =
        (int) heading1 + " " + (int) x + " " + (int) y;
    String to =
        (int) heading2 + " " + (int) x + " " + (int) y;
    switch (shapeType) {
      case "rectangle":
        return String.format(
            "<animateTransform attributeType=\"xml\" type=\"rotate\" begin=\"%.2f\" dur=\"%.2f\" "
                + "attributeName=\"transform\" "
                + "from=\"" + from + "\" to=\"" + to + "\" fill=\"freeze\"/>\n",
            (double) super.start / speed,
            (double) (super.end - super.start) / speed);
      case "ellipse":
        return String.format(
            "<animateTransform attributeType=\"xml\" type=\"rotate\" begin=\"%.2f\" dur=\"%.2f\" "
                + "attributeName=\"transform\" "
                + "from=\"" + from + "\" to=\"" + to + "\" fill=\"freeze\"/>\n",
            (double) super.start / speed,
            (double) (super.end - super.start) / speed);
      default:
        throw new IllegalArgumentException("Unsupported shape type " + shapeType);
    }
  }
}
