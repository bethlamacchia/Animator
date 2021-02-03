package cs3500.animator.model.motion;

import cs3500.animator.model.shape.IShape;
import java.awt.Color;

/**
 * Represents a motion that changes the color of the shape from the starting color to the end color.
 * Can change the color incrementally over time by calculating the change per tick.
 */
public final class ColorMotion extends AMotion {

  private final Color startColor;
  private final Color endColor;
  //change in r, g, b per tick
  private final double dR;
  private final double dG;
  private final double dB;

  /**
   * Constructs a new ColorMotion with a start time, end time, shape ID, starting color, and ending
   * color.
   *
   * @param start      the start time of the motion
   * @param end        the end time of the motion
   * @param id         the name of the shape the motion is applied to
   * @param startColor the starting color of the shape
   * @param endColor   the ending color of the shape
   * @throws IllegalArgumentException if trying to pass a null id, start color or end color
   */
  public ColorMotion(int start, int end, String id, Color startColor, Color endColor) {
    super(start, end, id);
    if (id == null || startColor == null || endColor == null) {
      throw new IllegalArgumentException("Color cannot be null");
    }
    this.startColor = startColor;
    this.endColor = endColor;
    if (start == end) {
      this.dR = (startColor.getRed() - endColor.getRed());
      this.dG = (startColor.getGreen() - endColor.getGreen());
      this.dB = (startColor.getBlue() - endColor.getBlue());
    } else {
      this.dR = (startColor.getRed() - endColor.getRed()) / (start - end);
      this.dG = (startColor.getGreen() - endColor.getGreen()) / (start - end);
      this.dB = (startColor.getBlue() - endColor.getBlue()) / (start - end);
    }
  }

  @Override
  public void apply(IShape shape, int ticks) {
    if (ticks >= super.start && ticks < super.end) {
      int timeDif = (ticks - super.start);
      Color newColor = new Color((int) (this.startColor.getRed() + (dR * timeDif)),
          (int) (this.startColor.getGreen() + (dG * timeDif)),
          (int) (this.startColor.getBlue() + (dB * timeDif)));
      shape.setColor(newColor);
    } else if (ticks == super.end) {
      shape.setColor(new Color(endColor.getRGB()));
    }
  }

  @Override
  public boolean overlaps(Motion m) {
    if (m instanceof ColorMotion) {
      return checkOverlap(m) && !startColor.equals(endColor);
    }
    return false;
  }

  @Override
  public boolean isCompatibleWith(IShape shape) {
    if (shape == null) {
      throw new IllegalArgumentException("Cannot apply a motion to a null shape");
    } else if (!(shape.getName().equals(this.getId()))) {
      throw new IllegalArgumentException("Motions can only operate on one shape");
    }
    if (startColor.equals(shape.getColor()) || this.start < shape.getStartTime()) {
      return true;
    } else {
      throw new IllegalArgumentException(String.format(
          "Start color of motion does not equal color of the shape %s at t = %d", shape.getName(),
          getStart()));
    }
  }

  @Override
  public String toSVG(String shapeType, int speed) {
    return
        String.format(
            "<animate attributeType=\"xml\" begin=\"%.2f\" dur=\"%.2f\" attributeName=\"fill\" "
                + "from=\"rgb(%d,%d,%d)\" to=\"rgb(%d,%d,%d)\" fill=\"freeze\"/>\n",
            (double) super.start / speed, (double) (super.end - super.start) / speed,
            startColor.getRed(), startColor.getGreen(), startColor.getBlue(),
            endColor.getRed(), endColor.getGreen(), endColor.getBlue());
  }
}