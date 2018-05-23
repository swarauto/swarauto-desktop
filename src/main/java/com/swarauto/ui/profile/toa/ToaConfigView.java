package com.swarauto.ui.profile.toa;

import com.swarauto.game.indicator.Indicator;
import com.swarauto.game.profile.Profile;
import com.swarauto.ui.profile.widget.IndicatorConfigView;
import com.swarauto.util.MemImageCache;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.image.BufferedImage;

@Getter
public class ToaConfigView extends JPanel implements IndicatorConfigView.Listener {
    private Listener listener;

    private IndicatorConfigView toaDefeatedIndicator;
    private IndicatorConfigView toaNextStageIndicator;

    private int rowCount = 0;

    public ToaConfigView(Listener listener) {
        this.listener = listener;

        setLayout(new MigLayout("", "[grow,fill]", "[][][][][][][][][]"));

        toaDefeatedIndicator = new IndicatorConfigView(this, Indicator.toaDefeatedIndicator, "Screen: `TOA stage DEFEATED`",
                "For detecting if TOA stage defeated, stop auto");
        addRow(toaDefeatedIndicator);
        toaNextStageIndicator = new IndicatorConfigView(this, Indicator.toaNextStageIndicator, "Screen: `Next Stage` button",
                "For detecting if TOA stage victory, process to next stage");
        addRow(toaNextStageIndicator);
    }

    private void addRow(IndicatorConfigView component) {
        add(component, "cell 0 " + rowCount++);
    }

    public void populateFromModel(Profile profile) {
        populateFromModel(profile, toaDefeatedIndicator);
        populateFromModel(profile, toaNextStageIndicator);
    }

    private void populateFromModel(Profile profile, IndicatorConfigView indicatorConfigView) {
        indicatorConfigView.setData(MemImageCache.getInstance().get(profile.getIndicatorFile(indicatorConfigView.getIndicator())));
    }

    @Override
    public void onIndicatorImageChanged(IndicatorConfigView view, BufferedImage bufferedImage) {
        listener.onToaConfigViewIndicatorChanged(view.getIndicator(), bufferedImage);
    }

    public interface Listener {
        void onToaConfigViewIndicatorChanged(Indicator indicator, BufferedImage bufferedImage);
    }
}
