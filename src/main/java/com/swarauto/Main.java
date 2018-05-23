package com.swarauto;

import com.swarauto.dependencies.DependenciesRegistry;
import com.swarauto.game.profile.ProfileManager;
import com.swarauto.ui.ViewRegistry;
import com.swarauto.ui.main.PcMainView;
import com.swarauto.util.PcCommandUtil;
import com.swarauto.util.PcOcrUtil;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SaharaSkin;
import org.pushingpixels.substance.api.skin.SubstanceSaharaLookAndFeel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args) throws UnsupportedLookAndFeelException {
        LOG.info("App starting...");

        SubstanceLookAndFeel.setSkin(new SaharaSkin());
        UIManager.setLookAndFeel(new SubstanceSaharaLookAndFeel());

        DependenciesRegistry.settings = new PcSettings();
        DependenciesRegistry.commandUtil = new PcCommandUtil();
        DependenciesRegistry.ocrUtil = new PcOcrUtil();
        DependenciesRegistry.ocrUtil.initialize();
        DependenciesRegistry.profileManager = new ProfileManager();
        DependenciesRegistry.profileManager.setLocation(DependenciesRegistry.settings.getProfilesFolderPath());

        SwingUtilities.invokeLater(() -> {
            ViewRegistry.showView(PcMainView.class);
        });
    }

    private Main() {
        // Hidden constructor
    }
}
