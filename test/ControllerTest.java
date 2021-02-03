import static org.junit.Assert.assertEquals;

import cs3500.animator.controller.AnimatorController;
import cs3500.animator.controller.IAnimatorController;
import cs3500.animator.model.AnimatorModel;
import cs3500.animator.model.frame.IFrame;
import cs3500.animator.model.motion.Motion;
import cs3500.animator.model.shape.IShape;
import cs3500.animator.model.shape.Position2D;
import cs3500.animator.model.shape.Rectangle;
import cs3500.animator.view.IView;
import cs3500.animator.view.visual.draw.DrawCommand;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/**
 * Class to test the methods and constructors of an {@link IAnimatorController} object.
 */
public class ControllerTest {

  @Test
  public void testController() {
    AnimatorModel model = new ModelMock();
    StringBuilder out = new StringBuilder();
    StringReader in = new StringReader("");
    IView view = new ViewMock(out);
    IAnimatorController controller = new AnimatorController(view, model, 1);
    controller.run();
    assertEquals("Bounds received\n"
        + "setting shapes\n"
        + "50.0\n", out.toString());
  }

  /**
   * Mock of a model. Only has one shape and the update method increments its width by 1 each tick
   */
  private final class ModelMock implements AnimatorModel {

    private final Map<String, IShape> shapes;

    public ModelMock() {
      this.shapes = new HashMap<>();
      shapes.put("R", new Rectangle(Color.RED, new Position2D(200, 200), 50, 100, "R"));
    }

    @Override
    public void addShape(IShape shape) throws IllegalArgumentException {
      // method stub, not needed for the purpose of the mock
    }

    @Override
    public void update(int ticks) throws IllegalArgumentException {
      this.shapes.get("R").setWidth(50 + ticks);
    }

    @Override
    public void setStateTo(int ticks) throws IllegalArgumentException {
      this.update(ticks);
    }

    @Override
    public Map<String, IShape> getShapes() {
      return shapes;
    }

    @Override
    public void addMotions(List<Motion> motions) {
      // method stub, not needed for the purpose of the mock
    }

    @Override
    public void addMotion(Motion m) {
      // method stub, not needed for the purpose of the mock
    }

    @Override
    public int[] getBounds() {
      return new int[4];
    }

    @Override
    public void setKeyFrames(Map<String, List<IFrame>> keyFrames) {
      // method stub, not needed for the purpose of the mock
    }

    @Override
    public void removeShape(String s) {
      // method stub, not needed for the purpose of the mock
    }

    @Override
    public void removeMotion(Motion m) {
      // method stub, not needed for the purpose of the mock
    }

    @Override
    public int getLastTick() {
      return 10;
    }

    @Override
    public void addKeyFrame(IFrame f) {
      // method stub, not needed for the purpose of the mock
    }

    @Override
    public void removeKeyFrame(IFrame f) {
      // method stub, not needed for the purpose of the mock
    }

    @Override
    public List<Map<String, IShape>> getShapesByLayer() {
      return null;
    }

    @Override
    public void addShapeToLayer(String shape, int layer) {
      // method stub, not needed for the purpose of the mock
    }

    @Override
    public void deleteLayer(int layer) {
      // method stub, not needed for the purpose of the mock
    }

    @Override
    public void addLayer(int layer) {
      // method stub, not needed for the purpose of the mock
    }

    @Override
    public void reorder(int layer1, int layer2) {
      // method stub, not needed for the purpose of the mock
    }

    @Override
    public void changeShapeLayer(String name, int layer) {
      // method stub, not needed for the purpose of the mock
    }
  }

  /**
   * Mock of a view. Writes output to an appendable all signaling that it received the proper
   * input.
   */
  private final class ViewMock implements IView {

    private final Appendable out;
    private List<IShape> shapes;

    public ViewMock(Appendable out) {
      this.shapes = new ArrayList<>();
      this.out = out;
    }

    @Override
    public void setShapes(Map<String, IShape> shapes) {
      try {
        out.append("setting shapes\n");
        this.shapes = new ArrayList<>(shapes.values());
      } catch (IOException ioe) {
        throw new IllegalArgumentException("Append failed");
      }
    }


    @Override
    public void displayOutput() {
      try {
        out.append(String.valueOf(shapes.get(0).getWidth())).append("\n");
      } catch (IOException ioe) {
        throw new IllegalArgumentException("Append failed");
      }
    }

    @Override
    public void passBounds(int x, int y, int width, int height) {
      try {
        out.append("Bounds received\n");
      } catch (IOException ioe) {
        throw new IllegalArgumentException("Append failed");
      }
    }

    @Override
    public void setOutput(Appendable a) {
      // method stub, not needed for the purpose of the mock
    }

    @Override
    public void setCommands(Map<String, DrawCommand> knownCommands) {
      // method stub, not needed for the purpose of the mock
    }

    @Override
    public void resetFocus() {
      // method stub, not needed for the purpose of the mock
    }

    @Override
    public void addActionListener(ActionListener listener) {
      // method stub, not needed for the purpose of the mock
    }

  }
}
