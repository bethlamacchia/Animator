package cs3500.animator.view.visual.draw;

import cs3500.animator.model.shape.IShape;
import java.awt.Graphics2D;

/**
 * Interface for function objects that serve the purpose of drawing passed in {@link IShape}s onto
 * the passed in {@link Graphics2D}. Also is able to output an SVG representation of a shape and
 * its associated movements.
 */
public interface DrawCommand {

  /**
   * Execute the draw command according to the specifications of the implementing function object.
   *
   * @param s {@link IShape} to be drawn
   * @param g {@link Graphics2D} to draw the shape on
   */
  void draw(IShape s, Graphics2D g);

  /**
   * Get an SVG representation of the shape and its associated motions represented as a String.
   *
   * @return a String with the SVG representation
   */
  String writeSVG(IShape s);
}
