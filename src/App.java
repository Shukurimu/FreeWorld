import java.lang.System.Logger;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.HPos;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import control.FocusCameraAdapter;
import control.KeyboardInput;
import control.KeyboardManager;
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
    ResourceManager.loadImage("owl", "owl.jpg");
  }

  private Text createText(String format, Object o) {
    Text text = new Text();
    text.textProperty().bind(Bindings.format(format, o));
    text.setFont(Font.font("Consolas", 20));
    GridPane.setHgrow(text, Priority.ALWAYS);
    GridPane.setHalignment(text, HPos.CENTER);
    return text;
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    PhongMaterial material = new PhongMaterial();
    material.setDiffuseMap(ResourceManager.getImage("owl"));
    Sphere mainCharacter = new Sphere(10);
    mainCharacter.setTranslateY(-10);
    mainCharacter.setMaterial(material);
    mainCharacter.setRotationAxis(Rotate.Y_AXIS);

    Camera camera = new PerspectiveCamera(true);
    camera.setFarClip(3000);
    FocusCameraAdapter focusCameraAdapter = FocusCameraAdapter.of(camera, mainCharacter);

    FreeSpace space = FreeSpace.create(800, 600);
    space.setCamera(camera);
    space.relocate(0, 0);
    space.addChild(mainCharacter);

    GridPane gridPane = new GridPane();
    gridPane.addRow(
        0,
        createText("x %4.0f", mainCharacter.translateXProperty()),
        createText("y %4.0f", mainCharacter.translateYProperty()),
        createText("z %4.0f", mainCharacter.translateZProperty()),
        createText("f %4.0f", mainCharacter.rotateProperty()),
        createText("e %4.0f", focusCameraAdapter.getElevation()),
        createText("d %4.0f", focusCameraAdapter.getDirection())
    );
    gridPane.relocate(0, 0);
    gridPane.setPrefWidth(800);

    Pane root = new Pane(space, gridPane);

    KeyboardInput kmove = new KeyboardInput(mainCharacter, focusCameraAdapter.getDirection());
    executor.scheduleAtFixedRate(kmove, 0L, KeyboardInput.PERIOD_MS, TimeUnit.MILLISECONDS);

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
    executor.shutdown();
  }

  public static void main(String[] args) throws Exception {
    launch(args);
  }

}
