package com.swarauto;

import com.swarauto.dependencies.DependenciesRegistry;
import com.swarauto.util.OcrUtil;
import com.swarauto.util.Rectangle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author tuan3.nguyen@gmail.com
 */
public class OcrUtilTest extends BaseTest {
    private OcrUtil ocrUtil;

    @Before
    public void setup() {
        ocrUtil = DependenciesRegistry.ocrUtil;
        ocrUtil.initialize();
    }

    /**
     * Test method for {@link OcrUtil#text(File)}.
     */
    @Test
    public void testTextBufferedImage() {
        final String sourceFile = "src/test/resources/hero.png";
        Assert.assertEquals("Hero", ocrUtil.text(new File(sourceFile)));
    }

    @Test
    public void testTextFile_withPercent() throws IOException {
        final String sourceFile = "src/test/resources/percent.png";
        Assert.assertTrue(ocrUtil.text(new File(sourceFile)).contains("°/o"));
    }

    @Test
    public void testRectangleRune() {
        final String sourceFile = "src/test/resources/hero.png";
        Rectangle rectangle = new Rectangle(0, 0, 163, 68);
        Assert.assertEquals("Hero", ocrUtil.text(new File(sourceFile), rectangle));
    }

    @Test
    public void testReadRune() {
        final String sourceFile = "src/test/resources/heroRuneScreen.png";
        Rectangle rectangle = new Rectangle(1171, 349, 156, 55);
        Assert.assertEquals("Hero", ocrUtil.text(new File(sourceFile), rectangle));
    }
}
