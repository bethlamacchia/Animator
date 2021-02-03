package cs3500.animator.view;

import cs3500.animator.model.shape.IShape;
import cs3500.animator.view.visual.draw.DrawCommand;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Interface for a view in a model-view-controller design for an animator application that
 * manipulates {@link IShape} objects. Requires methods to set the list of shapes, to display the
 * output, and to pass in the bounds. The interface also provides a static method to display an
 * error message on a {@link JFrame} object.
 */
public interface IView {

  /**
   * Display the given message in a pop-up window.
   *
   * @param errorMes the error message to be displayed.
   */
  static void showErrorMessage(String errorMes) {
    JFrame frame = new JFrame();
    frame.setSize(100, 100);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JOptionPane.showMessageDialog(frame, errorMes,
        "Error", JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Provide the view with the list of shapes from the model.
   *
   * @param shapes Map of {@link IShape}s from the model
   */
  void setShapes(Map<String, IShape> shapes);

  /**
   * Display the output of the given view.
   */
  void displayOutput();

  /**
   * Pass the bounds of the model to the appropriate view.
   *
   * @param x      integer representing x offset of the display
   * @param y      integer representing y offset of the display
   * @param width  non-negative integer representing width of the display
   * @param height non-negative integer representing height of the display
   */
  void passBounds(int x, int y, int width, int height);

  /**
   * Sets the output to all text based views to the given {@link Appendable}. If the output is not
   * text based it throws an unsupported operation exception.
   *
   * @param a Appendable to send the view's output to
   * @throws UnsupportedOperationException If the view is not text based (no output to set)
   */
  void setOutput(Appendable a);

  /**
   * Set the map of known commands to views that use {@link DrawCommand}s in their displayOutput()
   * method.
   *
   * @param knownCommands Map of strings to draw commands that represents all known commands
   * @throws UnsupportedOperationException if the view does not use draw commands
   */
  void setCommands(Map<String, DrawCommand> knownCommands);

  /**
   * Reset the focus of the view.
   */
  void resetFocus();

  /**
   * Add an action listener to the view.
   *
   * @param listener the listener to add.
   */
  void addActionListener(ActionListener listener);
}
