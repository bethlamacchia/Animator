package cs3500.animator.model.shape;

import cs3500.animator.model.frame.Frame;
import cs3500.animator.model.frame.IFrame;
import cs3500.animator.model.motion.Motion;
import cs3500.animator.model.motion.MotionFactory;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Abstract class representing a shape in an animation that uses {@link Motion} objects to
 * manipulate the properties of shapes as a function of time. Each shape has a list of associated
 * frames and motions that it uses to update its properties when the model calls its applyMotion()
 * method.
 */
public abstract class Shape implements IShape {

  private final String name;
  //current motions
  private final List<Motion> activeMotions;
  private final Comparator<IFrame> compareFrames = new Comparator<IFrame>() {
    @Override
    public int compare(IFrame o1, IFrame o2) {
      return o1.getTime() - o2.getTime();
    }
  };
  //map of times to keyframes
  private final Map<Integer, IFrame> keyFramesMap;
  // what type of shape
  protected String type;
  //mappings of start times to motions
  private Map<Integer, List<Motion>> motionsByTime;
  //list of motions in chronological order
  private List<Motion> motions;
  private Color color;
  private Position2D position;
  private double width;
  private double height;
  //what time the shape first appear
  private int startTime;
  private int endTime;
  //has the shape appeared on screen yet
  private boolean visible;
  //list of key frames
  private List<IFrame> keyFrames;
  // rotation
  private double heading;
  private int layer;

  /**
   * Constructs a shape with a color, position, width, height, and unique name. Default heading of
   * no rotation.
   *
   * @param color    the initial color of the shape
   * @param position the initial position of the shape
   * @param width    the initial width of the shape
   * @param height   the initial height of the shape
   * @param name     the unique name of the shape
   */
  public Shape(Color color, Position2D position, double width, double height, String name,
      int layer) {
    if (color == null) {
      throw new IllegalArgumentException("Color cannot be null!");
    } else if (position == null) {
      throw new IllegalArgumentException("Position cannot be null!");
    } else if (name == null) {
      throw new IllegalArgumentException("Name cannot be null!");
    } else if (!isValidDimension(width) || !isValidDimension(height)) {
      throw new IllegalArgumentException("Dimensions of a shape must be non-negative integers");
    }
    this.color = color;
    this.position = position;
    this.width = width;
    this.height = height;
    this.name = name;
    this.heading = 0;
    this.motions = new ArrayList<>();
    this.motionsByTime = new HashMap<>();
    this.activeMotions = new ArrayList<>();
    this.startTime = Integer.MAX_VALUE;
    this.endTime = -1;
    this.visible = false;
    this.layer = layer;

    this.keyFrames = new ArrayList<>();
    this.keyFramesMap = new LinkedHashMap<>();
  }

  /**
   * Constructs a shape with a color, position, width, height, heading in degrees, and unique name.
   *
   * @param color    the initial color of the shape
   * @param position the initial position of the shape
   * @param width    the initial width of the shape
   * @param height   the initial height of the shape
   * @param heading  the heading of the shape in degrees
   * @param name     the unique name of the shape
   */
  public Shape(Color color, Position2D position, double width, double height,
      double heading, String name, int layer) {
    if (color == null) {
      throw new IllegalArgumentException("Color cannot be null!");
    } else if (position == null) {
      throw new IllegalArgumentException("Position cannot be null!");
    } else if (name == null) {
      throw new IllegalArgumentException("Name cannot be null!");
    } else if (!isValidDimension(width) || !isValidDimension(height)) {
      throw new IllegalArgumentException("Dimensions of a shape must be non-negative integers");
    } else if (layer < 0) {
      throw new IllegalArgumentException("Cannot assign a shape a negative layer!");
    }
    this.color = color;
    this.position = position;
    this.width = width;
    this.height = height;
    this.name = name;
    this.heading = heading;
    this.motions = new ArrayList<>();
    this.motionsByTime = new HashMap<>();
    this.activeMotions = new ArrayList<>();
    this.startTime = Integer.MAX_VALUE;
    this.endTime = -1;
    this.visible = false;
    this.layer = layer;

    this.keyFrames = new ArrayList<>();
    this.keyFramesMap = new LinkedHashMap<>();
  }

