package cs3500.animator.model;

import cs3500.animator.model.frame.Frame;
import cs3500.animator.model.frame.IFrame;
import cs3500.animator.model.motion.Motion;
import cs3500.animator.model.motion.MotionFactory;
import cs3500.animator.model.shape.IShape;
import cs3500.animator.model.shape.Position2D;
import cs3500.animator.model.shape.ShapeFactory;
import cs3500.animator.util.AnimationBuilder;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class for the implementation of an AnimatorModel. The model holds a list of shapes for the
 * animation and represents one animation and all of the objects (shapes) in the animation and their
 * movements throughout the animation (motions). The model is responsible for the behavior of the
 * shapes in the animation by updating them each tick and setting their states at any given point in
 * time, as well as enforcing any constraints on the shapes and motions. A motion cannot exist in if
 * the shape it belongs to does not exist in the model. Motions of the same type cannot overlap in
 * time on any one shape. Each shape has a unique name. the model if the shape it belongs to does
 * not exist in the model. Motions of the same type cannot overlap in time on any shape.
 */
public class AnimatorModelImpl implements AnimatorModel {

  private final Map<String, IShape> shapes;
  // bounds of the display in order: x, y, width, height
  private final int[] bounds;
  private List<Map<String, IShape>> shapesByLayer;

  /**
   * Create a new {@link AnimatorModelImpl} with the default screen width, height, x and y.
   */
  public AnimatorModelImpl() {
    this(0, 0, 500, 500);
  }

  /**
   * Create a new {@code AnimatorModelImpl} with the given screen width, height and x and y offset.
   *
   * @param x      integer representing the x offset of the display
   * @param y      integer representing the y offset of the display
   * @param width  integer representing the width of the display
   * @param height integer representing the height of the display
   * @throws IllegalArgumentException if the passed in width or height are negative
   */
  public AnimatorModelImpl(int x, int y, int width, int height) {
    if (width > 0 && height > 0) {
      this.bounds = new int[]{x, y, width, height};
      this.shapes = new LinkedHashMap<>();
      this.shapesByLayer = new ArrayList<>();
    } else {
      throw new IllegalArgumentException("Cannot have negative dimensions!");
    }
  }

  /**
   * Add the given shape to the given layer if the layer is valid.
   *
   * @param layer         non-negative integer representing layer to add given shape to
   * @param shape         IShape to be added to the layer
   * @param shapesByLayer List of maps of shapes names to shapes organized by layer.
   */
  private static void addToLayer(int layer, IShape shape, List<Map<String, IShape>> shapesByLayer) {
    if (shapesByLayer.size() >= layer) {
      try {
        shapesByLayer.get(layer).put(shape.getName(), shape);
      } catch (Exception ignored) {
        Map<String, IShape> newLayerMap = new LinkedHashMap<>();
        newLayerMap.put(shape.getName(), shape);
        shapesByLayer.add(layer, newLayerMap);
      }
    } else {
      for (int i = shapesByLayer.size(); i <= layer; i++) {
        Map<String, IShape> newLayerMap = new LinkedHashMap<>();
        shapesByLayer.add(i, newLayerMap);
      }
    }
  }

  @Override
  public List<Map<String, IShape>> getShapesByLayer() {
    List<Map<String, IShape>> newList = new ArrayList<>();
    for (Map<String, IShape> map : this.shapesByLayer) {
      Map<String, IShape> newMap = new LinkedHashMap<>();
      for (IShape shape : map.values()) {
        newMap.put(shape.getName(), shape.copy());
      }
      newList.add(newMap);
    }
    return newList;
  }

  @Override
  public void addShapeToLayer(String s, int layer) {
    if (!(shapes.containsKey(s))) {
      throw new IllegalArgumentException("Shape doesn't exist!");
    }
    IShape shape = shapes.get(s);
    shape.setLayer(layer);
    addToLayer(layer, shape, shapesByLayer);
  }

  @Override
  public void deleteLayer(int layer) {
    List<String> shapesToRemove = new ArrayList<>();
    for (String s : shapesByLayer.get(layer).keySet()) {
      shapesToRemove.add(s);
    }
    for (String s : shapesToRemove) {
      removeShape(s);
    }
  }

  @Override
  public void addLayer(int layer) {
    //if the layer doesn't exist
    if (shapesByLayer.size() <= layer) {
      // create a new layer
      for (int i = shapesByLayer.size(); i <= layer; i++) {
        Map<String, IShape> newLayerMap = new LinkedHashMap<>();
        shapesByLayer.add(i, newLayerMap);
      }
    }
  }

  @Override
  public void reorder(int layer1, int layer2) {
    for (IShape s : shapesByLayer.get(layer1).values()) {
      s.setLayer(layer2);
    }
    for (IShape s : shapesByLayer.get(layer2).values()) {
      s.setLayer(layer1);
    }
    Collections.swap(shapesByLayer, layer1, layer2);
  }

