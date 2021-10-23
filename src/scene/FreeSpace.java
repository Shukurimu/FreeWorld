package scene;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

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

  public void addChild(Node child) {
    parent.getChildren().add(child);
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
    Cylinder yAxis = new Cylinder(1, RANGE);
    yAxis.setMaterial(new PhongMaterial(Color.BLUE));

    Cylinder xAxis = new Cylinder(1, RANGE);
    xAxis.setMaterial(new PhongMaterial(Color.RED));
    xAxis.setRotate(90);
    xAxis.setRotationAxis(Rotate.Z_AXIS);

    Cylinder zAxis = new Cylinder(1, RANGE);
    zAxis.setMaterial(new PhongMaterial(Color.GREEN));
    zAxis.setRotate(90);
    zAxis.setRotationAxis(Rotate.X_AXIS);

    Image floorImage = ResourceManager.getImage("doge");
    ImageView floor = initImageView(floorImage);
    floor.setRotationAxis(Rotate.X_AXIS);
    floor.setRotate(-90);

    Translate move = new Translate(0, -HALF_MID, HALF_MID);
    Image wallImage = ResourceManager.getImage("cattle");
    ImageView wallN = initImageView(wallImage);
    wallN.getTransforms().addAll(move);
    ImageView wallS = initImageView(wallImage);
    wallS.setRotationAxis(Rotate.Y_AXIS);
    wallS.setRotate(90);
    wallS.getTransforms().addAll(move);
    ImageView wallE = initImageView(wallImage);
    wallE.setRotationAxis(Rotate.Y_AXIS);
    wallE.setRotate(180);
    wallE.getTransforms().addAll(move);
    ImageView wallW = initImageView(wallImage);
    wallW.setRotationAxis(Rotate.Y_AXIS);
    wallW.setRotate(270);
    wallW.getTransforms().addAll(move);

    Group parent = new Group( xAxis, yAxis, zAxis, floor, wallN, wallS, wallE, wallW);
    parent.setAutoSizeChildren(false);
    return new FreeSpace(parent, width, height);
  }

}
