package com.swarauto.ui.profile.refill;

import com.swarauto.game.profile.Profile;
import com.swarauto.ui.profile.widget.PointPicker;
import com.swarauto.ui.profile.widget.ValueListener;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

import static com.swarauto.util.PcConverter.toAwtPoint;

@Getter
public class RefillView extends JPanel implements ValueListener {
    private Listener listener;

    private PointPicker rechargeEnergyYes;
    private PointPicker rechargeEnergyNo;
    private PointPicker energyLocationInShop;
    private PointPicker confirmUseCrystal;
    private PointPicker ackRechargeSuccess;
    private PointPicker closeShop;

    public RefillView(Listener listener) {
        this.listener = listener;

        setLayout(new MigLayout("", "[grow,fill]", "[][][][][][][][][]"));

        add(new JLabel("If default auto refill function not working, please create your own config as below"), "cell 0 0");

        // Points
        rechargeEnergyYes = new PointPicker("Recharge Energy YES button", "When energy is not enough to start new battle, screen will display confirmation to recharge with 2 options: YES, NO. This location will be YES");
        rechargeEnergyYes.setValueListener(this);
        add(rechargeEnergyYes, "cell 0 1");

        rechargeEnergyNo = new PointPicker("Recharge Energy NO button", "When energy is not enough to start new battle, screen will display confirmation to recharge with 2 options: YES, NO. This location will be NO");
        rechargeEnergyNo.setValueListener(this);
        add(rechargeEnergyNo, "cell 0 2");

        energyLocationInShop = new PointPicker("Energy Location on Shop", "On refill shop, this is the location to refill energy");
        energyLocationInShop.setValueListener(this);
        add(energyLocationInShop, "cell 0 3");

        confirmUseCrystal = new PointPicker("Confirm Recharge Energy button", "On refill shop, this location will be used to confirm YES to use 30 crystal to refill.");
        confirmUseCrystal.setValueListener(this);
        add(confirmUseCrystal, "cell 0 4");

        ackRechargeSuccess = new PointPicker("Refill Successful OK button", "After refill energy successful, a screen with OK button will be displayed. This will be OK location");
        ackRechargeSuccess.setValueListener(this);
        add(ackRechargeSuccess, "cell 0 5");

        closeShop = new PointPicker("Close Refill Shop button", "On refill shop, this is the point of CLOSE button");
        closeShop.setValueListener(this);
        add(closeShop, "cell 0 6");
    }

    public void populateFromModel(Profile profile) {
        rechargeEnergyYes.setData(toAwtPoint(profile.getRechargeEnergyYes()));
        rechargeEnergyNo.setData(toAwtPoint(profile.getRechargeEnergyNo()));
        energyLocationInShop.setData(toAwtPoint(profile.getRechargeEnergy()));
        confirmUseCrystal.setData(toAwtPoint(profile.getConfirmRechargeEnergy()));
        ackRechargeSuccess.setData(toAwtPoint(profile.getAckRechargeEnergyOk()));
        closeShop.setData(toAwtPoint(profile.getCloseRechargeEnergy()));
    }

    @Override
    public void valueChanged(Object source, Object newValue) {
        listener.onRefillViewChanged();
    }

    public interface Listener {
        void onRefillViewChanged();
    }
}