  @Override
  public void changeShapeLayer(String name, int layer) {
    if (layer < 0) {
      throw new IllegalArgumentException("Layer must be non-negative!");
    }
    if (layer >= shapesByLayer.size()) {
      for (int i = shapesByLayer.size(); i <= layer; i++) {
        addLayer(i);
      }
    }
    if (this.shapes.containsKey(name)) {
      int oldLayer = shapes.get(name).getLayer();
      shapesByLayer.get(oldLayer).remove(name);
      shapes.get(name).setLayer(layer);
      shapesByLayer.get(layer).put(name, shapes.get(name));
    } else {
      throw new IllegalArgumentException("Cannot find shape " + name + " in the model!");
    }
  }

  @Override
  public void setKeyFrames(Map<String, List<IFrame>> keyFrames) {
    for (String s : keyFrames.keySet()) {
      if (this.shapes.containsKey(s)) {
        IShape shape = shapes.get(s);
        shape.setKeyFrames(keyFrames.get(s));
      } else {
        throw new IllegalArgumentException("Shape hasn't been added to model yet!");
      }
    }
  }

  @Override
  public void removeShape(String name) {
    if (this.shapes.containsKey(name)) {
      this.shapesByLayer.get(shapes.get(name).getLayer()).remove(name);
      this.shapes.remove(name);
    } else {
      throw new IllegalArgumentException(String.format("Shape %s doesn't exist!", name));
    }
  }

  @Override
  public void removeMotion(Motion m) {
    if (this.shapes.containsKey(m.getId())) {
      this.shapes.get(m.getId()).removeMotion(m);
    } else {
      throw new IllegalArgumentException("Shape for this motion doesn't exist!");
    }
  }

  @Override
  public int getLastTick() {
    return Collections.max(shapes.values().stream().map(IShape::getEndTime).collect(
        Collectors.toSet()));
  }

  @Override
  public void addKeyFrame(IFrame f) {
    IShape s = shapes.get(f.getName());
    s.addKeyFrame(f);
  }

  @Override
  public void removeKeyFrame(IFrame f) {
    if (shapes.containsKey(f.getName())) {
      shapes.get(f.getName()).removeKeyFrame(f.getTime());
    } else {
      throw new IllegalArgumentException("Shape doesn't exist");
    }
  }

  /**
   * Add a new shape to this mapping of {@code String}s to {@link IShape}s.
   *
   * @param shape the shape to add
   * @throws IllegalArgumentException if the shape already exists in the model
   */
  @Override
  public void addShape(IShape shape) {
    if (!shapes.containsKey(shape.getName())) {
      shapes.put(shape.getName(), shape);
      if (shapesByLayer.size() >= shape.getLayer()) {
        try {
          shapesByLayer.get(shape.getLayer()).put(shape.getName(), shape);
        } catch (Exception ignored) {
          Map<String, IShape> newLayerMap = new LinkedHashMap<>();
          newLayerMap.put(shape.getName(), shape);
          shapesByLayer.add(shape.getLayer(), newLayerMap);
        }
      } else {
        for (int i = shapesByLayer.size(); i <= shape.getLayer(); i++) {
          Map<String, IShape> newLayerMap = new LinkedHashMap<>();
          shapesByLayer.add(i, newLayerMap);
        }
        shapesByLayer.get(shape.getLayer()).put(shape.getName(), shape);
      }
    } else {
      throw new IllegalArgumentException("Shape with this name already exists!");
    }
  }

  @Override
  public void addMotions(List<Motion> motions) {
    // now add keyframes, since addBuilderMotions doesn't use keyframes since they were
    // already added in the builder
    for (Motion m : motions) {
      if (!shapes.containsKey(m.getId())) {
        throw new IllegalArgumentException(
            String.format("Motion specified for shape %s not found in the model!", m.getId()));
      }
      IShape s = shapes.get(m.getId());
      s.addMotion(m);
      IShape s1 = s.getStateAt(m.getStart());
      IFrame f1 = new Frame(m.getStart(), s1.getName(), s1.getPosition(), s1.getColor(),
          s1.getWidth(),
          s1.getHeight(), s1.getHeading());
      m.apply(s1, m.getEnd());
      IFrame f2 = new Frame(m.getEnd(), s1.getName(), s1.getPosition(), s1.getColor(),
          s1.getWidth(),
          s1.getHeight(), s1.getHeading());
      addKeyFrame(f1);
      addKeyFrame(f2);
      if (m.getStart() < s.getStartTime()) {
        s.setStartTime(m.getStart());
      }
    }
  }

  @Override
  public void addMotion(Motion m) {
    this.addMotions(Arrays.asList(m));
  }

