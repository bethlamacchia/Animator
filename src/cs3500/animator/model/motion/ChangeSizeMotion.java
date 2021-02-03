package cs3500.animator.model.motion;

import cs3500.animator.model.shape.IShape;


/**
 * Represents a motion that changes the size of the shape from the starting dimensions to ending
 * dimensions. Can change the dimensions incrementally over time by calculating the change per
 * tick.
 */
public final class ChangeSizeMotion extends AMotion {

  private final double dW;
  private final double dH;
  private final double startW;
  private final double startH;
  private final double endW;
  private final double endH;

  /**
   * Constructs a motion that changes the dimensions of this shape given the starting dimensions and
   * the ending dimensions.
   *
   * @param start  the start time of the motion
   * @param end    the end time of the motion
   * @param id     the name of the shape this motion is applied to
   * @param startW the starting width of the shape
   * @param startH the starting height of the shape
   * @param endW   the ending width of the shape
   * @param endH   the ending height of the shape
   * @throws IllegalArgumentException if any of the dimensions are negative, or if the shape ID is
   *                                  null
   */
  public ChangeSizeMotion(int start, int end, String id, double startW, double startH,
      double endW, double endH) {
    super(start, end, id);
    if (startW <= 0 || startH <= 0 || endW <= 0 || endH <= 0) {
      throw new IllegalArgumentException("All dimensions must be positive");
    }
    if (id == null) {
      throw new IllegalArgumentException("Shape ID cannot be null");
    }

    this.startW = startW;
    this.startH = startH;
    this.endW = endW;
    this.endH = endH;
    if (start == end) {
      this.dW = (endW - this.startW);
      this.dH = (endH - this.startH);
    } else {
      this.dW = (endW - this.startW) / (end - start);
      this.dH = (endH - this.startH) / (end - start);
    }
  }

  @Override
  public void apply(IShape shape, int ticks) {
    if (ticks >= super.start && ticks <= super.end) {
      int timeSoFar = (ticks - super.start);

      double newW = this.startW + (dW * timeSoFar);
      double newH = this.startH + (dH * timeSoFar);

      shape.setWidth(newW);
      shape.setHeight(newH);
    }
  }

  @Override
  public boolean overlaps(Motion m) {
    if (m instanceof ChangeSizeMotion) {
      return checkOverlap(m) && !(startW == endW && startH == endH);
    }
    // other is not a ChangeSizeMotion so don't need to worry about it overlapping
    return false;
  }

  @Override
  public boolean isCompatibleWith(IShape shape) {
    if (shape == null) {
      throw new IllegalArgumentException("Cannot apply a motion to a null shape");
    } else if (!(shape.getName().equals(this.getId()))) {
      throw new IllegalArgumentException("Motions can only operate on one shape");
    } else if ((startW == shape.getWidth() && startH == shape.getHeight())
        || this.start < shape.getStartTime()) {
      return true;
    } else {
      throw new IllegalArgumentException(String.format(
          "Start size of motion does not equal size of the shape %s at t = %d", shape.getName(),
          getStart()));
    }
  }

  @Override
  public String toSVG(String shapeType, int speed) {
    switch (shapeType) {
      case "rectangle":
        return
            String.format(
                "<animate attributeType=\"xml\" begin=\"%.2f\" dur=\"%.2f\" attributeName=\"width\""
                    + " from=\"%.1f\" to=\"%.1f\" fill=\"freeze\"/>\n",
                (double) super.start / speed,
                (double) (super.end - super.start) / speed,
                startW, endW)
                + String.format(
                "<animate attributeType=\"xml\" begin=\"%.2f\" dur=\"%.2f\" "
                    + "attributeName=\"height\" "
                    + "from=\"%.1f\" to=\"%.1f\" fill=\"freeze\"/>\n", (double) super.start / speed,
                (double) (super.end - super.start) / speed,
                startH, endH);
      case "ellipse":
        return
            String.format(
                "<animate attributeType=\"xml\" begin=\"%.2f\" dur=\"%.2f\" attributeName=\"rx\" "
                    + "from=\"%.1f\" to=\"%.1f\" fill=\"freeze\"/>\n", (double) super.start / speed,
                (double) (super.end - super.start) / speed,
                startW, endW)
                + String.format(
                "<animate attributeType=\"xml\" begin=\"%.2f\" dur=\"%.2f\" attributeName=\"ry\" "
                    + "from=\"%.1f\" to=\"%.1f\" fill=\"freeze\"/>\n", (double) super.start / speed,
                (double) (super.end - super.start) / speed,
                startH, endH);
      default:
        throw new IllegalArgumentException("Unsupported shape type " + shapeType);
    }
  }
}
