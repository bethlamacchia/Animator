package cs3500.animator.view.visual;

import cs3500.animator.model.shape.IShape;
import cs3500.animator.view.visual.draw.DrawCommand;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

/**
 * Panel on which the shapes passed into the visual view are actually drawn. Extends the JPanel
 * class and overrides its paintComponent() method to display the shapes on a {@link Graphics2D}
 * object.
 */
public class VisualViewPanel extends JPanel {


  private Map<String, DrawCommand> knownCommands;
  private List<IShape> shapes;

  /**
   * Construct a new VisualViewPanel() object. Constructor initializes map of known shapes that can
   * be drawn.
   */
  public VisualViewPanel() {
    knownCommands = new HashMap<>();
  }

  @Override
  protected void paintComponent(Graphics gr) {
    if (shapes == null) {
      return;
    }
    Graphics2D g = (Graphics2D) gr;
    super.paintComponent(g);
    shapes.sort(compareShapes);
    for (IShape s : this.shapes) {
      if (s.isVisible()) {
        g.setColor(new Color(s.getColor().getRGB()));
        DrawCommand cmd = knownCommands.getOrDefault(s.getType(), null);
        if (cmd == null) {
          throw new IllegalArgumentException("Shape type not supported!");
        } else {
          cmd.draw(s, g);
        }
      }
    }
  }

  /**
   * Sets the list of shapes in the view panel to the given list of shapes.
   *
   * @param shapes the list of shapes to set this panels list of shapes to.
   */
  public void setShapes(List<IShape> shapes) {
    this.shapes = shapes;
  }

  /**
   * Set the map of known commands to the passed in map.
   *
   * @param knownCommands map of strings (shape types) to known commands
   */
  public void setCommands(Map<String, DrawCommand> knownCommands) {
    this.knownCommands = knownCommands;
  }

  /**
   * A comparator to compare shapes by their layers.
   */
  private final Comparator<IShape> compareShapes = new Comparator<IShape>() {
    @Override
    public int compare(IShape s1, IShape s2) {
      return s1.getLayer() - s2.getLayer();
    }
  };
}