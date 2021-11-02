package control;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;

import node.Physical;
import util.Misc;

public class KeyboardInput {
  public static final long PERIOD_MS = 16L;
  private static final double MOVEMENT_SPEED = 100.0 / PERIOD_MS;
  private static final double ROTATION_SPEED = 400.0 / PERIOD_MS;

  private final ReadOnlyDoubleProperty x;
  private final ReadOnlyDoubleProperty z;
  private final ReadOnlyDoubleProperty r;
  private final ReadOnlyDoubleProperty direction;
  private double destR = 0.0;
  private final Physical node;

  public KeyboardInput(Physical target, ReadOnlyDoubleProperty direction) {
    this.node = target;
    Node fxNode = target.getFxNode();
    this.x = fxNode.translateXProperty();
    this.z = fxNode.translateZProperty();
    this.r = fxNode.rotateProperty();
    this.direction = direction;
  }

  public void resetPosition() {
    node.setPosition(0, 0, 0);
  }

  public void run() {
    int state = (KeyboardManager.get(KeyCode.W).holding ? 8 : 0)
              | (KeyboardManager.get(KeyCode.S).holding ? 4 : 0)
              | (KeyboardManager.get(KeyCode.A).holding ? 2 : 0)
              | (KeyboardManager.get(KeyCode.D).holding ? 1 : 0);
    boolean doMove = true;
    switch (state) {
      case 0b1010 -> {
        destR = Misc.normalizeDegree(direction.get() + 45 * 7);
      }
      case 0b1001 -> {
        destR = Misc.normalizeDegree(direction.get() + 45 * 1);
      }
      case 0b0110 -> {
        destR = Misc.normalizeDegree(direction.get() + 45 * 5);
      }
      case 0b0101 -> {
        destR = Misc.normalizeDegree(direction.get() + 45 * 3);
      }
      case 0b1000, 0b1011 -> {
        destR = Misc.normalizeDegree(direction.get() + 45 * 0);
      }
      case 0b0100, 0b0111 -> {
        destR = Misc.normalizeDegree(direction.get() + 45 * 4);
      }
      case 0b0010, 0b1110 -> {
        destR = Misc.normalizeDegree(direction.get() + 45 * 6);
      }
      case 0b0001, 0b1101 -> {
        destR = Misc.normalizeDegree(direction.get() + 45 * 2);
      }
      default -> {
        doMove = false;
      }
    }

    double oldR = r.get();
    double deltaR = Misc.normalizeDegree(destR - oldR);
    double absDeltaR = Math.abs(deltaR);
    if (absDeltaR >= ROTATION_SPEED) {
      node.setDirection(oldR + Math.copySign(ROTATION_SPEED, deltaR));
      return;
    }

    boolean doRotate = destR != oldR;
    if (!doMove) {
      if (doRotate) {
        node.setDirection(destR);
      }
      return;
    }

    double timeRotate = absDeltaR < 0.01 ? 0.0 : absDeltaR / ROTATION_SPEED;
    double moveLength = MOVEMENT_SPEED * (1.0 - timeRotate);
    double radian = Math.toRadians(destR);
    final double newX = x.get() + moveLength * Math.sin(radian);
    final double newZ = z.get() + moveLength * Math.cos(radian);
    if (doRotate) {
      node.setDirection(destR);
    }
    node.setPosition(newX, 0, newZ);
  }

}
