import static org.junit.Assert.assertEquals;

import cs3500.animator.model.AnimatorModel;
import cs3500.animator.model.AnimatorModelImpl;
import cs3500.animator.model.motion.ChangeSizeMotion;
import cs3500.animator.model.motion.ColorMotion;
import cs3500.animator.model.motion.Motion;
import cs3500.animator.model.motion.MoveMotion;
import cs3500.animator.model.shape.Ellipse;
import cs3500.animator.model.shape.IShape;
import cs3500.animator.model.shape.Position2D;
import cs3500.animator.model.shape.Rectangle;
import cs3500.animator.view.IView;
import cs3500.animator.view.TextView;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the methods and constructors of the {@link AnimatorModelImpl} class.
 */
public final class AnimatorModelImplTest {

  // had to get rid of model toString, so testing the view of the model
  IView v;
  Appendable a;
  // IShape objects
  // rectangles
  IShape r1;
  IShape r2;
  //ovals
  IShape o1;
  IShape o2;
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
  private AnimatorModel m;

  /**
   * Before each test reinitialize all of the mutable objects that are to be used.
   */
  @Before
  public void init() {
    a = new StringBuilder();
    v = new TextView(a);
    m = new AnimatorModelImpl();
    r1 = new Rectangle(Color.RED, new Position2D(200, 200), 50, 100, "R1");
    r2 = new Rectangle(Color.BLACK, new Position2D(0, 0), 25, 25, "R2");
    o1 = new Ellipse(Color.RED, new Position2D(200, 200), 50, 100, "O1");
    o2 = new Ellipse(Color.RED, new Position2D(200, 200), 50, 100, "R1");
  }

  /**
   * Test that a single {@link Rectangle} can be added to the animator.
   */
  @Test
  public void testAddRectangle() {
    m.addShape(r1);
    v.setShapes(m.getShapes());
    v.displayOutput();
    assertEquals(a.toString(), "canvas 0 0 0 0\n" + r1.toString());
  }

  /**
   * Test that a single {@link Ellipse} can be added to the animator.
   */
  @Test
  public void testAddEllipse() {
    m.addShape(o1);
    v.setShapes(m.getShapes());
    v.displayOutput();
    assertEquals(a.toString(), "canvas 0 0 0 0\n" + o1.toString());
  }

  /**
   * Test that multiple of the same {@link IShape}s can be added to the animator.
   */
  @Test
  public void testAddTwoSameShape() {
    m.addShape(r1);
    m.addShape(r2);
    v.setShapes(m.getShapes());
    v.displayOutput();
    assertEquals(a.toString(), "canvas 0 0 0 0\n" + r1.toString() + r2.toString());
  }

  /**
   * Test that multiple different {@link IShape}s can be added to the animator.
   */
  @Test
  public void testAddTwoDifferentShape() {
    m.addShape(r1);
    m.addShape(o1);
    v.setShapes(m.getShapes());
    v.displayOutput();
    assertEquals(a.toString(), "canvas 0 0 0 0\n" + r1.toString() + o1.toString());
  }

  /**
   * Test that the same {@link IShape} object cannot be added twice when given the same name both
   * times.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddSameShapeTwiceSameName() {
    m.addShape(r1);
    m.addShape(r1);
  }

  /**
   * Test that two different {@link IShape} objects cannot be added with the same name.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddSameShapeTwice() {
    m.addShape(r1);
    m.addShape(o2);
  }

  /**
   * Test that a single valid motion can be added if that shape is in the model.
   */
  @Test
  public void testAddSingleValidMotion() {
    m.addShape(r1);
    m.addMotions(Arrays.asList(r1Move1));
    v.setShapes(m.getShapes());
    v.displayOutput();
    assertEquals("canvas 0 0 0 0\n" + "shape R1 rectangle\n"
        + "motion R1 1 200 200 50 100 255 0 0 10 200 10 50 100 255 0 0\n", a.toString());
  }

