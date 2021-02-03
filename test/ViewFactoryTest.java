import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import cs3500.animator.view.SVGView;
import cs3500.animator.view.TextView;
import cs3500.animator.view.ViewFactory;
import cs3500.animator.view.visual.VisualView;
import org.junit.Test;

/**
 * A test class to test that the view factory creates the correct views and throws an error if the
 * view type is not one of text, visual, or svg.
 */
public class ViewFactoryTest {

  @Test
  public void testFactoryCreatesCorrectView() {
    assertTrue(ViewFactory.setView("svg") instanceof SVGView);
    assertTrue(ViewFactory.setView("visual") instanceof VisualView);
    assertTrue(ViewFactory.setView("text") instanceof TextView);
  }

  @Test
  public void testFactoryFakeView() {
    try {
      ViewFactory.setView("abc");
    } catch (IllegalArgumentException e) {
      assertEquals("View type not supported!", e.getLocalizedMessage());
    }
  }

  @Test
  public void testFactoryEmptyView() {
    try {
      ViewFactory.setView("");
    } catch (IllegalArgumentException e) {
      assertEquals("View type not supported!", e.getLocalizedMessage());
    }
  }
}
