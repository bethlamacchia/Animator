import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import cs3500.animator.model.AnimatorModel;
import cs3500.animator.model.AnimatorModelImpl;
import cs3500.animator.model.motion.ChangeSizeMotion;
import cs3500.animator.model.motion.ColorMotion;
import cs3500.animator.model.motion.Motion;
import cs3500.animator.model.motion.MoveMotion;
import cs3500.animator.model.shape.Ellipse;
import cs3500.animator.model.shape.Position2D;
import cs3500.animator.model.shape.Rectangle;
import cs3500.animator.model.shape.Shape;
import cs3500.animator.view.IView;
import cs3500.animator.view.TextView;
import java.awt.Color;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the methods and constructors of the {@link Motion} class.
 */
public class MotionsTest {

  /**
   * * Create rectangle named "R" Create oval named "C" From time 1 to 10, R moves from (200,200) to
   * (10,200), stays size 50x100, and stays red. From time 10 to 50, R moves from (10,200) to
   * (300,300), stays size 50x100, and stays red. From time 50 to 51, R does nothing. From time 51
   * to 70, R stays put, shrinks from 50x100 to 25x100, and stays red.
   **/

  private AnimatorModel a;
  // MoveMotions
  private Motion rMove1;
  private Motion rMove2;
  private Motion doNothing;
  // ColorMotions
  private Motion rColorChange1;
  private Motion rColorChange2;
  // ChangeSizeMotions
  private Motion rSizeChange1;
  private Motion rSizeChange2;
  private Motion rSizeChange3;
  // C motions
  private Motion cMoveDoNothing;
  private Motion cMove1;
  private Motion cTurnGreen;
  private Shape redR;
  private Shape blueC;

  @Before
  public void init() {
    redR = new Rectangle(Color.RED, new Position2D(200, 200), 50,
        100, "R");
    blueC = new Ellipse(Color.BLUE, new Position2D(440, 70), 120,
        60, "C");
    a = new AnimatorModelImpl();

    // C motions
    cTurnGreen = new ColorMotion(50, 70, "C", Color.BLUE, Color.GREEN);
    cMove1 = new MoveMotion(20, 50, "C", new Position2D(440, 70), new
        Position2D(440, 250));
    cMoveDoNothing = new MoveMotion(1, 20, "C", new Position2D(440, 70), new
        Position2D(440, 70));
    // ChangeSizeMotions
    rSizeChange1 = new ChangeSizeMotion(1, 20, "R", 50, 100, 50, 50);
    rSizeChange2 = new ChangeSizeMotion(20, 50, "R", 50, 50, 50, 100);
    rSizeChange3 = new ChangeSizeMotion(50, 70, "R", 50, 100, 25, 100);

    // ColorMotions
    rColorChange1 = new ColorMotion(5, 10, "R", Color.RED, Color.BLUE);
    rColorChange2 = new ColorMotion(10, 30, "R", Color.BLUE, Color.GREEN);

    rMove1 = new MoveMotion(1, 10, "R", new Position2D(200, 200),
        new Position2D(200, 10));
    rMove2 = new MoveMotion(10, 50, "R", new Position2D(200, 10),
        new Position2D(300, 300));
    doNothing = new MoveMotion(50, 51, "R", new Position2D(300, 300),
        new Position2D(300, 300));
  }

  @Test
  public void testMoveMotionConstructorExceptions() {
    try {
      Motion m = new MoveMotion(0, 10, "R", null, null);
    } catch (IllegalArgumentException iae) {
      assertEquals("Motions cannot accept any null inputs!", iae.getMessage());
    }
  }

  @Test
  public void testColorMotionConstructorExceptions() {
    try {
      Motion m = new ColorMotion(0, 10, "R", null, null);
    } catch (IllegalArgumentException iae) {
      assertEquals("Color cannot be null", iae.getMessage());
    }
  }

