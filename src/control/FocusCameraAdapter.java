package control;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class FocusCameraAdapter {
  private static final double MAX_DISTANCE = 800;
  private static final double MIN_DISTANCE = 100;
  private static final double DEFAULT_DISTANCE = 400;
  private static final double ZOOM_MULTIPLIER = -0.8;
  private static final double DRAG_MULTIPLIER = 0.1;
  private static final double MAX_ELEVATION = 30;
  private static final double MIN_ELEVATION = -50;
  private static final double DEFAULT_ELEVATION = -20;

  private double distance = 0;
  private final DoubleProperty elevation = new SimpleDoubleProperty(0.0);
  private final DoubleProperty direction = new SimpleDoubleProperty(0.0);
  private final Translate translate = new Translate();
  private final Rotate rotateElevation = new Rotate();
  private final Rotate rotateDirection = new Rotate(0, Rotate.Y_AXIS);

  public FocusCameraAdapter(Node camera, Node target) {
    camera.translateXProperty().bind(target.translateXProperty());
    camera.translateYProperty().bind(target.translateYProperty());
    camera.translateZProperty().bind(target.translateZProperty());
    rotateElevation.angleProperty().bind(elevation);
    rotateDirection.angleProperty().bind(direction);
    camera.getTransforms().addAll(rotateElevation, rotateDirection, translate);
    reset();
  }

  public ReadOnlyDoubleProperty getDirection() {
    return direction;
  }

  public void zoom(double v) {
    distance += v;
    distance = Math.min(MAX_DISTANCE, Math.max(MIN_DISTANCE, distance));
    translate.setZ(-distance);
  }

  public void updateViewport() {
    double radian = Math.toRadians(direction.get());
    double unitX = Math.sin(radian);
    double unitZ = Math.cos(radian);
    rotateElevation.setAxis(new Point3D(unitZ, 0, -unitX));
  }

  public void reset() {
    distance = DEFAULT_DISTANCE;
    this.zoom(0.0);
    elevation.set(DEFAULT_ELEVATION);
    direction.set(0.0);
    this.updateViewport();
  }

  private double dragStartX = 0.0;
  private double dragStartY = 0.0;
  private double dragStartElevation = 0.0;
  private double dragStartDirection = 0.0;

  public void handlePressed(MouseEvent event) {
    dragStartX = event.getScreenX();
    dragStartY = event.getScreenY();
    dragStartElevation = elevation.get();
    dragStartDirection = direction.get();
  }

  public void handleDragged(MouseEvent event) {
    double deltaX = event.getScreenX() - dragStartX;
    double deltaY = event.getScreenY() - dragStartY;
    direction.set(dragStartDirection + deltaX * DRAG_MULTIPLIER);
    double temp = dragStartElevation - deltaY * DRAG_MULTIPLIER;
    temp = Math.max(MIN_ELEVATION, Math.min(MAX_ELEVATION, temp));
    elevation.set(temp);
    updateViewport();
  }

  public void handleScroll(ScrollEvent event) {
    zoom(event.getDeltaY() * ZOOM_MULTIPLIER);
  }

}
