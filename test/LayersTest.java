import static org.junit.Assert.assertEquals;

import cs3500.animator.model.AnimatorModel;
import cs3500.animator.model.AnimatorModelImpl;
import cs3500.animator.model.shape.Ellipse;
import cs3500.animator.model.shape.IShape;
import cs3500.animator.model.shape.Rectangle;
import org.junit.Before;
import org.junit.Test;

/**
 * Test functionality of layers in the animator, including adding and removing layers,
 * adding shapes to specific layers, and reordering layers.
 */
public class LayersTest {

  AnimatorModel layersModel;

  @Before
  public void init() {
    layersModel = new AnimatorModelImpl.Builder().setBounds(0, 0,
        100, 100).declareShape("R2", "rectangle", 2)
        .declareShape("R1", "rectangle", 1)
        .declareShape("C2", "ellipse", 2)
        .declareShape("C3", "ellipse", 3)
        .declareShape("C0", "ellipse", 0)
        .build();
  }

  @Test
  public void testAddShapeToLayer() {

    // add a shape at a new layer
    IShape r4 = new Rectangle("R4");
    layersModel.addShape(r4);
    assertEquals(0, r4.getLayer());
    layersModel.addShapeToLayer("R4", 4);

    assertEquals(6, layersModel.getShapes().size());
    assertEquals(1, layersModel.getShapesByLayer().get(4).size());
    assertEquals(4, r4.getLayer());

    // add a shape at an existing layer
    IShape c1 = new Ellipse("C1");
    layersModel.addShape(c1);
    assertEquals(0, c1.getLayer());
    assertEquals(1, layersModel.getShapesByLayer().get(1).size());
    layersModel.addShapeToLayer("C1", 1);

    assertEquals(7, layersModel.getShapes().size());
    assertEquals(2, layersModel.getShapesByLayer().get(1).size());
    assertEquals(1, c1.getLayer());
  }

  @Test
  public void testRemoveLayer() {
    assertEquals(5, layersModel.getShapes().size());

    layersModel.deleteLayer(2);
    assertEquals(3, layersModel.getShapes().size());

    layersModel.deleteLayer(0);
    assertEquals(2, layersModel.getShapes().size());

    layersModel.deleteLayer(1);
    assertEquals(1, layersModel.getShapes().size());

    layersModel.deleteLayer(3);
    assertEquals(0, layersModel.getShapes().size());
  }

  @Test
  public void testAddLayer() {
    assertEquals(4, layersModel.getShapesByLayer().size());

    layersModel.addLayer(4);
    assertEquals(5, layersModel.getShapesByLayer().size());

    layersModel.addLayer(5);
    assertEquals(6, layersModel.getShapesByLayer().size());

  }

  @Test
  public void testReorder() {

    assertEquals(1, layersModel.getShapesByLayer().get(1).size());
    assertEquals(2, layersModel.getShapesByLayer().get(2).size());

    assertEquals(1, layersModel.getShapes().get("R1").getLayer());

    layersModel.reorder(1, 2);

    assertEquals(1, layersModel.getShapesByLayer().get(2).size());
    assertEquals(2, layersModel.getShapesByLayer().get(1).size());

    assertEquals(2, layersModel.getShapes().get("R1").getLayer());

  }
  
  @Test
  public void testChangeShapeLayer() {
    assertEquals(1, layersModel.getShapesByLayer().get(1).size());
    assertEquals(2, layersModel.getShapesByLayer().get(2).size());

    assertEquals(1, layersModel.getShapes().get("R1").getLayer());

    layersModel.changeShapeLayer("R1", 2);

    assertEquals(0, layersModel.getShapesByLayer().get(1).size());
    assertEquals(3, layersModel.getShapesByLayer().get(2).size());

    assertEquals(2, layersModel.getShapes().get("R1").getLayer());
  }
}
