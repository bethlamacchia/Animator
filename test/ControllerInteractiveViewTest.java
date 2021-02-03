import static org.junit.Assert.assertEquals;

import cs3500.animator.controller.AnimatorController;
import cs3500.animator.controller.IAnimatorController;
import cs3500.animator.model.AnimatorModel;
import cs3500.animator.model.AnimatorModelImpl;
import cs3500.animator.view.IInteractiveView;
import org.junit.Before;
import org.junit.Test;

/**
 * Test that the controller calls its appropriate methods by testing it with a mock of the
 * interactive view.
 */
public class ControllerInteractiveViewTest {

  IInteractiveView mock;
  AnimatorModel model;
  IAnimatorController controller;
  Appendable out;
  Appendable modelOut;
  AnimatorModel mockModel;


  @Before
  public void init() {
    out = new StringBuilder();
    mock = new MockEditorView(out);
    model = new AnimatorModelImpl();
    controller = new AnimatorController(mock, model, 1);
  }

  @Test
  public void testEditorView() {
    controller.run();
    assertEquals("Adding action listener\n"
        + "Setting bounds\n"
        + "Setting commands\n"
        + "Setting shapes\n"
        + "Displaying view\n", out.toString());
  }

}
