package com.swarauto;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Test;

/**
 * @author tuan3.nguyen@gmail.com
 */
public class MainTest {

    @Test
    public void testMainMethod() throws NoSuchMethodException, SecurityException {
        Class<Main> classUnderTest = Main.class;
        Method methodUnderTest = classUnderTest.getDeclaredMethod("main", String[].class);
        assertTrue(methodUnderTest != null);
    }
}
