package com.swarauto.ui.profile.battle;

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
public class BattleConfigView extends JPanel implements ValueListener, IndicatorConfigView.Listener {
    private Listener listener;

    // Common
    private IndicatorConfigView startBattleButtonIndicator;
    private PointPicker startBattleButtonLocation;
    private IndicatorConfigView notReviveButtonIndicator;
    private PointPicker notReviveButtonLocation;
    private IndicatorConfigView battleEndIndicator;
    private IndicatorConfigView runeRewardIndicator;
    private PointPicker sellRuneButtonLocation;
    private IndicatorConfigView confirmSellRuneIndicator;
    private PointPicker sellRuneConfirmButtonLocation;
    private PointPicker getRuneButtonLocation;
    private IndicatorConfigView otherRewardIndicator;
    private PointPicker getRewardButtonLocation;
    private IndicatorConfigView replayBattleIndicator;
    private PointPicker replayBattleButtonLocation;
    private IndicatorConfigView noEnergyIndicator;

    private int rowCount = 0;

    public BattleConfigView(Listener listener) {
        this.listener = listener;

        setLayout(new MigLayout("", "[grow,fill]", "[][][][][][][][][]"));

        // Common
        startBattleButtonIndicator = new IndicatorConfigView(this, Indicator.startBattleIndicator, "Screen: `Team Selection`",
                "Pick a screen area for detecting `Team selection` screen");
        addRow(startBattleButtonIndicator);
        startBattleButtonLocation = new PointPicker("Location: `Start Battle` button",
                "Pick a point inside button to click");
        addRow(startBattleButtonLocation);

        notReviveButtonIndicator = new IndicatorConfigView(this, Indicator.reviveIndicator, "Screen: `Revive now to continue`",
                "Pick a screen area for detecting if all monsters die, and command NOT use Crystal to reviving");
        addRow(notReviveButtonIndicator);
        notReviveButtonLocation = new PointPicker("Location: `No Revive` button", "");
        addRow(notReviveButtonLocation);

        battleEndIndicator = new IndicatorConfigView(this, Indicator.battleEndIndicator, "Screen: `Battle Ended`",
                "Pick a screen area for detecting battle ended, victory or defeated");
        addRow(battleEndIndicator);

        runeRewardIndicator = new IndicatorConfigView(this, Indicator.runeRewardIndicator, "Popup: `Rune Reward`",
                "For detecting if there is Rune dropped");
        addRow(runeRewardIndicator);
        sellRuneButtonLocation = new PointPicker("Location: `SELL` button (for Runes)",
                "In the popup: `Rune Reward`, this is `SELL` button");
        addRow(sellRuneButtonLocation);

        confirmSellRuneIndicator = new IndicatorConfigView(this, Indicator.confirmSellRuneIndicator,
                "Popup: `Confirm Sell`<br/>(for Runes 5*, Rare or Legend)",
                "Sell rare rune will popup window ask for confirm. Pick a screen area for detecting this popup.");
        addRow(confirmSellRuneIndicator);
        sellRuneConfirmButtonLocation = new PointPicker("Location: `Confirm Sell YES` button<br/>(for Runes 5*, Rare or Legend)",
                "Pick a point inside YES button");
        addRow(sellRuneConfirmButtonLocation);

        getRuneButtonLocation = new PointPicker("Location: `GET` button (for Runes)",
                "In the popup: `Rune Reward`, this is `GET` button");
        addRow(getRuneButtonLocation);

        otherRewardIndicator = new IndicatorConfigView(this, Indicator.otherRewardIndicator, "Popup: `Other Reward`",
                "For detecting if other reward dropped, e.g. Rainbowmon, Scrolls, Materials...");
        addRow(otherRewardIndicator);
        getRewardButtonLocation = new PointPicker("Location: `Get Other Reward` button",
                "In the popup: `Other Reward`, this is `OK` button");
        addRow(getRewardButtonLocation);

        replayBattleIndicator = new IndicatorConfigView(this, Indicator.replayBattleIndicator, "Screen: `Replay`",
                "For detecting Replay button, to auto replay battle");
        addRow(replayBattleIndicator);
        replayBattleButtonLocation = new PointPicker("Location: `Replay` button",
                "Pick a point inside REPLAY button.");
        addRow(replayBattleButtonLocation);

        // Out of energy
        noEnergyIndicator = new IndicatorConfigView(this, Indicator.noEnergyIndicator, "Popup: `Not enough Energy`",
                "Pick a screen area for detecting `Not enough Energy` dialog");
        addRow(noEnergyIndicator);
    }

    private void addRow(PointPicker component) {
        component.setValueListener(this);
        add(component, "cell 0 " + rowCount++);
    }

    private void addRow(IndicatorConfigView component) {
        add(component, "cell 0 " + rowCount++);
    }

    public void populateFromModel(Profile profile) {
        populateFromModel(profile, startBattleButtonIndicator);
        populateFromModel(profile, notReviveButtonIndicator);
        populateFromModel(profile, battleEndIndicator);
        populateFromModel(profile, runeRewardIndicator);
        populateFromModel(profile, confirmSellRuneIndicator);
        populateFromModel(profile, otherRewardIndicator);
        populateFromModel(profile, replayBattleIndicator);
        populateFromModel(profile, noEnergyIndicator);
        replayBattleButtonLocation.setData(toAwtPoint(profile.getReplayBattle()));
        startBattleButtonLocation.setData(toAwtPoint(profile.getStartBattle()));
        notReviveButtonLocation.setData(toAwtPoint(profile.getReviveNo()));
        sellRuneButtonLocation.setData(toAwtPoint(profile.getSellRuneLocation()));
        sellRuneConfirmButtonLocation.setData(toAwtPoint(profile.getSellRuneConfirmation()));
        getRuneButtonLocation.setData(toAwtPoint(profile.getGetRuneLocation()));
        getRewardButtonLocation.setData(toAwtPoint(profile.getGetRewardLocation()));
    }

    private void populateFromModel(Profile profile, IndicatorConfigView indicatorConfigView) {
        indicatorConfigView.setData(MemImageCache.getInstance().get(profile.getIndicatorFile(indicatorConfigView.getIndicator())));
    }

    @Override
    public void valueChanged(Object source, Object newValue) {
        listener.onBattleConfigViewLocationsChanged();
    }

    @Override
    public void onIndicatorImageChanged(IndicatorConfigView view, BufferedImage bufferedImage) {
        listener.onBattleConfigViewIndicatorChanged(view.getIndicator(), bufferedImage);
    }

    public interface Listener {
        void onBattleConfigViewLocationsChanged();

        void onBattleConfigViewIndicatorChanged(Indicator indicator, BufferedImage bufferedImage);
    }
}
