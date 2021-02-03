package cs3500.animator.view;

import cs3500.animator.model.shape.IShape;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to abstract out the common functionality of {@link IView} objects that implement a text
 * based view that writes to an {@link Appendable} object.
 */
public abstract class ATextBasedView implements IView {

  protected Appendable out;

  protected List<Map<String, IShape>> shapesByLayer;
  protected List<IShape> shapes;
  protected int x;
  protected int y;
  protected int w;
  protected int h;

  /**
   * Super class constructor to initialize the out field.
   *
   * @param out {@link Appendable}
   */
  protected ATextBasedView(Appendable out) {
    this.out = out;
  }

  @Override
  public void passBounds(int x, int y, int w, int h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }

  @Override
  public void setShapes(Map<String, IShape> shapes) {
    this.shapes = new ArrayList<>(shapes.values());
  }

  @Override
  public void setOutput(Appendable a) {
    this.out = a;
  }

  /**
   * Append a message to the {@link Appendable} out.
   *
   * @param message String to be appended to the output
   * @throws IllegalStateException if the {@code append()} method fails
   */
  protected void write(String message) {
    try {
      out.append(message);
    } catch (IOException ioe) {
      throw new IllegalStateException("Append failed!");
    }
  }

  /**
   * Attempt to close the output file. If the output is not a file do nothing.
   */
  protected void close() {
    if (out instanceof BufferedWriter) {
      try {
        ((BufferedWriter) out).close();
      } catch (Exception e) {
        throw new IllegalStateException(e.getLocalizedMessage());
      }
    }
  }

  @Override
  public void resetFocus() {
    throw new UnsupportedOperationException("Can't reset focus of a text view!");
  }

  @Override
  public void addActionListener(ActionListener listener) {
    throw new UnsupportedOperationException("Can't add action listener to this view!");
  }

}
