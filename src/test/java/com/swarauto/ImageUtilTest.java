package com.swarauto;

import com.swarauto.util.ImageUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author tuan3.nguyen@gmail.com
 */
public final class ImageUtilTest {

    @Test
    public void testContainsMethodNegative_byInvalidTemplate() {
        String sourceFile = "src/test/resources/sampleScreen.png";
        String templateFile = "src/test/resources/invalidTemplate.png";
        Assert.assertNull(ImageUtil.contains(sourceFile, templateFile, 99));
    }

    @Test
    public void testContainsMethodNegative_byValidTemplate1_butThresholdMoreThan100() {
        String sourceFile = "src/test/resources/sampleScreen.png";
        String templateFile = "src/test/resources/validTemplate1.png";
        Assert.assertNull(ImageUtil.contains(sourceFile, templateFile, 101));
    }

    @Test
    public void testContainsMethodNegative_byValidTemplate2_butThresholdMoreThan100() {
        String sourceFile = "src/test/resources/sampleScreen.png";
        String templateFile = "src/test/resources/validTemplate2.png";
        Assert.assertNull(ImageUtil.contains(sourceFile, templateFile, 101));
    }

    @Test
    public void testContainsMethodPositive_byValidTemplate1() {
        String sourceFile = "src/test/resources/sampleScreen.png";
        String templateFile = "src/test/resources/validTemplate1.png";
        Assert.assertNotNull(ImageUtil.contains(sourceFile, templateFile, 99));
    }

    @Test
    public void testContainsMethodPositive_byValidTemplate2() {
        String sourceFile = "src/test/resources/sampleScreen.png";
        String templateFile = "src/test/resources/validTemplate2.png";
        Assert.assertNotNull(ImageUtil.contains(sourceFile, templateFile, 99));
    }
}