  @Test
  public void testSizeChangeMotionConstructorExceptions() {
    try {
      Motion m = new ChangeSizeMotion(0, 10, "R", 5, 10, 0,
          10);
    } catch (IllegalArgumentException iae) {
      assertEquals("All dimensions must be positive", iae.getMessage());
    }
    try {
      Motion m = new ChangeSizeMotion(0, 10, "R", -5, 10, 10,
          10);
    } catch (IllegalArgumentException iae) {
      assertEquals("All dimensions must be positive", iae.getMessage());
    }
    try {
      Motion m = new ChangeSizeMotion(0, 10, "R", 5, -10, 10,
          10);
    } catch (IllegalArgumentException iae) {
      assertEquals("All dimensions must be positive", iae.getMessage());
    }
    try {
      Motion m = new ChangeSizeMotion(0, 10, "R", 5, 10, 10,
          -10);
    } catch (IllegalArgumentException iae) {
      assertEquals("All dimensions must be positive", iae.getMessage());
    }
    try {
      Motion m = new ChangeSizeMotion(0, 10, null, 20, 10, 10,
          10);
    } catch (IllegalArgumentException iae) {
      assertEquals("Shape ID cannot be null", iae.getMessage());
    }
  }

  @Test
  public void testIllegalTimes() {
    try {
      Motion m = new ChangeSizeMotion(-5, 10, "R", 20, 10, 10,
          10);
    } catch (IllegalArgumentException iae) {
      assertEquals("Start and end times cannot be negative.", iae.getMessage());
    }
    try {
      Motion m = new ChangeSizeMotion(5, -10, "R", 20, 10, 10,
          10);
    } catch (IllegalArgumentException iae) {
      assertEquals("Start and end times cannot be negative.", iae.getMessage());
    }
    try {
      Motion m = new ChangeSizeMotion(10, 5, "R", 20, 10, 10,
          10);
    } catch (IllegalArgumentException iae) {
      assertEquals("Start time cannot be after end time.", iae.getMessage());
    }
  }

  @Test
  public void testChangeSize() {
    Shape e = new Ellipse(Color.BLUE, new Position2D(10, 10), 100,
        100, "C");
    Motion sizeChange = new ChangeSizeMotion(1, 10,
        "C", 100, 100, 50, 50);

    a.addShape(e);
    a.addMotions(Arrays.asList(sizeChange));
    assertEquals("shape C ellipse\n"
        + "motion C 1 10 10 100 100 0 0 255 10 10 10 50 50 0 0 255\n", e.toString());
  }

  /**
   * Test that {@link MoveMotion}s are able to be properly added to a shape and can be properly
   * displayed.
   */
  @Test
  public void testMoveMotion() {
    a.addShape(redR);
    a.addMotions(Arrays.asList(rMove1, rMove2));

    assertEquals("shape R rectangle\n"
            + "motion R 1 200 200 50 100 255 0 0 10 200 10 50 100 255 0 0\n"
            + "motion R 10 200 10 50 100 255 0 0 50 300 300 50 100 255 0 0\n",
        redR.toString());

    a.addMotions(Arrays.asList(doNothing));

    assertEquals(
        "shape R rectangle\n"
            + "motion R 1 200 200 50 100 255 0 0 10 200 10 50 100 255 0 0\n"
            + "motion R 10 200 10 50 100 255 0 0 50 300 300 50 100 255 0 0\n"
            + "motion R 50 300 300 50 100 255 0 0 51 300 300 50 100 255 0 0\n",
        redR.toString());
  }

  /**
   * Test that {@link ColorMotion}s are able to be properly added to a shape and can be properly
   * displayed.
   */
  @Test
  public void testColorMotion() {
    a.addShape(redR);
    a.addMotions(Arrays.asList(rColorChange1, rColorChange2));

    assertEquals("shape R rectangle\n"
            + "motion R 5 200 200 50 100 255 0 0 10 200 200 50 100 0 0 255\n"
            + "motion R 10 200 200 50 100 0 0 255 30 200 200 50 100 0 255 0\n",
        redR.toString());
  }

