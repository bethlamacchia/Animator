package cs3500.animator.view.visual;

import cs3500.animator.model.shape.IShape;
import cs3500.animator.view.IView;
import cs3500.animator.view.visual.draw.DrawCommand;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * Class to represent a visual view on which {@link IShape}s received from the model can be drawn.
 * Has a {@link VisualViewPanel} object on which the shapes are actually drawn. This class sets
 * the title and size of the display as well as adding scroll bars and signaling the panel to draw.
 */
public class VisualView extends JFrame implements IView {

  private final VisualViewPanel panel;
  private List<IShape> shapes;

  /**
   * Construct a visual view with the default size.
   */
  public VisualView() {
    this(0, 0, 1000, 1000);
  }

  /**
   * Construct a visual view with the passed in size.
   *
   * @param height non-negative integer representing the height of the screen
   * @param width  non-negative integer representing the width of the screen
   */
  public VisualView(int x, int y, int width, int height) {
    super();
    this.setTitle("Excellence");
    this.setSize(width, height);
    this.panel = new VisualViewPanel();
    panel.setPreferredSize(new Dimension(1000, 1000));

    JScrollPane scrollPane = new JScrollPane(panel);
    add(scrollPane);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.pack();
  }

  /**
   * Draw the output onto the {@link VisualViewPanel}.
   */
  @Override
  public void displayOutput() {
    panel.setShapes(this.shapes);
    panel.repaint();
    this.setVisible(true);
  }

  @Override
  public void setShapes(Map<String, IShape> shapes) {
    this.shapes = new ArrayList<>(shapes.values());
  }


  @Override
  public void passBounds(int x, int y, int w, int h) {
    this.setSize(w, h);
  }

  @Override
  public void setOutput(Appendable a) {
    throw new UnsupportedOperationException("Can't set output file for visual view!");
  }

  @Override
  public void setCommands(Map<String, DrawCommand> knownCommands) {
    panel.setCommands(knownCommands);
  }


  @Override
  public void resetFocus() {
    this.setFocusable(true);
    this.requestFocus();
  }

  @Override
  public void addActionListener(ActionListener listener) {
    throw new UnsupportedOperationException("Can't add action listener to this view!");
  }
}
