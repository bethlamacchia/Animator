package cs3500.animator.view;

import cs3500.animator.model.shape.IShape;
import cs3500.animator.view.visual.draw.DrawCommand;
import java.util.HashMap;
import java.util.Map;

/**
 * Class implementing an SVG view for an animator. This view serves the purpose of accepting data
 * from a model implementing the functionality of the animator and convert it to SVG formatted text
 * which is sent to the specified output.
 */
public class SVGView extends ATextBasedView {

  private Map<String, DrawCommand> knownCommands;

  /**
   * Construct a SVGView object with the given output location.
   *
   * @param out output location
   */
  public SVGView(Appendable out) {
    super(out);
    knownCommands = new HashMap<>();
  }


  /**
   * Create a textual SVG formatted description of the animation.
   */
  @Override
  public void displayOutput() {
    write(String.format("<svg viewBox=\"%d %d %d %d\" version=\"1.1\" "
        + "xmlns=\"http://www.w3.org/2000/svg\">\n", x, y, w, h));
    for (IShape shape : shapes) {
      DrawCommand cmd = knownCommands.getOrDefault(shape.getType(), null);
      if (cmd == null) {
        throw new IllegalArgumentException("Undefined shape type");
      } else {
        write(cmd.writeSVG(shape));
      }
    }
    write("</svg>");
    close();
  }

  @Override
  public void setCommands(Map<String, DrawCommand> knownCommands) {
    this.knownCommands = knownCommands;
  }


}