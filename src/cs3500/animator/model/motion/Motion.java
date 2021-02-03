package cs3500.animator.model.motion;

import cs3500.animator.model.shape.IShape;

/**
 * The Motion interface represents some change in a shape over a period of time. This can be a
 * change in color, dimension, or position.
 */
public interface Motion {

  /**
   * Gets the start time of the motion.
   *
   * @return the start time of the motion
   */
  int getStart();

  /**
   * Gets the end time of the motion.
   *
   * @return the end time of the motion
   */
  int getEnd();

  /**
   * Gets the id of the motion, or the name of the {@link IShape} it is associated with.
   *
   * @return the id of the motion
   */
  String getId();

  /**
   * Apply the {@link Motion} to the given {@link IShape} changing either its color, position or
   * dimensions.
   *
   * @param shape {@link IShape} to be effected by the motion
   * @param ticks Integer representing the amount of ticks since the start of the animation
   */
  void apply(IShape shape, int ticks);

  /**
   * Determines if the given motion overlaps with this motion. A motion only overlaps if both
   * motions are of the same type.
   *
   * @param m the motion being compared to this motion
   * @return true if the given motion overlaps with this motion and is the same type
   */
  boolean overlaps(Motion m);

  /**
   * Determines if the motion has the same starting properties as the given {@link IShape}.
   *
   * @param shape {@link IShape} to check the motion on
   * @return whether or not the motion is compatible with the shape
   * @throws IllegalArgumentException if the shape passed in is {@code null} or the shape is
   *                                  incompatible with the motion
   */
  boolean isCompatibleWith(IShape shape) throws IllegalArgumentException;

  /**
   * Convert the motion to an SVG animate call that creates an SVG representation of the motion.
   *
   * @param shapeType the type of the shape in this motion.
   *
   * @return the motion as an SVG string.
   */
  String toSVG(String shapeType, int speed);
}
