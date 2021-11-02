package control;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import node.Physical;
import util.Misc;

public class FocusCameraAdapter {
  private static final double FAREST_DISTANCE = -400;
  private static final double NEAREST_DISTANCE = -50;
  private static final double DEFAULT_DISTANCE = -200;
  private static final double SCROLL_MULTIPLIER = -0.8;
  private static final double DRAG_MULTIPLIER = 0.1;
  private static final double MAX_ELEVATION = 0;
  private static final double MIN_ELEVATION = -40;
  private static final double DEFAULT_ELEVATION = -20;

  private final Translate distance = new Translate();
  private final Rotate elevation = new Rotate();
  private final Rotate direction = new Rotate(0, Rotate.Y_AXIS);

  private FocusCameraAdapter() {
    elevation.axisProperty()
             .bind(Bindings.createObjectBinding(this::calcRotateAxis, direction.angleProperty()));
  }

  public static FocusCameraAdapter of(Node camera, Physical target) {
    FocusCameraAdapter o = new FocusCameraAdapter();
    Node fxNode = target.getFxNode();
    camera.translateXProperty().bind(fxNode.translateXProperty());
    camera.translateYProperty().bind(fxNode.translateYProperty());
    camera.translateZProperty().bind(fxNode.translateZProperty());
    camera.getTransforms().addAll(o.elevation, o.direction, o.distance);
    o.reset();
    return o;
  }

  public Point3D calcRotateAxis() {
    double radian = Math.toRadians(direction.angleProperty().get() + 90);
    return new Point3D(Math.sin(radian), 0, Math.cos(radian));
  }

  public ReadOnlyDoubleProperty getElevation() {
    return elevation.angleProperty();
  }

  public ReadOnlyDoubleProperty getDirection() {
    return direction.angleProperty();
  }

  public void reset() {
    distance.setZ(DEFAULT_DISTANCE);
    elevation.angleProperty().set(DEFAULT_ELEVATION);
    direction.angleProperty().set(0.0);
  }

  private double anchorX = 0.0;
  private double anchorY = 0.0;
  private double anchorElevation = 0.0;
  private double anchorDirection = 0.0;

  public void handlePressed(MouseEvent event) {
    anchorX = event.getScreenX();
    anchorY = event.getScreenY();
    anchorElevation = elevation.angleProperty().get();
    anchorDirection = direction.angleProperty().get();
  }

  public void handleDragged(MouseEvent event) {
    double deltaX = event.getScreenX() - anchorX;
    double newDirection = anchorDirection + deltaX * DRAG_MULTIPLIER;
    newDirection = Misc.normalizeDegree(newDirection);
    direction.angleProperty().set(newDirection);

    double deltaY = event.getScreenY() - anchorY;
    double newElevation = anchorElevation - deltaY * DRAG_MULTIPLIER;
    newElevation = Math.max(MIN_ELEVATION, Math.min(MAX_ELEVATION, newElevation));
    elevation.angleProperty().set(newElevation);
  }

  public void handleScroll(ScrollEvent event) {
    double newDistance = distance.getZ() - event.getDeltaY() * SCROLL_MULTIPLIER;
    newDistance = Math.max(FAREST_DISTANCE, Math.min(NEAREST_DISTANCE, newDistance));
    distance.setZ(newDistance);
  }

}
