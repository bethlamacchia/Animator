package cs3500.animator.model.motion;

import cs3500.animator.model.frame.IFrame;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class used to generate {@link Motion} objects that represent the changes between the passed in
 * starting and ending {@link IFrame}s representing the starting and ending state of an object in an
 * animation.
 */
public class MotionFactory {

  /**
   * Generate a list of the {@link Motion}s on a shape created by the two adjacent keyFrames f1 and
   * f2.
   *
   * @param f1 {@link IFrame} representing the starting key frame
   * @param f2 {@link IFrame} representing the ending key frame
   * @return list of motions between f1 and f2
   * @throws NullPointerException     if f1 or f2 is null
   * @throws IllegalArgumentException if f1 and f2 do not have the same name
   */
  public static List<Motion> generateMotions(IFrame f1, IFrame f2, int remaining) {
    Objects.requireNonNull(f1, "Cannot generate a motion with a null start frame");
    Objects.requireNonNull(f1, "Cannot generate a motion with a null end frame");
    List<Motion> motions = new ArrayList<>();
    if (f1.getName().equals(f2.getName())) {
      if (!f1.getColor().equals(f2.getColor())) {
        motions.add(new ColorMotion(f1.getTime(), f2.getTime(), f1.getName(), f1.getColor(),
            f2.getColor()));
      }
      if (!f1.getPos().equals(f2.getPos())) {
        motions.add(
            new MoveMotion(f1.getTime(), f2.getTime(), f1.getName(), f1.getPos(), f2.getPos()));
      }
      if (!(f1.getWidth() == f2.getWidth() && f1.getHeight() == f2.getHeight())) {
        motions.add(new ChangeSizeMotion(f1.getTime(), f2.getTime(), f1.getName(), f1.getWidth(),
            f1.getHeight(), f2.getWidth(), f2.getHeight()));
      }
      if (f1.getHeading() != f2.getHeading()) {
        motions.add(new Rotation(f1.getTime(), f2.getTime(), f1.getName(), f1.getHeading(),
            f2.getHeading()));
      }
      if (motions.isEmpty() && remaining > 1) {
        motions.add(
            new MoveMotion(f1.getTime(), f2.getTime(), f1.getName(), f1.getPos(), f2.getPos()));
      }
    } else {
      throw new IllegalArgumentException("The two frames must operate on the same shape!");
    }
    return motions;
  }
}