  /**
   * Test that a motion cannot be added for a shape that does not exist in the model.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddSingleValidMotionShapeNotInModel() {
    m.addShape(r1);
    m.addMotions(Arrays.asList(o1Move1));
  }


  @Test
  public void testGetLastTick() {
    m.addShape(r1);
    m.addShape(o1);
    m.addMotions(Arrays.asList(r1Move1, r1Move2, r1Grow1, r1TurnBlue, o1Move2, o1TurnGreen,
        o1Move1, o1Grow1));
    assertEquals(60, m.getLastTick());
  }


  /**
   * Test the {@code update()} method on a model with one shape with one motion.
   */
  @Test
  public void testUpdateOneShape() {
    //add r1 to the model
    m.addShape(r1);
    //add a motion to r1 moving the objects y position from 200 -> 10 in time 1 -> 10
    m.addMotions(Arrays.asList(r1Move1));
    //make sure y position is right pre move
    assertEquals(200, m.getShapes().get("R1").getPosition().getY(), .01);
    //"play" the animation updating it for 10 ticks
    for (int i = 0; i <= 10; i++) {
      m.update(i);
    }
    //make sure y position is right post move
    assertEquals(10, m.getShapes().get("R1").getPosition().getY(), .01);
  }

  /**
   * Test the {@code update()} method on a model with multiple shapes.
   */
  @Test
  public void testUpdateMultipleShapes() {
    //add r1 to the model
    m.addShape(r1);
    m.addShape(o1);
    //add r1 motions
    m.addMotions(Arrays.asList(r1Move1, r1Move2, r1Grow1, r1TurnBlue));
    //add o1 motions
    m.addMotions(Arrays.asList(o1Move2, o1TurnGreen, o1Move1, o1Grow1));
    //make sure x and y position, color and dimensions are right for r1 at t = 0
    assertEquals(200, m.getShapes().get("R1").getPosition().getX(), .01);
    assertEquals(200, m.getShapes().get("R1").getPosition().getY(), .01);
    assertEquals(Color.RED, m.getShapes().get("R1").getColor());
    assertEquals(50, m.getShapes().get("R1").getWidth(), .01);
    assertEquals(100, m.getShapes().get("R1").getHeight(), .01);
    //make sure x and y positions, color and dimensions are right for o2 at t = 0
    assertEquals(200, m.getShapes().get("O1").getPosition().getX(), .01);
    assertEquals(200, m.getShapes().get("O1").getPosition().getY(), .01);
    assertEquals(Color.RED, m.getShapes().get("O1").getColor());
    assertEquals(50, m.getShapes().get("O1").getWidth(), .01);
    assertEquals(100, m.getShapes().get("O1").getHeight(), .01);
    //"play" the animation updating it for 10 ticks
    for (int i = 1; i <= 10; i++) {
      m.update(i);
    }
    //make sure x and y position, color and dimensions are right for r1 at t = 10
    assertEquals(200, m.getShapes().get("R1").getPosition().getX(), .01);
    assertEquals(10, m.getShapes().get("R1").getPosition().getY(), .01);
    assertEquals(Color.RED, m.getShapes().get("R1").getColor());
    assertEquals(50, m.getShapes().get("R1").getWidth(), .01);
    assertEquals(100, m.getShapes().get("R1").getHeight(), .01);
    //make sure x and y positions, color and dimensions are right for o2 at t = 10
    assertEquals(10, m.getShapes().get("O1").getPosition().getX(), .01);
    assertEquals(10, m.getShapes().get("O1").getPosition().getY(), .01);
    assertEquals(Color.RED, m.getShapes().get("O1").getColor());
    assertEquals(50, m.getShapes().get("O1").getWidth(), .01);
    assertEquals(100, m.getShapes().get("O1").getHeight(), .01);
    for (int i = 11; i <= 35; i++) {
      m.update(i);
    }
    //make sure dimensions of r1 are correct at t = 35
    assertEquals(100, m.getShapes().get("R1").getWidth(), .01);
    assertEquals(100, m.getShapes().get("R1").getHeight(), .01);
    for (int i = 36; i <= 40; i++) {
      m.update(i);
    }
    //make sure that r1 changed to blue at t = 40
    assertEquals(Color.BLUE, m.getShapes().get("R1").getColor());
    for (int i = 41; i <= 50; i++) {
      m.update(i);
    }
    //make sure that r1 changed position to (300, 300) at t = 50
    assertEquals(300, m.getShapes().get("R1").getPosition().getX(), .01);
    assertEquals(300, m.getShapes().get("R1").getPosition().getY(), .01);
    //make sure that o1 changed position to (250, 250) at t = 50
    assertEquals(250, m.getShapes().get("O1").getPosition().getX(), .01);
    assertEquals(250, m.getShapes().get("O1").getPosition().getY(), .01);
    for (int i = 51; i <= 60; i++) {
      m.update(i);
    }
    //make sure x and y position, color and dimensions are right for r1 at t = 60
    assertEquals(300, m.getShapes().get("R1").getPosition().getX(), .01);
    assertEquals(300, m.getShapes().get("R1").getPosition().getY(), .01);
    assertEquals(Color.RED, m.getShapes().get("R1").getColor());
    assertEquals(50, m.getShapes().get("R1").getWidth(), .01);
    assertEquals(100, m.getShapes().get("R1").getHeight(), .01);
    //make sure x and y positions, color and dimensions are right for o2 at t = 60
    assertEquals(10, m.getShapes().get("O1").getPosition().getX(), .01);
    assertEquals(10, m.getShapes().get("O1").getPosition().getY(), .01);
    assertEquals(Color.RED, m.getShapes().get("O1").getColor());
    assertEquals(50, m.getShapes().get("O1").getWidth(), .01);
    assertEquals(50, m.getShapes().get("O1").getHeight(), .01);
    for (int i = 60; i <= 100; i++) {
      m.update(i);
    }
    //Nothing should've changes since last motion on either shape ends at t = 60
    //make sure x and y position, color and dimensions are right for r1 at t = 100
    assertEquals(300, m.getShapes().get("R1").getPosition().getX(), .01);
    assertEquals(300, m.getShapes().get("R1").getPosition().getY(), .01);
    assertEquals(Color.RED, m.getShapes().get("R1").getColor());
    assertEquals(50, m.getShapes().get("R1").getWidth(), .01);
    assertEquals(100, m.getShapes().get("R1").getHeight(), .01);
    //make sure x and y positions, color and dimensions are right for o2 at t = 100
    assertEquals(10, m.getShapes().get("O1").getPosition().getX(), .01);
    assertEquals(10, m.getShapes().get("O1").getPosition().getY(), .01);
    assertEquals(Color.RED, m.getShapes().get("O1").getColor());
    assertEquals(50, m.getShapes().get("O1").getWidth(), .01);
    assertEquals(50, m.getShapes().get("O1").getHeight(), .01);
  }

