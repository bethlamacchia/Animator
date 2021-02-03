import cs3500.animator.model.shape.IShape;
import cs3500.animator.view.IInteractiveView;
import cs3500.animator.view.visual.draw.DrawCommand;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.swing.event.ChangeListener;

/**
 * A mock of the editor view used for testing purposes.
 */
public class MockEditorView implements IInteractiveView, ActionListener {

  public int speed;
  private final Appendable out;


  /**
   * Construct a new MockEditorView object with the given Appendable as the output.
   *
   * @param out Appendable output
   */
  MockEditorView(Appendable out) {
    this.speed = 1;
    this.out = out;
  }


  /**
   * Return the mock view's speed. Used for testing purposes.
   */
  public int getSpeed() {
    return this.speed;
  }

  /**
   * Get the appendable output.
   *
   * @return the appendable as a string.
   */
  public String returnOutput() {
    return out.toString();
  }

  @Override
  public String getCommand(String option) {
    try {
      out.append("Getting command: " + option + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }

  @Override
  public void populateTextFields(IShape s) {
    try {
      out.append("Populating text fields\n");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void clearTextFields() {
    try {
      out.append("Clearing text fields\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void displaySpeed(int speed) {
    this.speed = speed;
    try {
      out.append("Displaying speed " + speed + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setShapes(Map<String, IShape> shapes) {
    try {
      out.append("Setting shapes\n");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }


  @Override
  public void displayOutput() {
    try {
      out.append("Displaying view\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void passBounds(int x, int y, int width, int height) {
    try {
      out.append("Setting bounds\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setOutput(Appendable a) {
    // suppress
  }

  @Override
  public void setCommands(Map<String, DrawCommand> knownCommands) {
    try {
      out.append("Setting commands\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void resetFocus() {
    try {
      out.append("Resetting focus\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void addActionListener(ActionListener actionListener) {
    try {
      out.append("Adding action listener\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void configureSlider(int max, ChangeListener c) {
    // stub
  }


  @Override
  public void setSliderTick(int tick) {
    // stub
  }

  @Override
  public void updateLayerInterface(List<Map<String, IShape>> shapesByLayer) {
    //stub
  }


  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "Add shape":
        try {
          out.append("Adding shape\n");
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
        break;
      case "Add keyframe":
        try {
          out.append("Adding keyframe\n");
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
        break;
      case "Remove shape":
        try {
          out.append("Removing shape\n");
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
        break;
      case "Remove keyframe":
        try {
          out.append("Removing keyframe\n");
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
        break;
      case "Get keyframe":
        try {
          out.append("Getting keyframe\n");
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
        break;
      case "Change visible frames":
        try {
          out.append("Changing visible frames\n");
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
        break;
      default:
        break;
    }
  }
}
