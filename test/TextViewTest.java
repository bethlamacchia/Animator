import static org.junit.Assert.assertEquals;

import cs3500.animator.model.AnimatorModel;
import cs3500.animator.model.AnimatorModelImpl;
import cs3500.animator.model.motion.MoveMotion;
import cs3500.animator.model.shape.Ellipse;
import cs3500.animator.model.shape.Position2D;
import cs3500.animator.util.AnimationBuilder;
import cs3500.animator.util.AnimationReader;
import cs3500.animator.view.IView;
import cs3500.animator.view.TextView;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import org.junit.Test;

/**
 * Test the methods and constructor of a {@link TextView} object.
 */
public class TextViewTest {

  @Test
  public void testTextView() {
    AnimationBuilder<AnimatorModel> test1 = new AnimatorModelImpl.Builder().setBounds(0, 0,
        100, 100).declareShape("R", "rectangle")
        .addMotion("R", 1, 200, 200, 50, 100, 255, 0, 0, 0,
            10, 200, 10, 50, 100, 255, 0, 0, 0);
    AnimatorModel model = test1.build();

    assertEquals(model.getShapes().get("R").getKeyFrames().size(), 2);
    assertEquals(model.getShapes().get("R").getKeyFrames().size(), 2);

    Appendable a = new StringBuilder();
    IView txtView = new TextView(a);

    txtView.setShapes(model.getShapes());
    txtView.displayOutput();
    assertEquals("canvas 0 0 0 0\n"
        + "shape R rectangle\n"
        + "motion R 1 200 200 50 100 255 0 0 10 200 10 50 100 255 0 0\n", a.toString());
  }

  @Test
  public void testSmallDemoText() throws FileNotFoundException {
    AnimationBuilder<AnimatorModel> builder = new AnimatorModelImpl.Builder();
    File f = new File("smalldemo.txt");
    String path = f.getAbsolutePath();
    AnimatorModel model = new AnimatorModelImpl();
    FileReader input = new FileReader(path);
    model = AnimationReader.parseFile(input, builder);

    Appendable a = new StringBuilder();
    IView txtView = new TextView(a);

    txtView.setShapes(model.getShapes());
    txtView.displayOutput();

    assertEquals("canvas 0 0 0 0\n"
        + "shape R rectangle\n"
        + "motion R 1 200 200 50 100 255 0 0 10 200 200 50 100 255 0 0\n"
        + "motion R 10 200 200 50 100 255 0 0 50 300 300 50 100 255 0 0\n"
        + "motion R 50 300 300 50 100 255 0 0 51 300 300 50 100 255 0 0\n"
        + "motion R 51 300 300 50 100 255 0 0 70 300 300 25 100 255 0 0\n"
        + "motion R 70 300 300 25 100 255 0 0 100 200 200 25 100 255 0 0\n"
        + "shape C ellipse\n"
        + "motion C 6 440 70 120 60 0 0 255 20 440 70 120 60 0 0 255\n"
        + "motion C 20 440 70 120 60 0 0 255 50 440 250 120 60 0 0 255\n"
        + "motion C 50 440 250 120 60 0 0 255 70 440 370 120 60 0 170 85\n"
        + "motion C 70 440 370 120 60 0 170 85 80 440 370 120 60 0 255 0\n"
        + "motion C 80 440 370 120 60 0 255 0 100 440 370 120 60 0 255 0\n", a.toString());

  }

  @Test
  public void testOneShapeNoMotions() {
    AnimationBuilder<AnimatorModel> test1 = new AnimatorModelImpl.Builder().setBounds(0, 0,
        100, 100).declareShape("R", "rectangle");
    AnimatorModel model = test1.build();
    Appendable a = new StringBuilder();
    IView txtView = new TextView(a);

    txtView.setShapes(model.getShapes());
    txtView.displayOutput();
    assertEquals("canvas 0 0 0 0\n"
        + "shape R rectangle\n", a.toString());
  }

  @Test
  public void testEmptyModel() {
    AnimationBuilder<AnimatorModel> test1 = new AnimatorModelImpl.Builder().setBounds(0, 0,
        100, 100);
    AnimatorModel model = test1.build();
    Appendable a = new StringBuilder();
    IView txtView = new TextView(a);

    txtView.setShapes(model.getShapes());
    txtView.displayOutput();
    assertEquals("canvas 0 0 0 0\n", a.toString());
  }

  @Test
  public void testBuilderAndManualAdding() throws FileNotFoundException {

    AnimationBuilder<AnimatorModel> builder = new AnimatorModelImpl.Builder();
    File f = new File("smalldemo.txt");
    String path = f.getAbsolutePath();
    AnimatorModel model = new AnimatorModelImpl();
    FileReader input = new FileReader(path);
    model = AnimationReader.parseFile(input, builder);

    model.addShape(new Ellipse(Color.RED, new Position2D(200, 200), 50, 100, "O1"));
    model.addMotions(Arrays.asList(new MoveMotion(1, 10, "O1", new Position2D(200, 200),
        new Position2D(10, 10))));


    Appendable a = new StringBuilder();
    IView txtView = new TextView(a);

    txtView.setShapes(model.getShapes());
    txtView.displayOutput();
    assertEquals("canvas 0 0 0 0\n"
        + "shape R rectangle\n"
        + "motion R 1 200 200 50 100 255 0 0 10 200 200 50 100 255 0 0\n"
        + "motion R 10 200 200 50 100 255 0 0 50 300 300 50 100 255 0 0\n"
        + "motion R 50 300 300 50 100 255 0 0 51 300 300 50 100 255 0 0\n"
        + "motion R 51 300 300 50 100 255 0 0 70 300 300 25 100 255 0 0\n"
        + "motion R 70 300 300 25 100 255 0 0 100 200 200 25 100 255 0 0\n"
        + "shape C ellipse\n"
        + "motion C 6 440 70 120 60 0 0 255 20 440 70 120 60 0 0 255\n"
        + "motion C 20 440 70 120 60 0 0 255 50 440 250 120 60 0 0 255\n"
        + "motion C 50 440 250 120 60 0 0 255 70 440 370 120 60 0 170 85\n"
        + "motion C 70 440 370 120 60 0 170 85 80 440 370 120 60 0 255 0\n"
        + "motion C 80 440 370 120 60 0 255 0 100 440 370 120 60 0 255 0\n"
        + "shape O1 ellipse\n"
        + "motion O1 1 200 200 50 100 255 0 0 10 10 10 50 100 255 0 0\n", a.toString());
  }
}