  /**
   * Copy constructor to construct a shape object identical to the given shape object in everything
   * aside from pointer references.
   *
   * @param shape shape to be copied
   * @throws IllegalArgumentException if the shape passed in is {@code null}
   */
  public Shape(IShape shape) {
    if (shape == null) {
      throw new IllegalArgumentException("Cannot construct a null shape");
    }
    this.color = shape.getColor();
    this.position = shape.getPosition();
    this.width = shape.getWidth();
    this.height = shape.getHeight();
    this.name = shape.getName();
    this.startTime = shape.getStartTime();
    this.endTime = shape.getEndTime();
    this.visible = shape.isVisible();
    this.heading = shape.getHeading();
    this.activeMotions = new ArrayList<>();
    this.motionsByTime = new LinkedHashMap<>();
    this.motions = new ArrayList<>();
    this.keyFrames = shape.getKeyFrames();
    this.keyFramesMap = new LinkedHashMap<>();
    for (IFrame f : keyFrames) {
      this.keyFramesMap.put(f.getTime(), f);
    }
    generateMotions();
    this.layer = shape.getLayer();
  }

  @Override
  public int getLayer() {
    return this.layer;
  }

  @Override
  public void setLayer(int layer) {
    this.layer = layer;
  }

  @Override
  public int getStartTime() {
    return this.startTime;
  }

  @Override
  public void setStartTime(int time) {
    if (time >= 1) {
      this.startTime = time;
    } else {
      throw new IllegalArgumentException("Cannot have a shape exist at a negative time!");
    }
  }

  @Override
  public int getEndTime() {
    return this.endTime;
  }

  @Override
  public void addMotion(Motion m) {
    if (m == null) {
      throw new IllegalArgumentException("Cannot give a shape null as a motion");
    }
    if (this.startTime != Integer.MAX_VALUE) {
      if (this.overlaps(m)) {
        throw new IllegalArgumentException("Same motion types cannot overlap.");
      } else if (!m.isCompatibleWith(getStateAt(m.getStart()))) {
        throw new IllegalArgumentException(
            "Shape cannot have one property with two values simultaneously");
      } else if (!motionsByTime.containsKey(m.getStart())) {
        //add motion to the motion map
        List<Motion> mList = new ArrayList<>();
        mList.add(m);
        motionsByTime.put(m.getStart(), mList);
      } else { //there is at least one motion with same start time in the map
        //add it to the list of motions that start at that time
        motionsByTime.get(m.getStart()).add(m);
      }
      // add it to the list of motions
    } else {
      List<Motion> mList = new ArrayList<>();
      mList.add(m);
      motionsByTime.put(m.getStart(), mList);
    }
    motions.add(m);
    this.endTime = Math.max(m.getEnd(), this.endTime);
    this.startTime = Math.min(m.getStart(), this.startTime);
  }

  @Override
  public void applyMotion(int ticks) {
    visible = ticks >= this.getStartTime() && ticks <= this.endTime;
    if (ticks == this.getStartTime() && keyFramesMap.containsKey(ticks)) {
      keyFramesMap.get(ticks).apply(this);
    }
    updateMotion(ticks);
    //need to use iterator to avoid ConcurrentModificationException
    Iterator<Motion> itr = activeMotions.iterator();
    while (itr.hasNext()) {
      //get next active motion
      Motion m = itr.next();
      //remove complete motions
      if (ticks > m.getEnd() || ticks < m.getStart()) {
        itr.remove();
      } else {
        //apply active motions
        m.apply(this, ticks);
      }
    }
  }

  /**
   * Update the list of active motions by checking if there are any new motions starting at this
   * time and adding them if that is the case.
   *
   * @param ticks ticks elapsed since the start of the animation.
   */
  private void updateMotion(int ticks) {
    if (motionsByTime.containsKey(ticks)) {
      activeMotions.addAll(motionsByTime.get(ticks));
    }
    if (this.motionsByTime.size() != this.keyFrames.size() - 1) {
      generateMotions();
    }
  }

  @Override
  public Color getColor() {
    return new Color(color.getRGB());
  }

  @Override
  public void setColor(Color c) {
    if (c == null) {
      throw new IllegalArgumentException("Color of a shape cannot be set to null!");
    }
    this.color = c;
  }

