import static org.junit.Assert.assertEquals;

import cs3500.animator.model.AnimatorModel;
import cs3500.animator.model.AnimatorModelImpl;
import cs3500.animator.model.motion.ColorMotion;
import cs3500.animator.model.motion.Motion;
import cs3500.animator.model.motion.Rotation;
import cs3500.animator.model.shape.IShape;
import cs3500.animator.model.shape.Position2D;
import cs3500.animator.model.shape.Rectangle;
import cs3500.animator.util.AnimationBuilder;
import cs3500.animator.view.IView;
import cs3500.animator.view.SVGView;
import cs3500.animator.view.visual.draw.DrawCommand;
import cs3500.animator.view.visual.draw.DrawEllipse;
import cs3500.animator.view.visual.draw.DrawRectangle;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * Test rotation functionality in the model, including that adding rotations via model or builder
 * works correctly, shapes have correct headings, and SVG output for a rotation is correct.
 */
public class RotationTest {


  @Test
  public void testAddingRotationsInModel() {

    IShape redR = new Rectangle(Color.RED, new Position2D(200, 200), 50,
        100, "R");
    AnimatorModel model = new AnimatorModelImpl();
    model.addShape(redR);

    Motion rColorChange1 = new ColorMotion(1, 10, "R", Color.RED, Color.BLUE);
    Motion rotationR = new Rotation(1, 10, "R", 0, 180);
    model.addMotions(Arrays.asList(rColorChange1, rotationR));

    assertEquals(model.getShapes().get("R").getKeyFrames().size(), 2);

    assertEquals(Color.RED, model.getShapes().get("R").getColor());
    assertEquals(0, model.getShapes().get("R").getHeading(), .1);

    model.setStateTo(10);

    //assertEquals(Color.BLUE, model.getShapes().get("R").getColor());
    assertEquals(180, model.getShapes().get("R").getHeading(), .1);

  }

  @Test
  public void testAddingRotationsInBuilder() {

    AnimationBuilder<AnimatorModel> test1 = new AnimatorModelImpl.Builder().setBounds(0, 0,
        100, 100).declareShape("R", "rectangle")
        .addMotion("R", 1, 200, 200, 50, 100, 255, 0, 0, 0,
            10, 200, 10, 50, 100, 255, 0, 0, 0)
        .addMotion("R", 10, 200, 10, 50, 100, 255, 0, 0, 0,
            20, 200, 10, 50, 100, 255, 0, 0, 180);
    AnimatorModel model = test1.build();

    assertEquals(model.getShapes().get("R").getKeyFrames().size(), 3);

    assertEquals(200, model.getShapes().get("R").getPosition().getY(), .1);
    assertEquals(0, model.getShapes().get("R").getHeading(), .1);

    model.setStateTo(10);

    assertEquals(10, model.getShapes().get("R").getPosition().getY(), .1);
    assertEquals(0, model.getShapes().get("R").getHeading(), .1);

    model.setStateTo(20);

    assertEquals(10, model.getShapes().get("R").getPosition().getY(), .1);
    assertEquals(180, model.getShapes().get("R").getHeading(), .1);
  }

  @Test
  public void testRotationSVG() {
    Appendable a = new StringBuilder();
    IView svgView = new SVGView(a);

    AnimationBuilder<AnimatorModel> test1 = new AnimatorModelImpl.Builder().setBounds(0, 0,
        100, 100).declareShape("R", "rectangle")
        .addMotion("R", 1, 200, 200, 50, 100, 255, 0, 0, 0,
            10, 200, 10, 50, 100, 255, 0, 0, 0)
        .addMotion("R", 10, 200, 10, 50, 100, 255, 0, 0, 0,
            20, 200, 10, 50, 100, 255, 0, 0, 180);
    AnimatorModel model = test1.build();

    Map<String, DrawCommand> knownCommands = new HashMap<>();
    knownCommands.put("rectangle", new DrawRectangle(0, 0, 20));
    knownCommands.put("ellipse", new DrawEllipse(0, 0, 20));
    svgView.setCommands(knownCommands);
    svgView.setShapes(model.getShapes());
    svgView.displayOutput();
    assertEquals(
        "<svg viewBox=\"0 0 0 0\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "<rect id=\"R\" x=\"200.0\" y=\"200.0\" width=\"50.0\" height=\"100.0\" "
            + "fill=\"rgb(255,0,0)\" >\n"
            + "<set id=\"R\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"0\" "
            + "duration=\"0\" to=\"hidden\" >\n"
            + "</set>\n"
            + "<set id=\"R\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"0\" "
            + "to=\"visible\" >\n"
            + "</set>\n"
            + "<animate attributeType=\"xml\" begin=\"0.05\" dur=\"0.45\" attributeName=\"x\" "
            + "from=\"200.0\" to=\"200.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"0.05\" dur=\"0.45\" attributeName=\"y\" "
            + "from=\"200.0\" to=\"10.0\" fill=\"freeze\"/>\n"
            + "<animateTransform attributeType=\"xml\" type=\"rotate\" begin=\"0.50\" dur=\"0.50\" "
            + "attributeName=\"transform\" from=\"0 225 250\" to=\"180 225 250\" "
            + "fill=\"freeze\"/>\n"
            + "</rect>\n"
            + "</svg>", a.toString());
  }

}
