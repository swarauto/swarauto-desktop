package com.swarauto.util;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept;
import org.bytedeco.javacpp.tesseract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;

public class PcOcrUtil implements OcrUtil {
    private static final Logger LOG = LoggerFactory.getLogger(PcOcrUtil.class);
    private tesseract.TessBaseAPI api;

    @Override
    public void initialize() {
        api = new tesseract.TessBaseAPI();
        if (api.Init("./bin", "eng") != 0) {
            LOG.error("Could not initialize tesseract.");
            api = null;
        }
    }

    @Override
    public boolean isInitialized() {
        return api != null;
    }

    @Override
    public String text(final File imageFile) {
        return text(imageFile, null);
    }

    @Override
    public String text(File imageFile, Rectangle box) {
        lept.PIX image = pixRead(imageFile.getAbsolutePath());
        api.SetImage(image);
        if (box != null) {
            api.SetRectangle(box.x, box.y, box.width, box.height);
        }
        BytePointer outText = api.GetUTF8Text();
        String ret = outText.getString();
        if (ret != null) ret = ret.trim();

        // Destroy used object and release memory
        api.Clear();
        outText.deallocate();
        pixDestroy(image);

        return ret;
    }
}
