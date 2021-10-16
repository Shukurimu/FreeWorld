

import control.FocusCameraAdapter;
import control.KeyboardInput;
import control.KeyboardManager;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import scene.FreeSpace;

public class App extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {

    Sphere mainCharacter = new Sphere(10);

    Camera camera = new PerspectiveCamera(true);
    camera.setFarClip(3000);
    FocusCameraAdapter focusCameraAdapter = new FocusCameraAdapter(camera, mainCharacter);

    FreeSpace space = FreeSpace.create(800, 600);
    space.setCamera(camera);
    space.relocate(0, 200);
    space.addChild(mainCharacter);

    Pane pane = new Pane(space);
    Scene scene = new Scene(pane, 800, 800);

    KeyboardInput kmove = new KeyboardInput(mainCharacter, focusCameraAdapter.getDirection());
    kmove.activate();

    space.addEventHandler(MouseEvent.MOUSE_PRESSED, focusCameraAdapter::handlePressed);
    space.addEventHandler(MouseEvent.MOUSE_DRAGGED, focusCameraAdapter::handleDragged);
    space.addEventHandler(ScrollEvent.SCROLL, focusCameraAdapter::handleScroll);

    primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, KeyboardManager::handlePressed);
    primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, KeyboardManager::handleReleased);

    primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
      if (e.getCode() == KeyCode.ESCAPE) {
        javafx.application.Platform.exit();
      }
    });
    primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
      switch (e.getCode()) {
        case C:
          focusCameraAdapter.reset();
          return;
        case Z:
          kmove.resetPosition();
          return;
        default:
          return;
      }
    });
    primaryStage.setTitle("Test");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) throws Exception {
    launch(args);
  }

}
