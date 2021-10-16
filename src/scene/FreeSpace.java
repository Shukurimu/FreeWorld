package scene;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

public class FreeSpace extends SubScene {
  private final Group parent;

  private FreeSpace(Group parent, double width, double height) {
    super(parent, width, height, true, SceneAntialiasing.BALANCED);
    this.parent = parent;
    setFill(Color.SILVER);
  }

  public void addChild(Node child) {
    parent.getChildren().add(child);
  }

  public static FreeSpace create(double width, double height) {
    Cylinder yAxis = new Cylinder(1, 4000);
    yAxis.setMaterial(new PhongMaterial(Color.BLUE));

    Cylinder xAxis = new Cylinder(1, 4000);
    xAxis.setMaterial(new PhongMaterial(Color.RED));
    xAxis.setRotate(90);
    xAxis.setRotationAxis(Rotate.Z_AXIS);

    Cylinder zAxis = new Cylinder(1, 4000);
    zAxis.setMaterial(new PhongMaterial(Color.GREEN));
    zAxis.setRotate(90);
    zAxis.setRotationAxis(Rotate.X_AXIS);

    Group parent = new Group(xAxis, yAxis, zAxis);
    parent.setAutoSizeChildren(false);
    return new FreeSpace(parent, width, height);
  }

}
