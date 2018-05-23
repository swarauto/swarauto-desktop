package com.swarauto;

import java.io.IOException;

import com.swarauto.dependencies.DependenciesRegistry;
import com.swarauto.util.CommandUtil;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

/**
 * @author tuan3.nguyen@gmail.com
 */
public class CommandUtilTest extends BaseTest {

  private CommandUtil commandUtil;

  public CommandUtilTest() {
    super();
    commandUtil = DependenciesRegistry.commandUtil;
  }

  @Test
  public void testRunCmd_withInvalidCommand() {
    Assert.assertFalse(commandUtil.runCmd("this-is-an-invalid-command"));
  }

  /**
   * Test method for {@link CommandUtil#runCmd(java.lang.String[])}.
   *
   * @throws InterruptedException
   * @throws IOException
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRunCmd_withInvalidParams() {
    commandUtil.runCmd();
  }

  @Test
  public void testRunCmd_withLinuxPingCommand() {
    // This test only run on Linux
    Assume.assumeTrue(System.getProperty("os.name").toLowerCase().startsWith("linux"));
    Assert.assertTrue(commandUtil.runCmd("ping", "8.8.8.8", "-c", "1"));
  }

  @Test
  public void testRunCmd_withWindowsPingCommand() {
    // This test only run on Windows
    Assume.assumeTrue(System.getProperty("os.name").toLowerCase().startsWith("win"));
    Assert.assertTrue(commandUtil.runCmd("ping", "8.8.8.8", "-n", "1"));
  }
}
