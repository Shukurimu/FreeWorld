package node;

import java.lang.System.Logger;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;

public class BaseNode implements Physical {
  private static final Logger logger = System.getLogger("");
  private static int serialNumberCounter = 0;
  private final Node fxNode;
  private final DoubleProperty visualX;
  private final DoubleProperty visualY;
  private final DoubleProperty visualZ;
  private final DoubleProperty visualR;
  private final int serialNumber;
  private double x = 0.0;
  private double y = 0.0;
  private double z = 0.0;
  private double r = 0.0;

  public BaseNode(Node fxNode) {
    this.fxNode = fxNode;
    visualX = fxNode.translateXProperty();
    visualY = fxNode.translateYProperty();
    visualZ = fxNode.translateZProperty();
    visualR = fxNode.rotateProperty();
    serialNumber = ++serialNumberCounter;
  }

  @Override
  public Node getFxNode() {
    return fxNode;
  }

  @Override
  public void setPosition(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public void setDirection(double r) {
    this.r = r;
  }

  @Override
  public void applyGravity(double g) {
    if (y >= 0.0) {
      return;
    }
    y -= g;
    if (y >= 0.0) {
      doLanding();
    }
  }

  @Override
  public void doLanding() {
    logger.log(Logger.Level.DEBUG, "%s doLanding()", this);
  }

  @Override
  public void draw() {
    visualX.set(x);
    visualY.set(y);
    visualZ.set(z);
    visualR.set(r);
  }

  @Override
  public String toString() {
    return String.format("Node%d", serialNumber);
  }

}