  /**
   * Adds all of the {@link Motion}s for the animation to the model in chronological order by
   * sorting the motions, ensuring they are valid (consistent and non overlapping) and then adding
   * them to their respective shapes.
   *
   * @param motions {@link Motion}s to be added
   * @throws IllegalArgumentException if the shape does not exist in the model or if the motion
   *                                  creates any consistency issues with the shape's existing
   *                                  motions.
   */
  private void addBuilderMotions(List<Motion> motions) {
    //store start times of motions
    List<Integer> startTimes = new ArrayList<>();
    //store mapping of start times to motions
    Map<Integer, List<Motion>> motionMap = new HashMap<>();
    for (Motion m : motions) {
      //add start time of each motion
      if (!startTimes.contains(m.getStart())) {
        startTimes.add(m.getStart());

        List<Motion> mList = new ArrayList<>();
        mList.add(m);
        motionMap.put(m.getStart(), mList);
      } else {
        //add motion to map
        motionMap.get(m.getStart()).add(m);
      }
    }
    //sort list of starting times
    Collections.sort(startTimes);
    //add motions in chronological order
    for (int i : startTimes) {
      for (Motion m : motionMap.get(i)) {
        if (shapes.containsKey(m.getId())) {
          IShape s = shapes.get(m.getId());
          s.addMotion(m);
        } else {
          throw new IllegalArgumentException("Shape does not exist in model.");
        }
      }
    }
  }

  @Override
  public int[] getBounds() {
    return this.bounds.clone();
  }

  @Override
  public void update(int ticks) {
    for (IShape shape : shapes.values()) {
      shape.applyMotion(ticks);
    }
  }

  @Override
  public void setStateTo(int ticks) {
    if (ticks < 0) {
      throw new IllegalArgumentException("Cannot set state to a negative time");
    }
    for (IShape shape : shapes.values()) {
      shapes.replace(shape.getName(), shape.getStateAt(ticks));
    }
  }

  @Override
  public Map<String, IShape> getShapes() {
    Map<String, IShape> newMap = new LinkedHashMap<>();
    for (IShape s : shapes.values()) {
      newMap.put(s.getName(), s.copy());
    }
    return newMap;
  }

  /**
   * Static builder class to build new AnimatorModelImpl's with pre specified parameters.
   */
  public static final class Builder implements AnimationBuilder<AnimatorModel> {

    private final List<Map<String, IShape>> builderShapesByLayer;
    private final Map<String, IShape> builderShapes;
    private final Map<String, List<IFrame>> keyframes;
    private final List<Motion> builderMotions;
    private int x;
    private int y;
    private int width;
    private int height;

    /**
     * Construct a builder object. Initialize maps for shapes and keyframes and list for motions.
     */
    public Builder() {
      builderShapes = new LinkedHashMap<>();
      builderMotions = new ArrayList<>();
      keyframes = new HashMap<>();
      builderShapesByLayer = new LinkedList<>();
    }

    @Override
    public AnimatorModelImpl build() {
      AnimatorModelImpl model = new AnimatorModelImpl(x, y, width, height);
      model.shapesByLayer = builderShapesByLayer;
      for (IShape s : builderShapes.values()) {
        model.addShape(s);
      }
      model.setKeyFrames(this.keyframes);
      model.addBuilderMotions(builderMotions);
      return model;
    }

    @Override
    public AnimationBuilder<AnimatorModel> setBounds(int x, int y, int width, int height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      return this;
    }

    @Override
    public AnimationBuilder<AnimatorModel> declareShape(String name, String type) {
      return declareShape(name, type, 0);
    }

    @Override
    public AnimationBuilder<AnimatorModel> declareShape(String name, String type, int layer) {
      IShape shape = ShapeFactory.createShape(name, type, layer);
      builderShapes.put(name, shape);
      addToLayer(layer, shape, builderShapesByLayer);
      return this;
    }

    @Override
    public AnimationBuilder<AnimatorModel> addMotion(String name, int t1, int x1, int y1,
        int w1, int h1, int r1, int g1, int b1, int heading1, int t2, int x2, int y2, int w2,
        int h2, int r2,
        int g2, int b2, int heading2) {

      this.addKeyframe(name, t1, x1, y1, w1, h1, r1, g1, b1, heading1);
      this.addKeyframe(name, t2, x2, y2, w2, h2, r2, g2, b2, heading2);

      if (!(builderShapes.containsKey(name))) {
        throw new IllegalArgumentException("Shape has not been declared yet");
      }

      IFrame f1 = new Frame(t1, name, new Position2D(x1, y1), new Color(r1, g1, b1), w1, h1,
          heading1);

      IFrame f2 = new Frame(t2, name, new Position2D(x2, y2), new Color(r2, g2, b2), w2, h2,
          heading2);

      if (t1 < this.builderShapes.get(name).getStartTime()) {
        this.builderShapes.get(name).setStartTime(t1);
        f1.apply(builderShapes.get(name));
      }

      builderMotions.addAll(MotionFactory.generateMotions(f1, f2, 7));
      return this;
    }

    @Override
    public AnimationBuilder<AnimatorModel> addKeyframe(String name, int t, int x, int y, int w,
        int h, int r, int g, int b, int heading) {

      if (keyframes.containsKey(name)) {
        keyframes.get(name).add(new Frame(t, name, new Position2D(x, y), new Color(r, g, b), w, h,
            heading));
      } else {
        List<IFrame> start = new ArrayList<>();
        start.add(new Frame(t, name, new Position2D(x, y), new Color(r, g, b), w, h, heading));
        keyframes.put(name, start);
      }
      return this;
    }
  }
}