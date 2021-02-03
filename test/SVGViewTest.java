import static org.junit.Assert.assertEquals;

import cs3500.animator.model.AnimatorModel;
import cs3500.animator.model.AnimatorModelImpl;
import cs3500.animator.model.motion.MoveMotion;
import cs3500.animator.model.shape.Position2D;
import cs3500.animator.util.AnimationBuilder;
import cs3500.animator.util.AnimationReader;
import cs3500.animator.view.IView;
import cs3500.animator.view.SVGView;
import cs3500.animator.view.visual.draw.DrawCommand;
import cs3500.animator.view.visual.draw.DrawEllipse;
import cs3500.animator.view.visual.draw.DrawRectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * Test that an SVG view provides the appropriate output.
 */
public class SVGViewTest {

  // test SVG with a different speed (20)
  @Test
  public void testModelAndBuilderSVG20Speed() {

    AnimationBuilder<AnimatorModel> test1 = new AnimatorModelImpl.Builder().setBounds(0, 0,
        100, 100).declareShape("R", "rectangle");
    AnimatorModel model = test1.build();
    model.addMotion(new MoveMotion(1, 10, "R", new Position2D(0, 0),
        new Position2D(20, 20)));

    Appendable a = new StringBuilder();
    IView svgView = new SVGView(a);

    Map<String, DrawCommand> knownCommands = new HashMap<>();
    knownCommands.put("rectangle", new DrawRectangle(0, 0, 20));
    knownCommands.put("ellipse", new DrawEllipse(0, 0, 20));
    svgView.setCommands(knownCommands);
    svgView.setShapes(model.getShapes());
    svgView.displayOutput();
    assertEquals(
        "<svg viewBox=\"0 0 0 0\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "<rect id=\"R\" x=\"0.0\" y=\"0.0\" width=\"0.0\" height=\"0.0\" fill=\"rgb(0,0,0)\" "
            + ">\n"
            + "<set id=\"R\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"0\" "
            + "duration=\"0\" to=\"hidden\" >\n"
            + "</set>\n"
            + "<set id=\"R\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"0\" "
            + "to=\"visible\" >\n"
            + "</set>\n"
            + "<animate attributeType=\"xml\" begin=\"0.05\" dur=\"0.45\" attributeName=\"x\" "
            + "from=\"0.0\" to=\"20.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"0.05\" dur=\"0.45\" attributeName=\"y\" "
            + "from=\"0.0\" to=\"20.0\" fill=\"freeze\"/>\n"
            + "</rect>\n"
            + "</svg>", a.toString());
  }

