package cs3500.animator.view;

import cs3500.animator.model.shape.IShape;
import cs3500.animator.view.visual.draw.DrawCommand;
import java.util.Map;

/**
 * Implementation of a view of an animator that displays the output as a text output describing the
 * contents of the animation and their respective motions. This output is formatted in the same way
 * as the input file except instead of declaring all of the shapes first this output lists each
 * shape followed by its own associated motions then the next shape, etc. The output of the text
 * view is written to its appendable field.
 */
public class TextView extends ATextBasedView {

  /**
   * Construct a TextView object.
   *
   * @param out {@link Appendable} for the output to be sent to
   */
  public TextView(Appendable out) {
    super(out);
  }


  @Override
  public void displayOutput() {
    write(String.format("canvas %d %d %d %d\n", x, y, w, h));
    for (IShape shape : shapes) {
      write(shape.toString());
    }
    //close the output file if applicable
    close();
  }

  @Override
  public void setCommands(Map<String, DrawCommand> knownCommands) {
    throw new UnsupportedOperationException("Text view does not use DrawCommands!");
  }

}