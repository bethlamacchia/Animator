package cs3500.animator.view;

import cs3500.animator.model.shape.IShape;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import javax.swing.event.ChangeListener;

/**
 * Interface representing a view for a MVC structured animator program that allows the user to
 * interact with the program in some way, whether it be through buttons or adding text or any
 * similar type of user-program interaction.
 */
public interface IInteractiveView extends IView {

  /**
   * Get the command from the view. In different implementations this may mean different things, but
   * in general this means that a command from the controller is being used as the options input to
   * retrieve certain data used in that command's callback to perform the desired operation in the
   * model
   *
   * @param option string processed to determine which data to return
   * @return string used by action listener
   */
  String getCommand(String option);


  /**
   * Populate the text fields of the interactive view to the parameters of the given shape. Used to
   * generate the tweening keyframe when adding a new keyframe, which can then be edited in the
   * view.
   *
   * @param s the shape whose values represent the tweening keyframe.
   */
  void populateTextFields(IShape s);

  /**
   * Clear all the text fields of the view.
   */
  void clearTextFields();

  /**
   * Display the current speed to the user.
   */
  void displaySpeed(int speed);

  /**
   * Add an action listener to the view.
   *
   * @param a the action listener to be added.
   */
  void addActionListener(ActionListener a);

  /**
   * Configure the slider with a max and a changelistener.
   *
   * @param max the max tick of the animation.
   * @param c the changelistener to add to the slider.
   */
  void configureSlider(int max, ChangeListener c);

  /**
   * Set the current tick of the slider.
   *
   * @param tick the tick of the animation.
   */
  void setSliderTick(int tick);

  /**
   * Update the part of the user interface dealing with layers, passing in a list of each layer
   * with its associated shapes.
   *
   * @param shapesByLayer List of maps of shape names to shapes organized by layer
   */
  void updateLayerInterface(List<Map<String, IShape>> shapesByLayer);
}
