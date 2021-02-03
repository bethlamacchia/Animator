package cs3500.animator.model;

import cs3500.animator.model.frame.IFrame;
import cs3500.animator.model.motion.Motion;
import cs3500.animator.model.shape.IShape;
import java.util.List;
import java.util.Map;

/**
 * Interface for an animator model, which represents the model aspect of a model-view-controller
 * program for a single animation that operates on shapes with associated frames and motions. The
 * model stores the shapes in the animation and all of their movements as well as the dimensions of
 * the display. The methods in the model are responsible for initializing the shapes and their
 * motions and controlling/updating their states at any point in time, as well as adding individual
 * shapes or motions to the animation. The implementation of the AnimatorModel enforces constraints
 * on the data and its methods.
 */
public interface AnimatorModel {


  /**
   * Add a new {@link IShape} to the animation.
   *
   * @param shape {@link IShape} to be added
   * @throws IllegalArgumentException if a shape with the name of the passed in shape already exists
   *                                  in the model
   */
  void addShape(IShape shape) throws IllegalArgumentException;

  /**
   * Update the state of the animation by applying each shape's current motion to itself.
   *
   * @param ticks non-negative integer representing the number of ticks since the start
   * @throws IllegalArgumentException if ticks is negative
   */
  void update(int ticks) throws IllegalArgumentException;

  /**
   * Set the state of all shapes in the model to their state at time t = ticks. This means that the
   * properties of each shape (position, dimension, color) will all be set to the values they would
   * be at if the motions currently assigned to them were run up until t = ticks.
   *
   * @param ticks non-negative integer representing the time since the animation started
   * @throws IllegalArgumentException if the ticks passed in is negative
   */
  void setStateTo(int ticks) throws IllegalArgumentException;

  /**
   * Get a deep copy of the map of names to shapes from the model.
   *
   * @return The map of {@code String}s to {@link IShape}s
   */
  Map<String, IShape> getShapes();

  /**
   * Adds all of the {@link Motion}s for the animation to the model in chronological order by
   * sorting the motions, ensuring they are valid (consistent and non overlapping) and then adding
   * them to their respective shapes. Also adds the associated keyframes to the respective shape's
   * list of keyframes.
   *
   * @param motions {@link Motion}s to be added
   * @throws IllegalArgumentException if the shape does not exist in the model or if the motion
   *                                  creates any consistency issues with the shape's existing
   *                                  motions.
   */
  void addMotions(List<Motion> motions);

  /**
   * Adds a single {@link Motion}s for the animation to the model.
   *
   * @param m {@link Motion} to be added
   * @throws IllegalArgumentException if the shape does not exist in the model or if the motion
   *                                  creates any consistency issues with the shape's existing
   *                                  motions.
   */
  void addMotion(Motion m);

  /**
   * Get the bounds of the screen in a four element integer array containing in this order the x and
   * y coordinates of the screen and the width an height of the screen.
   *
   * @return integer array containing the bounds of the screen.
   */
  int[] getBounds();


  /**
   * Sets the keyframes of each shape in the map to its list of keyframes.
   *
   * @param keyFrames the map of shape IDs to lists of keyframes.
   */
  void setKeyFrames(Map<String, List<IFrame>> keyFrames);

  /**
   * Removes the shape from the shapes stored in the model.
   *
   * @param name the shape to remove.
   */
  void removeShape(String name);


  /**
   * Removes the motion from the motions in this animation..
   *
   * @param m the motion to remove.
   */
  void removeMotion(Motion m);

  /**
   * Get the final tick of this animation - when it "ends".
   *
   * @return the last tick in this animation where shapes have specified motions.
   */
  int getLastTick();

  /**
   * Add a keyframe to the model by adding it to its shape's list of keyframes.
   *
   * @param f the frame to be added.
   */
  void addKeyFrame(IFrame f);

  /**
   * Remove the specified keyframe from the model by removing it from the shape it belongs to.
   *
   * @param f {@link IFrame} to be removed
   */
  void removeKeyFrame(IFrame f);

  /**
   * Return a map of shape layer to a map of all shapes at that layer's names to the object itself.
   *
   * @return map of layer to map of names to shapes
   */
  List<Map<String, IShape>> getShapesByLayer();

  /**
   * Add the shape to the specified layer.
   *
   * @param shape the ID of the shape
   * @param layer the layer to add the shape to
   */
  void addShapeToLayer(String shape, int layer);

  /**
   * Removes a layer and all the shapes within it.
   *
   * @param layer the layer to remove.
   */
  void deleteLayer(int layer);

  /**
   * Add a new empty layer. If that layer already exists, do nothing.
   *
   * @param layer the layer to add.
   */
  void addLayer(int layer);

  /**
   * Reorder the two layers.
   *
   * @param layer1 the first layer to be swapped.
   * @param layer2 the second layer to be swapped.
   */
  void reorder(int layer1, int layer2);

  /**
   * Change the layer of an individual shape.
   *
   * @param name String representing the name of the shape
   * @param layer integer representing the layer for the shape to be drawn
   * @throws IllegalArgumentException if layer is negative
   */
  void changeShapeLayer(String name, int layer);
}
