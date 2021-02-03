import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import cs3500.animator.model.frame.Frame;
import cs3500.animator.model.frame.IFrame;
import cs3500.animator.model.motion.ChangeSizeMotion;
import cs3500.animator.model.motion.ColorMotion;
import cs3500.animator.model.motion.Motion;
import cs3500.animator.model.motion.MotionFactory;
import cs3500.animator.model.motion.MoveMotion;
import cs3500.animator.model.shape.Position2D;
import java.awt.Color;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Test that a {@link MotionFactory} properly creates Motions.
 */
public class MotionFactoryTest {

  //base frame
  private IFrame f1;
  //no change frame
  private IFrame f2;
  //change color frame
  private IFrame f3;
  //change size frame
  private IFrame f4;
  //change position frame;
  private IFrame f5;
  //change multiple frame
  private IFrame f6;

  @Before
  public void init() {
    f1 = new Frame(1, "r", new Position2D(1, 1), Color.RED, 1, 1);
    f2 = new Frame(10, "r", new Position2D(1, 1), Color.RED, 1, 1);
    f3 = new Frame(10, "r", new Position2D(1, 1), Color.BLACK, 1, 1);
    f4 = new Frame(10, "r", new Position2D(1, 1), Color.RED, 2, 2);
    f5 = new Frame(10, "r", new Position2D(2, 2), Color.RED, 1, 1);
    f6 = new Frame(10, "r", new Position2D(2, 2), Color.BLUE, 1, 1);
  }

  @Test
  public void testNewEmptyMoveMotion() {
    List<Motion> motions = MotionFactory.generateMotions(f1, f2, 3);
    assertEquals(1, motions.size());
    assertTrue(motions.get(0) instanceof MoveMotion);
  }

  @Test
  public void testNoNewEmptyMoveMotion() {
    List<Motion> motions = MotionFactory.generateMotions(f1, f2, 2);
    assertEquals(0, motions.size());
  }

  @Test
  public void testNewColorMotion() {
    List<Motion> motions = MotionFactory.generateMotions(f1, f3, 3);
    assertEquals(1, motions.size());
    assertTrue(motions.get(0) instanceof ColorMotion);
  }

  @Test
  public void testNewSizeChangeMotion() {
    List<Motion> motions = MotionFactory.generateMotions(f1, f4, 3);
    assertEquals(1, motions.size());
    assertTrue(motions.get(0) instanceof ChangeSizeMotion);
  }

  @Test
  public void testNewMoveMotion() {
    List<Motion> motions = MotionFactory.generateMotions(f1, f5, 3);
    assertEquals(1, motions.size());
    assertTrue(motions.get(0) instanceof MoveMotion);
  }

  @Test
  public void testNewMultipleMotion() {
    List<Motion> motions = MotionFactory.generateMotions(f1, f6, 3);
    assertEquals(2, motions.size());
    assertTrue(motions.get(0) instanceof ColorMotion);
    assertTrue(motions.get(1) instanceof MoveMotion);
  }

  @Test(expected = NullPointerException.class)
  public void testNoNullF1() {
    MotionFactory.generateMotions(null, f2, 3);
  }

  @Test(expected = NullPointerException.class)
  public void testNoNullF2() {
    MotionFactory.generateMotions(f1, null, 3);
  }
}
