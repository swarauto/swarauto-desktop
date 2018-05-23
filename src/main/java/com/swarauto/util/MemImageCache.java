package com.swarauto.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MemImageCache {
  private static final Logger LOG = LoggerFactory.getLogger(MemImageCache.class);
  private static MemImageCache instance;
  private Map<String, BufferedImage> data = new HashMap<>();

  public static MemImageCache getInstance() {
    if (instance == null) instance = new MemImageCache();
    return instance;
  }

  private MemImageCache() {

  }

  public BufferedImage get(File file) {
    if (file == null || !file.exists()) return null;

    return get(file.getAbsolutePath());
  }

  public BufferedImage get(String path) {
    if (path == null || path.length() == 0) return null;

    if (data.containsKey(path)) {
      return data.get(path);
    }

    File imageFile = new File(path);
    if (!imageFile.exists()) return null;

    try {
      BufferedImage image = ImageIO.read(imageFile);
      data.put(path, image);
      return image;
    } catch (IOException e) {
      LOG.error("get", e);
      return null;
    }
  }
}
