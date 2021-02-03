package cs3500.animator.view.visual.draw;

import cs3500.animator.model.motion.Motion;
import cs3500.animator.model.motion.Rotation;
import cs3500.animator.model.shape.IShape;
import java.awt.Graphics2D;

/**
 * Drawing command implementation for drawing an ellipse {@link IShape}. Uses the {@link Graphics2D}
 * fillOval() method and the properties of the shape to draw the ellipse.
 */
public class DrawEllipse extends ADrawCommand {

  /**
   * Call the abstract class constructor to set the x and y offsets.
   *
   * @param x integer representing the x offset
   * @param y integer representing the y offset
   */
  public DrawEllipse(int x, int y, int speed) {
    super(x, y, speed);
  }

  @Override
  public void draw(IShape s, Graphics2D g) {
    setupRotation(g, s);
    g.fillOval((int) s.getPosition().getX() - super.x, (int) s.getPosition().getY() - super.y,
        (int) s.getWidth(), (int) s.getHeight());
  }

  @Override
  public String writeSVG(IShape s) {
    String[] output = new String[s.getMotionsList().size() + 3];
    output[0] = String.format(
        "<ellipse id=\"%s\" cx=\"%.1f\" cy=\"%.1f\" rx=\"%.1f\" ry=\"%.1f\" "
            + "fill=\"rgb(%d,%d,%d)\" >\n",
        s.getName(), s.getPosition().getX() - super.x, s.getPosition().getY() - super.y,
        s.getWidth(), s.getHeight(), s.getColor().getRed(),
        s.getColor().getBlue(), s.getColor().getBlue());
    output[1] = setVisibility(s.getName(), s.getStartTime());
    int i = 2;
    for (Motion m : s.getMotionsList()) {
      if (m instanceof Rotation) {
        output[i] = ((Rotation) m).toSVG(s.getType(), speed,
            s.getPosition().getX() + (s.getWidth() / 2) - super.x,
            s.getPosition().getY() + (s.getHeight() / 2) - super.y);
      } else {
        output[i] = m.toSVG(s.getType(), speed);
      }
      i++;
    }
    output[i] = "</ellipse>\n";
    return String.join("", output);
  }
}