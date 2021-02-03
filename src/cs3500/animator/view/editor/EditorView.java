package cs3500.animator.view.editor;

import cs3500.animator.model.frame.IFrame;
import cs3500.animator.model.shape.IShape;
import cs3500.animator.view.IInteractiveView;
import cs3500.animator.view.visual.VisualViewPanel;
import cs3500.animator.view.visual.draw.DrawCommand;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeListener;

/**
 * A type of view where the user can edit the existing view by pausing and restarting the animation,
 * as well as edit the animation by adding and removing shapes and keyframes.
 */
public class EditorView extends JFrame implements IInteractiveView, ActionListener {


  private final JPanel textPanel;
  private final JPanel sliderPanel;
  private final VisualViewPanel visualPanel;
  private final JButton increaseSpeed;
  private final JButton decreaseSpeed;
  private final JButton loopOnButton;
  private final JButton loopOffButton;
  private final JButton playButton;
  private final JButton startButton;
  private final JButton pauseButton;
  private final JButton restartButton;
  //buttons to modify shapes and keyframes
  private final JButton addShapeButton;
  private final JButton addKeyFrameButton;
  private final JButton removeShapeButton;
  private final JButton removeKeyFrameButton;
  private final JButton keyframeAtButton;
  private final JButton editLayerButton;
  // text fields
  private final JTextArea timeTextArea;
  private final JTextArea widthTextArea;
  private final JTextArea heightTextArea;
  private final JTextArea xTextArea;
  private final JTextArea yTextArea;
  private final JTextArea bColorArea;
  private final JTextArea rColorArea;
  private final JTextArea gColorArea;
  //shape name text area
  private final JTextArea sTextArea;
  private final JTextArea nameTextArea;
  //text area for the layer number
  private final JTextArea layerTextArea;
  //text area for editing preexisting shapes' layers
  private final JTextArea editLayerTextArea;
  private final JLabel speedDisplay;
  //drop down with list of shape names
  private final JComboBox<String> shapeNames;
  private final DefaultComboBoxModel<String> shapeNamesModel;
  //drop down with list of shape keyframes
  private final JComboBox<String> keyFrames;
  private final DefaultComboBoxModel<String> keyFramesModel;
  //drop down with list of possible shape types
  private final JComboBox<String> shapeTypes;
  //components for the purpose of editing layers
  private final JComboBox<String> layers;
  private final DefaultComboBoxModel<String> layersModel;
  private final DefaultComboBoxModel<String> shapesByLayerModel;
  private final JTextArea layer1;
  private final JTextArea layer2;
  private final JButton swapButton;
  private Map<String, IShape> shapes;
  private String shapeToAdd;
  private String shapeToRemove;
  private String keyframeToAdd;
  private String keyframeToRemove;
  private String timeToGetKeyframe;
  //private String selected;
  private JSlider slider;

