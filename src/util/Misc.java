package util;

public class Misc {

  /**
   * Normalizes a degree value to be in range (-180, 180].
   *
   * @param  deg  a degree.
   * @return the normalized degree.
   */
  public static double normalizeDegree(double deg) {
    return deg - Math.ceil(deg / 360.0 - 0.5) * 360.0;
  }

}
