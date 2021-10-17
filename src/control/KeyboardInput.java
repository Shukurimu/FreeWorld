package control;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;

public class KeyboardInput implements Runnable {
  private static final double MOVEMENT_SPEED = 5;

  private final DoubleProperty x;
  private final DoubleProperty z;
  private final ReadOnlyDoubleProperty direction;

  public KeyboardInput(Node target, ReadOnlyDoubleProperty direction) {
    this.x = target.translateXProperty();
    this.z = target.translateZProperty();
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
    int result = switch (state) {
      case 0b1010 -> 7;
      case 0b1001 -> 1;
      case 0b0110 -> 5;
      case 0b0101 -> 3;
      case 0b1000, 0b1011 -> 0;
      case 0b0100, 0b0111 -> 4;
      case 0b0010, 0b1110 -> 6;
      case 0b0001, 0b1101 -> 2;
      default -> -1;
    };
    if (result >= 0) {
      final double radian = Math.toRadians(direction.get() + result * 45);
      Platform.runLater(() -> {
        x.set(x.get() + MOVEMENT_SPEED * Math.sin(radian));
        z.set(z.get() + MOVEMENT_SPEED * Math.cos(radian));
      });
    }
  }

}
