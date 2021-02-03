import static org.junit.Assert.assertEquals;

import cs3500.animator.controller.ButtonListener;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the ButtonListener class's methods.
 */
public class ButtonListenerTest {

  private ButtonListener b;

  @Before
  public void init() {
    b = new ButtonListener();
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
}