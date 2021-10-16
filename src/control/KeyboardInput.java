package control;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class KeyboardInput {
  private static final double MOVEMENT_SPEED = 5;
  private static final double MONITOR_INTERVAL = 1000 / 60;

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

  private void update() {
    int state = (KeyboardManager.get(KeyCode.W).holding ? 8 : 0)
              | (KeyboardManager.get(KeyCode.S).holding ? 4 : 0)
              | (KeyboardManager.get(KeyCode.A).holding ? 2 : 0)
              | (KeyboardManager.get(KeyCode.D).holding ? 1 : 0);
    double result = switch (state) {
      case 0b1010 -> 360 - 45;
      case 0b1001 -> 360 + 45;
      case 0b0110 -> 180 + 45;
      case 0b0101 -> 180 - 45;
      case 0b1000, 0b1011 -> 360;
      case 0b0100, 0b0111 -> 180;
      case 0b0010, 0b1110 -> 270;
      case 0b0001, 0b1101 -> 90;
      default -> 0;
    };
    if (result > 0) {
      double radian = Math.toRadians(direction.get() + result);
      x.set(x.get() + MOVEMENT_SPEED * Math.sin(radian));
      z.set(z.get() + MOVEMENT_SPEED * Math.cos(radian));
    }
  }

  public void activate() {
    ScheduledService<Void> monitor = new ScheduledService<>() {
      @Override
      protected Task<Void> createTask() {
        return new Task<>() {
          @Override
          protected Void call() throws Exception {
            update();
            return null;
          }
        };
      }
    };
    monitor.setPeriod(Duration.millis(MONITOR_INTERVAL));
    monitor.start();
  }

}
