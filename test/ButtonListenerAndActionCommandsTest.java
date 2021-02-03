import static org.junit.Assert.assertEquals;

import cs3500.animator.controller.AnimatorController;
import cs3500.animator.controller.ButtonListener;
import cs3500.animator.controller.IAnimatorController;
import cs3500.animator.model.AnimatorModel;
import cs3500.animator.model.AnimatorModelImpl;
import cs3500.animator.view.IInteractiveView;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the ButtonListener and the action commands that work with the view and controller when
 * a button is pressed.
 */
public class ButtonListenerAndActionCommandsTest {

  private ButtonListener b;

  private IInteractiveView mock;
  private IAnimatorController controller;
  private Map<String, Runnable> buttonClickedMap;
  private Appendable out;

  @Before
  public void init() {
    out = new StringBuilder();
    mock = new MockEditorView(out);
    AnimatorModel model = new AnimatorModelImpl();
    controller = new AnimatorController(mock, model, 1);
    b = new ButtonListener();

    buttonClickedMap = new HashMap<>();
  }

  @Test
  public void testSetActionMapAndActionPerformed() {
    Map<String, Runnable> commandMap = new HashMap<>();
    Appendable out = new StringBuilder();
    commandMap.put("a", () -> {
      try {
        out.append("a ");
      } catch (Exception ignored) {
      }
    });
    commandMap.put("b", () -> {
      try {
        out.append("b ");
      } catch (Exception ignored) {
      }
    });
    b.setButtonClickedActionMap(commandMap);
    b.actionPerformed(new ActionEvent(b, 1, "a"));
    assertEquals("a ", out.toString());
    b.actionPerformed(new ActionEvent(b, 1, "b"));
    assertEquals("a b ", out.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullActionEvent() {
    b.actionPerformed(null);
  }

  @Test
  public void testAddShape() {
    JButton button = new JButton("add a shape");
    button.setActionCommand("Add shape");

    ActionEvent e = new ActionEvent(button, 1, "Add shape");

    ((MockEditorView) mock).actionPerformed(e);

    assertEquals("Adding action listener\n"
        + "Adding shape\n", out.toString());
  }

  @Test
  public void testAddKeyframe() {
    JButton button = new JButton("add a keyframe");
    button.setActionCommand("Add keyframe");

    ActionEvent e = new ActionEvent(button, 1, "Add keyframe");

    controller.run();
    ((MockEditorView) mock).actionPerformed(e);

    assertEquals("Adding action listener\n"
        + "Setting bounds\n"
        + "Setting commands\n"
        + "Setting shapes\n"
        + "Displaying view\n"
        + "Adding keyframe\n", out.toString());
  }

  @Test
  public void testRemoveShape() {
    JButton button = new JButton("add a shape");
    button.setActionCommand("Remove shape");

    ActionEvent e = new ActionEvent(button, 1, "Remove shape");

    controller.run();
    ((MockEditorView) mock).actionPerformed(e);

    assertEquals("Adding action listener\n"
        + "Setting bounds\n"
        + "Setting commands\n"
        + "Setting shapes\n"
        + "Displaying view\n"
        + "Removing shape\n", out.toString());
  }

  @Test
  public void testRemoveKeyframe() {
    JButton button = new JButton("add a shape");
    button.setActionCommand("Remove keyframe");

    ActionEvent e = new ActionEvent(button, 1, "Remove keyframe");

    controller.run();
    ((MockEditorView) mock).actionPerformed(e);

    assertEquals("Adding action listener\n"
        + "Setting bounds\n"
        + "Setting commands\n"
        + "Setting shapes\n"
        + "Displaying view\n"
        + "Removing keyframe\n", out.toString());
  }

  @Test
  public void testGenerateKeyframe() {
    JButton button = new JButton("get frame");
    button.setActionCommand("Get keyframe");

    ActionEvent e = new ActionEvent(button, 1, "Get keyframe");

    controller.run();
    ((MockEditorView) mock).actionPerformed(e);

    assertEquals("Adding action listener\n"
        + "Setting bounds\n"
        + "Setting commands\n"
        + "Setting shapes\n"
        + "Displaying view\n"
        + "Getting keyframe\n", out.toString());
  }

  @Test
  public void testButtonWorksWithEditorView() {

    JButton button = new JButton("increase speed");
    button.setActionCommand("increase speed");

    ActionEvent increaseSpeed = new ActionEvent(button, 1, "increase speed");

    JButton button2 = new JButton("decrease speed");
    button2.setActionCommand("decrease speed");

    ActionEvent decreaseSpeed = new ActionEvent(button2, 1, "decrease speed");

    buttonClickedMap
        .put("increase speed", () -> mock.displaySpeed((((MockEditorView) mock).getSpeed() + 1)));
    buttonClickedMap
        .put("decrease speed", () -> mock.displaySpeed((((MockEditorView) mock).getSpeed() - 1)));

    b.setButtonClickedActionMap(buttonClickedMap);

    b.actionPerformed(increaseSpeed);
    assertEquals(2, ((MockEditorView) mock).getSpeed());

    b.actionPerformed(increaseSpeed);
    assertEquals(3, ((MockEditorView) mock).getSpeed());

    b.actionPerformed(decreaseSpeed);
    assertEquals(2, ((MockEditorView) mock).getSpeed());

    assertEquals("Adding action listener\n"
        + "Displaying speed 2\n"
        + "Displaying speed 3\n"
        + "Displaying speed 2\n", out.toString());

  }
}