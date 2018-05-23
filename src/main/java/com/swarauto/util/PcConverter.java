package com.swarauto.util;

public class PcConverter {
  public static Point fromAwtPoint(java.awt.Point point) {
    if (point == null) return null;
    return new Point(point.x, point.y);
  }

  public static java.awt.Point toAwtPoint(Point point) {
    if (point == null) return null;
    return new java.awt.Point(point.x, point.y);
  }

  public static java.awt.Rectangle toAwtRectangle(Rectangle rectangle) {
    if (rectangle == null) return null;
    return new java.awt.Rectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
  }

  public static Rectangle fromAwtRectangle(java.awt.Rectangle rectangle) {
    if (rectangle == null) return null;
    return new Rectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
  }
}