  // Test for getting the description of an empty view that has not shapes and animations
  @Test
  public void testGetDescriptionEmptyView() {

    AnimatorModel model = new AnimatorModelImpl();

    Appendable a = new StringBuilder();
    IView svgView = new SVGView(a);

    Map<String, DrawCommand> knownCommands = new HashMap<>();
    knownCommands.put("rectangle", new DrawRectangle(0, 0, 1));
    knownCommands.put("ellipse", new DrawEllipse(0, 0, 1));
    svgView.setCommands(knownCommands);
    svgView.setShapes(model.getShapes());
    svgView.displayOutput();
    assertEquals(
        "<svg viewBox=\"0 0 0 0\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "</svg>", a.toString());
  }


  @Test
  public void testAddShapeBuilderSVGView() {

    AnimationBuilder<AnimatorModel> test1 = new AnimatorModelImpl.Builder().setBounds(0, 0,
        100, 100).declareShape("R", "rectangle");
    AnimatorModel model = test1.build();

    Appendable a = new StringBuilder();
    IView svgView = new SVGView(a);

    Map<String, DrawCommand> knownCommands = new HashMap<>();
    knownCommands.put("rectangle", new DrawRectangle(0, 0, 1));
    knownCommands.put("ellipse", new DrawEllipse(0, 0, 1));
    svgView.setCommands(knownCommands);
    svgView.setShapes(model.getShapes());
    svgView.displayOutput();
    assertEquals(
        "<svg viewBox=\"0 0 0 0\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "<rect id=\"R\" x=\"0.0\" y=\"0.0\" width=\"0.0\" height=\"0.0\" fill=\"rgb(0,0,0)\" "
            + ">\n"
            + "<set id=\"R\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"0\" "
            + "duration=\"2147483647\" to=\"hidden\" >\n"
            + "</set>\n"
            + "<set id=\"R\" attributeName=\"visibility\" attributeType=\"xml\" "
            + "begin=\"2147483647\" to=\"visible\" >\n"
            + "</set>\n"
            + "</rect>\n"
            + "</svg>", a.toString());
  }

  @Test
  public void testSVGOneMotionView() {
    AnimationBuilder<AnimatorModel> test1 = new AnimatorModelImpl.Builder().setBounds(0, 0,
        100, 100).declareShape("R", "rectangle")
        .addMotion("R", 1, 200, 200, 50, 100, 255, 0, 0, 0,
            10, 200, 10, 50, 100, 255, 0, 0, 0);
    AnimatorModel model = test1.build();
    Appendable a = new StringBuilder();
    IView svgView = new SVGView(a);

    Map<String, DrawCommand> knownCommands = new HashMap<>();
    knownCommands.put("rectangle", new DrawRectangle(0, 0, 1));
    knownCommands.put("ellipse", new DrawEllipse(0, 0, 1));

    svgView.setCommands(knownCommands);
    svgView.setShapes(model.getShapes());
    svgView.displayOutput();
    assertEquals(
        "<svg viewBox=\"0 0 0 0\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "<rect id=\"R\" x=\"200.0\" y=\"200.0\" width=\"50.0\" height=\"100.0\" "
            + "fill=\"rgb(255,0,0)\" >\n"
            + "<set id=\"R\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"0\" "
            + "duration=\"1\" to=\"hidden\" >\n"
            + "</set>\n"
            + "<set id=\"R\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"1\""
            + " to=\"visible\" >\n"
            + "</set>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" attributeName=\"x\" "
            + "from=\"200.0\" to=\"200.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" attributeName=\"y\" "
            + "from=\"200.0\" to=\"10.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" attributeName=\"fill\" "
            + "from=\"rgb(255,0,0)\" to=\"rgb(255,0,0)\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" attributeName=\"width\" "
            + "from=\"50.0\" to=\"50.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" attributeName=\"height\" "
            + "from=\"100.0\" to=\"100.0\" fill=\"freeze\"/>\n"
            + "</rect>\n"
            + "</svg>", a.toString());
  }


  @Test
  public void testSVGMultipleShapesAndMotions() {
    AnimationBuilder<AnimatorModel> test1 = new AnimatorModelImpl.Builder().setBounds(0, 0,
        100, 100).declareShape("R", "rectangle").declareShape("C",
        "ellipse")
        .addMotion("R", 1, 200, 200, 50, 100, 255, 0, 0, 0,
            10, 200, 10, 50, 100, 255, 0, 0, 0)
        .addMotion("C", 1, 200, 200, 50, 100, 255, 0, 0, 0,
            10, 200, 10, 50, 100, 255, 0, 0, 0);
    AnimatorModel model = test1.build();
    Appendable a = new StringBuilder();
    IView svgView = new SVGView(a);

    Map<String, DrawCommand> knownCommands = new HashMap<>();
    knownCommands.put("rectangle", new DrawRectangle(0, 0, 1));
    knownCommands.put("ellipse", new DrawEllipse(0, 0, 1));

    svgView.setCommands(knownCommands);
    svgView.setShapes(model.getShapes());
    svgView.displayOutput();
    assertEquals(
        "<svg viewBox=\"0 0 0 0\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "<rect id=\"R\" x=\"200.0\" y=\"200.0\" width=\"50.0\" height=\"100.0\" "
            + "fill=\"rgb(255,0,0)\" >\n"
            + "<set id=\"R\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"0\" "
            + "duration=\"1\" to=\"hidden\" >\n"
            + "</set>\n"
            + "<set id=\"R\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"1\" "
            + "to=\"visible\" >\n"
            + "</set>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" attributeName=\"x\" "
            + "from=\"200.0\" to=\"200.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" attributeName=\"y\" "
            + "from=\"200.0\" to=\"10.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" attributeName=\"fill\" "
            + "from=\"rgb(255,0,0)\" to=\"rgb(255,0,0)\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" attributeName=\"width\" "
            + "from=\"50.0\" to=\"50.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" attributeName=\"height\" "
            + "from=\"100.0\" to=\"100.0\" fill=\"freeze\"/>\n"
            + "</rect>\n"
            + "<ellipse id=\"C\" cx=\"200.0\" cy=\"200.0\" rx=\"50.0\" ry=\"100.0\" "
            + "fill=\"rgb(255,0,0)\" >\n"
            + "<set id=\"C\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"0\" "
            + "duration=\"1\" to=\"hidden\" >\n"
            + "</set>\n"
            + "<set id=\"C\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"1\" "
            + "to=\"visible\" >\n"
            + "</set>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" attributeName=\"cx\" "
            + "from=\"200.0\" to=\"200.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" attributeName=\"cy\""
            + " from=\"200.0\" to=\"10.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" attributeName=\"fill\" "
            + "from=\"rgb(255,0,0)\" to=\"rgb(255,0,0)\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" attributeName=\"rx\""
            + " from=\"50.0\" to=\"50.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" attributeName=\"ry\" "
            + "from=\"100.0\" to=\"100.0\" fill=\"freeze\"/>\n"
            + "</ellipse>\n"
            + "</svg>", a.toString());
  }

  @Test
  public void testSmallDemoSVG() throws FileNotFoundException {
    AnimationBuilder<AnimatorModel> builder = new AnimatorModelImpl.Builder();
    File f = new File("smalldemo.txt");
    String path = f.getAbsolutePath();
    AnimatorModel model = new AnimatorModelImpl();
    FileReader input = new FileReader(path);
    model = AnimationReader.parseFile(input, builder);

    Appendable a = new StringBuilder();
    IView svgView = new SVGView(a);
    int[] bounds = model.getBounds();

    svgView.passBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
    Map<String, DrawCommand> knownCommands = new HashMap<>();
    knownCommands.put("rectangle", new DrawRectangle(0, 0, 1));
    knownCommands.put("ellipse", new DrawEllipse(0, 0, 1));

    svgView.setCommands(knownCommands);
    svgView.setShapes(model.getShapes());
    svgView.displayOutput();
    assertEquals(
        "<svg viewBox=\"200 70 360 360\" version=\"1.1\" "
            + "xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "<rect id=\"R\" x=\"200.0\" y=\"200.0\" width=\"50.0\" height=\"100.0\" "
            + "fill=\"rgb(255,0,0)\" >\n"
            + "<set id=\"R\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"0\" "
            + "duration=\"1\" to=\"hidden\" >\n"
            + "</set>\n"
            + "<set id=\"R\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"1\" "
            + "to=\"visible\" >\n"
            + "</set>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" attributeName=\"x\" "
            + "from=\"200.0\" to=\"200.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" attributeName=\"y\" "
            + "from=\"200.0\" to=\"200.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" attributeName=\"fill\" "
            + "from=\"rgb(255,0,0)\" to=\"rgb(255,0,0)\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" attributeName=\"width\" "
            + "from=\"50.0\" to=\"50.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" attributeName=\"height\""
            + " from=\"100.0\" to=\"100.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"10.00\" dur=\"40.00\" attributeName=\"x\""
            + " from=\"200.0\" to=\"300.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"10.00\" dur=\"40.00\" attributeName=\"y\" "
            + "from=\"200.0\" to=\"300.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"10.00\" dur=\"40.00\" attributeName=\"fill\" "
            + "from=\"rgb(255,0,0)\" to=\"rgb(255,0,0)\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"10.00\" dur=\"40.00\" attributeName=\"width\""
            + " from=\"50.0\" to=\"50.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"10.00\" dur=\"40.00\" "
            + "attributeName=\"height\" from=\"100.0\" to=\"100.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"50.00\" dur=\"1.00\" "
            + "attributeName=\"x\" from=\"300.0\" to=\"300.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"50.00\" dur=\"1.00\" "
            + "attributeName=\"y\" from=\"300.0\" to=\"300.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"50.00\" dur=\"1.00\" "
            + "attributeName=\"fill\" from=\"rgb(255,0,0)\" to=\"rgb(255,0,0)\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"50.00\" dur=\"1.00\" "
            + "attributeName=\"width\" from=\"50.0\" to=\"50.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"50.00\" dur=\"1.00\" "
            + "attributeName=\"height\" from=\"100.0\" to=\"100.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"51.00\" dur=\"19.00\" "
            + "attributeName=\"x\" from=\"300.0\" to=\"300.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"51.00\" dur=\"19.00\" "
            + "attributeName=\"y\" from=\"300.0\" to=\"300.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"51.00\" dur=\"19.00\" "
            + "attributeName=\"fill\" from=\"rgb(255,0,0)\" to=\"rgb(255,0,0)\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"51.00\" dur=\"19.00\" "
            + "attributeName=\"width\" from=\"50.0\" to=\"25.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"51.00\" dur=\"19.00\" "
            + "attributeName=\"height\" from=\"100.0\" to=\"100.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"70.00\" dur=\"30.00\" "
            + "attributeName=\"x\" from=\"300.0\" to=\"200.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"70.00\" dur=\"30.00\" "
            + "attributeName=\"y\" from=\"300.0\" to=\"200.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"70.00\" dur=\"30.00\" "
            + "attributeName=\"fill\" from=\"rgb(255,0,0)\" to=\"rgb(255,0,0)\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"70.00\" dur=\"30.00\" "
            + "attributeName=\"width\" from=\"25.0\" to=\"25.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"70.00\" dur=\"30.00\" "
            + "attributeName=\"height\" from=\"100.0\" to=\"100.0\" fill=\"freeze\"/>\n"
            + "</rect>\n"
            + "<ellipse id=\"C\" cx=\"440.0\" cy=\"70.0\" rx=\"120.0\" ry=\"60.0\""
            + " fill=\"rgb(0,255,255)\" >\n"
            + "<set id=\"C\" attributeName=\"visibility\" attributeType=\"xml\" "
            + "begin=\"0\" duration=\"6\" to=\"hidden\" >\n"
            + "</set>\n"
            + "<set id=\"C\" attributeName=\"visibility\" attributeType=\"xml\""
            + " begin=\"6\" to=\"visible\" >\n"
            + "</set>\n"
            + "<animate attributeType=\"xml\" begin=\"6.00\" dur=\"14.00\""
            + " attributeName=\"cx\" from=\"440.0\" to=\"440.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"6.00\" dur=\"14.00\" "
            + "attributeName=\"cy\" from=\"70.0\" to=\"70.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"6.00\" dur=\"14.00\" "
            + "attributeName=\"fill\" from=\"rgb(0,0,255)\" to=\"rgb(0,0,255)\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"6.00\" dur=\"14.00\" "
            + "attributeName=\"rx\" from=\"120.0\" to=\"120.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"6.00\" dur=\"14.00\" "
            + "attributeName=\"ry\" from=\"60.0\" to=\"60.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"20.00\" dur=\"30.00\" "
            + "attributeName=\"cx\" from=\"440.0\" to=\"440.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"20.00\" dur=\"30.00\" "
            + "attributeName=\"cy\" from=\"70.0\" to=\"250.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"20.00\" dur=\"30.00\" "
            + "attributeName=\"fill\" from=\"rgb(0,0,255)\" to=\"rgb(0,0,255)\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"20.00\" dur=\"30.00\" "
            + "attributeName=\"rx\" from=\"120.0\" to=\"120.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"20.00\" dur=\"30.00\" "
            + "attributeName=\"ry\" from=\"60.0\" to=\"60.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"50.00\" dur=\"20.00\" "
            + "attributeName=\"cx\" from=\"440.0\" to=\"440.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"50.00\" dur=\"20.00\" "
            + "attributeName=\"cy\" from=\"250.0\" to=\"370.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"50.00\" dur=\"20.00\""
            + " attributeName=\"fill\" from=\"rgb(0,0,255)\" to=\"rgb(0,170,85)\" "
            + "fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"50.00\" dur=\"20.00\""
            + " attributeName=\"rx\" from=\"120.0\" to=\"120.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"50.00\" dur=\"20.00\""
            + " attributeName=\"ry\" from=\"60.0\" to=\"60.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"70.00\" dur=\"10.00\""
            + " attributeName=\"cx\" from=\"440.0\" to=\"440.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"70.00\" dur=\"10.00\""
            + " attributeName=\"cy\" from=\"370.0\" to=\"370.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"70.00\" dur=\"10.00\""
            + " attributeName=\"fill\" from=\"rgb(0,170,85)\" to=\"rgb(0,255,0)\" "
            + "fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"70.00\" dur=\"10.00\""
            + " attributeName=\"rx\" from=\"120.0\" to=\"120.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"70.00\" dur=\"10.00\""
            + " attributeName=\"ry\" from=\"60.0\" to=\"60.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"80.00\" dur=\"20.00\" "
            + "attributeName=\"cx\" from=\"440.0\" to=\"440.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"80.00\" dur=\"20.00\""
            + " attributeName=\"cy\" from=\"370.0\" to=\"370.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"80.00\" dur=\"20.00\" "
            + "attributeName=\"fill\" from=\"rgb(0,255,0)\" to=\"rgb(0,255,0)\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"80.00\" dur=\"20.00\" "
            + "attributeName=\"rx\" from=\"120.0\" to=\"120.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"80.00\" dur=\"20.00\" "
            + "attributeName=\"ry\" from=\"60.0\" to=\"60.0\" fill=\"freeze\"/>\n"
            + "</ellipse>\n"
            + "</svg>", a.toString());
  }

  @Test
  public void testDifferentBounds() {
    AnimationBuilder<AnimatorModel> test1 = new AnimatorModelImpl.Builder().setBounds(20, -20,
        200, 100).declareShape("R", "rectangle").declareShape("C",
        "ellipse")
        .addMotion("R", 1, 200, 200, 50, 100, 255, 0, 0, 0,
            10, 200, 10, 50, 100, 255, 0, 0, 0)
        .addMotion("C", 1, 200, 200, 50, 100, 255, 0, 0, 0,
            10, 200, 10, 50, 100, 255, 0, 0, 0);
    AnimatorModel model = test1.build();
    Appendable a = new StringBuilder();
    IView svgView = new SVGView(a);
    svgView.passBounds(20, -20, 200, 100);

    Map<String, DrawCommand> knownCommands = new HashMap<>();
    knownCommands.put("rectangle", new DrawRectangle(0, 0, 1));
    knownCommands.put("ellipse", new DrawEllipse(0, 0, 1));

    svgView.setCommands(knownCommands);
    svgView.setShapes(model.getShapes());
    svgView.displayOutput();
    assertEquals(
        "<svg viewBox=\"20 -20 200 100\" version=\"1.1\" "
            + "xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "<rect id=\"R\" x=\"200.0\" y=\"200.0\" width=\"50.0\" "
            + "height=\"100.0\" fill=\"rgb(255,0,0)\" >\n"
            + "<set id=\"R\" attributeName=\"visibility\" attributeType=\"xml\" "
            + "begin=\"0\" duration=\"1\" to=\"hidden\" >\n"
            + "</set>\n"
            + "<set id=\"R\" attributeName=\"visibility\" attributeType=\"xml\" "
            + "begin=\"1\" to=\"visible\" >\n"
            + "</set>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\""
            + " attributeName=\"x\" from=\"200.0\" to=\"200.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\""
            + " attributeName=\"y\" from=\"200.0\" to=\"10.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" "
            + "attributeName=\"fill\" from=\"rgb(255,0,0)\" to=\"rgb(255,0,0)\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" "
            + "attributeName=\"width\" from=\"50.0\" to=\"50.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" "
            + "attributeName=\"height\" from=\"100.0\" to=\"100.0\" fill=\"freeze\"/>\n"
            + "</rect>\n"
            + "<ellipse id=\"C\" cx=\"200.0\" cy=\"200.0\" rx=\"50.0\""
            + " ry=\"100.0\" fill=\"rgb(255,0,0)\" >\n"
            + "<set id=\"C\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"0\" "
            + "duration=\"1\" to=\"hidden\" >\n"
            + "</set>\n"
            + "<set id=\"C\" attributeName=\"visibility\" attributeType=\"xml\" "
            + "begin=\"1\" to=\"visible\" >\n"
            + "</set>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" "
            + "attributeName=\"cx\" from=\"200.0\" to=\"200.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" "
            + "attributeName=\"cy\" from=\"200.0\" to=\"10.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" "
            + "attributeName=\"fill\" from=\"rgb(255,0,0)\" to=\"rgb(255,0,0)\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" "
            + "attributeName=\"rx\" from=\"50.0\" to=\"50.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"9.00\" "
            + "attributeName=\"ry\" from=\"100.0\" to=\"100.0\" fill=\"freeze\"/>\n"
            + "</ellipse>\n"
            + "</svg>", a.toString());
  }
}