  @Override
  public Position2D getPosition() {
    return new Position2D(position);
  }

  @Override
  public void setPosition(Position2D p) {
    if (p == null) {
      throw new IllegalArgumentException("Position of a shape cannot be set to null!");
    }
    this.position = p;
  }

  @Override
  public double getWidth() {
    return this.width;
  }

  @Override
  public void setWidth(double w) {
    if (!isValidDimension(w)) {
      throw new IllegalArgumentException("Width of a shape must be a non-negative integer");
    }
    this.width = w;
  }

  @Override
  public double getHeight() {
    return this.height;
  }

  @Override
  public void setHeight(double h) {
    if (!isValidDimension(h)) {
      throw new IllegalArgumentException("Height of a shape must be a non-negative integer");
    }
    this.height = h;
  }

  @Override
  public String getName() {
    if (name == null || name.equals("")) {
      throw new IllegalArgumentException("Name of a shape must have at least one character");
    }
    return this.name;
  }

  @Override
  public String getType() {
    return this.type;
  }

  /**
   * Returns a deep copy of the map except that the Motion objects pointed to in the Lists of
   * Motions are not deep copied because they are entirely immutable.
   *
   * @return a deep copy of the {@code motionsByTime} field
   */
  @Override
  public Map<Integer, List<Motion>> getMotionsMap() {
    Map<Integer, List<Motion>> newMap = new HashMap<>();
    for (Map.Entry<Integer, List<Motion>> entry : motionsByTime.entrySet()) {
      newMap.put(entry.getKey(), new ArrayList<>(entry.getValue()));
    }
    return newMap;
  }

  @Override
  public List<Motion> getActiveMotions() {
    return new ArrayList<>(this.activeMotions);
  }

  @Override
  public List<Motion> getMotionsList() {
    return new ArrayList<>(this.motions);
  }

  /**
   * Represents the animation of this shape as a string, with each motion on a new line and the
   * start and end time, position, size, and color specified for each motion.
   *
   * @return the animation represented as a string
   */
  @Override
  public String toString() {
    String[] output;
    if (!(keyFrames.isEmpty())) {
      output = new String[keyFrames.size() * 2 - 1];
    } else {
      output = new String[1];
    }
    output[0] = "shape " + this.name + " " + this.type + "\n";
    int j = 1;
    for (int i = 0; i < keyFrames.size() - 1; i++) {
      IFrame f1 = keyFrames.get(i);
      IFrame f2 = keyFrames.get(i + 1);

      output[j] = "motion " + this.name + " " + f1.toString() + " ";
      j++;
      output[j] = f2.toString() + "\n";
      j++;
    }
    return String.join("", output);
  }

  @Override
  public IShape getStateAt(int end) {
    IShape s = this.copy();
    if (end < this.startTime) {
      this.keyFramesMap.get(startTime).apply(s);
      return s;
    } else if (end > this.endTime) {
      this.keyFramesMap.get(endTime).apply(s);
      return s;
    }
    int index = end;
    int minDiff = Integer.MAX_VALUE;
    if (!keyFramesMap.containsKey(end)) {
      for (int i : this.keyFramesMap.keySet()) {
        if (end - i < minDiff && end >= i) {
          index = i;
          minDiff = end - i;
        }
      }
    }
    if (keyFramesMap.containsKey(index)) {
      IFrame f = this.keyFramesMap.get(index);
      f.apply(s);
    }
    s.applyMotion(end);
    return s;
  }

  @Override
  public abstract IShape copy();

