package cs3500.animator.model.frame;

import cs3500.animator.model.shape.IShape;
import cs3500.animator.model.shape.Position2D;
import java.awt.Color;

/**
 * Interface for the Frame class, which represents the values of a shape at some point in time
 * (position, color, and dimensions). Two frames are equal if their string representations (and
 * hence all of their values) are equal, and their hashcodes will be equal if their string
 * representations are equal.
 */
public interface IFrame {

  /**
   * Get the time that this frame occurs.
   *
   * @return integer representing the tick of this frame
   */
  int getTime();

  /**
   * Get the position of the shape in this frame.
   *
   * @return the position of the shape in this frame.
   */
  Position2D getPos();

  /**
   * Get the color of the shape in this frame.
   *
   * @return the color of the shape in this frame.
   */
  Color getColor();

  /**
   * Get the width of the shape in this frame.
   *
   * @return the width of the shape in this frame.
   */
  double getWidth();

  /**
   * Get the height of the shape in this frame.
   *
   * @return the height of the shape in this frame.
   */
  double getHeight();

  /**
   * Get the name of the shape in this frame.
   *
   * @return the name of the shape in this frame.
   */
  String getName();

  /**
   * Apply the keyframe to the given shape by setting its values.
   */
  void apply(IShape shape);

  /**
   * Get the heading of the keyframe.
   * @return the heading in degrees.
   */
  double getHeading();

}
