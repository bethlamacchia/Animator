package cs3500.animator.controller;

/**
 * The controller for the animator, which takes in a view and a model and runs the program via the
 * run method. Represents the controller for a single animation.
 */
public interface IAnimatorController {

  /**
   * Run the animation, creating either a visual view or a file depending on what view type was
   * specified by the user.
   */
  void run();
}
