package cs3500.animator.controller;

import cs3500.animator.model.AnimatorModel;
import cs3500.animator.model.frame.Frame;
import cs3500.animator.model.frame.IFrame;
import cs3500.animator.model.shape.IShape;
import cs3500.animator.model.shape.Position2D;
import cs3500.animator.model.shape.ShapeFactory;
import cs3500.animator.view.IInteractiveView;
import cs3500.animator.view.IView;
import cs3500.animator.view.editor.EditorView;
import cs3500.animator.view.visual.VisualView;
import cs3500.animator.view.visual.draw.DrawCommand;
import cs3500.animator.view.visual.draw.DrawEllipse;
import cs3500.animator.view.visual.draw.DrawRectangle;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Class representing an controller for a single animation application. The view uses MVC design,
 * taking in a model and a view, and passes the bounds and shapes of the model to the view. It also
 * takes in a speed and is responsible for updating the ticks of the visual view.
 */
public class AnimatorController implements IAnimatorController {

  private final AnimatorModel model;
  private final IView view;
  private int counter;
  private int speed;
  private Timer timer;
  private boolean loops;
  private boolean isRunning;

  /**
   * Construct a controller object with the given view, model and ticks per second.
   *
   * @param view  Non null {@link IView} object to display output
   * @param model Non null {@link AnimatorModel} object to run animation functionality
   * @param speed Positive integer representing ticks per second
   * @throws NullPointerException if view, model, in or out is null
   */
  public AnimatorController(IView view, AnimatorModel model, int speed) {
    Objects.requireNonNull(view);
    Objects.requireNonNull(model);

    this.view = view;
    this.model = model;
    this.speed = speed;
    this.loops = false;
    if (this.view instanceof EditorView) {
      configureButtonListener();
      ((EditorView) view).configureSlider(model.getLastTick(), new SliderChangeListener());
      ((IInteractiveView) view).updateLayerInterface(model.getShapesByLayer());
    }
    setTimer();
    this.counter = 0;
    isRunning = false;
  }


