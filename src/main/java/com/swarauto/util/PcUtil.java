package com.swarauto.util;

import org.bytedeco.javacpp.indexer.UByteRawIndexer;
import org.bytedeco.javacpp.opencv_core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import static org.bytedeco.javacpp.opencv_core.CV_8UC;

public class PcUtil {
    private static final Logger LOG = LoggerFactory.getLogger(PcUtil.class);
    public static OS os;

    static {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("mac") || osName.contains("darwin")) {
            os = OS.MAC;
        } else if (osName.contains("win")) {
            os = OS.WINDOWS;
        } else if (osName.contains("nux")) {
            os = OS.LINUX;
        } else {
            os = OS.OTHER;
        }
    }

    public static String getAdbPath() {
        String path = "./bin/adb/adb";
        switch (os) {
            case MAC:
                path += ".mac";
                break;
            case WINDOWS:
                path += ".win.exe";
                break;
            case LINUX:
                path += ".linux";
                break;
            default:
                path += ".win.exe";
                break;
        }
        return path;
    }

    public static String runCmd(final String... params) {
        if (params == null || params.length == 0) {
            throw new IllegalArgumentException("command is null or empty");
        }
        final StringBuilder stringBuilder = new StringBuilder();
        final ProcessBuilder pb = new ProcessBuilder(Arrays.asList(params));
        try {
            final Process process = pb.redirectErrorStream(true).start();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String consoleLine;
            while ((consoleLine = reader.readLine()) != null) {
                stringBuilder.append(consoleLine);
            }
            process.waitFor();
            return stringBuilder.toString().trim();
        } catch (IOException | InterruptedException ex) {
            return null;
        }
    }

    public static String getConnectedDeviceName() {
        String result = PcUtil.runCmd(getAdbPath(), "devices");
        if (result != null) {
            result = result.replace("List of devices attached", "");
            result = result.replace("device", "").trim();

            if (result.contains("daemon not running")) {
                return null;
            }

            if (result.length() == 0) return null;
            return result;
        } else {
            return null;
        }
    }

    public static boolean isDeviceConnected() {
        String deviceName = getConnectedDeviceName();
        return !(deviceName == null || deviceName.length() == 0);
    }

    public static int getDeviceRotation() {
        String console = runCmd(PcUtil.getAdbPath(), "shell", "dumpsys input");
        if (console != null) {
            int begin = console.indexOf("SurfaceOrientation");
            begin = console.indexOf(":", begin);
            begin += 2;
            String rotation = console.substring(begin, begin + 1);

            try {
                return Integer.parseInt(rotation.trim());
            } catch (NumberFormatException e) {
                LOG.error("getDeviceRotation()", e);
            }
        }

        return -1;
    }

    public static opencv_core.Mat bufferedImageToMat(BufferedImage bi) {
        opencv_core.Mat mat = new opencv_core.Mat(bi.getHeight(), bi.getWidth(), CV_8UC(3));

        int r, g, b;
        UByteRawIndexer indexer = mat.createIndexer();
        for (int y = 0; y < bi.getHeight(); y++) {
            for (int x = 0; x < bi.getWidth(); x++) {
                int rgb = bi.getRGB(x, y);

                r = (byte) ((rgb >> 0) & 0xFF);
                g = (byte) ((rgb >> 8) & 0xFF);
                b = (byte) ((rgb >> 16) & 0xFF);

                indexer.put(y, x, 0, r);
                indexer.put(y, x, 1, g);
                indexer.put(y, x, 2, b);
            }
        }
        indexer.release();
        return mat;
    }

    public static BufferedImage readImage(String filePath) {
        File imageFile = new File(filePath);
        if (imageFile.exists()) {
            try {
                return ImageIO.read(imageFile);
            } catch (IOException e) {
                LOG.error("readImage", e);
            }
        }
        return null;
    }
}