  /**
   * The editor view containing a visual view panel for displaying the animation, as well as panels
   * and buttons for adding and removing shapes and keyframes and playing and pausing the
   * animation.
   */
  public EditorView() {
    super();

    this.shapes = new HashMap<>();

    this.setTitle("Animation Editor");
    this.setPreferredSize(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel mainPanel = new JPanel();
    //for elements to be arranged vertically within this panel
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
    //scroll bars around this main panel
    JScrollPane mainScrollPane = new JScrollPane(mainPanel);
    add(mainScrollPane);

    //use a borderlayout with drawing panel in center
    this.visualPanel = new VisualViewPanel();
    visualPanel.setPreferredSize(new Dimension(500, 500));
    JScrollPane visScrollPane = new JScrollPane(visualPanel);
    mainPanel.add(visScrollPane, BorderLayout.CENTER);

    slider = new JSlider();
    sliderPanel = new JPanel();
    mainPanel.add(sliderPanel);

    JPanel controlPanel = new JPanel();
    controlPanel.setLayout(new FlowLayout());
    mainPanel.add(controlPanel);

    JPanel playPanel = new JPanel();
    playPanel.setLayout(new FlowLayout());
    playPanel.setBorder(BorderFactory.createTitledBorder("Animation controls"));
    startButton = new JButton("Start");
    pauseButton = new JButton("Pause");
    playButton = new JButton("Play");
    restartButton = new JButton("Restart");

    startButton.setActionCommand("Start");
    playButton.setActionCommand("Play");
    pauseButton.setActionCommand("Pause");
    restartButton.setActionCommand("Restart");

    playPanel.add(pauseButton);
    playPanel.add(startButton);
    playPanel.add(restartButton);
    playPanel.add(playButton);

    controlPanel.add(playPanel);

    JPanel speedPanel = new JPanel();
    speedPanel.setLayout(new FlowLayout());
    speedPanel.setBorder(BorderFactory.createTitledBorder("Change animation speed"));

    increaseSpeed = new JButton("Increase speed");
    increaseSpeed.setActionCommand("Increase speed");
    decreaseSpeed = new JButton("Decrease speed");
    decreaseSpeed.setActionCommand("Decrease speed");
    speedPanel.add(increaseSpeed);
    speedPanel.add(decreaseSpeed);

    speedDisplay = new JLabel("Current speed: ");
    speedPanel.add(speedDisplay);

    controlPanel.add(speedPanel);

    JPanel loopPanel = new JPanel();
    loopPanel.setLayout(new FlowLayout());
    loopPanel.setBorder(BorderFactory.createTitledBorder("Toggle looping"));
    loopOnButton = new JButton("Enable looping");
    loopOnButton.setActionCommand("Enable Loop");
    loopOffButton = new JButton("Disable looping");
    loopOffButton.setActionCommand("Disable Loop");
    loopPanel.add(loopOnButton);
    loopPanel.add(loopOffButton);

    controlPanel.add(loopPanel);

    //panel for adding shapes
    JPanel addPanel = new JPanel();
    addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.X_AXIS));
    //panel for name and type
    JPanel nameAndTypePanel = new JPanel();
    nameAndTypePanel.setLayout(new BoxLayout(nameAndTypePanel, BoxLayout.X_AXIS));
    //dropdown for available shape types
    shapeTypes = new JComboBox<>(new String[]{"rectangle", "ellipse"});
    shapeTypes.setBorder(BorderFactory.createTitledBorder("Supported shape types:"));
    nameAndTypePanel.add(shapeTypes);
    //set the name of the shape to be added in text area
    sTextArea = new JTextArea();
    JScrollPane sScrollPane = new JScrollPane(sTextArea);
    sScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    sScrollPane.setBorder(BorderFactory.createTitledBorder("New shape name:"));
    nameAndTypePanel.add(sScrollPane);
    //layer info
    layerTextArea = new JTextArea();
    JScrollPane layerScrollPane = new JScrollPane(layerTextArea);
    layerScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    layerScrollPane.setBorder(BorderFactory.createTitledBorder("Layer:"));
    nameAndTypePanel.add(layerScrollPane);
    //add name and type panel to the add shape panel
    addPanel.add(nameAndTypePanel);
    addShapeButton = new JButton("Add Shape");
    addShapeButton.setActionCommand("Add shape");
    addPanel.add(addShapeButton);
    addPanel.setBorder(BorderFactory.createTitledBorder("Add new shape"));
    mainPanel.add(addPanel);

    //panel for shape and keyframe text
    textPanel = new JPanel();
    textPanel.setLayout(new FlowLayout());
    textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
    mainPanel.add(textPanel);

