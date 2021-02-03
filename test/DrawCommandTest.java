import static org.junit.Assert.assertEquals;

import cs3500.animator.model.motion.ChangeSizeMotion;
import cs3500.animator.model.motion.ColorMotion;
import cs3500.animator.model.motion.Motion;
import cs3500.animator.model.shape.Ellipse;
import cs3500.animator.model.shape.IShape;
import cs3500.animator.model.shape.Position2D;
import cs3500.animator.model.shape.Rectangle;
import cs3500.animator.view.visual.draw.DrawEllipse;
import cs3500.animator.view.visual.draw.DrawRectangle;
import java.awt.Color;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the methods of DrawCommand objects.
 */
public final class DrawCommandTest {

  private IShape r1;
  private IShape o1;
  private Motion rSizeChange1;
  private Motion rSizeChange2;
  private Motion cColorChange1;

  @Before
  public void init() {
    r1 = new Rectangle(Color.RED, new Position2D(200, 200), 50, 100, "R1");
    o1 = new Ellipse(Color.RED, new Position2D(200, 200), 50, 100, "O1");
    rSizeChange1 = new ChangeSizeMotion(1, 20, "R1", 50, 100, 50, 50);
    rSizeChange2 = new ChangeSizeMotion(21, 50, "R1", 50, 50, 50, 100);
    cColorChange1 = new ColorMotion(5, 10, "O1", Color.RED, Color.BLUE);
  }

  @Test
  public void testEmptyRectangleToSVG() {
    assertEquals(
        "<rect id=\"R1\" x=\"200.0\" y=\"200.0\" width=\"50.0\" height=\"100.0\" "
            + "fill=\"rgb(255,0,0)\" >\n"
            + "<set id=\"R1\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"0\" "
            + "duration=\"2147483647\" to=\"hidden\" >\n"
            + "</set>\n"
            + "<set id=\"R1\" attributeName=\"visibility\" attributeType=\"xml\" "
            + "begin=\"2147483647\" to=\"visible\" >\n"
            + "</set>\n"
            + "</rect>\n", new DrawRectangle(0, 0, 1).writeSVG(r1));
  }

  @Test
  public void testEmptyEllipseToSVG() {
    assertEquals(
        "<ellipse id=\"O1\" cx=\"200.0\" cy=\"200.0\" rx=\"50.0\" ry=\"100.0\" "
            + "fill=\"rgb(255,0,0)\" >\n"
            + "<set id=\"O1\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"0\" "
            + "duration=\"2147483647\" to=\"hidden\" >\n"
            + "</set>\n"
            + "<set id=\"O1\" attributeName=\"visibility\" attributeType=\"xml\" "
            + "begin=\"2147483647\" to=\"visible\" >\n"
            + "</set>\n"
            + "</ellipse>\n", new DrawEllipse(0,0, 1).writeSVG(o1));

  }

  @Test
  public void testRectangleToSVG() {
    r1.addMotion(rSizeChange1);
    r1.addMotion(rSizeChange2);
    assertEquals(
        "<rect id=\"R1\" x=\"200.0\" y=\"200.0\" width=\"50.0\" height=\"100.0\" "
            + "fill=\"rgb(255,0,0)\" >\n"
            + "<set id=\"R1\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"0\" "
            + "duration=\"1\" to=\"hidden\" >\n"
            + "</set>\n"
            + "<set id=\"R1\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"1\" "
            + "to=\"visible\" >\n"
            + "</set>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"19.00\" attributeName=\"width\" "
            + "from=\"50.0\" to=\"50.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"1.00\" dur=\"19.00\" "
            + "attributeName=\"height\" "
            + "from=\"100.0\" to=\"50.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"21.00\" dur=\"29.00\" "
            + "attributeName=\"width\" "
            + "from=\"50.0\" to=\"50.0\" fill=\"freeze\"/>\n"
            + "<animate attributeType=\"xml\" begin=\"21.00\" dur=\"29.00\" "
            + "attributeName=\"height\" "
            + "from=\"50.0\" to=\"100.0\" fill=\"freeze\"/>\n"
            + "</rect>\n", new DrawRectangle(0,0, 1).writeSVG(r1));
  }

  @Test
  public void testEllipseToSVG() {
    o1.addMotion(cColorChange1);
    assertEquals(
        "<ellipse id=\"O1\" cx=\"200.0\" cy=\"200.0\" rx=\"50.0\" ry=\"100.0\" "
            + "fill=\"rgb(255,0,0)\" >\n"
            + "<set id=\"O1\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"0\" "
            + "duration=\"5\" to=\"hidden\" >\n"
            + "</set>\n"
            + "<set id=\"O1\" attributeName=\"visibility\" attributeType=\"xml\" begin=\"5\" "
            + "to=\"visible\" >\n"
            + "</set>\n"
            + "<animate attributeType=\"xml\" begin=\"5.00\" dur=\"5.00\" attributeName=\"fill\" "
            + "from=\"rgb(255,0,0)\" to=\"rgb(0,0,255)\" fill=\"freeze\"/>\n"
            + "</ellipse>\n", new DrawEllipse(0,0, 1).writeSVG(o1));
  }
}