  @Override
  public boolean overlaps(Motion m) {
    if (this.motions.isEmpty()) {
      return false;
    }
    for (Motion mo : this.motions) {
      if (mo.overlaps(m)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Determines whether the given dimension (width or height) is non-negative and within the bounds
   * of a Java integer value.
   *
   * @param d double representing a dimension of the shape
   * @return whether or not the dimension is valid
   */
  private boolean isValidDimension(double d) {
    return d >= 0 && d <= Integer.MAX_VALUE;
  }

  @Override
  public boolean isVisible() {
    return this.visible;
  }

  @Override
  public List<IFrame> getKeyFrames() {
    return new ArrayList<>(this.keyFrames);
  }

  @Override
  public void setKeyFrames(List<IFrame> keyframes) {
    List<IFrame> toSet = new ArrayList<>();
    for (IFrame f : keyframes) {
      if (!keyFramesMap.containsKey(f.getTime())) {
        keyFramesMap.put(f.getTime(), f);
        toSet.add(f);
        if (f.getTime() > this.getEndTime()) {
          this.endTime = f.getTime();
        } else if (f.getTime() < this.startTime) {
          setStartTime(f.getTime());
        }
      }
    }
    this.keyFrames = toSet;
  }

  @Override
  public void removeMotion(Motion m) {
    if (m == null) {
      throw new IllegalArgumentException("Can't remove a null motion!");
    }
    if (!(this.motionsByTime.containsKey(m.getStart()))) {
      throw new IllegalArgumentException("Motion doesn't exist for this shape!");
    }
    // if the motion is the first remove the first keyframe and set the starting time
    // if the motion is the last remove the last keyframe
    // otherwise remove the end frame and replace it with a new frame identical to the start
    // frame at that time
    if (m.getStart() == this.startTime) {
      removeKeyFrame(m.getStart());
      setStartTime(m.getEnd());
    } else if (m.getEnd() == this.getEndTime()) {
      removeKeyFrame(m.getEnd());
      this.endTime = m.getStart();
    } else {
      removeKeyFrame(m.getEnd());
      IFrame toCopy = keyFramesMap.get(m.getStart());
      addKeyFrame(new Frame(m.getEnd(), toCopy.getName(), toCopy.getPos(), toCopy.getColor(),
          toCopy.getWidth(), toCopy.getHeight(), toCopy.getHeading()));
    }
  }

  /**
   * Remove a keyframe from this shape and regenerate the list of motions without that keyframe.
   *
   * @param index time of the keyframe
   */
  @Override
  public void removeKeyFrame(int index) {
    if (!keyFramesMap.containsKey(index)) {
      throw new IllegalArgumentException("Frame to remove does not exist in shape!");
    }
    if (this.keyFrames.size() > 1) {
      if (index == this.startTime) {
        setStartTime(motionsByTime.get(index).get(0).getEnd());
      } else if (index == this.endTime) {
        this.endTime = keyFrames.get(keyFrames.size() - 2).getTime();
      }
    } else {
      setStartTime(Integer.MAX_VALUE);
      this.endTime = 0;
    }
    this.keyFrames.remove(keyFramesMap.get(index));
    this.keyFramesMap.remove(index);
    this.generateMotions();
  }

  @Override
  public void addKeyFrame(IFrame f) {
    Objects.requireNonNull(f, "Cannot add a null frame to a shape!");
    int time = f.getTime();
    if (keyFramesMap.containsKey(time)) {
      // remove it (replace it) and notify user
      this.keyFrames.remove(keyFramesMap.get(time));
      this.keyFramesMap.remove(time);
    }
    keyFrames.add(f);
    keyFramesMap.put(f.getTime(), f);
    if (f.getTime() < this.startTime) {
      setStartTime(f.getTime());
      f.apply(this);
    } else if (f.getTime() > this.endTime) {
      this.endTime = f.getTime();
    }
    this.generateMotions();
  }

  /**
   * Using the keyframes currently inside the shape, generate a list of motions that correspond to
   * the changes between keyframes, and set the new motions in the motions map and list fields.
   */
  private void generateMotions() {
    //clear motions map and list
    this.motionsByTime.clear();
    this.motions.clear();
    keyFrames.sort(compareFrames);

    if (keyFrames.size() > 1) {
      Map<Integer, List<Motion>> toSet = new HashMap<>();
      for (int i = 0; i < keyFrames.size() - 1; i++) {
        IFrame f1 = keyFrames.get(i);
        IFrame f2 = keyFrames.get(i + 1);
        toSet.put(f1.getTime(), MotionFactory.generateMotions(f1, f2, keyFrames.size()));
      }
      //set the motions map
      this.motionsByTime = toSet;
      //set the motions list
      List<Motion> newMotions = new ArrayList<>();
      for (List<Motion> ms : toSet.values()) {
        newMotions.addAll(ms);
      }
      this.motions = newMotions;
    }
  }

  @Override
  public double getHeading() {
    return this.heading;
  }

  @Override
  public void setHeading(double newHeading) {
    this.heading = newHeading;
  }
}