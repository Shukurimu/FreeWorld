package scene;

import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;

import node.Physical;
import util.ResourceManager;

public class FreeSpace extends SubScene {
  private final Group parent;
  public static final double RANGE = 4000;
  public static final double HALF_MID = RANGE * 0.5;

  private FreeSpace(Group parent, double width, double height) {
    super(parent, width, height, true, SceneAntialiasing.BALANCED);
    this.parent = parent;
    setFill(Color.SILVER);
  }

  public void add(Physical node) {
    parent.getChildren().add(node.getFxNode());
  }

  private static ImageView initImageView(Image image) {
    ImageView x = new ImageView(image);
    x.setFitWidth(RANGE);
    x.setFitHeight(RANGE);
    x.setTranslateX(-HALF_MID);
    x.setTranslateY(-HALF_MID);
    return x;
  }

  public static FreeSpace create(double width, double height) {
    Box box = new Box(RANGE, RANGE, RANGE);
    box.setDrawMode(DrawMode.LINE);
    box.setMaterial(new PhongMaterial(Color.RED));
    box.setTranslateY(-HALF_MID);

    Image floorImage = ResourceManager.getImage("doge");
    ImageView floor = initImageView(floorImage);
    floor.setRotationAxis(Rotate.X_AXIS);
    floor.setRotate(-90);

    Group parent = new Group(box, floor);
    parent.setAutoSizeChildren(false);
    return new FreeSpace(parent, width, height);
  }

}
