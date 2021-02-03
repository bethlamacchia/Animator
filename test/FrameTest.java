import static org.junit.Assert.assertEquals;

import cs3500.animator.model.frame.Frame;
import cs3500.animator.model.frame.IFrame;
import cs3500.animator.model.shape.IShape;
import cs3500.animator.model.shape.Position2D;
import cs3500.animator.model.shape.Rectangle;
import java.awt.Color;
import org.junit.Before;
import org.junit.Test;

/**
 * Class to test the methods and constructors of an {@link Frame}.
 */
public class FrameTest {

  //base frame
  private IFrame f1;
  //no change frame
  private IFrame f2;
  //change color frame
  private IFrame f3;
  //change size frame
  private IFrame f4;
  //change multiple frame
  private IFrame f6;

  @Before
  public void init() {
    f1 = new Frame(1, "r", new Position2D(1, 1), Color.RED, 1, 1);
    f2 = new Frame(10, "r", new Position2D(1, 1), Color.RED, 1, 1);
    f3 = new Frame(10, "r", new Position2D(1, 1), Color.BLACK, 1, 1);
    f4 = new Frame(10, "r", new Position2D(1, 1), Color.RED, 2, 2);
    f6 = new Frame(10, "r", new Position2D(2, 2), Color.BLUE, 1, 1);
  }

  @Test(expected = NullPointerException.class)
  public void testNoNullName() {
    IFrame f = new Frame(1, null, new Position2D(1, 1), Color.BLACK, 1, 1);
  }

  @Test(expected = NullPointerException.class)
  public void testNoNullPos() {
    IFrame f = new Frame(1, "r", null, Color.BLACK, 1, 1);
  }

  @Test(expected = NullPointerException.class)
  public void testNoNullColor() {
    IFrame f = new Frame(1, "r", new Position2D(1, 1), null, 1, 1);
  }

  @Test
  public void testToString() {
    assertEquals("1 1 1 1 1 255 0 0", f1.toString());
  }

  @Test
  public void testApply() {
    IShape shape = new Rectangle("r");
    assertEquals(Color.BLACK, shape.getColor());
    f1.apply(shape);
    assertEquals(Color.RED, shape.getColor());
  }

  @Test
  public void testGetTime() {
    assertEquals(1, f1.getTime());
    assertEquals(10, f2.getTime());
  }

  @Test
  public void testGetPos() {
    assertEquals(new Position2D(1, 1), f1.getPos());
    assertEquals(new Position2D(2, 2), f6.getPos());
  }

  @Test
  public void testGetColor() {
    assertEquals(Color.RED, f1.getColor());
    assertEquals(Color.BLACK, f3.getColor());
  }

  @Test
  public void testGetWidth() {
    assertEquals(1, f1.getWidth(), .01);
    assertEquals(2, f4.getWidth(), .01);
  }

  @Test
  public void testGetHeight() {
    assertEquals(1, f1.getHeight(), .01);
    assertEquals(2, f4.getHeight(), .01);
  }

  @Test
  public void testGetName() {
    assertEquals("r", f1.getName());
    assertEquals("r", f2.getName());
  }
}
