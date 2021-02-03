import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import cs3500.animator.model.AnimatorModel;
import cs3500.animator.model.AnimatorModelImpl;
import cs3500.animator.model.frame.Frame;
import cs3500.animator.model.frame.IFrame;
import cs3500.animator.model.motion.ChangeSizeMotion;
import cs3500.animator.model.motion.ColorMotion;
import cs3500.animator.model.motion.Motion;
import cs3500.animator.model.motion.MoveMotion;
import cs3500.animator.model.shape.Ellipse;
import cs3500.animator.model.shape.IShape;
import cs3500.animator.model.shape.Position2D;
import cs3500.animator.model.shape.Rectangle;
import cs3500.animator.model.shape.Shape;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Class to test the methods and constructors of the {@link Shape} class as well as all of its
 * extending classes.
 */
public final class ShapeTest {

  AnimatorModel a;
  // MoveMotions
  Motion move1 = new MoveMotion(1, 10, "R", new Position2D(200, 200),
      new Position2D(200, 10));
  Motion move2overlaps1 = new MoveMotion(5, 20, "R", new Position2D(10, 200),
      new Position2D(300, 300));
  Motion colorChangeOverlapsMove1 = new ColorMotion(5, 10, "R", Color.RED, Color.BLUE);
  // Motion objects for R1
  Motion r1Move1 = new MoveMotion(1, 10, "R1", new Position2D(200, 200),
      new Position2D(200, 10));
  Motion r1Move2 = new MoveMotion(10, 50, "R1", new Position2D(200, 10),
      new Position2D(300, 300));
  Motion r1TurnBlue = new ColorMotion(20, 40, "R1", Color.RED,
      Color.BLUE);
  Motion r1Grow1 = new ChangeSizeMotion(15, 35, "R1", 50, 100, 100, 100);
  // Motion objects for O1
  Motion o1Move1 = new MoveMotion(1, 10, "O1", new Position2D(200, 200),
      new Position2D(10, 10));
  Motion o1Move2 = new MoveMotion(10, 50, "O1", new Position2D(10, 10),
      new Position2D(250, 250));
  Motion o1TurnGreen = new ColorMotion(1, 10, "O1", Color.RED, Color.GREEN);
  Motion o1Grow1 = new ChangeSizeMotion(30, 60, "O1", 50, 100, 50, 50);
  //shape objects
  private IShape r1;
  private IShape o1;
  private IShape redR;
  private IShape redC;

  /**
   * Set up shape objects before each test is run.
   */
  @Before
  public void init() {
    r1 = new Rectangle(Color.RED, new Position2D(200, 200), 50, 100, "R1");
    o1 = new Ellipse(Color.RED, new Position2D(200, 200), 50, 100, "O1");
    redR = new Rectangle(Color.RED, new Position2D(200, 200), 50,
        100, "R");
    redC = new Ellipse(Color.RED, new Position2D(200, 200), 120,
        60, "C");
    a = new AnimatorModelImpl();
  }

  /**
   * Testing that adding an overlapping motion of the same type throws an error.
   */
  @Test
  public void testOverlapThrowsError() {
    redR.addMotion(move1);
    try {
      redR.addMotion(move2overlaps1);
      fail("Error not thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Same motion types cannot overlap.", e.getMessage());
    }
  }

