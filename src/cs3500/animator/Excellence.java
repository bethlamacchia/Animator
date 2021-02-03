package cs3500.animator;

import cs3500.animator.controller.AnimatorController;
import cs3500.animator.model.AnimatorModel;
import cs3500.animator.model.AnimatorModelImpl;
import cs3500.animator.util.AnimationBuilder;
import cs3500.animator.util.AnimationReader;
import cs3500.animator.view.IView;
import cs3500.animator.view.ViewFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Main class from which the animation is executed.
 */
public final class Excellence {

  /**
   * Main function which initializes the view, model and controller. Command line arguments are
   * passed into the program with at least (-in fileName) and (-view viewType) are required
   * arguments. If the speed is not specified the default is set to 1 tick per second. If an output
   * file is not specified the default is System.out. Formatted as follows: -in
   * "name-of-animation-file" -view "type-of-view" -out "where-output-show-go" -speed
   * "integer-ticks-per-second". The ordering of the arguments is not important.
   *
   * @param args command line arguments passed into the program
   */
  public static void main(String[] args) {
    AnimationBuilder<AnimatorModel> builder = new AnimatorModelImpl.Builder();
    AnimatorModel model = new AnimatorModelImpl();
    String viewType = "";
    String infile = "";
    String out = "";

    int speed = 1;
    for (int i = 0; i < args.length; i += 2) {
      switch (args[i]) {
        case "-in":
          // set input file name
          infile = args[i + 1];
          break;
        case "-view":
          viewType = args[i + 1];
          break;
        case "-out":
          // set output file name
          out = args[i + 1];
          break;
        case "-speed":
          // set speed
          speed = Integer.parseInt(args[i + 1]);
          break;
        default:
          IView.showErrorMessage("Invalid command line arguments!");
          System.exit(0);
      }
    }
    if (viewType.equals("")) {
      IView.showErrorMessage("Must specify type of view!");
      System.exit(0);
    } else if (infile.equals("")) {
      IView.showErrorMessage("Missing input file");
      System.exit(0);
    } else {
      try {
        File f = new File(infile);
        String path = f.getAbsolutePath();
        FileReader input = new FileReader(path);
        model = AnimationReader.parseFile(input, builder);
      } catch (Exception e) {
        IView.showErrorMessage(e.getLocalizedMessage());
        System.exit(0);
      }
    }

    IView view = generateView(out, viewType);

    AnimatorController controller = new AnimatorController(view, model, speed);
    controller.run();
  }

  /**
   * Set up the appendable object to pass textual based outputs to and return the desired view with
   * its appropriate parameters. For textual based views this method attempts to create a {@link
   * BufferedWriter} object to write to an output file if one is specified. If that fails an error
   * message popup is displayed. If no output file is specified System.out is used as the output
   * location.
   *
   * @param fileName String representing the name of the output file or "" if it was not specified
   * @param viewType String representing the type of view
   * @return {@link IView} object to be used in the animation
   * @throws IllegalArgumentException if the viewType parameter is not a supported view type
   */
  private static IView generateView(String fileName, String viewType) {
    Appendable ap;
    IView view = null;
    if (!fileName.equals("") && !viewType.equals("visual")) {
      try {
        ap = new BufferedWriter(new FileWriter(fileName, false));
        view = ViewFactory.setView(viewType);
        view.setOutput(ap);
      } catch (IOException e) {
        IView.showErrorMessage(e.getLocalizedMessage());
        System.exit(0);
      }
    } else {
      view = ViewFactory.setView(viewType);
    }
    return view;
  }
}