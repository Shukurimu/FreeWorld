package util;

import java.io.FileInputStream;
import java.lang.System.Logger;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class ResourceManager {
  private static final Logger logger = System.getLogger("");
  private static final Map<String, Image> images = new HashMap<>();
  private static final Image EMPTY_IMAGE = new WritableImage(8, 8);

  private static String getPath(String fileName) {
    return Path.of("res", fileName).toAbsolutePath().toString();
  }

  public static void loadImage(String key, String fileName) {
    try {
      Image image = new Image(new FileInputStream(getPath(fileName)));
      images.put(key, image);
    } catch (Exception ex) {
      logger.log(Logger.Level.WARNING, ex);
    }
  }

  public static Image getImage(String key) {
    return images.getOrDefault(key, EMPTY_IMAGE);
  }

}
