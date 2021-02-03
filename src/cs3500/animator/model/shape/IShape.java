package cs3500.animator.model.shape;

import cs3500.animator.model.frame.IFrame;
import cs3500.animator.model.motion.Motion;
import java.awt.Color;
import java.util.List;
import java.util.Map;

/**
 * Interface containing the common methods for a shape including obtaining its color and position,
 * setting and applying motions and copying it.
 */
public interface IShape {

  /**
   * Return the color of the {@link IShape}.
   *
   * @return the Color of the shape
   */
  Color getColor();

  /**
   * Set the color of the {@link IShape}.
   *
   * @param c {@link Color} to be assigned to the shape
   */
  void setColor(Color c);

  /**
   * Return a {@link Position2D} representing the position of the {@link IShape}.
   *
   * @return a {@link Position2D} representing the position of the shape.
   */
  Position2D getPosition();

  /**
   * Set the center position of the {@link IShape}.
   *
   * @param p {@link Position2D} representing the middle position of the shape
   */
  void setPosition(Position2D p);

  /**
   * Return the height of the {@link IShape}.
   *
   * @return the height of the shape
   */
  double getHeight();

  /**
   * Set the height of the {@link IShape}.
   *
   * @param h the height to set the shape
   */
  void setHeight(double h);

  /**
   * Return the width of the {@link IShape}.
   *
   * @return the width of the shape
   */
  double getWidth();

  /**
   * Set the width of the {@link IShape}.
   *
   * @param w the width to set the shape
   */
  void setWidth(double w);

  /**
   * Apply the {@link Motion} to the shape, updating its position, color and dimensions as a
   * function of time specified by the motion. This method is meant to be called inside of a loop
   * that iterates each tick from the start of the animation, calling it on a tick value before
   * calling it on previous tick values can result in unexpected behaviour.
   *
   * @param ticks represents the amount of ticks since the animation started
   */
  void applyMotion(int ticks);

  /**
   * Return the name of the {@link IShape}.
   *
   * @return String representing the name of the shape
   */
  String getName();


  /**
   * Add the given motion to this shape's list of motions. Motions can be added in any order, but
   * cannot overlap with any same-type motions in this shape's existing motions.
   *
   * @param m the motion being added to this shapes motion
   * @throws IllegalArgumentException if adding this motion would create an overlap with any
   *                                  existing motions of the same type for this shape or if it
   *                                  would require the shape to have two conflicting properties at
   *                                  an instant of time.
   */
  void addMotion(Motion m) throws IllegalArgumentException;

  /**
   * Determines if the given motion overlaps with any of this shape's motion. A motion only overlaps
   * if both motions are of the same type.
   *
   * @param m the motion being compared to this shape's motions
   * @return true if the given motion overlaps with any of this shape's same-type motions
   */
  boolean overlaps(Motion m);

  /**
   * Return a shape with the properties that this shape would have at t = ticks.
   *
   * @param ticks non-negative integer representing the ticks since the animation started
   * @return a new shape with the properties of the orignal shape a t = ticks
   */
  IShape getStateAt(int ticks);

  /**
   * Get the list of active motions of the shape.
   *
   * @return the list of active motions
   */
  List<Motion> getActiveMotions();

  /**
   * Get the map of ticks (integers) to lists of motions.
   *
   * @return the map of integers to lists of motions
   */
  Map<Integer, List<Motion>> getMotionsMap();

  /**
   * Get the {@link List} of all {@link Motion}s.
   *
   * @return the list of motions
   */
  List<Motion> getMotionsList();

  /**
   * Creates a copy of this shape, using dynamic dispatch to determine which type of shape to
   * return.
   *
   * @return a copy of this shape.
   */
  IShape copy();

  /**
   * Get the type of shape this object is (i.e. rectangle, ellipse, etc.).
   *
   * @return {@code String} representing the type of shape
   */
  String getType();

  /**
   * Get the starting time of the shape, the time at which it becomes visible / the time at which
   * the first motion / keyframe begins.
   */
  int getStartTime();

  /**
   * Set the starting time of the shape, the time at which it becomes visible / the time at which
   * the first motion / keyframe begins.
   *
   * @param time integer representing the ticks since the start of the animation.
   */
  void setStartTime(int time);

  /**
   * Get the ending time of the shape, the time at which its last motion ends.
   *
   * @return an integer representing the last tick of this shape's final motion
   */
  int getEndTime();

  /**
   * Determine whether or not the shape has appeared on screen yet and thus should be drawn.
   *
   * @return whether the shape's initial motion has started
   */
  boolean isVisible();

  /**
   * Get the shape's list of keyframes.
   *
   * @return a copy of this shape's list of keyframes.
   */
  List<IFrame> getKeyFrames();

  /**
   * Set the keyframes of this shape.
   *
   * @param keyframes the list of keyframes to set this shape's keyframes to.
   */
  void setKeyFrames(List<IFrame> keyframes);

  /**
   * Remove the motion from this shape.
   *
   * @param m the motion to remove.
   */
  void removeMotion(Motion m);

  /**
   * Add a keyframe to this shape's list of keyframes.
   *
   * @param f the frame to add.
   */
  void addKeyFrame(IFrame f);

  /**
   * Remove the keyframe at the given time from the shape.
   *
   * @param index index of the keyframe in the shape
   * @throws IllegalArgumentException if the index is invalid
   */
  void removeKeyFrame(int index) throws IllegalArgumentException;

  /**
   * Set the heading of this shape in degrees.
   *
   * @param newHeading the heading to set this shape to.
   */
  void setHeading(double newHeading);

  /**
   * Get the heading of this shape in degrees.
   */
  double getHeading();

  /**
   * Get the layer of the shape.
   *
   * @return integer representing the layer of a shape
   */
  int getLayer();

  /**
   * Set the layer of the shape.
   *
   * @param l the layer to add this shape to
   */
  void setLayer(int l);
}