  /**
   * Test that {@link ChangeSizeMotion}s are able to be properly added to a shape and can be
   * properly displayed.
   */
  @Test
  public void testChangeSizeMotion() {
    a.addShape(redR);
    a.addMotions(Arrays.asList(rSizeChange1, rSizeChange2, rSizeChange3));

    assertEquals(
        "shape R rectangle\n"
            + "motion R 1 200 200 50 100 255 0 0 20 200 200 50 50 255 0 0\n"
            + "motion R 20 200 200 50 50 255 0 0 50 200 200 50 100 255 0 0\n"
            + "motion R 50 200 200 50 100 255 0 0 70 200 200 25 100 255 0 0\n",
        redR.toString());
  }

  /**
   * Test that multiple shapes in an animation model, with their own motions, can be properly added
   * and displayed.
   */
  @Test
  public void testListOfShapesToString() {
    a.addShape(redR);
    a.addShape(blueC);
    a.addMotions(Arrays.asList(rMove1, rMove2, doNothing, cMoveDoNothing, cMove1, cTurnGreen));

    Appendable ap = new StringBuilder();
    IView view = new TextView(ap);
    view.setShapes(a.getShapes());
    view.displayOutput();
    assertEquals(
        "canvas 0 0 0 0\n"
            + "shape R rectangle\n"
            + "motion R 1 200 200 50 100 255 0 0 10 200 10 50 100 255 0 0\n"
            + "motion R 10 200 10 50 100 255 0 0 50 300 300 50 100 255 0 0\n"
            + "motion R 50 300 300 50 100 255 0 0 51 300 300 50 100 255 0 0\n"
            + "shape C ellipse\n"
            + "motion C 1 440 70 120 60 0 0 255 20 440 70 120 60 0 0 255\n"
            + "motion C 20 440 70 120 60 0 0 255 50 440 250 120 60 0 0 255\n"
            + "motion C 50 440 250 120 60 0 0 255 70 440 250 120 60 0 255 0\n",
        ap.toString());
  }

  @Test
  public void testCompatibleMoveStatesWithApply() {
    assertTrue(rMove1.isCompatibleWith(redR));
    rMove1.apply(redR, 10);
    assertTrue(rMove2.isCompatibleWith(redR));
  }

  @Test
  public void testCompatibleColorStatesWithApply() {
    assertTrue(rColorChange1.isCompatibleWith(redR));
    rColorChange1.apply(redR, 10);
    assertTrue(rColorChange2.isCompatibleWith(redR));
  }

  @Test
  public void testCompatibleDimensionStates() {
    assertTrue(rSizeChange1.isCompatibleWith(redR));
    rSizeChange1.apply(redR, 20);
    assertTrue(rSizeChange2.isCompatibleWith(redR));
  }

  @Test
  public void testIncompatiblePositionStates() {
    try {
      rMove2.isCompatibleWith(redR);
    } catch (IllegalArgumentException iae) {
      assertEquals("Start position of motion does not equal position of the shape at t = 10",
          iae.getMessage());
    }
  }

  @Test
  public void testIncompatibleColorStates() {
    try {
      rColorChange2.isCompatibleWith(redR);
    } catch (IllegalArgumentException iae) {
      assertEquals("Start color of motion does not equal color of the shape at t = 10",
          iae.getMessage());
    }
  }

  @Test
  public void testIncompatibleDimensionStates() {
    try {
      rSizeChange2.isCompatibleWith(redR);
    } catch (IllegalArgumentException iae) {
      assertEquals("Start size of motion does not equal size of the shape at t = 20",
          iae.getMessage());
    }
  }

  @Test
  public void testShapeAndMotionHaveDifferentIDs() {
    try {
      rMove1.isCompatibleWith(blueC);
    } catch (IllegalArgumentException iae) {
      assertEquals("Motions can only operate on one shape",
          iae.getMessage());
    }
  }

  @Test
  public void testGetStart() {
    assertEquals(rColorChange1.getStart(), 5);
    assertEquals(rSizeChange2.getStart(), 20);
    assertEquals(rMove2.getStart(), 10);
  }

  @Test
  public void testGetEnd() {
    assertEquals(rColorChange1.getEnd(), 10);
    assertEquals(rSizeChange2.getEnd(), 50);
    assertEquals(rMove2.getEnd(), 50);
  }

  @Test
  public void testGetID() {
    assertEquals(rMove2.getId(), "R");
    assertEquals(cMove1.getId(), "C");
  }
}