  /**
   * Test that the setStateTo() method will properly configure the state of the model to be that of
   * what it is supposed to be at t = ticks (passed into the method).
   */
  @Test
  public void testSetStateTo() {
    //add r1 to the model
    m.addShape(r1);
    m.addShape(o1);
    //add r1 motions
    m.addMotions(Arrays.asList(r1Move1, r1Move2, r1Grow1, r1TurnBlue));
    //add o1 motions
    m.addMotions(Arrays.asList(o1Move2, o1TurnGreen, o1Move1, o1Grow1));
    //make sure x and y position, color and dimensions are right for r1 at t = 0
    assertEquals(200, m.getShapes().get("R1").getPosition().getX(), .01);
    assertEquals(200, m.getShapes().get("R1").getPosition().getY(), .01);
    assertEquals(Color.RED, m.getShapes().get("R1").getColor());
    assertEquals(50, m.getShapes().get("R1").getWidth(), .01);
    assertEquals(100, m.getShapes().get("R1").getHeight(), .01);
    //make sure x and y positions, color and dimensions are right for o2 at t = 0
    assertEquals(200, m.getShapes().get("O1").getPosition().getX(), .01);
    assertEquals(200, m.getShapes().get("O1").getPosition().getY(), .01);
    assertEquals(Color.RED, m.getShapes().get("O1").getColor());
    assertEquals(50, m.getShapes().get("O1").getWidth(), .01);
    assertEquals(100, m.getShapes().get("O1").getHeight(), .01);

    //set the state to t = 60
    m.setStateTo(60);

    //make sure x and y position, color and dimensions are right for r1 at t = 60
    assertEquals(300, m.getShapes().get("R1").getPosition().getX(), .01);
    assertEquals(300, m.getShapes().get("R1").getPosition().getY(), .01);
    assertEquals(Color.RED, m.getShapes().get("R1").getColor());
    assertEquals(50, m.getShapes().get("R1").getWidth(), .01);
    assertEquals(100, m.getShapes().get("R1").getHeight(), .01);
    //make sure x and y positions, color and dimensions are right for o2 at t = 60
    assertEquals(10, m.getShapes().get("O1").getPosition().getX(), .01);
    assertEquals(10, m.getShapes().get("O1").getPosition().getY(), .01);
    assertEquals(Color.RED, m.getShapes().get("O1").getColor());
    assertEquals(50, m.getShapes().get("O1").getWidth(), .01);
    assertEquals(50, m.getShapes().get("O1").getHeight(), .01);

    //set state to t = 50
    m.setStateTo(50);

    //make sure that r1 changed position to (300, 300) at t = 50
    assertEquals(300, m.getShapes().get("R1").getPosition().getX(), .01);
    assertEquals(300, m.getShapes().get("R1").getPosition().getY(), .01);
    //make sure that o1 changed position to (250, 250) at t = 50
    assertEquals(250, m.getShapes().get("O1").getPosition().getX(), .01);
    assertEquals(250, m.getShapes().get("O1").getPosition().getY(), .01);
  }