  /**
   * Testing that adding an overlapping motion of a different type is valid, and that the game state
   * outputs correctly for overlapping motions.
   */
  @Test
  public void testOverlapMoveAndColorDoesntThrowError() {
    a.addShape(redR);
    a.addMotions(Arrays.asList(move1, colorChangeOverlapsMove1));
    assertEquals("shape R rectangle\n"
            + "motion R 1 200 200 50 100 255 0 0 10 200 10 50 100 0 0 255\n"
            + "motion R 5 200 115 50 100 255 0 0 10 200 10 50 100 0 0 255\n",
        redR.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSameMoveDifferentShapes() {
    redR.addMotion(move1);
    redC.addMotion(move1);
  }

  /**
   * Test that the rectangle constructor properly initializes a rectangle object.
   */
  @Test
  public void testRectangleConstructor() {
    IShape r = new Rectangle(Color.RED, new Position2D(200, 200), 50, 100, "R");
    assertEquals(new Position2D(200, 200), r.getPosition());
    assertEquals(Color.RED, r.getColor());
    assertEquals(50, r.getWidth(), .01);
    assertEquals(100, r.getHeight(), .01);
    assertEquals("R", r.getName());
    assertEquals("rectangle", r.getType());
  }

  /**
   * Test that the rectangle constructor does not allow a null color.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNoNullColorRectangle() {
    IShape r = new Rectangle(null, new Position2D(200, 200), 50, 100, "R");
  }

  /**
   * Test that the rectangle constructor does not allow a null position.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNoNullPositionRectangle() {
    IShape r = new Rectangle(Color.RED, null, 50, 100, "R");
  }

  /**
   * Test that the rectangle constructor does not allow a null name.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNoNullNameRectangle() {
    IShape r = new Rectangle(Color.RED, new Position2D(200, 200), 50, 100, null);
  }

  /**
   * Test that the ellipse constructor properly initializes an ellipse object.
   */
  @Test
  public void testEllipseConstructor() {
    IShape e = new Ellipse(Color.RED, new Position2D(200, 200), 50, 100, "E");
    assertEquals(new Position2D(200, 200), e.getPosition());
    assertEquals(Color.RED, e.getColor());
    assertEquals(50, e.getWidth(), .01);
    assertEquals(100, e.getHeight(), .01);
    assertEquals("E", e.getName());
    assertEquals("ellipse", e.getType());
  }

  /**
   * Test that the ellipse constructor does not allow a null color.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNoNullColorEllipse() {
    IShape r = new Ellipse(null, new Position2D(200, 200), 50, 100, "R");
  }

  /**
   * Test that the ellipse constructor does not allow a null position.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNoNullPositionEllipse() {
    IShape r = new Ellipse(Color.RED, null, 50, 100, "R");
  }

  /**
   * Test that the ellipse constructor does not allow a null name.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNoNullNameEllipse() {
    IShape r = new Ellipse(Color.RED, new Position2D(200, 200), 50, 100, null);
  }

  /**
   * Test that the rectangle copy constructor produces a new rectangle object with all the same
   * parameters as the original but does not point to the same reference.
   */
  @Test
  public void testRectangleCopyConstructor() {
    IShape r = new Rectangle(r1);
    assertEquals(new Position2D(200, 200), r.getPosition());
    assertEquals(Color.RED, r.getColor());
    assertEquals(50, r.getWidth(), .01);
    assertEquals(100, r.getHeight(), .01);
    assertEquals("R1", r.getName());
    //make sure objects don't point to the same reference
    assertNotEquals(r, r1);
  }

  /**
   * Test that the rectangle copy constructor does not allow a null constructor.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullRectangleCopyConstructor() {
    IShape r = new Rectangle((IShape) null);
  }

  /**
   * Test that the ellipse copy constructor produces a new rectangle object with all the same
   * parameters as the original but does not point to the same reference.
   */
  @Test
  public void testEllipseCopyConstructor() {
    IShape e = new Ellipse(o1);
    assertEquals(new Position2D(200, 200), e.getPosition());
    assertEquals(Color.RED, e.getColor());
    assertEquals(50, e.getWidth(), .01);
    assertEquals(100, e.getHeight(), .01);
    assertEquals("O1", e.getName());
    //make sure objects don't point to the same reference
    assertNotEquals(e, o1);
  }

  /**
   * Test that the ellipse copy constructor does not allow a null constructor.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullEllipseCopyConstructor() {
    IShape r = new Ellipse((IShape) null);
  }

  //test the getters

  /**
   * Test that the getColor() method returns the appropriate value on a rectangle.
   */
  @Test
  public void testRectangleGetColor() {
    assertEquals(Color.RED, r1.getColor());
  }

  /**
   * Test that the getColor() method returns the appropriate value on an ellipse.
   */
  @Test
  public void testEllipseGetColor() {
    assertEquals(Color.RED, o1.getColor());
  }

  /**
   * Test that the getPosition() method returns the appropriate value on a rectangle.
   */
  @Test
  public void testRectangleGetPosition() {
    assertEquals(new Position2D(200, 200), r1.getPosition());
  }

  /**
   * Test that the getPosition() method returns the appropriate value on an ellipse.
   */
  @Test
  public void testEllipseGetPosition() {
    assertEquals(new Position2D(200, 200), o1.getPosition());
  }

  /**
   * Test that the getWidth() method returns the appropriate value on a rectangle.
   */
  @Test
  public void testRectangleGetWidth() {
    assertEquals(50, r1.getWidth(), .01);
  }

  /**
   * Test that the getWidth() method returns the appropriate value on an ellipse.
   */
  @Test
  public void testEllipseGetWidth() {
    assertEquals(50, o1.getWidth(), .01);
  }

  /**
   * Test that the getHeight() method returns the appropriate value on a rectangle.
   */
  @Test
  public void testRectangleGetHeight() {
    assertEquals(50, r1.getWidth(), .01);
  }

  /**
   * Test that the getHeight() method returns the appropriate value on an ellipse.
   */
  @Test
  public void testEllipseGetHeight() {
    assertEquals(50, o1.getWidth(), .01);
  }

  /**
   * Test that the getName() method returns the appropriate value on a rectangle.
   */
  @Test
  public void testRectangleGetName() {
    assertEquals("R1", r1.getName());
  }

  /**
   * Test that the getName() method returns the appropriate value on an ellipse.
   */
  @Test
  public void testEllipseGetName() {
    assertEquals("O1", o1.getName());
  }

  /**
   * Test that the getType() method returns the appropriate value on a rectangle.
   */
  @Test
  public void testRectangleGetType() {
    assertEquals("rectangle", r1.getType());
  }

  /**
   * Test that the getType() method returns the appropriate value on an ellipse.
   */
  @Test
  public void testEllipseGetType() {
    assertEquals("ellipse", o1.getType());
  }

  /**
   * Test that the getActiveMotions() method returns the appropriate value on a rectangle.
   */
  @Test
  public void testRectangleGetActiveMotions() {
    r1.addMotion(r1Grow1);
    r1.addMotion(r1Move1);
    r1.applyMotion(1);
    assertEquals(1, r1.getActiveMotions().size());
  }

  /**
   * Test that the getActiveMotions() method returns the appropriate value on an ellipse.
   */
  @Test
  public void testEllipseGetActiveMotions() {
    o1.addMotion(o1Move1);
    o1.addMotion(o1TurnGreen);
    o1.applyMotion(1);
    assertEquals(2, o1.getActiveMotions().size());
  }

  /**
   * Test that the getMotionsMap() method returns the appropriate value on a rectangle.
   */
  @Test
  public void testRectangleGetMotionsMap() {
    r1.addMotion(r1Grow1);
    r1.addMotion(r1Move1);
    assertEquals(2, r1.getMotionsMap().size());
  }

  /**
   * Test that the getMotionsMap() method returns the appropriate value on an ellipse.
   */
  @Test
  public void testEllipseGetMotionsMap() {
    o1.addMotion(o1Move1);
    o1.addMotion(o1TurnGreen);
    //size is one because they have the same start time
    assertEquals(1, o1.getMotionsMap().size());
  }

  /**
   * Test that the getMotionsList() method returns the appropriate value on a rectangle.
   */
  @Test
  public void testRectangleGetMotionsList() {
    r1.addMotion(r1Grow1);
    r1.addMotion(r1Move1);
    assertEquals(2, r1.getMotionsList().size());
  }

  /**
   * Test that the getMotionsList() method returns the appropriate value on an ellipse.
   */
  @Test
  public void testEllipseGetMotionsList() {
    o1.addMotion(o1Move1);
    o1.addMotion(o1TurnGreen);
    assertEquals(2, o1.getMotionsList().size());
  }

  //test setters

  /**
   * Test that the setColor() method properly sets the appropriate field of a rectangle.
   */
  @Test
  public void testRectangleSetColor() {
    assertEquals(Color.RED, r1.getColor());
    r1.setColor(Color.BLACK);
    assertEquals(Color.BLACK, r1.getColor());
  }

  /**
   * Test that the setColor() method properly sets the appropriate field of an ellipse.
   */
  @Test
  public void testEllipseSetColor() {
    assertEquals(Color.RED, o1.getColor());
    o1.setColor(Color.BLACK);
    assertEquals(Color.BLACK, o1.getColor());
  }

  /**
   * Test that the setPosition() method properly sets the appropriate field of a rectangle.
   */
  @Test
  public void testRectangleSetPosition() {
    assertEquals(new Position2D(200, 200), r1.getPosition());
    r1.setPosition(new Position2D(100, 100));
    assertEquals(new Position2D(100, 100), r1.getPosition());
  }

  /**
   * Test that the setPosition() method properly sets the appropriate field of an ellipse.
   */
  @Test
  public void testEllipseSetPosition() {
    assertEquals(new Position2D(200, 200), o1.getPosition());
    o1.setPosition(new Position2D(100, 100));
    assertEquals(new Position2D(100, 100), o1.getPosition());
  }

  /**
   * Test that the setWidth() method properly sets the appropriate field of a rectangle.
   */
  @Test
  public void testRectangleSetWidth() {
    assertEquals(50, r1.getWidth(), .01);
    r1.setWidth(10);
    assertEquals(10, r1.getWidth(), .01);
  }

  /**
   * Test that the setWidth() method properly sets the appropriate field of an ellipse.
   */
  @Test
  public void testEllipseSetWidth() {
    assertEquals(50, o1.getWidth(), .01);
    o1.setWidth(10);
    assertEquals(10, o1.getWidth(), .01);
  }

  /**
   * Test that the setHeight() method properly sets the appropriate field of a rectangle.
   */
  @Test
  public void testRectangleSetHeight() {
    assertEquals(50, r1.getWidth(), .01);
    r1.setHeight(10);
    assertEquals(10, r1.getHeight(), .01);
  }

  /**
   * Test that the setHeight() method properly sets the appropriate field of an ellipse.
   */
  @Test
  public void testEllipseSetHeight() {
    assertEquals(50, o1.getWidth(), .01);
    o1.setHeight(10);
    assertEquals(10, o1.getHeight(), .01);
  }

  /**
   * Test the toString() method on a rectangle with no motions.
   */
  @Test
  public void testToStringRectangleNoMotions() {
    assertEquals("shape R1 rectangle\n", r1.toString());
  }

  /**
   * Test the toString() method on a ellipse with no motions.
   */
  @Test
  public void testToStringEllipseNoMotions() {
    assertEquals("shape O1 ellipse\n", o1.toString());
  }

  /**
   * Test the toString() method on a rectangle with motions.
   */
  @Test
  public void testToStringRectangleMotions() {
    a.addShape(r1);
    a.addMotions(Arrays.asList(r1Move1, r1Move2));
    assertEquals("shape R1 rectangle\n"
        + "motion R1 1 200 200 50 100 255 0 0 10 200 10 50 100 255 0 0\n"
        + "motion R1 10 200 10 50 100 255 0 0 50 300 300 50 100 255 0 0\n", r1.toString());
  }

  /**
   * Test the toString() method on a ellipse with motions.
   */
  @Test
  public void testToStringEllipseMotions() {
    a.addShape(o1);
    a.addMotions(Arrays.asList(o1Move1, o1TurnGreen));
    assertEquals("shape O1 ellipse\n"
        + "motion O1 1 200 200 50 100 255 0 0 10 10 10 50 100 0 255 0\n"
        + "motion O1 1 200 200 50 100 255 0 0 10 10 10 50 100 0 255 0\n", o1.toString());
  }

  /**
   * Test that the applyMotion() method works properly on a rectangle.
   */
  @Test
  public void testApplyMotionRectangle() {
    a.addShape(r1);
    a.addMotions(Arrays.asList(r1Move1, r1Move2));
    assertEquals(new Position2D(200, 200), r1.getPosition());
    assertEquals(Color.RED, r1.getColor());
    assertEquals(50, r1.getWidth(), .01);
    assertEquals(100, r1.getHeight(), .01);
    assertEquals("R1", r1.getName());
    for (int i = 0; i <= 50; i++) {
      r1.applyMotion(i);
    }
    assertEquals(new Position2D(300, 300), r1.getPosition());
    assertEquals(Color.RED, r1.getColor());
    assertEquals(50, r1.getWidth(), .01);
    assertEquals(100, r1.getHeight(), .01);
    assertEquals("R1", r1.getName());
  }

  @Test
  public void testApplyMotionEllipse() {
    a.addShape(o1);
    a.addMotions(Arrays.asList(o1Move1, o1TurnGreen));
    assertEquals(new Position2D(200, 200), o1.getPosition());
    assertEquals(Color.RED, o1.getColor());
    assertEquals(50, o1.getWidth(), .01);
    assertEquals(100, o1.getHeight(), .01);
    assertEquals("O1", o1.getName());
    for (int i = 0; i <= 10; i++) {
      o1.applyMotion(i);
    }
    assertEquals(new Position2D(10, 10), o1.getPosition());
    assertEquals(Color.GREEN, o1.getColor());
    assertEquals(50, o1.getWidth(), .01);
    assertEquals(100, o1.getHeight(), .01);
    assertEquals("O1", o1.getName());
  }

  @Test
  public void testGetStateAtEllipse() {
    a.addShape(o1);
    a.addMotions(Arrays.asList(o1Move1, o1TurnGreen));
    //test that initial state is correct
    assertEquals(new Position2D(200, 200), o1.getPosition());
    assertEquals(Color.RED, o1.getColor());
    assertEquals(50, o1.getWidth(), .01);
    assertEquals(100, o1.getHeight(), .01);
    assertEquals("O1", o1.getName());
    //get state at t = 10
    IShape s10 = o1.getStateAt(10);
    //test that state is correct at t = 10
    assertEquals(new Position2D(10, 10), s10.getPosition());
    assertEquals(Color.GREEN, s10.getColor());
    assertEquals(50, s10.getWidth(), .01);
    assertEquals(100, s10.getHeight(), .01);
    assertEquals("O1", s10.getName());
    //get state at t = 1 again
    IShape s1 = o1.getStateAt(1);
    assertEquals(new Position2D(200, 200), s1.getPosition());
    assertEquals(Color.RED, s1.getColor());
    assertEquals(50, s1.getWidth(), .01);
    assertEquals(100, s1.getHeight(), .01);
    assertEquals("O1", s1.getName());
  }

  @Test
  public void testGetStateAtRectangle() {
    a.addShape(r1);
    a.addMotions(Arrays.asList(r1Move1, r1Move2));
    //test that initial state is correct
    assertEquals(new Position2D(200, 200), r1.getPosition());
    assertEquals(Color.RED, r1.getColor());
    assertEquals(50, r1.getWidth(), .01);
    assertEquals(100, r1.getHeight(), .01);
    assertEquals("R1", r1.getName());
    //get state at t = 50
    IShape s50 = r1.getStateAt(50);
    //test that state is correct at t = 50
    assertEquals(new Position2D(300, 300), s50.getPosition());
    assertEquals(Color.RED, s50.getColor());
    assertEquals(50, s50.getWidth(), .01);
    assertEquals(100, s50.getHeight(), .01);
    assertEquals("R1", s50.getName());
    //get state at t = 1 again
    IShape s1 = r1.getStateAt(1);
    assertEquals(new Position2D(200, 200), s1.getPosition());
    assertEquals(Color.RED, s1.getColor());
    assertEquals(50, s1.getWidth(), .01);
    assertEquals(100, s1.getHeight(), .01);
    assertEquals("R1", s1.getName());
  }

  @Test
  public void testRectangleCopy() {
    IShape r = r1.copy();
    assertEquals(new Position2D(200, 200), r.getPosition());
    assertEquals(Color.RED, r.getColor());
    assertEquals(50, r.getWidth(), .01);
    assertEquals(100, r.getHeight(), .01);
    assertEquals("R1", r.getName());
    //make sure objects don't point to the same reference
    assertNotEquals(r, r1);
  }

  @Test
  public void testEllipseCopy() {
    IShape e = o1.copy();
    assertEquals(new Position2D(200, 200), e.getPosition());
    assertEquals(Color.RED, e.getColor());
    assertEquals(50, e.getWidth(), .01);
    assertEquals(100, e.getHeight(), .01);
    assertEquals("O1", e.getName());
    //make sure objects don't point to the same reference
    assertNotEquals(e, o1);
  }

  @Test
  public void testRectangleAddMotions() {
    a.addShape(r1);
    a.addMotions(Arrays.asList(r1Grow1, r1Move1));
    assertEquals(2, r1.getMotionsList().size());
  }

  @Test
  public void testEllipseAddMotions() {
    o1.addMotion(o1Move1);
    o1.addMotion(o1TurnGreen);
    assertEquals(2, o1.getMotionsList().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddNullMotion() {
    o1.addMotion(null);
  }

  @Test
  public void testGetAndSetStartTime() {
    r1.setStartTime(10);
    assertEquals(10, r1.getStartTime());
    r1.setStartTime(100000);
    assertEquals(100000, r1.getStartTime());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNoNegativeStartTime() {
    r1.setStartTime(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNoZeroStartTime() {
    r1.setStartTime(0);
  }

  @Test
  public void testGetAndSetKeyFrames1() {
    List<IFrame> frames = new ArrayList<>();
    frames.add(new Frame(1, "R1", new Position2D(1, 1), Color.BLACK, 1, 1));
    frames.add(new Frame(2, "R1", new Position2D(1, 1), Color.BLACK, 1, 1));
    r1.setKeyFrames(frames);
    assertEquals(2, r1.getKeyFrames().size());
  }

  @Test
  public void testGetAndSetKeyFrames2() {
    List<IFrame> frames = new ArrayList<>();
    frames.add(new Frame(1, "R1", new Position2D(1, 1), Color.BLACK, 1, 1));
    r1.setKeyFrames(frames);
    assertEquals(1, r1.getKeyFrames().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTryToRemoveNonexistantMotion() {
    redR.addMotion(move1);
    redR.removeMotion(colorChangeOverlapsMove1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTryToRemoveNullMotion() {
    redR.addMotion(move1);
    redR.removeMotion(null);
  }

  @Test
  public void testAddKeyFrame() {
    redR.addKeyFrame(new Frame(1, "R", new Position2D(1, 1), Color.BLUE, 100, 100));
    assertEquals(1, redR.getKeyFrames().size());
  }

  @Test
  public void testAddMultipleKeyFrame() {
    redR.addKeyFrame(new Frame(1, "R", new Position2D(1, 1), Color.BLUE, 100, 100));
    redR.addKeyFrame(new Frame(10, "R", new Position2D(1, 1), Color.BLACK, 100, 100));
    assertEquals(2, redR.getKeyFrames().size());
  }

  @Test
  public void testRemoveKeyFrame() {
    redR.addKeyFrame(new Frame(1, "R", new Position2D(1, 1), Color.BLUE, 100, 100));
    assertEquals(1, redR.getKeyFrames().size());
    redR.removeKeyFrame(1);
    assertEquals(0, redR.getKeyFrames().size());
  }

  @Test
  public void testRemoveMultipleKeyFrame() {
    redR.addKeyFrame(new Frame(1, "R", new Position2D(1, 1), Color.BLUE, 100, 100));
    redR.addKeyFrame(new Frame(10, "R", new Position2D(1, 1), Color.BLACK, 100, 100));
    assertEquals(2, redR.getKeyFrames().size());
    redR.removeKeyFrame(1);
    assertEquals(1, redR.getKeyFrames().size());
    redR.removeKeyFrame(10);
    assertEquals(0, redR.getKeyFrames().size());
  }

  @Test(expected = NullPointerException.class)
  public void testAddNullFrame() {
    redR.addKeyFrame(null);
  }

  @Test
  public void testGetEndTime() {
    redR.addKeyFrame(new Frame(1, "R", new Position2D(1, 1), Color.BLUE, 100, 100));
    assertEquals(1, redR.getEndTime());
    redR.addKeyFrame(new Frame(10, "R", new Position2D(1, 1), Color.BLACK, 100, 100));
    assertEquals(10, redR.getEndTime());
  }

  @Test
  public void testGetLayer() {
    assertEquals(0, redR.getLayer());
    IShape r1 = new Rectangle("R1", 4);
    assertEquals(4, r1.getLayer());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidLayerError() {
    IShape r1 = new Rectangle("R1", -4);
  }
}