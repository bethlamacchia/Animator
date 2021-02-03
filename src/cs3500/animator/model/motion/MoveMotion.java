package cs3500.animator.model.motion;

import cs3500.animator.model.shape.Position2D;
import cs3500.animator.model.shape.IShape;

/**
 * Represents a motion that changes the position of the shape from the starting position to ending
 * position. Can change the position incrementally over time by calculating the change per tick.
 */
public final class MoveMotion extends AMotion {

  private final Position2D startPos;
  private final Position2D endPos;
  //change in x and y per tick
  private final double dX;
  private final double dY;

  /**
   * Create a position altering {@link Motion} object that takes in the name of the shape it is to
   * act on as well as the time interval in ticks and the starting and ending positions.
   *
   * @param start    Integer representing the start time of the motion in ticks
   * @param end      Integer representing the end time of the motion in ticks
   * @param id       String representing the name of the shape to be acted on
   * @param startPos {@link Position2D} representing the starting position of the motion
   * @param endPos   {@link Position2D} representing the ending position of the motion
   * @throws IllegalArgumentException if id, startPos, or endPos are {@code null}
   */
  public MoveMotion(int start, int end, String id, Position2D startPos, Position2D endPos) {
    super(start, end, id);
    if (id == null || startPos == null || endPos == null) {
      throw new IllegalArgumentException("Motions cannot accept any null inputs!");
    }
    this.startPos = startPos;
    this.endPos = endPos;
    if (start == end) {
      this.dX = (endPos.getX() - startPos.getX());
      this.dY = (endPos.getY() - startPos.getY());
    } else {
      this.dX = (endPos.getX() - startPos.getX()) / (end - start);
      this.dY = (endPos.getY() - startPos.getY()) / (end - start);
    }
  }

  @Override
  public void apply(IShape shape, int ticks) {
    if (ticks >= super.start && ticks <= super.end) {
      int timeDif = (ticks - super.start);
      Position2D newPos = new Position2D(this.startPos.getX() + (dX * timeDif),
          this.startPos.getY() + (dY * timeDif));
      shape.setPosition(newPos);
    }
  }

  @Override
  public boolean overlaps(Motion m) {
    if (m instanceof MoveMotion) {
      return checkOverlap(m) && !startPos.equals(endPos);
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
    if (startPos.equals(shape.getPosition()) || this.start < shape.getStartTime()) {
      return true;
    } else {
      throw new IllegalArgumentException(String.format(
          "Start position of motion does not equal position of the shape %s at t = %d",
          shape.getName(), getStart()));
    }
  }

  @Override
  public String toSVG(String shapeType, int speed) {
    switch (shapeType) {
      case "rectangle":
        return
            String.format(
                "<animate attributeType=\"xml\" begin=\"%.2f\" dur=\"%.2f\" attributeName=\"x\" "
                    + "from=\"%.1f\" to=\"%.1f\" fill=\"freeze\"/>\n", (double) super.start / speed,
                (double) (super.end - super.start) / speed,
                startPos.getX(), endPos.getX())
                + String.format(
                "<animate attributeType=\"xml\" begin=\"%.2f\" dur=\"%.2f\" attributeName=\"y\" "
                    + "from=\"%.1f\" to=\"%.1f\" fill=\"freeze\"/>\n", (double) super.start / speed,
                (double) (super.end - super.start) / speed,
                startPos.getY(), endPos.getY());
      case "ellipse":
        return
            String.format(
                "<animate attributeType=\"xml\" begin=\"%.2f\" dur=\"%.2f\" attributeName=\"cx\" "
                    + "from=\"%.1f\" to=\"%.1f\" fill=\"freeze\"/>\n", (double) super.start / speed,
                (double) (super.end - super.start) / speed,
                startPos.getX(), endPos.getX())
                + String.format(
                "<animate attributeType=\"xml\" begin=\"%.2f\" dur=\"%.2f\" attributeName=\"cy\" "
                    + "from=\"%.1f\" to=\"%.1f\" fill=\"freeze\"/>\n", (double) super.start / speed,
                (double) (super.end - super.start) / speed,
                startPos.getY(), endPos.getY());
      default:
        throw new IllegalArgumentException("Unsupported shape type " + shapeType);
    }
  }
}
