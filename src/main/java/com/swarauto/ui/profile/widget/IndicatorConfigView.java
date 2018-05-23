package com.swarauto.ui.profile.widget;

import com.swarauto.PcSettings;
import com.swarauto.dependencies.DependenciesRegistry;
import com.swarauto.game.indicator.Indicator;
import com.swarauto.util.PcUtil;
import lombok.Getter;

import java.awt.image.BufferedImage;

public class IndicatorConfigView extends ImagePicker implements ValueListener {
    private Listener listener;
    @Getter
    private Indicator indicator;

    public IndicatorConfigView(Listener listener, Indicator indicator, String labelText, String labelToolTip) {
        super(labelText, labelToolTip);

        this.listener = listener;
        this.indicator = indicator;
        setValueListener(this);

        // Load Tutorial Image
        PcSettings pcSettings = (PcSettings) DependenciesRegistry.settings;
        tutorialImage = PcUtil.readImage(pcSettings.getTutorialsFolderPath() + "/screen-analyzer/" + indicator.name() + ".png");
    }

    @Override
    public void valueChanged(Object source, Object newValue) {
        if (source == this) {
            listener.onIndicatorImageChanged(this, (BufferedImage) newValue);
            setData((BufferedImage) newValue);
        }
    }

    public interface Listener {
        void onIndicatorImageChanged(IndicatorConfigView view, BufferedImage bufferedImage);
    }
}