  /**
   * Set a new {@link Timer} that updates the counter at a certain tick per second specified by
   * {@code speed} and updates the model and view each tick.
   */
  private void setTimer() {
    ActionListener timerListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (counter < model.getLastTick()) {
          model.update(counter);
          view.setShapes(model.getShapes());
          ((EditorView) view).setSliderTick(counter);
          view.displayOutput();
          counter++;
        }
        if (loops) {
          if (counter >= model.getLastTick()) {
            counter = 0;
            timer.restart();
          }
        }
      }
    };
    this.timer = new Timer(1000 / this.speed, timerListener);
  }

  @Override
  public void run() {

    int[] bounds = model.getBounds();

    view.passBounds(bounds[0], bounds[1], bounds[2], bounds[3]);

    // pass commands to the view
    try {
      view.setCommands(this.getDrawCommands(bounds));
    } catch (Exception ignored) {
    }

    if (view instanceof VisualView) {
      timer.start();
    } else {
      view.setShapes(model.getShapes());
      view.displayOutput();
    }
  }

  /**
   * Get the list of known {@link DrawCommand}s. In other words get the methods that allow the view
   * to draw all of the currently supported shapes. As more shapes are added more DrawCommands will
   * need to be added.
   *
   * @param bounds bounds of the model
   * @return a mapping of strings representing shape types to DrawCommands
   */
  private Map<String, DrawCommand> getDrawCommands(int[] bounds) {
    // create list of known draw commands
    Map<String, DrawCommand> knownCommands = new HashMap<>();
    knownCommands.put("rectangle", new DrawRectangle(bounds[0], bounds[1], speed));
    knownCommands.put("ellipse", new DrawEllipse(bounds[0], bounds[1], speed));
    return knownCommands;
  }

  /**
   * Configure the button listener with a map of string commands to {@link Runnable}s.
   */
  private void configureButtonListener() {
    Map<String, Runnable> buttonClickedMap = new HashMap<>();
    ButtonListener buttonListener = new ButtonListener();

    buttonClickedMap.put("Start", () -> {
          timer.start();
          ((IInteractiveView) view).displaySpeed(speed);
          view.resetFocus();
          isRunning = true;
        }
    );

    buttonClickedMap.put("Play", () -> {
          if (counter != 0) {
            if (!(timer.isRunning())) {
              timer.start();
            }
            view.resetFocus();
            isRunning = true;
          }
        }
    );

    buttonClickedMap.put("Pause", () -> {
          this.timer.stop();
          view.resetFocus();
          isRunning = false;
        }
    );
    buttonClickedMap.put("Restart", () -> {
      this.counter = 0;
      this.timer.restart();
      view.resetFocus();
    });
    buttonClickedMap.put("Enable Loop", () -> {
      this.loops = true;
      view.resetFocus();
    });
    buttonClickedMap.put("Disable Loop", () -> {
      this.loops = false;
      view.resetFocus();
    });

    buttonClickedMap.put("Increase speed", () -> {
      this.speed++;
      this.timer.setDelay(1000 / this.speed);
      ((IInteractiveView) view).displaySpeed(speed);
      view.resetFocus();
    });

    buttonClickedMap.put("Decrease speed", () -> {
      if (this.speed > 1) {
        this.speed--;
        this.timer.setDelay(1000 / this.speed);
      }
      ((IInteractiveView) view).displaySpeed(speed);
      view.resetFocus();
    });

    buttonClickedMap.put("Add shape", () -> {
      try {
        Scanner in = new Scanner(((IInteractiveView) view).getCommand("Add shape"));
        String name = in.next();
        String type = in.next();
        if (in.hasNextInt()) {
          int layer = in.nextInt();
          model.addShape(ShapeFactory.createShape(name, type, layer));
        } else {
          model.addShape(ShapeFactory.createShape(name, type));
        }
        view.setShapes(model.getShapes());
        ((IInteractiveView) view).updateLayerInterface(model.getShapesByLayer());
      } catch (Exception e) {
        IView.showErrorMessage(e.getMessage());
      }
      view.resetFocus();
    });

    buttonClickedMap.put("Remove shape", () -> {
      try {
        model.removeShape(((IInteractiveView) view).getCommand("Remove shape"));
        view.setShapes(model.getShapes());
        ((IInteractiveView) view).updateLayerInterface(model.getShapesByLayer());
      } catch (Exception e) {
        IView.showErrorMessage(e.getMessage());
      }
      view.resetFocus();
    });

    buttonClickedMap.put("Add keyframe", () -> {
      try {
        model.addKeyFrame(parseFrame(((IInteractiveView) view).getCommand("Add keyframe")));
        view.setShapes(model.getShapes());
        ((IInteractiveView) view).clearTextFields();
      } catch (Exception e) {
        IView.showErrorMessage(e.getLocalizedMessage());
      }
      view.resetFocus();
    });

    buttonClickedMap.put("Remove keyframe", () -> {
      try {
        IFrame toRemove = parseFrame(((IInteractiveView) view).getCommand("Remove keyframe"));
        model.removeKeyFrame(toRemove);
        view.setShapes(model.getShapes());
      } catch (Exception e) {
        IView.showErrorMessage(e.getMessage());
      }
      view.resetFocus();
    });

    buttonClickedMap.put("Get keyframe", () -> {
      try {
        Scanner in = new Scanner(((IInteractiveView) view).getCommand("Generate keyframe"));
        IShape keyframeAt =
            model.getShapes().get(in.next()).getStateAt(in.nextInt());
        view.setShapes(model.getShapes());
        ((IInteractiveView) view).populateTextFields(keyframeAt);
      } catch (Exception e) {
        IView.showErrorMessage(e.getMessage());
      }
      view.resetFocus();
    });

    buttonClickedMap.put("Change visible frames", () -> {
      view.setShapes(model.getShapes());
      view.displayOutput();
      view.resetFocus();
    });

    buttonClickedMap.put("Change Layer", () -> {
      try {
        Scanner in = new Scanner(((IInteractiveView) view).getCommand("Change layer"));
        String name = in.next();
        int layer = in.nextInt();
        model.changeShapeLayer(name, layer);
        view.setShapes(model.getShapes());
        ((IInteractiveView) view).updateLayerInterface(model.getShapesByLayer());
        view.displayOutput();
        view.resetFocus();
      } catch (Exception e) {
        IView.showErrorMessage(e.getMessage());
      }
    });

    buttonClickedMap.put("Swap layers", () -> {
      try {
        Scanner in = new Scanner(((IInteractiveView) view).getCommand("Swap layers"));
        int l1 = in.nextInt();
        int l2 = in.nextInt();
        model.reorder(l1, l2);
        view.setShapes(model.getShapes());
        ((IInteractiveView) view).updateLayerInterface(model.getShapesByLayer());
        view.displayOutput();
        view.resetFocus();
      } catch (Exception e) {
        IView.showErrorMessage(e.getMessage());
      }
    });

    buttonClickedMap.put("Interacting with layer list", () -> {
      ((IInteractiveView) view).updateLayerInterface(model.getShapesByLayer());
    });

    buttonListener.setButtonClickedActionMap(buttonClickedMap);
    view.addActionListener(buttonListener);
  }

  /**
   * Given a string representation of a {@link IFrame} object, construct an instance with the given
   * quantities in the string.
   *
   * @param message string representation of the frame
   * @return {@link IFrame} represented in the string
   */
  private IFrame parseFrame(String message) {
    Scanner in = new Scanner(message);
    String name = in.next();
    int t = Integer.parseInt(in.next());

    Position2D pos = new Position2D(Double.parseDouble(in.next()), Double.parseDouble(in.next()));
    Color color = new Color(Integer.parseInt(in.next()), Integer.parseInt(in.next()),
        Integer.parseInt(in.next()));
    double w = Double.parseDouble(in.next());
    double h = Double.parseDouble(in.next());
    return new Frame(t, name, pos, color, w, h);
  }

  /**
   * Class used to handle the action of moving the animation slider.
   */
  private class SliderChangeListener implements ChangeListener {
    @Override
    public void stateChanged(ChangeEvent e) {
      JSlider slider = (JSlider) e.getSource();
      if (slider.getValueIsAdjusting()) {
        timer.stop();
      }
      counter = slider.getValue();
      model.update(counter);
      view.setShapes(model.getShapes());
      view.displayOutput();
    }
  }
}