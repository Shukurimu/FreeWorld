package node;

import javafx.scene.Node;

public interface Physical {

  Node getFxNode();
  void setPosition(double x, double y, double z);
  void setDirection(double r);
  void applyGravity(double g);
  void doLanding();
  void draw();

}