  /**
   * Test that the setStateTo() method doesn't do anything or throw an error when applied to a model
   * without shapes.
   */
  @Test
  public void testSetStateToEmptyModel() {
    m.setStateTo(100);
    v.setShapes(m.getShapes());
    v.displayOutput();
    assertEquals("canvas 0 0 0 0\n", a.toString());
  }

  /**
   * Test that the setStateTo() method does not allow a time before the animation (negative time) as
   * a parameter.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSetStateToNegative() {
    m.setStateTo(-10);
  }


  @Test
  public void testRemoveShape() {
    m.addShape(r1);
    m.addShape(o1);
    assertEquals(2, m.getShapes().size());
    m.removeShape("O1");
    assertEquals(1, m.getShapes().size());
  }

  @Test
  public void testRemoveMultipleShape() {
    m.addShape(r1);
    m.addShape(o1);
    assertEquals(2, m.getShapes().size());
    m.removeShape("O1");
    m.removeShape("R1");
    assertEquals(0, m.getShapes().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveNonexistantShape() {
    m.addShape(r1);
    m.removeShape("O1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveShapeFromEmptyModel() {
    m.removeShape("O1");
  }

  @Test
  public void testRemoveMotions() {
    m.addShape(r1);
    m.addShape(o1);
    m.addMotions(Arrays.asList(r1Move1, o1Move1, o1Grow1, r1TurnBlue));
    assertEquals(6, r1.getMotionsList().size() + o1.getMotionsList().size());
    m.removeMotion(r1Move1);
    m.removeMotion(o1Grow1);
    assertEquals(4, r1.getMotionsList().size() + o1.getMotionsList().size());
  }

  @Test
  public void testRemoveOneMotion() {
    m.addShape(r1);
    m.addShape(o1);
    m.addMotions(Arrays.asList(r1Move1, o1Move1, o1Grow1, r1TurnBlue));
    assertEquals(6, r1.getMotionsList().size() + o1.getMotionsList().size());
    m.removeMotion(r1Move1);
    assertEquals(5, r1.getMotionsList().size() + o1.getMotionsList().size());
  }

  @Test
  public void testRemoveAllMotions() {
    m.addShape(r1);
    m.addShape(o1);
    m.addMotions(Arrays.asList(r1Move1, o1Move1, o1Grow1, r1TurnBlue));
    assertEquals(6, r1.getMotionsList().size() + o1.getMotionsList().size());
    m.removeMotion(r1Move1);
    m.removeMotion(r1TurnBlue);
    m.removeMotion(o1Grow1);
    m.removeMotion(o1Move1);
    assertEquals(2, r1.getMotionsList().size() + o1.getMotionsList().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveMotionThatHasntBeenAdded() {
    m.addShape(o1);
    m.removeMotion(o1Grow1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveMotionFromEmptyModel() {
    m.removeMotion(o1Grow1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveMotionShapeDoesntExist() {
    m.addShape(r1);
    m.removeMotion(o1Grow1);
  }

  @Test
  public void testGetShapesByLayer() {
    IShape r1 = new Rectangle("R1", 4);
    IShape r2 = new Rectangle("R2", 4);
    IShape r3 = new Rectangle("R3", 2);
    IShape r4 = new Rectangle("R4", 0);

    m.addShape(r1);
    m.addShape(r2);
    m.addShape(r3);
    m.addShape(r4);

    List<Map<String, IShape>> fromModel = m.getShapesByLayer();

    assertEquals(1, fromModel.get(0).size());
    assertEquals(0, fromModel.get(1).size());
    assertEquals(1, fromModel.get(2).size());
    assertEquals(0, fromModel.get(3).size());
    assertEquals(2, fromModel.get(4).size());
  }
}
