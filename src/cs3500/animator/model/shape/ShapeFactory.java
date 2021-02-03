package cs3500.animator.model.shape;

import java.util.Objects;

/**
 * Class that takes in the name and type of a shape and returns a new appropriate shape with the
 * default parameters.
 */
public final class ShapeFactory {

  /**
   * Create a new {@link IShape} of the given type with the given name or throw an exception if an
   * unsupported shape type is passed in.
   *
   * @param name  String representing the name of the shape
   * @param type  String representing the type of the shape
   * @param layer the layer that the shape is assigned to
   * @return a new shape with the given name of the given type
   * @throws NullPointerException if the type passed in is null
   */
  public static IShape createShape(String name, String type, int layer) {
    Objects.requireNonNull(type, "Cannot pass in a null type to the shape factory!");
    if (layer < 0) {
      throw new IllegalArgumentException(
          String.format("Attempted to assign invalid layer %d to shape %s!", layer, name));
    }
    switch (type) {
      case "rectangle":
        return new Rectangle(name, layer);
      case "ellipse":
        return new Ellipse(name, layer);
      default:
        throw new IllegalArgumentException("Unsupported shape type " + type);
    }
  }

  /**
   * Call the createShape() method passing in 0 as the layer. Creates a new IShape of the given name
   * and type.
   *
   * @param name String representing the name of the shape
   * @param type String representing the type of the shape
   * @return a new shape with the given name of the given type assigned layer 0
   */
  public static IShape createShape(String name, String type) {
    return createShape(name, type, 0);
  }
}
