package cs3500.animator.view;

import cs3500.animator.view.editor.EditorView;
import cs3500.animator.view.visual.VisualView;

/**
 * A class whose purpose is creating a view based on the factory pattern, where the view type
 * returned depends on the parameters passed to the method.
 */
public final class ViewFactory {

  /**
   * Construct a view depending on the type specified.
   *
   * @param type the type of view to be generated. Supports text, visual, and svg views.
   * @return an IView implementation dependent on the string input type.
   */
  public static IView setView(String type) {
    Appendable out = System.out;
    IView view;
    switch (type) {
      case "text":
        view = new TextView(out); // parameters not finalized
        break;
      case "svg":
        view = new SVGView(out); // parameters not finalized
        break;
      case "visual":
        view = new VisualView(); // parameters not finalized
        break;
      case "edit":
        view = new EditorView();
        break;
      default:
        throw new IllegalArgumentException("View type not supported!");
    }
    return view;

  }
}
