package control;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;

import util.Misc;

public class KeyboardInput implements Runnable {
  public static final long PERIOD_MS = 16L;
  private static final double MOVEMENT_SPEED = 100.0 / PERIOD_MS;
  private static final double ROTATION_SPEED = 400.0 / PERIOD_MS;

  private final DoubleProperty x;
  private final DoubleProperty z;
  private final DoubleProperty r;
  private final ReadOnlyDoubleProperty direction;
  private double destR = 0.0;

  public KeyboardInput(Node target, ReadOnlyDoubleProperty direction) {
    this.x = target.translateXProperty();
    this.z = target.translateZProperty();
    this.r = target.rotateProperty();
    this.direction = direction;
  }

  public void resetPosition() {
    x.set(0);
    z.set(0);
  }

  @Override
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
      final double newR = oldR + Math.copySign(ROTATION_SPEED, deltaR);
      Platform.runLater(() -> {
        r.set(newR);
      });
      return;
    }

    boolean doRotate = destR != oldR;
    if (!doMove) {
      if (doRotate) {
        Platform.runLater(() -> {
          r.set(destR);
        });
      }
      return;
    }

    double timeRotate = absDeltaR < 0.01 ? 0.0 : absDeltaR / ROTATION_SPEED;
    double moveLength = MOVEMENT_SPEED * (1.0 - timeRotate);
    double radian = Math.toRadians(destR);
    final double newX = x.get() + moveLength * Math.sin(radian);
    final double newZ = z.get() + moveLength * Math.cos(radian);
    Platform.runLater(() -> {
      if (doRotate) {
        r.set(destR);
      }
      x.set(newX);
      z.set(newZ);
    });
  }

}
