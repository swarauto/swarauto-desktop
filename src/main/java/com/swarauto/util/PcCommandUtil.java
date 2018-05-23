package com.swarauto.util;

import com.swarauto.game.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Arrays;

public class PcCommandUtil implements CommandUtil {
    protected static final Logger LOG = LoggerFactory.getLogger(PcCommandUtil.class);

    private Point centerScreen = new Point();

    @Override
    public String capturePhoneScreen() {
        final File screenshotFile = new File(System.getProperty("java.io.tmpdir"), "phoneScreenshot.png");
        long startTime = System.currentTimeMillis();
        runCmd(PcUtil.getAdbPath(), "shell", "screencap", "-p", "/mnt/sdcard/output.png");
        long afterScreenCap = System.currentTimeMillis();
        runCmd(PcUtil.getAdbPath(), "pull", "/mnt/sdcard/output.png", screenshotFile.getAbsolutePath());
        long afterPull = System.currentTimeMillis();
        runCmd(PcUtil.getAdbPath(), "shell", "rm", "/mnt/sdcard/output.png");
        long afterRm = System.currentTimeMillis();

        if (screenshotFile.exists()) {
            // Check to rotate image
            try {
                BufferedImage image = ImageIO.read(new File(screenshotFile.getAbsolutePath()));

                // Meet devices always capture as portrait
                if (image.getWidth() < image.getHeight()) {
                    centerScreen.x = image.getHeight() / 2;
                    centerScreen.y = image.getWidth() / 2;

                    int rotation = PcUtil.getDeviceRotation();
                    int quadrants = 0;
                    switch (rotation) {
                        case 1: // 90CW
                            quadrants = 3;
                            break;
                        case 3: // 90CCW
                            quadrants = 1;
                            break;
                    }
                    if (quadrants != 0) {
                        BufferedImage transformedImage = rotateImage(image, quadrants);
                        ImageIO.write(transformedImage, "PNG", new File(screenshotFile.getAbsolutePath()));
                    }
                } else {
                    centerScreen.x = image.getWidth() / 2;
                    centerScreen.y = image.getHeight() / 2;
                }
            } catch (IOException e) {
                LOG.error("capturePhoneScreen() {} {} {}"
                        , afterScreenCap - startTime
                        , afterPull - afterScreenCap
                        , afterRm - afterPull, e);
                return null;
            }
            long afterAll = System.currentTimeMillis();

            LOG.info("capturePhoneScreen() {} {} {} {}"
                    , afterScreenCap - startTime
                    , afterPull - afterScreenCap
                    , afterRm - afterPull
                    , afterAll - afterRm);
            return screenshotFile.getAbsolutePath();
        } else {
            LOG.error("capturePhoneScreen() {} {} {}"
                    , afterScreenCap - startTime
                    , afterPull - afterScreenCap
                    , afterRm - afterPull);
            return null;
        }
    }

    private BufferedImage rotateImage(BufferedImage image, int quadrants) throws IOException {
        int w0 = image.getWidth();
        int h0 = image.getHeight();
        int w1 = w0;
        int h1 = h0;
        int centerX = w0 / 2;
        int centerY = h0 / 2;

        if (quadrants % 2 == 1) {
            w1 = h0;
            h1 = w0;
        }

        if (quadrants % 4 == 1) {
            centerX = h0 / 2;
            centerY = h0 / 2;
        } else if (quadrants % 4 == 3) {
            centerX = w0 / 2;
            centerY = w0 / 2;
        }

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.setToQuadrantRotation(quadrants, centerX, centerY);
        AffineTransformOp opRotated = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage transformedImage = new BufferedImage(w1, h1, image.getType());
        transformedImage = opRotated.filter(image, transformedImage);
        return transformedImage;
    }

    @Override
    public boolean runCmd(final String... params) {
        if (params == null || params.length == 0) {
            throw new IllegalArgumentException("command is null or empty");
        }
        final ProcessBuilder pb = new ProcessBuilder(Arrays.asList(params));
        try {
            final Process process = pb.redirectErrorStream(true).start();
            final BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            String consoleLine;
            while ((consoleLine = reader.readLine()) != null) {
                // Just make buffer empty to prevent process from endless execution, especially on platform
                // that limited buffer size for standard input and output streams.
            }
            return process.waitFor() == 0;
        } catch (IOException | InterruptedException ex) {
            LOG.error("runCmd()", ex);
            return false;
        }
    }

    @Override
    public void tapScreen(final String x, final String y) {
        LOG.info("tapScreen: {},{}", x, y);
        runCmd(PcUtil.getAdbPath(), "shell", "input", "tap", x, y);
    }

    @Override
    public void tapScreenCenter() {
        int tapX = centerScreen.x + (int) (10 * (Math.random() - Math.random()));
        int tapY = centerScreen.y + (int) (10 * (Math.random() - Math.random()));

        if (tapX < 0) tapX = 0;
        if (tapY < 0) tapY = 0;

        tapScreen(String.valueOf(tapX), String.valueOf(tapY));
    }

    @Override
    public void screenLog(GameStatus status, File folder) {
        if (!folder.exists()) {
            folder.mkdirs();
        }

        if (status.getScreenFile() == null || status.getScreenFile().length() == 0) {
            return;
        }

        try {
            Files.copy(new File(status.getScreenFile()).toPath(),
                    new File(folder, String.format("%s.png", System.currentTimeMillis())).toPath());
        } catch (final IOException ex) {
            LOG.error("Could not log screenshot", ex);
        }
    }
}
