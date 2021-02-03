import static org.junit.Assert.assertEquals;

import cs3500.animator.model.shape.IShape;
import cs3500.animator.model.shape.ShapeFactory;
import org.junit.Test;

/**
 * Test the method of the {@link ShapeFactoryTest} class.
 */
public class ShapeFactoryTest {

  @Test
  public void testCreateRectangle() {
    IShape r1 = ShapeFactory.createShape("r", "rectangle");
    assertEquals("r", r1.getName());
    assertEquals("rectangle", r1.getType());
  }

  @Test
  public void testCreateEllipse() {
    IShape e1 = ShapeFactory.createShape("e", "ellipse");
    assertEquals("e", e1.getName());
    assertEquals("ellipse", e1.getType());
  }

  @Test
  public void testCreateRectangleSetLayer() {
    IShape r1 = ShapeFactory.createShape("r", "rectangle", 2);
    assertEquals(2, r1.getLayer());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidShape() {
    IShape e1 = ShapeFactory.createShape("l", "lamp");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullName() {
    IShape e1 = ShapeFactory.createShape(null, "lamp");
  }

  @Test(expected = NullPointerException.class)
  public void testNullType() {
    IShape e1 = ShapeFactory.createShape("l", null);
  }
}