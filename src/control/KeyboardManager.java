package control;

import java.util.EnumMap;
import java.util.Map;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyboardManager {
  private static final Map<KeyCode, InputState> keyState = new EnumMap<>(KeyCode.class);

  public static InputState get(KeyCode code) {
    return keyState.computeIfAbsent(code, key -> new InputState());
  }

  public static void handlePressed(KeyEvent event) {
    keyState.computeIfAbsent(event.getCode(), key -> new InputState()).holding = true;
  }

  public static void handleReleased(KeyEvent event) {
    keyState.computeIfAbsent(event.getCode(), key -> new InputState()).holding = false;
  }

}
