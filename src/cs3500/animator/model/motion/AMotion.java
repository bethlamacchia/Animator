package cs3500.animator.model.motion;

import cs3500.animator.model.shape.IShape;

/**
 * The abstract class AMotion represents the base class for various types of motions, including
 * moving, changing color, and resizing. It holds the start and end time of the motion, as well as
 * the shape the motion is being performed on.
 */
public abstract class AMotion implements Motion {

  protected final int start;
  protected final int end;
  private final String id;

  /**
   * The constructor for the abstract motion class.
   *
   * @param start the start time of this motion
   * @param end   the end time of this motion
   * @throws IllegalArgumentException if start or end times are negative or if start is not greater
   *                                  than end time
   */
  public AMotion(int start, int end, String id) {
    if (start < 0 || end < 0) {
      throw new IllegalArgumentException("Start and end times cannot be negative.");
    }
    if (start > end) {
      throw new IllegalArgumentException("Start time cannot be after end time.");
    }
    this.start = start;
    this.end = end;
    this.id = id;
  }

  @Override
  public int getStart() {
    return this.start;
  }

  @Override
  public int getEnd() {
    return this.end;
  }

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public abstract boolean isCompatibleWith(IShape shape);

  /**
   * Check that two motions times do not overlap. Called from the {@code overlaps()} method.
   *
   * @param m motion that is being tested against this
   * @return whether or not the two motions overlap
   */
  protected boolean checkOverlap(Motion m) {
    if (m.getStart() == this.getEnd()
        || m.getEnd() == this.getStart()) {
      return false;
    } else if (m.getStart() > this.start) {
      // m starts after this; if m ends before this, it overlaps
      return (m.getStart() < this.end);
    } else {
      //m starts before this - if m doesn't end before this, it overlaps
      return (m.getEnd() > this.start);
    }
  }
}
