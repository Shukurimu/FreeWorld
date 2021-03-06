import java.lang.System.Logger;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import control.FocusCameraAdapter;
import control.KeyboardInput;
import control.KeyboardManager;
import node.BaseNode;
import node.Physical;
import scene.FreeSpace;
import util.ResourceManager;

public class App extends Application {
  private static final Logger logger = System.getLogger("");
  private ScheduledThreadPoolExecutor executor = null;

  @Override
  public void init() throws Exception {
    logger.log(Logger.Level.INFO, javafx.application.Platform.isFxApplicationThread());
    executor = new ScheduledThreadPoolExecutor(1);
    ResourceManager.loadImage("doge", "doge.jpg");
    ResourceManager.loadImage("cattle", "cattle.jpeg");
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    PhongMaterial material = new PhongMaterial();
    material.setDiffuseMap(ResourceManager.getImage("cattle"));
    Sphere innerObject = new Sphere(10);
    innerObject.getTransforms().add(new Translate(0, -10, 0));
    innerObject.setMaterial(material);
    innerObject.setRotationAxis(Rotate.Y_AXIS);

    Physical mainCharacter = new BaseNode(innerObject);


    Camera camera = new PerspectiveCamera(true);
    camera.setFarClip(3000);
    FocusCameraAdapter focusCameraAdapter = FocusCameraAdapter.of(camera, mainCharacter);

    FreeSpace space = FreeSpace.create(800, 600);
    space.setCamera(camera);
    space.relocate(0, 0);
    space.add(mainCharacter);

    GridPane gridPane = new GridPane();
    gridPane.relocate(0, 0);
    gridPane.setPrefWidth(800);

    Pane root = new Pane(space, gridPane);

    KeyboardInput kmove = new KeyboardInput(mainCharacter, focusCameraAdapter.getDirection());
    executor.scheduleAtFixedRate(() -> {
      kmove.run();
      mainCharacter.draw();
    }, 0L, KeyboardInput.PERIOD_MS, TimeUnit.MILLISECONDS);

    space.addEventHandler(MouseEvent.MOUSE_PRESSED, focusCameraAdapter::handlePressed);
    space.addEventHandler(MouseEvent.MOUSE_DRAGGED, focusCameraAdapter::handleDragged);
    space.addEventHandler(ScrollEvent.SCROLL, focusCameraAdapter::handleScroll);

    primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, KeyboardManager::handlePressed);
    primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, KeyboardManager::handleReleased);
    primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
      switch (e.getCode()) {
        case ESCAPE -> javafx.application.Platform.exit();
        case C -> focusCameraAdapter.reset();
        case Z -> kmove.resetPosition();
        default -> {}
      }
    });
    primaryStage.setTitle("Test");
    primaryStage.setScene(new Scene(root, 800, 600));
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  @Override
  public void stop() throws Exception {
    logger.log(Logger.Level.INFO, javafx.application.Platform.isFxApplicationThread());
    executor.shutdown();
  }

  public static void main(String[] args) throws Exception {
    launch(args);
  }

}
