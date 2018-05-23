package com.swarauto.ui.profile.rift;

import com.swarauto.game.indicator.Indicator;
import com.swarauto.game.profile.Profile;
import com.swarauto.ui.profile.widget.IndicatorConfigView;
import com.swarauto.ui.profile.widget.PointPicker;
import com.swarauto.ui.profile.widget.ValueListener;
import com.swarauto.util.MemImageCache;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.image.BufferedImage;

import static com.swarauto.util.PcConverter.toAwtPoint;

@Getter
public class RiftConfigView extends JPanel implements ValueListener, IndicatorConfigView.Listener {
    private Listener listener;

    private IndicatorConfigView riftBattleEndIndicator;
    private IndicatorConfigView stoneRewardIndicator;
    private PointPicker sellStoneLocation;
    private IndicatorConfigView confirmSellStoneIndicator;
    private PointPicker sellStoneConfirmLocation;
    private PointPicker getStoneLocation;

    private int rowCount = 0;

    public RiftConfigView(Listener listener) {
        this.listener = listener;

        setLayout(new MigLayout("", "[grow,fill]", "[][][][][][][][][]"));

        riftBattleEndIndicator = new IndicatorConfigView(this, Indicator.riftBattleEndIndicator, "Screen: `Rift Battle Ended`",
                "For detecting battle end");
        addRow(riftBattleEndIndicator);

        stoneRewardIndicator = new IndicatorConfigView(this, Indicator.stoneRewardIndicator, "Stone Reward dialog",
                "For detecting if there is Stone/Gem dropped in Rifts");
//        addRow(stoneRewardIndicator);

        sellStoneLocation = new PointPicker("Sell Stone button click location", "");
//        addRow(sellStoneLocation);

        confirmSellStoneIndicator = new IndicatorConfigView(this, Indicator.confirmSellStoneIndicator, "Confirm Sell Stone button image",
                "Confirm sell Stone/Gem");
//        addRow(confirmSellStoneIndicator);

        sellStoneConfirmLocation = new PointPicker("Confirm Sell Rare Stone button click location", "");
//        addRow(sellStoneConfirmLocation);

        getStoneLocation = new PointPicker("Get Stone button click location", "");
//        addRow(getStoneLocation);
    }

    private void addRow(PointPicker component) {
        component.setValueListener(this);
        add(component, "cell 0 " + rowCount++);
    }

    private void addRow(IndicatorConfigView component) {
        add(component, "cell 0 " + rowCount++);
    }

    public void populateFromModel(Profile profile) {
        populateFromModel(profile, riftBattleEndIndicator);
        populateFromModel(profile, stoneRewardIndicator);
        populateFromModel(profile, confirmSellStoneIndicator);
        sellStoneLocation.setData(toAwtPoint(profile.getSellGemLocation()));
        sellStoneConfirmLocation.setData(toAwtPoint(profile.getSellStoneConfirmation()));
        getStoneLocation.setData(toAwtPoint(profile.getGetGemLocation()));
    }

    private void populateFromModel(Profile profile, IndicatorConfigView indicatorConfigView) {
        indicatorConfigView.setData(MemImageCache.getInstance().get(profile.getIndicatorFile(indicatorConfigView.getIndicator())));
    }

    @Override
    public void valueChanged(Object source, Object newValue) {
        listener.onRiftConfigViewLocationsChanged();
    }

    @Override
    public void onIndicatorImageChanged(IndicatorConfigView view, BufferedImage bufferedImage) {
        listener.onRiftConfigViewIndicatorChanged(view.getIndicator(), bufferedImage);
    }

    public interface Listener {
        void onRiftConfigViewLocationsChanged();

        void onRiftConfigViewIndicatorChanged(Indicator indicator, BufferedImage bufferedImage);
    }
}
