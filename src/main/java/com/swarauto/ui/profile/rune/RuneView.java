package com.swarauto.ui.profile.rune;

import com.swarauto.PcSettings;
import com.swarauto.dependencies.DependenciesRegistry;
import com.swarauto.game.profile.Profile;
import com.swarauto.ui.profile.widget.BoxPicker;
import com.swarauto.ui.profile.widget.IndicatorConfigView;
import com.swarauto.ui.profile.widget.ValueListener;
import com.swarauto.util.PcUtil;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;

import static com.swarauto.util.PcConverter.toAwtRectangle;

@Getter
public class RuneView extends JPanel implements ValueListener, ItemListener, IndicatorConfigView.Listener {
    private Listener listener;

    private BoxPicker rarityBox;
//    private IndicatorConfigView fiveStarRuneConfig;
//    private IndicatorConfigView sixStarRuneConfig;

    public RuneView(Listener listener) {
        this.listener = listener;
        PcSettings pcSettings = (PcSettings) DependenciesRegistry.settings;

        setLayout(new MigLayout("", "[grow,fill]", "[][][][][][][][][]"));

        // Rune rarity OCR
        rarityBox = new BoxPicker("Rune Rarity Area", "");
        rarityBox.setValueListener(this);
        rarityBox.setTutorialImage(PcUtil.readImage(pcSettings.getTutorialsFolderPath() + "/screen-analyzer/rarityBox.png"));
        add(rarityBox, "cell 0 0");

//        fiveStarRuneConfig = new IndicatorConfigView(this, fiveStarRuneIndicator, "Detect 5-star rune",
//                "For detecting if rune is 5-star");
//        add(fiveStarRuneConfig, "cell 0 1");
//
//        sixStarRuneConfig = new IndicatorConfigView(this, sixStarRuneIndicator, "Detect 6-star rune",
//                "For detecting if rune is 6-star");
//        add(sixStarRuneConfig, "cell 0 2");
    }

    public void populateFromModel(Profile profile) {
        rarityBox.setData(toAwtRectangle(profile.getRareLevelBox()));
//        fiveStarRuneConfig.setData(MemImageCache.getInstance().get(profile.getIndicatorFile(fiveStarRuneIndicator)));
//        sixStarRuneConfig.setData(MemImageCache.getInstance().get(profile.getIndicatorFile(sixStarRuneIndicator)));
    }

    @Override
    public void valueChanged(Object source, Object newValue) {
        listener.onRuneViewChanged();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        listener.onRuneViewChanged();
    }

    @Override
    public void onIndicatorImageChanged(IndicatorConfigView view, BufferedImage bufferedImage) {
        view.setData(bufferedImage);
        listener.onRuneViewChanged();
    }

    public interface Listener {
        void onRuneViewChanged();
    }
}