    //panel for dealing with sets of shapes expressed as layers
    JPanel layerPanel = new JPanel();
    layerPanel.setLayout(new BoxLayout(layerPanel, BoxLayout.X_AXIS));
    layerPanel.setBorder(BorderFactory.createTitledBorder("Animation by layer:"));
    layersModel = new DefaultComboBoxModel<>();
    layers = new JComboBox<>(layersModel);
    layers.setActionCommand("Interacting with layer list");
    layers.setBorder(BorderFactory.createTitledBorder("Current Layers:"));
    layerPanel.add(layers);
    shapesByLayerModel = new DefaultComboBoxModel<>();
    JComboBox<String> shapesByLayer = new JComboBox<>(shapesByLayerModel);
    shapesByLayer.setBorder(BorderFactory.createTitledBorder("Shapes in selected layer:"));
    layerPanel.add(shapesByLayer);
    //panel for the layer swap operation
    JPanel swapPanel = new JPanel();
    swapPanel.setLayout(new BoxLayout(swapPanel, BoxLayout.X_AXIS));
    swapPanel.setBorder(BorderFactory.createTitledBorder("Swap shapes in layers"));
    layer1 = new JTextArea();
    JScrollPane layer1ScrollPane = new JScrollPane(layer1);
    layer1ScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    layer1ScrollPane.setBorder(BorderFactory.createTitledBorder("Layer 1:"));
    swapPanel.add(layer1ScrollPane);
    swapButton = new JButton("swap");
    swapButton.setActionCommand("Swap layers");
    swapPanel.add(swapButton);
    layer2 = new JTextArea();
    layer2.setSize(new Dimension(10, 10));
    JScrollPane layer2ScrollPane = new JScrollPane(layer2);
    layer2ScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    layer2ScrollPane.setBorder(BorderFactory.createTitledBorder("Layer 2:"));
    swapPanel.add(layer2ScrollPane);
    layerPanel.add(swapPanel);
    mainPanel.add(layerPanel);
    //panel for editing new shapes / adding motions/keyframes
    JPanel editPanel = new JPanel();
    //for elements to be arranged vertically within this panel
    editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.X_AXIS));
    //sub-panel of the editPanel for the drop down of shape names and the button to remove a shape
    JPanel shapePanel = new JPanel();
    shapePanel.setLayout(new BoxLayout(shapePanel, BoxLayout.PAGE_AXIS));
    //store and update the shape names with the shapeNames combo box and its model that updates its
    //fields in setShapes().
    shapeNamesModel = new DefaultComboBoxModel<>();
    shapeNames = new JComboBox<>(shapeNamesModel);
    shapeNames.setBorder(BorderFactory.createTitledBorder("Current shapes:"));
    shapeNames.setActionCommand("Change visible frames");
    shapePanel.add(shapeNames);
    removeShapeButton = new JButton("Remove Shape");
    removeShapeButton.setActionCommand("Remove shape");
    shapePanel.add(removeShapeButton);
    editLayerTextArea = new JTextArea();
    JScrollPane editLayerScrollPane = new JScrollPane(editLayerTextArea);
    editLayerScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    editLayerScrollPane.setBorder(BorderFactory.createTitledBorder("Layer:"));
    shapePanel.add(editLayerScrollPane);
    editLayerButton = new JButton("Change Layer");
    editLayerButton.setActionCommand("Change Layer");
    shapePanel.add(editLayerButton);
    editPanel.add(shapePanel);
    //panel inside of the editPanel specifically for keyframes/motions
    JPanel framePanel = new JPanel();
    framePanel.setLayout(new BoxLayout(framePanel, BoxLayout.PAGE_AXIS));
    //panel for removing existing frames
    JPanel frameRemovePanel = new JPanel();
    frameRemovePanel.setLayout(new BoxLayout(frameRemovePanel, BoxLayout.X_AXIS));
    this.keyFramesModel = new DefaultComboBoxModel<>();
    this.keyFrames = new JComboBox<>(keyFramesModel);
    keyFrames.setBorder(BorderFactory.createTitledBorder("Frames for selected shape:"));
    frameRemovePanel.add(keyFrames);
    removeKeyFrameButton = new JButton("Remove Frame");
    removeKeyFrameButton.setActionCommand("Remove keyframe");
    frameRemovePanel.add(removeKeyFrameButton);
    this.timeTextArea = new JTextArea();
    timeTextArea.setBorder(BorderFactory.createTitledBorder("Frame time for selected shape"));
    frameRemovePanel.add(timeTextArea);
    keyframeAtButton = new JButton("Auto-generate frame at time");
    keyframeAtButton.setActionCommand("Get keyframe");
    frameRemovePanel.add(keyframeAtButton);
    framePanel.add(frameRemovePanel);
    //sub-panel of framePanel for adding frames
    JPanel frameAddPanel = new JPanel();
    frameAddPanel.setLayout(new BoxLayout(frameAddPanel, BoxLayout.X_AXIS));
    frameAddPanel.setBorder(BorderFactory.createTitledBorder("Frame to add:"));

    this.nameTextArea = new JTextArea();
    nameTextArea.setBorder(BorderFactory.createTitledBorder("Shape"));
    frameAddPanel.add(nameTextArea);

    this.xTextArea = new JTextArea();
    xTextArea.setBorder(BorderFactory.createTitledBorder("Pos X"));
    frameAddPanel.add(xTextArea);

    this.yTextArea = new JTextArea();
    yTextArea.setBorder(BorderFactory.createTitledBorder("Pos Y"));
    frameAddPanel.add(yTextArea);

    this.widthTextArea = new JTextArea();
    widthTextArea.setBorder(BorderFactory.createTitledBorder("Width"));
    frameAddPanel.add(widthTextArea);

    this.heightTextArea = new JTextArea();
    heightTextArea.setBorder(BorderFactory.createTitledBorder("Height"));
    frameAddPanel.add(heightTextArea);

    this.rColorArea = new JTextArea();
    rColorArea.setBorder(BorderFactory.createTitledBorder("R"));
    frameAddPanel.add(rColorArea);

    this.gColorArea = new JTextArea();
    gColorArea.setBorder(BorderFactory.createTitledBorder("G"));
    frameAddPanel.add(gColorArea);

    this.bColorArea = new JTextArea();
    bColorArea.setBorder(BorderFactory.createTitledBorder("B"));
    frameAddPanel.add(bColorArea);

    addKeyFrameButton = new JButton("Add Frame");
    addKeyFrameButton.setActionCommand("Add keyframe");
    frameAddPanel.add(addKeyFrameButton);

    framePanel.add(frameAddPanel);

    editPanel.add(framePanel);
    //set title of the editPanel
    editPanel.setBorder(BorderFactory.createTitledBorder("Edit existing shapes"));
    textPanel.add(editPanel);

    this.pack();
    setVisible(true);
  }

  @Override
  public void setShapes(Map<String, IShape> shapes) {
    if (shapes != null) {
      this.shapes = shapes;
      //whenever view's shapes are set so are the shape names that appear in the dropdown
      String s = (String) shapeNames.getSelectedItem();
      shapeNamesModel.removeAllElements();

      for (String st : shapes.keySet()) {
        shapeNamesModel.addElement(st);
      }
      if (shapes.containsKey(s)) {
        shapeNamesModel.setSelectedItem(s);
        editLayerTextArea.setText(Integer.toString(shapes.get(s).getLayer()));
      }
      visualPanel.setShapes(new ArrayList<>(this.shapes.values()));
    }
  }

  @Override
  public void displayOutput() {
    visualPanel.setShapes(new ArrayList<>(this.shapes.values()));
    textPanel.repaint();
    visualPanel.repaint();
    this.setVisible(true);
  }

  @Override
  public void passBounds(int x, int y, int width, int height) {
    visualPanel.setPreferredSize(new Dimension(width, height));
  }

  @Override
  public void setOutput(Appendable a) {
    throw new UnsupportedOperationException("No output file in non texted-based views!");
  }

  @Override
  public void setCommands(Map<String, DrawCommand> knownCommands) {
    visualPanel.setCommands(knownCommands);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "Add shape":
        this.shapeToAdd = sTextArea.getText()
            + " " + shapeTypes.getSelectedItem() + " " + layerTextArea.getText();
        break;
      case "Add keyframe":
        this.keyframeToAdd =
            nameTextArea.getText() + " " + timeTextArea.getText() + " "
                + xTextArea.getText() + " " + yTextArea.getText() + " "
                + rColorArea.getText() + " " + gColorArea.getText() + " " + bColorArea.getText()
                + " "
                + widthTextArea.getText() + " " + heightTextArea.getText();

        break;
      case "Remove shape":
        this.shapeToRemove = (String) shapeNames.getSelectedItem();
        break;
      case "Remove keyframe":
        this.keyframeToRemove =
            shapeNames.getSelectedItem() + " "
                + keyFrames.getSelectedItem();
        break;
      case "Get keyframe":
        this.timeToGetKeyframe = shapeNames.getSelectedItem() + " "
            + timeTextArea.getText();
        nameTextArea.setText((String) shapeNames.getSelectedItem());
        break;
      case "Change visible frames":
        keyFramesModel.removeAllElements();
        if (shapeNamesModel.getSelectedItem() != null) {
          if (shapes.containsKey(shapeNames.getSelectedItem())) {
            for (IFrame f : shapes.get(shapeNames.getSelectedItem()).getKeyFrames()) {
              keyFramesModel.addElement(f.toString());
            }
          } else {
            throw new IllegalArgumentException(
                "Invalid selected item " + shapeNamesModel.getSelectedItem());
          }
        }
        break;
      case "Swap layers":
        this.layer1.setText("");
        this.layer2.setText("");
        break;
      default:
        break;
    }
  }

  @Override
  public String getCommand(String controllerCommand) {
    switch (controllerCommand) {
      case "Add shape":
        return this.shapeToAdd;
      case "Remove shape":
        return this.shapeToRemove;
      case "Add keyframe":
        return this.keyframeToAdd;
      case "Remove keyframe":
        return this.keyframeToRemove;
      case "Generate keyframe":
        return this.timeToGetKeyframe;
      case "Change layer":
        return this.shapeNames.getSelectedItem() + " " + this.editLayerTextArea.getText();
      case "Swap layers":
        return this.layer1.getText() + " " + this.layer2.getText();
      default:
        throw new IllegalArgumentException("Unexpected command " + controllerCommand);
    }
  }

  @Override
  public void addActionListener(ActionListener actionListener) {
    startButton.addActionListener(actionListener);
    playButton.addActionListener(actionListener);
    pauseButton.addActionListener(actionListener);
    restartButton.addActionListener(actionListener);
    increaseSpeed.addActionListener(actionListener);
    decreaseSpeed.addActionListener(actionListener);
    loopOnButton.addActionListener(actionListener);
    loopOffButton.addActionListener(actionListener);
    addKeyFrameButton.addActionListener(actionListener);
    addKeyFrameButton.addActionListener(this);
    keyframeAtButton.addActionListener(actionListener);
    keyframeAtButton.addActionListener(this);
    removeShapeButton.addActionListener(actionListener);
    removeShapeButton.addActionListener(this);
    addShapeButton.addActionListener(actionListener);
    addShapeButton.addActionListener(this);
    removeKeyFrameButton.addActionListener(actionListener);
    removeKeyFrameButton.addActionListener(this);
    shapeNames.addActionListener(this);
    shapeNames.addActionListener(actionListener);
    shapeTypes.addActionListener(this);
    editLayerButton.addActionListener(actionListener);
    layers.addActionListener(actionListener);
    swapButton.addActionListener(actionListener);
  }

  @Override
  public void resetFocus() {
    this.setFocusable(true);
    this.requestFocus();
  }

  @Override
  public void populateTextFields(IShape s) {
    this.xTextArea.setText(String.valueOf(s.getPosition().getX()));
    this.yTextArea.setText(String.valueOf(s.getPosition().getY()));
    this.heightTextArea.setText(String.valueOf(s.getHeight()));
    this.widthTextArea.setText(String.valueOf(s.getWidth()));
    this.rColorArea.setText(String.valueOf(s.getColor().getRed()));
    this.gColorArea.setText(String.valueOf(s.getColor().getGreen()));
    this.bColorArea.setText(String.valueOf(s.getColor().getBlue()));
  }

  @Override
  public void clearTextFields() {
    this.xTextArea.setText("");
    this.yTextArea.setText("");
    this.heightTextArea.setText("");
    this.widthTextArea.setText("");
    this.rColorArea.setText("");
    this.gColorArea.setText("");
    this.bColorArea.setText("");
  }

  @Override
  public void displaySpeed(int speed) {
    speedDisplay.setText("Current speed: " + speed);
  }

  @Override
  public void configureSlider(int max, ChangeListener c) {
    this.slider = new JSlider();
    slider.setMinimum(0);
    slider.setValue(0);
    slider.setMajorTickSpacing(40);
    slider.setMinorTickSpacing(5);
    slider.setPaintTicks(true);
    slider.setPaintLabels(true);
    sliderPanel.add(slider);
    slider.setMaximum(max);
    slider.addChangeListener(c);
  }

  @Override
  public void setSliderTick(int t) {
    slider.setValue(t);
  }

  @Override
  public void updateLayerInterface(List<Map<String, IShape>> shapesByLayer) {
    int selected = layers.getSelectedIndex();
    layersModel.removeAllElements();
    shapesByLayerModel.removeAllElements();
    for (int i = 0; i < shapesByLayer.size(); i++) {
      layersModel.addElement(Integer.toString(i));
    }
    if (shapesByLayer.size() > selected && selected != -1) {
      for (IShape shape : shapesByLayer.get(selected).values()) {
        shapesByLayerModel.addElement(shape.getName());
      }
      layersModel.setSelectedItem(selected);
    }
  }
}