package com.swarauto;

import com.swarauto.dependencies.DependenciesRegistry;
import com.swarauto.game.profile.ProfileManager;
import com.swarauto.util.PcCommandUtil;
import com.swarauto.util.PcOcrUtil;

/**
 * Created by akivamu on 02/01/18.
 */
public abstract class BaseTest {
  public BaseTest() {
    DependenciesRegistry.settings = new PcSettings();
    DependenciesRegistry.commandUtil = new PcCommandUtil();
    DependenciesRegistry.ocrUtil = new PcOcrUtil();
    DependenciesRegistry.profileManager = new ProfileManager();
    DependenciesRegistry.profileManager.setLocation(DependenciesRegistry.settings.getProfilesFolderPath());
  }
}
